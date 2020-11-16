package project.cmake;

def main(_Targets,_Stage){
    def M_T4d = new system.tools4dev()
    def M_System = new system.workspace()

    switch(_Stage){
        case "Configure" :
            stage("SCM"){
                try {
                        withCredentials([usernamePassword(credentialsId: 'jenkins_git_credentials', passwordVariable: 'userPassword', usernameVariable: 'userName')]) {
                            M_System.wksClean()
                            M_System.wksClone("http ${env.userName}:${env.userPassword} -force --single-branch","false")
                        }
                } catch (err) {
                    M_System.wksClean()
                    error "Clone failed"
                }
            }


        break
        case "End" :
            stage("End"){
                M_System.wksClean()
            }
        break
        default :
            lock("${env.M_Project}_${env.SlaveName}"){
                    withCredentials([usernamePassword(credentialsId: "slave_default_user", passwordVariable: 'userPassword', usernameVariable: 'userName')]){
                            M_T4d.exec("wks ${_Stage}")
                    }
            }
        break
    }


}

def notifBuildStarted(){
    def M_Irc= new webServices.irc()  
    M_Message="""{
            "attachments": [
                {
                    "color": "#313131",
                    "author_name": "${env.M_JobName} - ${env.M_Version}",
                    "author_link": "Author Link test",
                    "author_icon": "https://avatars1.githubusercontent.com/u/16338861?v=3&s=400",
                    "title": "Build #${env.BUILD_NUMBER} Started",
                    "title_link": "${env.M_CurrentBuildUrl}",
                    "fields": [
                        {
                            "title": "Branch",
                            "value": "${env.BRANCH_NAME}",
                            "short": true
                        },
                        {
                            "title": "Changes",
                            "value": "${env.M_GitSHA7} ${env.M_GitCommiterEmail}",
                            "short": true
                        }
                    ],
                    "thumb_url": "https://i.imgur.com/DBveMrC.png",
                    "footer": "Provided By iFeelSmart"
                }
            ]
        }"""
    M_Irc.SendMessage("${M_Message}","${env.M_IrcChannel}")
}

def notifBuildFailed(_error='unkown'){
    def M_Irc= new webServices.irc()  
    M_Message="""{
            "attachments": [
                {
                    "color": "#313131",
                    "author_name": "${env.M_JobName} - ${env.M_Version}",
                    "author_link": "Author Link test",
                    "author_icon": "https://avatars1.githubusercontent.com/u/16338861?v=3&s=400",
                    "title": "Build #${env.BUILD_NUMBER} Failed",
                    "title_link": "${env.M_CurrentBuildUrl}",
                    "text": "${_error}",
                    "fields": [
                        {
                            "title": "Branch",
                            "value": "${env.BRANCH_NAME}",
                            "short": true
                        },
                        {
                            "title": "Changes",
                            "value":  "<https://git.ifeelsmart.net/projects/WKS/repos/smartui/commits?until=${env.M_GitSHA7}|${env.M_GitSHA7}> ${env.M_GitCommiterEmail}",
                            "short": true
                        }
                    ],
                    "thumb_url": "https://i.imgur.com/d5QJtHJ.png",
                    "footer": "Provided By iFeelSmart"
                }
            ]
        }"""
    M_Irc.SendMessage("${M_Message}","${env.M_IrcChannel}")
}

def notifBuildSucceeded(){
    def M_Irc= new webServices.irc()  
    M_Message="""{
            "attachments": [
                {
                    "color": "#313131",
                    "author_name": "${env.M_JobName} -  <https://git.ifeelsmart.net/projects/WKS/repos/smartui/commits?until=${env.M_GitSHA7}|${env.M_Version} - ${env.M_GitSHA7}>",
                    "author_link": "Author Link test",
                    "author_icon": "https://avatars1.githubusercontent.com/u/16338861?v=3&s=400",
                    "title": "Build #${env.BUILD_NUMBER} On branch ${env.BRANCH_NAME} Succeeded",
                    "title_link": "${env.M_CurrentBuildUrl}",
                    "thumb_url": "https://i.imgur.com/ZOvE5Mp.png"
                }
            ]
        }"""
    M_Irc.SendMessage("${M_Message}","${env.M_IrcChannel}")
} 
