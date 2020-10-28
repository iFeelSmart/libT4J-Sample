package api;

def start(){
    stage('Start'){
        def M_T4d = new system.tools4dev()
        def M_Wks = new system.workspace()
        def M_Ci = new system.jenkins.main()

        def M_MainNode = new slaves.main();
        def M_System = new system.jenkins.utils();
        def N_Vars = M_MainNode.getEnvVars()
        N_Vars["SlaveName"]="${M_MainNode.getName()}"

        env.M_BuildState="Started"
        env.M_BuildPassing=true
        env.N_MainNode="${N_Vars["SlaveName"]}"
        env.N_T4dPath="${N_Vars["Tools4Dev_PATH"]}"
        env.N_NodeType="${N_Vars["OS_TYPE"]}"

        try {
            lock("master"){

                node("${env.N_MainNode}") {
                    ws(){
                        M_Wks.gitClone("${env.M_RepositoryUrl}")

                        env.M_JobName="${M_Ci.getJobName()}"
                        env.M_AwsS3Pipeline="${M_Ci.getPipelineDirName()}"
                        if ( ( "${env.M_Hardware}" == "null" ) || ( "${env.M_Hardware}" == "" ) ){
                            env.M_Hardware="${M_Ci.getJobName()}"
                        }
                        if ( ( "${env.M_BranchName}" == "null" ) || ( "${env.M_BranchName}" == "" ) ){
                            env.M_BranchName="${env.BRANCH_NAME}"
                        }
                        env.M_CurrentDate="${M_Ci.getCurrentDate()}"
                        env.M_Workspace="${M_Ci.getWorkspace()}"
                        
                        env.M_BranchFullName="${env.M_BranchName.replaceAll('/','_')}"
                        env.M_CurrentBuildUrl="${env.JENKINS_URL}blue/organizations/jenkins/${M_Ci.getJobUrlName()}/detail/${env.BRANCH_NAME.replaceAll('/','%2F')}/${env.BUILD_NUMBER}/pipeline"
                        env.M_CurrentBuildOldUrl="${env.JENKINS_URL}job/${M_Ci.getJobUrlName().replaceAll('%2F','/job/')}/job/${env.BRANCH_NAME.replaceAll('/','%2F')}/${env.BUILD_NUMBER}/"
                        env.M_GitCommiterEmail="${M_T4d.getStdoutOneLine(      "_t4dSrcGitInfoCommitersEmail")}"
                        env.M_GitCommitMessage="${M_T4d.getStdoutOneLine(      "_t4dSrcGitInfoLastCommitMessage")}"
                        env.M_Version="${M_T4d.getStdoutOneLine(               "_wksGetVersion")}"
                        env.M_GitSHA7="${M_T4d.getStdoutOneLine(               "_t4dSrcGitInfoSha7").take(7)}"
                        env.M_GitSHA="${M_T4d.getStdoutOneLine(                "_t4dSrcGitInfoSha").take(40)}"
                        if ( "${env.M_IrcChannel}" == "null" ){
                            env.M_IrcChannel="${params.SlackChannel}"
                        }
                        
                        M_T4d.log("_t4dSrcJenkinsLogAdd -job")

                        env.M_Params="${params}"
                        env.M_TargetsList=""
                        env.M_TargetsFailed=""
                        env.M_TargetSucceeded=0
                        env.M_TargetsSucceeding=""
                        env.M_TargetsTotal=0
                        env.M_BuildUnstable=""
                        sh "env | grep '^M_' |sort"
                        sh "env | grep '^N_' |sort"
                        deleteDir()
                    }
                }
            }
        } catch (err){
            env.M_BuildPassing=false
            env.M_BuildError="${err}"
        }
    }
}

def end(){
    echo ""
}


def convertBooleanAsTargets(_Param=params,_Extra="",_Filter=""){
    M_Res=["Init"]

    for (M_Param in params){
        def M_Data="${M_Param}"
        def M_Value="${M_Data.tokenize("=")[1]}"
        def M_Name="${M_Data.tokenize("=")[0]}"
        if ( M_Name.startsWith('MBO_') ) {
            echo "Skipping $M_Name"
        } else if ( M_Name.startsWith('MBP_') ) {
            echo "Skipping $M_Name"
        } else {
            if ( "${M_Name}" != "-" ) {
                if ( "${M_Value}" == "true" ){
                    if ( "${_Filter}" != "" ){
                        def M_FilterName="${_Filter.tokenize("=")[1].tokenize("+")[0]}"
                        def M_FilterName2="${_Filter.tokenize("=")[1].tokenize("+")[1]}"
                        def M_FilterName3="${_Filter.tokenize("=")[1].tokenize("+")[2]}"
                        def M_FilterName4="${_Filter.tokenize("=")[1].tokenize("+")[3]}"
                        def M_FilterName5="${_Filter.tokenize("=")[1].tokenize("+")[4]}"
                        def M_FilterName6="${_Filter.tokenize("=")[1].tokenize("+")[5]}"
                        def M_FilterType="${_Filter.tokenize("=")[0]}"
                        if ( ("${M_FilterType}" == "exclude" ) && ( !M_Name.contains("${M_FilterName}") ) ){
                            M_Res.push("${M_Name} ${_Extra}")
                        } else if ( ( "${M_FilterType}" == "include" ) && ( M_Name.contains("${M_FilterName}") ) ){
                            M_Res.push("${M_Name} ${_Extra}")
                        } else if ( ( "${M_FilterType}" == "include" ) && ( M_Name.contains("${M_FilterName2}") ) ){
                            M_Res.push("${M_Name} ${_Extra}")
                        } else if ( ( "${M_FilterType}" == "include" ) && ( M_Name.contains("${M_FilterName3}") ) ){
                            M_Res.push("${M_Name} ${_Extra}")
                        } else if ( ( "${M_FilterType}" == "include" ) && ( M_Name.contains("${M_FilterName4}") ) ){
                            M_Res.push("${M_Name} ${_Extra}")
                        } else if ( ( "${M_FilterType}" == "include" ) && ( M_Name.contains("${M_FilterName5}") ) ){
                            M_Res.push("${M_Name} ${_Extra}")
                        } else if ( ( "${M_FilterType}" == "include" ) && ( M_Name.contains("${M_FilterName6}") ) ){
                            M_Res.push("${M_Name} ${_Extra}")
                        }
                    } else {
                        M_Res.push("${M_Name} ${_Extra}")
                    }
                    
                }
            }
        }
    }
    M_Res.remove("Init")
    return M_Res
}

def convertBooleanAsBuildOption(_Param=params){
    M_Res=""

    for (M_Param in params){
        def M_Data="${M_Param}"
        def M_Value="${M_Data.tokenize("=")[1]}"
        def M_Name="${M_Data.tokenize("=")[0]}"
        if ( M_Name.startsWith('MBO_') ) {
            if ( "${M_Value}" == 'true' ){
                M_Res="${M_Res} -${M_Name.replaceAll("MBO_","").toLowerCase()}"
            }
        }
    }
    return M_Res
}
