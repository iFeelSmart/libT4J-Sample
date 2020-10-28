package system;


def getStdout(_Args,_Tools4Dev_PATH="${env.Tools4Dev_PATH}"){
    //Fallback to N_T4dPath if Tools4Dev_PATH does not exist
    if ("${_Tools4Dev_PATH}" == "null") {    _Tools4Dev_PATH="${env.N_T4dPath}";     }

    try {
        def m_output=sh (script: """#!/bin/zsh 
                                    ${_Tools4Dev_PATH}/t4d ${_Args}""", returnStdout: true).tokenize('')
        return m_output[0]
    } catch (Exception err) {
        echo "Can't get strandard output - ${err}"
        return "Unknown"
    }
}

def getStdoutOneLine(_Args,_Tools4Dev_PATH="${env.Tools4Dev_PATH}"){
    //Fallback to N_T4dPath if Tools4Dev_PATH does not exist
    if ("${_Tools4Dev_PATH}" == "null") {    _Tools4Dev_PATH="${env.N_T4dPath}";     }
    
    try {
        def m_output=sh (script: """#!/bin/zsh 
                                    ${_Tools4Dev_PATH}/t4d ${_Args}""", returnStdout: true).replaceAll('\n','').tokenize('')
        return m_output[0]
    } catch (Exception err) {
        echo "Can't get strandard output - ${err}"
        return "Unknown"
    }
}

def log(_Args,_Tools4Dev_PATH="${env.N_T4dPath}"){

    node("${env.N_MainNode}") {
        ws(){
            lock("_LogSystem"){
                try {
                    sh """#!/bin/zsh
                                    cd \$WS_ROOT 
                                    ${_Tools4Dev_PATH}/t4d ${_Args} >> ${_Tools4Dev_PATH}/.t4d/.JenkinsLog"""
                } catch (Exception err) {
                    echo "Error -> ${_Tools4Dev_PATH}/t4d ${_Args}"
                    error "Error - ${err}"
                }
            }
        }
    }
}





def exec(_Args,_Tools4Dev_PATH="${env.Tools4Dev_PATH}"){
    //Fallback to N_T4dPath if Tools4Dev_PATH does not exist
    if ("${_Tools4Dev_PATH}" == "null") {    _Tools4Dev_PATH="${env.N_T4dPath}";     }
    
    try {
        sh """#!/bin/zsh
                        cd \$WS_ROOT 
                        ${_Tools4Dev_PATH}/t4d ${_Args}"""
        // def m_output=sh (script: , returnStdout: true).tokenize('')
        // return m_output[0]
    } catch (Exception err) {
        echo "Error -> ${_Tools4Dev_PATH}/t4d ${_Args}"
        error "Error - ${err}"
    }
}

