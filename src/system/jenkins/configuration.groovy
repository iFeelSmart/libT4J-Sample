package system.jenkins;

def getSlaveObject(_Uuid,_Scheme=''){
    // echo "${env.M_CiPipeline} - ${env.JENKINS_URL} - ${_Scheme}"
    switch(_Uuid){
        case "CMake"                              : def m_Res=new slaves.main();    return m_Res; break
        default :
            m_Res="Unknown"
            error "getSlaveObject -> Unknown Project ${_Uuid}"
        break
    }
}



def getSchemeObject(_Project){
    switch(_Project){
        case "CMake"                     : def m_Res=new project.cmake.Sequential();    return m_Res; break
        default :
            m_Res="Unknown"
            error "getSchemeObject -> Unknown scheme ${_Project}"
        break
    }
}

def getSlaveConfiguration(_Project,_Scheme=''){
    def M_Slave = getSlaveObject("${_Project}","${_Scheme}")
    def m_Vars = []
    // echo "${_Project}-${_Scheme}"
    m_Vars = M_Slave.getEnvVars()
    m_Vars["SlaveName"]="${M_Slave.getName()}"
    m_Vars["Scheme"]="${_Scheme}"
    return m_Vars
}
