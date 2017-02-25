//Γεώργιος Ζέρβας icsd13055
//Νικόλαος Φουρτούνης icsd13195
//Παύλος Σκούπρας icsd13171

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

//*******************************************
//********CLASS FOR CLIENTS TIMER************
public class Timer extends Thread {

    SSLSocket c;
    ObjectOutputStream out_Server;
    ObjectInputStream in_Server;
    User_Profile[] Online_users_list;

    //*******************************
    //********CONSTRUCTOR************
    Timer(SSLSocket c, ObjectOutputStream out_Server, ObjectInputStream in_Server) {
        this.c = c;
        this.out_Server = out_Server;
        this.in_Server = in_Server;
    }

    //****************************
    //********FUNCTION************
    public void run() {
        while (true) {
            try {
                //********************************************************************
                //********THREAD SLEEPS FOR 10SEC AND THE SENT A HEARTBEAT************
                Thread.sleep(1000 * 15);
            } catch (InterruptedException ex) {
                Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                //******************************************************
                //********SENT A HEARTBEAT MESSAGE TO SERVER************
                out_Server.writeObject(new Send_Message("HEARTBEAT"));
                out_Server.flush();

                System.out.println(Chat_Room_Panel.user + " I sent hearbeat");

                //*************************************************
                //********RECIEVE A MESSAGE FROM SERVER************
                Send_Message msg = (Send_Message) in_Server.readObject();
                Online_users_list = msg.getlist();

                //************************************************************
                //********GRAPHICS TO PRINT THE LIST IN THE WINDOW************
                Chat_Room_Panel.Online_users_list = Online_users_list;
                Chat_Room_Panel.online_users_area.setText("");
                for (int i = 0; i < msg.getlist().length; i++) {
                    if (!Chat_Room_Panel.user.get_nickname().equals(msg.getlist()[i].get_nickname())) {
                        Chat_Room_Panel.online_users_area.append(msg.getlist()[i].get_nickname() + "\n");
                        Chat_Room_Panel.online_users_area.setForeground(Color.green);
                        Chat_Room_Panel.online_users_area.setBackground(Color.LIGHT_GRAY);
                        Chat_Room_Panel.online_users_area.setFont(new Font("Segoe UI", Font.BOLD, 20));
                        Chat_Room_Panel.online_users_area.setBounds(450, 150, 220, 350);
                        Chat_Room_Panel.online_users_area.setEditable(false);
                        Chat_Room_Panel.online_users_area.setLineWrap(true);
                        Chat_Room_Panel.online_users_area.setWrapStyleWord(true);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
