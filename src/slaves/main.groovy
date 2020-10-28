package slaves;


def getName(){
    return 'local'
}


def getEnvVars(){
    m_Res = [:]
    m_Res["HOME"]="/home/test"
    m_Res["Tools4Dev_PATH"]="${m_Res["HOME"]}/.tools4dev/src"
    m_Res["WORKSPACE"]="${env.M_Workspace}"
    m_Res["WS_ROOT"]="${m_Res["HOME"]}/work/${m_Res["WORKSPACE"]}"
    m_Res["OS_TYPE"]="Linux"
    return m_Res
}
