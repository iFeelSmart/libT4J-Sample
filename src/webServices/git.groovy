package webServices;

def setBuildStatus(_BuildStatus){
    def M_T4d = new system.tools4dev()
    withCredentials([usernamePassword(credentialsId: "jenkins_git_credentials", passwordVariable: 'userPassword', usernameVariable: 'userName')]) {
        M_T4d.exec("_t4dTeamGitServerSetBuildStatus -user-password ${env.userName}:${env.userPassword} \"${env.M_GitSHA}\" \"${env.M_JobName}\" \"${_BuildStatus}\" \"${env.M_CurrentBuildUrl}\" ")
    }
}

def setSchemeStatus(_BuildStatus,_BuildName){
    def M_T4d = new system.tools4dev()
    withCredentials([usernamePassword(credentialsId: "jenkins_git_credentials", passwordVariable: 'userPassword', usernameVariable: 'userName')]) {
        M_T4d.exec("_t4dTeamGitServerSetBuildStatus -user-password ${env.userName}:${env.userPassword} \"${env.M_GitSHA}\" \"${_BuildName}\" \"${_BuildStatus}\" \"${env.M_CurrentBuildUrl}\" ")
    }
}
    