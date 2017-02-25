//Γεώργιος Ζέρβας icsd13055
//Νικόλαος Φουρτούνης icsd13195
//Παύλος Σκούπρας icsd13171

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

public class SSL_Server implements Runnable {

    //*****************************************************************
    //*************************VARIABLES*******************************
    private SSLSocket sslsocket;
    private ObjectOutputStream obo;
    private ObjectInputStream obi;
    private boolean flag;
    private static ArrayList<User_Profile> arraylist;
    private User_Profile User;
    private boolean connected = false;

    //***********************************
    //*********CONSTRUCTOR***************
    SSL_Server(Socket socket, ArrayList a) {
        //ssocket=socket;
        sslsocket = (SSLSocket) socket;
        arraylist = a;
    }

    public void run() {

        System.out.println("New client connected with IP: " + sslsocket.getInetAddress()
                + " in port: " + sslsocket.getLocalPort() + "\n");
        try {
            //*****************************************************************
            //********************CREATE CONNECTION STREAM*********************
            obo = new ObjectOutputStream(sslsocket.getOutputStream());
            obi = new ObjectInputStream(sslsocket.getInputStream());

            //****************************
            //*********VARIABLES********** 
            Send_Message recieved_message;
            int counter = 0;
            flag = true;

            Timer timer = new Timer();
            //*****************************************************************
            //**********SERVER IS RUNNING AND RECIEVE MESSAGES*****************
            while (((recieved_message = (Send_Message) obi.readObject()) != null) && flag == true) {
                //*****************************************************************
                //********************IF THE RECIEVED MESSAGE IS START*************
                if (recieved_message.getmessage().equals("START")) {
                    //*****************************************************************
                    //*********ADD THIS USER TO THE ARRAYLIST WITH ONLINE USERS********
                    arraylist.add(counter++, recieved_message.getuser());
                    User = recieved_message.getuser();
                    //*****************************************************************
                    //********************PRINT ALL ONLINE USERS***********************
                    System.out.println("Online users are: ");
                    for (int i = 0; i < arraylist.size(); i++) {
                        System.out.println(arraylist.get(i).get_nickname());
                    }
                    System.out.println(" ");

                    //*********************************************
                    //*********TAKE THE ARRAYLISTS NUMBER********** 
                    int number_online_users = arraylist.size();

                    //*****************************************************************
                    //*******PUT ALL USERS INTO A ARRAY TO SEND THEM TO CLIENT*********
                    User_Profile[] online_users_array = new User_Profile[arraylist.size()];
                    for (int i = 0; i < arraylist.size(); i++) {
                        online_users_array[i] = arraylist.get(i);
                    }

                    //*****************************************************************
                    //****************SEND IT TO THE CLIENT****************************
                    obo.writeObject(new Send_Message("list", online_users_array, number_online_users));
                    obo.flush();
                    System.out.println("List has been sended." + "\n");
                    connected = true;
                    addtimer();
                }

                //******************************************************
                //*************IF THE RECIEVED MESSAGE IS ΕΧΙΤ**********
                if (recieved_message.getmessage().equals("EXIT")) {
                    System.out.println("Client has disconnected!");
                    for (int i = 0; i < arraylist.size(); i++) {
                        System.out.println(arraylist.get(i).get_nickname());
                        System.out.println(User.get_nickname());
                        if (arraylist.get(i).get_nickname().equals(User.get_nickname())) {
                            System.out.println(arraylist.get(i).get_nickname());
                            arraylist.remove(i);
                        }
                    }
                    flag = false;
                    break;
                }

                //*****************************************************************
                //*************IF THE RECIEVED MESSAGE IS REFRESH******************
                if (recieved_message.getmessage().equals("REFRESH")) {

                    System.out.println("Client has requested for a new users list.");

                    int number_users = arraylist.size();

                    //*****************************************************************
                    //*******PUT ALL USERS INTO A ARRAY TO SEND THEM TO CLIENT*********
                    User_Profile[] k = new User_Profile[arraylist.size()];
                    for (int i = 0; i < arraylist.size(); i++) {
                        k[i] = arraylist.get(i);
                    }

                    //*****************************************************************
                    //****************SEND IT TO THE CLIENT****************************
                    obo.writeObject(new Send_Message("refresh_list", k, number_users));
                    obo.flush();
                    System.out.println("The new list has been sended." + "\n");
                }
                //*****************************************************************
                //*************IF THE RECIEVED MESSAGE IS HEARTBEAT****************
                if (recieved_message.getmessage().equals("HEARTBEAT")) {
                    System.out.println("Client " + User.get_nickname() + " sent HEARTBEAT");
                    int number_users = arraylist.size();
                    User_Profile[] k = new User_Profile[arraylist.size()];
                    for (int i = 0; i < arraylist.size(); i++) {
                        k[i] = arraylist.get(i);
                    }
                    obo.writeObject(new Send_Message("refresh_list", k, number_users));
                    obo.flush();
                    System.out.println("The new list has been sended." + "\n");
                    connected = true;
                }

            }
            //******************************************************
            //*****IF SERVER IS CLOSED THEN CLOSE ALL SOCKETS*******
            obi.close();
            obo.close();
            sslsocket.close();

            //*****************************
            //*********EXCEPTIONS********** 
        } catch (IOException e) {
            System.out.println("IOException" + "\n");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SSL_Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //******************************************************
    //********FUNCTION THAT STARTS SERVERS TIMER************
    public void addtimer() throws RemoteException {

        //****************************************
        //********CREATE THE NEW TIMER************
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            //***************************************************
            //********FUNCTION RUNS WHEN THEAD STARTS************
            public void run() {

                //******************************************************
                //********WHEN CLIENT DOESN'T SENT HEARTBEAT************
                if (!connected) {
                    try {
                        for (int i = 0; i < arraylist.size(); i++) {
                            if (arraylist.get(i).get_nickname().equals(User.get_nickname())) {
                                System.out.println("The user: " + arraylist.get(i).get_nickname() + " kicked off.");
                                arraylist.remove(i);
                            }
                        }
                        obi.close();
                        obo.close();
                        sslsocket.close();
                    } catch (IOException ex) {
                        Logger.getLogger(SSL_Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                try {
                    //*******************************************************************
                    //********CALL AGAIN THE SAME FUNCTION FOR THE NEXT 10SEC************
                    addtimer();
                } catch (RemoteException ex) {
                    Logger.getLogger(SSL_Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                connected = false;
            }
        }, 10 * 1000);
    }
}
