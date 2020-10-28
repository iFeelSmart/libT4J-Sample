package webServices;

def sendNotificationWithNode(_Status,_NotificationLvl,_Text=''){
    node("${env.N_MainNode}") {
        ws(){
            sendNotification(_Status,_NotificationLvl,_Text='')
        }
    }
}


def sendNotification(_status,_notificationLvl,_text=''){
    def M_Configuration = new system.jenkins.configuration()
    def M_Scheme = M_Configuration.getSchemeObject(env.M_Project)
    switch (_notificationLvl) {
        case "4":
            if ("${_status}" == "started") {
                M_Scheme.notifBuildStarted()
            }
            if ("${_status}" == "succeeded") {
                M_Scheme.notifBuildSucceeded()
            }
            if ("${_status}" == "failed") {
                M_Scheme.notifBuildFailed(_text)
            }
            if ("${_status}" == "unstable") {
                M_Scheme.notifBuildUnstable()
            }
            break
        case "3":
            if ("${_status}" == "started") {
                M_Scheme.notifBuildStarted()
            }
            if ("${_status}" == "succeeded") {
                M_Scheme.notifBuildSucceeded()
            }
            if ("${_status}" == "failed") {
                M_Scheme.notifBuildFailed(_text)
            }
            if ("${_status}" == "unstable") {
                M_Scheme.notifBuildUnstable()
            }
            break
        case "2":
            if ("${_status}" == "started") {
                M_Scheme.notifBuildStarted()
            }
            if ("${_status}" == "succeeded") {
                M_Scheme.notifBuildSucceeded()
            }
            if ("${_status}" == "failed") {
                M_Scheme.notifBuildFailed(_text)
            }
            if ("${_status}" == "unstable") {
                M_Scheme.notifBuildUnstable()
            }
            break
        case "1":
            if ("${_status}" == "succeeded") {
                M_Scheme.notifBuildSucceeded()
            }
            if ("${_status}" == "failed") {
                M_Scheme.notifBuildFailed(_text)
            }
            if ("${_status}" == "unstable") {
                M_Scheme.notifBuildUnstable()
            }
            break
        case "0":
            if ("${_status}" == "failed") {
                M_Scheme.notifBuildFailed(_text)
            }
            if ("${_status}" == "unstable") {
                M_Scheme.notifBuildUnstable()
            }
            break
        default:
            echo "Unknown Status or Notification LVL >${env.M_NotificationLvl}< IRC Notification Disabled"
            break
    }
}

def SendMessage(_Message,_Channel="Jenkins"){
    def M_T4d = new system.tools4dev()
    echo "IRC Channel ${env.M_IrcChannel}" 
     M_T4d.exec("_t4dTeamIrcSendNotif '${_Message}' '${_Channel}' ")
    
}
