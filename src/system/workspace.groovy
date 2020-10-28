package system;

def deleteWorkspace(){
     try {
        deleteDir()
    } catch (err) {
        sh "rm -rf ./*"
    }
}

def gitClone(_Repository){
    withCredentials([usernamePassword(credentialsId: 'jenkins_git_credentials', passwordVariable: 'userPassword', usernameVariable: 'userName')]) {
        try {
            deleteDir()
            sh "git clone \"https://${env.userName}:${env.userPassword}@${_Repository}\" ./"
            if ( ( "${env.M_GitSHA}" == "" ) || ( "${env.M_GitSHA}" == "null" ) ){
                echo "git branch >${env.BRANCH_NAME}<"
                sh "git checkout ${env.BRANCH_NAME}"
            } else {
                echo "git sha >${env.M_GitSHA}<"
                sh "git checkout ${env.M_GitSHA}"
            }
        } catch (err) {
            sleep 5
            deleteDir()
            sh "git clone \"https://${env.userName}:${env.userPassword}@${_Repository}\" ./"
            if ( ( "${env.M_GitSHA}" == "" ) || ( "${env.M_GitSHA}" == "null" ) ){
                echo "git branch >${env.BRANCH_NAME}<"
                sh "git checkout ${env.BRANCH_NAME}"
            } else {
                echo "git sha >${env.M_GitSHA}<"
                sh "git checkout ${env.M_GitSHA}"
            }
        }
    }
}

def wksDeps(){
    def M_T4d = new system.tools4dev()
    
    M_T4d.exec("wks deps")
}

def wksClean(){
   def M_System = new system.workspace()
    M_System.deleteWorkspace() 
}

def wksClone(_Arg,_ShaSync="true"){
    def M_T4d = new system.tools4dev()
    

    gitClone("${env.M_RepositoryUrl}")

    try {
            M_T4d.exec("wks clone ${_Arg}")
    } catch (err) {
        sleep 5
        try {
                M_T4d.exec("wks clone ${_Arg}")
        } catch (err2) {
            sleep 10
            try {
                M_T4d.exec("wks clone ${_Arg}")
            } catch (err3) {
                sleep 20
                M_T4d.exec("wks clone ${_Arg}")
            }
        }
    }
    if ( "${_ShaSync}" == "true" ){
        M_T4d.exec("gik shasync")
    }
    
}
