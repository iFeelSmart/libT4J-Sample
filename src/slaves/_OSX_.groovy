package slaves.<LOCAL>;


def getName(){
    return '<NAME>'
}


def getEnvVars(){
    m_Res = [:]
    m_Res["HOME"]="/Users/<USER_NAME>"
    m_Res["SHARED_WORKSPACE"]="/Users/Shared"
    m_Res["Tools4Dev_PATH"]="${m_Res["SHARED_WORKSPACE"]}/Tools4Dev"
    m_Res["WORKSPACE"]="${env.M_Workspace}"
    m_Res["WS_ROOT"]="${m_Res["HOME"]}/work/${m_Res["WORKSPACE"]}"
    m_Res["PATH"]=":/usr/bin:/bin:/usr/sbin:/usr/local/bin:/sbin"
    m_Res["OS_TYPE"]="MacOS"
    m_Res["JAVA_HOME"]="<JAVA_ROOT_PATH>"
    return m_Res
}