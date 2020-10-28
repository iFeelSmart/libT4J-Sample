package system.jenkins;

def convertAsBuildEnv(_Tab){
    def M_Res=["Tic=Tac"]
    for (M_Component in _Tab) {
        def String M_Value = "${M_Component}"
        M_Res.push("${M_Value.tokenize('=')[0]}=${M_Value.tokenize('=')[1]}")
    }
    return M_Res
}

def getArrayContent(_Array){
    def M_Res=""
    for (M_Item in _Array) {
            M_Res="${M_Item}\n${M_Res}"
    }
    // for (i=0;i<_Array.tokenize(',').size();i++) { 
        // M_Res="${_Array}" 
    // }
    return M_Res
}

def add(_int1,int _int2){
    int M_int=(_int1)
    return ( ( M_int ) + ( _int2 ) )
}

def minus(_int1,int _int2){
    int M_int=(_int1)
    return ( ( M_int ) - ( _int2 ) )
}

def bumpVersion(_version1,type='fix'){
  def m_res=""
  def m_major=""
  def m_minor=""
  def m_fix=""
    switch(type) {                                                                                                               
      case "major"  :
        m_major = sh (script: "expr \$(echo ${_version1} | cut -d '.' -f1) + 1", returnStdout: true).replaceAll('\n','').tokenize('-')

        m_minor = sh (script: "expr \$(echo ${_version1} | cut -d '.' -f2)", returnStdout: true).replaceAll('\n','').tokenize('-')
        m_fix = sh (script: "expr \$(echo ${_version1} | cut -d '.' -f3)", returnStdout: true).replaceAll('\n','').tokenize('-')
      break
      case "minor"  :
        m_minor = sh (script: "expr \$(echo ${_version1} | cut -d '.' -f2) + 1", returnStdout: true).replaceAll('\n','').tokenize('-')

        m_major = sh (script: "expr \$(echo ${_version1} | cut -d '.' -f1)", returnStdout: true).replaceAll('\n','').tokenize('-')
        m_fix = sh (script: "expr \$(echo ${_version1} | cut -d '.' -f3)", returnStdout: true).replaceAll('\n','').tokenize('-')
      break
      case "fix"    :
        m_fix = sh (script: "expr \$(echo ${_version1} | cut -d '.' -f3) + 1", returnStdout: true).replaceAll('\n','').tokenize('-')

        m_major = sh (script: "expr \$(echo ${_version1} | cut -d '.' -f1)", returnStdout: true).replaceAll('\n','').tokenize('-')
        m_minor = sh (script: "expr \$(echo ${_version1} | cut -d '.' -f2)", returnStdout: true).replaceAll('\n','').tokenize('-')
      break
      default :
          m_Res="unknown"
          error "unknown"
          break
  }
    return "${m_major[0]}.${m_minor[0]}.${m_fix[0]}"
}

def setAllTargets(_Comp1=[],_Comp2=[],_Comp3=[],_Comp4=[],_Comp5=[],_Comp6=[],_Comp7=[],_Comp8=[],_Comp9=[]){
    def M_Total = []
    for ( M_Target in _Comp1){
        M_Total << "${M_Target}"
    }
    for ( M_Target in _Comp2){
        M_Total << "${M_Target}"
    }
    for ( M_Target in _Comp3){
        M_Total << "${M_Target}"
    }
    for ( M_Target in _Comp4){
        M_Total << "${M_Target}"
    }
    for ( M_Target in _Comp5){
        M_Total << "${M_Target}"
    }
    for ( M_Target in _Comp6){
        M_Total << "${M_Target}"
    }
    for ( M_Target in _Comp7){
        M_Total << "${M_Target}"
    }
    for ( M_Target in _Comp8){
        M_Total << "${M_Target}"
    }
    for ( M_Target in _Comp9){
        M_Total << "${M_Target}"
    }

    return M_Total
}
