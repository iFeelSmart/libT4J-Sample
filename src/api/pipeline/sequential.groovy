package api.pipeline;

def exec(_BuildTargets,_Name="Build",_Notif="true"){
    def M_Vars = [:]
    def M_Nodes = [:]
    def M_Targets = ["Init"]
    def M_TargetFailed = ["Init"]
    def M_Configuration = new system.jenkins.configuration();
    def M_System= new system.jenkins.utils();
    def M_Irc= new webServices.irc();
    def M_Name="${_Name.tokenize(' ')[0].replaceAll('%',' ')}"

    stage("${M_Name}"){
        for (M_Target in _BuildTargets){
            /////////////////////////////
                M_Vars = M_Configuration.getSlaveConfiguration(env.M_Project,M_Target)
                M_Vars["Stage"]="${_Name}"
                M_Nodes["${M_Target.tokenize(' ')[0].replaceAll('%',' ')}"] = step(M_Target,M_Vars)
                M_Targets.push("${M_Target}")
            /////////////////////////////
        }

        M_Targets.remove("Init")
        def M_CurrentTargetsTotal=M_Nodes.size()
        env.M_TargetsTotal=M_System.add(env.M_TargetsTotal,M_CurrentTargetsTotal)
        env.M_TargetSucceeded=M_System.add(env.M_TargetSucceeded,M_CurrentTargetsTotal)
        env.M_TargetsList="${env.M_TargetsList}\n${M_System.getArrayContent(M_Targets)}"

        try {
            
            parallel M_Nodes
            
        } catch (err){
            env.M_BuildPassing=false
            env.M_BuildError="${err}"
            if ( "${_Notif}" == "true" ) {
                M_Irc.sendNotificationWithNode("failed",env.M_NotificationLvl)
            }
        }
    }
}

def workspace(_BuildTargets,_Name="Configure",_Notif="false"){
    def M_Vars = [:]
    def M_Nodes = [:]
    def M_Targets = ["Init"]
    def M_Configuration = new system.jenkins.configuration();
    def M_System= new system.jenkins.utils();
    def M_Irc= new webServices.irc();
    def M_T4d = new system.tools4dev()

    stage("${_Name}"){
        for (M_Target in _BuildTargets){
            /////////////////////////////
                M_Vars = M_Configuration.getSlaveConfiguration(env.M_Project,M_Target)
                M_Vars["Stage"]="${_Name}"
                M_Nodes["${M_Vars["SlaveName"]}"] = step("${_Name}",M_Vars,"false")
                M_Targets.push("${M_Target}")
                
                if ("${_Name}" != "End"){
                    M_T4d.log("_t4dSrcJenkinsLogAdd -workspace ${_Name}-Start ${M_Vars['SlaveName']}")
                }
            /////////////////////////////
        }
        M_Targets.remove("Init")

        try {

            if (("${_Notif}" == "true") || ("${_Notif}" == "start")) {
                M_Irc.sendNotificationWithNode("started",env.M_NotificationLvl)
            }

            parallel M_Nodes
            
            if ( "${env.M_BuildPassing}" == "false" ){
                error "${env.M_BuildError}"
            }
            if (("${_Notif}" == "true") || ("${_Notif}" == "end")) {
                M_Irc.sendNotificationWithNode("succeeded",env.M_NotificationLvl)
            }
            if ("${_Name}" == "End"){
                echo "ITEMS PASSING ${env.M_TargetsSucceeding.replaceAll('Clone','').replaceAll('Configure','').replaceAll('End','')}"
                echo "ITEMS FAILING ${env.M_TargetsFailed.replaceAll('Clone','').replaceAll('Configure','').replaceAll('End','')}" 
                M_T4d.log("_t4dSrcJenkinsLogAdd -workspace ${_Name} ${M_Vars['SlaveName']}") 
            } else {
                M_T4d.log("_t4dSrcJenkinsLogAdd -workspace ${_Name}-End ${M_Vars['SlaveName']}")
            }
        } catch (err){
            env.M_BuildPassing=false
            env.M_BuildError="${err}"
            if (("${_Notif}" == "true") || ("${_Notif}" == "end")) {
                M_Irc.sendNotificationWithNode("failed",env.M_NotificationLvl)
            } else if ("${_Name}" == "Clone"){
                M_Irc.sendNotificationWithNode("unstable",env.M_NotificationLvl)
            }
            if ("${_Name}" == "End"){
                echo "ITEMS PASSING ${env.M_TargetsSucceeding.replaceAll('Clone','').replaceAll('Configure','').replaceAll('End','')}"
                echo "ITEMS FAILING ${env.M_TargetsFailed.replaceAll('Clone','').replaceAll('Configure','').replaceAll('End','')}"  
            }
            error "${env.M_BuildError}"
        }
    }
}

def step(_Scheme,_Vars,_SetBuildStatus='true'){
    def M_Configuration = new system.jenkins.configuration();
    def M_System= new system.jenkins.utils();
    def M_GitServer= new webServices.git();
    def M_Res = [:]
    def M_BuildEnv = M_System.convertAsBuildEnv(_Vars)
    def M_Scheme = M_Configuration.getSchemeObject(env.M_Project)
    M_Res["${_Scheme}"] = {
        withEnv(M_BuildEnv) {
            // lock("${env.M_Project}_${env.SlaveName}"){
            timeout(activity: true, time: 12, unit: 'HOURS') {
                node("${env.SlaveName}") {
                    timeout(activity: true, time: 5, unit: 'HOURS') {
                        ws("${env.WS_ROOT}") {
                            try {
                                if ("${_SetBuildStatus}" == "true")  {
                                    M_GitServer.setSchemeStatus('INPROGRESS',"${_Scheme}")
                                }
                                echo "${env.SlaveName} - ${_Scheme}"
                                M_Scheme.main("${_Scheme}","${_Vars["Stage"]}")

                                lock("_Ressources"){
                                    if ("${_SetBuildStatus}" == "true")  {
                                        M_GitServer.setSchemeStatus('SUCCESSFUL',"${_Scheme}")
                                    }
                                    if (("${_Scheme}" != "Clone") && ("${_Scheme}" != "End")) {
                                        env.M_TargetsSucceeding="${env.M_TargetsSucceeding}\n${_Scheme.replaceAll(' ','%')}"
                                    }
                                }
                            } catch (err){
                                env.M_BuildPassing=false
                                env.M_BuildError="${err}"

                                lock("_Ressources"){
                                    if ("${_SetBuildStatus}" == "true")  {
                                        M_GitServer.setSchemeStatus('FAILED',"${_Scheme}")
                                    }
                                    if (("${_Scheme}" != "Clone") && ("${_Scheme}" != "End")) {
                                        env.M_TargetsFailed="${env.M_TargetsFailed}\n${_Scheme.replaceAll(' ','%')}"
                                        env.M_TargetSucceeded=M_System.minus(env.M_TargetSucceeded,1)
                                    } 
                                }
                                error "${env.M_BuildError}"
                            }
                        }
                    }
                }
            }
        }
    }
    return M_Res["${_Scheme}"]
}
