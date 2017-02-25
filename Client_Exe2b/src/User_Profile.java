//Γεώργιος Ζέρβας icsd13055
//Νικόλαος Φουρτούνης icsd13195
//Παύλος Σκούπρας icsd13171

import java.io.Serializable;

//*****************************************
//*****CLASS TO CREATE USERS INFO********** 
public class User_Profile implements Serializable {

    //****************************
    //*********VARIABLES********** 
    private String nickname;
    private String ip;
    private int option;
    private String what_are_you;

    //********************************
    //*********CONSTRUCTOR********** 
    User_Profile(String p_nick, String p_ip, int p_option) {
        nickname = p_nick;
        ip = p_ip;
        option = p_option;
    }

    //*********************************************
    //*********GETTERS FOR EVERY VARIABLE********** 
    String get_nickname() {
        return nickname;
    }

    String getip() {
        return ip;
    }

    int getoption() {
        return option;
    }

    String getwhat() {
        return what_are_you;
    }

    void setwhat(String l) {
        what_are_you = l;
    }
}
