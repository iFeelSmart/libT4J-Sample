package system.jenkins;

def getJobName() {
    def m_version = sh (script: "echo ${env.JOB_NAME} | sed 's|/|-|g' | sed 's|-${env.JOB_BASE_NAME}||g' ", returnStdout: true).tokenize(' ')
    return "${m_version[0].replaceAll('\n','')}"
}

def getPipelineDirName() {
    def m_version = sh (script: "dirname \$(dirname ${env.JOB_NAME} | rev) | rev", returnStdout: true).tokenize(' ')
    return "${m_version[0].replaceAll('\n','')}"
}

def getJobUrlName() {
    def m_JobName = sh (script: "echo ${env.JOB_NAME} | sed 's|/|%2F|g' | sed 's|%2F${env.JOB_BASE_NAME}||g' ", returnStdout: true).tokenize(' ')
    return "${m_JobName[0].replaceAll('\n','')}"
}

def getBranchName() {
  String m_Branch = env.BRANCH_NAME
  return m_Branch.replaceAll('/','_')
}

def getWorkspace() {
  return "${getJobName()}_Workspace/${getBranchName()}/${env.BUILD_NUMBER}"
}

def getCurrentDate(format='') {
  try {
    if ("${format}" == "full") {
      def m_date = sh (script: 'date', returnStdout: true).tokenize()
      return "${m_date[5]} ${m_date[1]} ${m_date[2]} ${m_date[3]}"
    }
    if ("${format}" == "integer") {
      def m_date = sh (script: 'date +%Y%m%d%H%M%S', returnStdout: true).tokenize()
      return "${m_date[0]}"
    }
    else
    {
      def m_date = sh (script: 'date +%Y-%m-%d', returnStdout: true).tokenize('-')
      return "${m_date[0]}-${m_date[1]}-${m_date[2].tokenize()[0]}"
    }
  } catch (Exception err) {
    echo "Can't get date"
  }
}

def getAppVersion(filePath=''){
  def m_version = sh (script: "cat ${filePath}", returnStdout: true).replaceAll('\n','').tokenize('-')
    return "${m_version[0]}"
}
