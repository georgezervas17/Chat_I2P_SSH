//Γεώργιος Ζέρβας icsd13055
//Νικόλαος Φουρτούνης icsd13195
//Παύλος Σκούπρας icsd13171

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

//*************************************************
//*****CLASS TO SEND AND RECIEVE MESSAGES********** 
public class Send_Message implements Serializable {

    //****************************
    //*********VARIABLES********** 
    private String message;
    private String meg;
    private Date message_date;
    private User_Profile user;
    private ArrayList<User_Profile> ar;
    User_Profile[] list;
    int number;

    //********************************
    //*********CONSTRUCTORS********** 
    Send_Message() {
    }

    Send_Message(String p_m) {
        message = p_m;
    }

    Send_Message(String p_message, Date p_date, User_Profile p_user) {
        message = p_message;
        message_date = p_date;
        user = p_user;
    }

    Send_Message(String p_m, User_Profile[] p_ar, int k) {
        message = p_m;
        list = p_ar;
        number = k;
    }

    Send_Message(String p_m, String h) {
        message = p_m;
        meg = h;
    }

    //*********************************************
    //*********GETTERS FOR EVERY VARIABLE********** 
    User_Profile[] getlist() {
        return list;
    }

    int getnumber() {
        return number;
    }

    String getmessage() {
        return message;
    }

    String getmeg() {
        return meg;
    }

    ArrayList<User_Profile> getArrayList() {
        return ar;
    }

    Date getdate() {
        return message_date;
    }

    User_Profile getuser() {
        return user;
    }

    void setarray(ArrayList a) {
        ar = a;
    }

}
