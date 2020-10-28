package slaves.<LOCAL>;


def getName(){
    return '<NAME>'
}


def getEnvVars(){
    m_Res = [:]
    m_Res["HOME"]="/home/<USER_NAME>"
    m_Res["SHARED_WORKSPACE"]="/home/shared"
    m_Res["Tools4Dev_PATH"]="${m_Res["SHARED_WORKSPACE"]}/Tools4Dev"
    m_Res["WORKSPACE"]="${env.M_Workspace}"
    m_Res["WS_ROOT"]="${m_Res["HOME"]}/work/${m_Res["WORKSPACE"]}"
    m_Res["OS_TYPE"]="Linux"
    return m_Res
}
