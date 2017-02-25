//Γεώργιος Ζέρβας icsd13055
//Νικόλαος Φουρτούνης icsd13195
//Παύλος Σκούπρας icsd13171

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.i2p.I2PException;
import net.i2p.client.I2PSession;
import net.i2p.client.streaming.I2PServerSocket;
import net.i2p.client.streaming.I2PSocket;
import net.i2p.client.streaming.I2PSocketManager;
import net.i2p.data.DataFormatException;
import net.i2p.data.Destination;
import net.i2p.util.I2PThread;

public class Chat_Room_Panel extends JFrame implements Serializable, ActionListener {

    private final JLabel chat_label;
    private final JLabel connection_label;
    private JLabel messages;
    private JLabel online;
    private final JLabel user_online;
    private final JTextField chat_with_field;
    public static JTextArea messages_area = null;
    public static  JTextArea online_users_area;
    public static JTextArea send_messages_area;
    private JLabel online_users_label;
    private final JButton send_button;
    private static JButton connect_button;
    public static JButton refresh = null;
    private final JButton disconnect_button;
    private JScrollPane scrollpane1;
    private JScrollPane scrollpane2;
    private final JLabel copyright;
    public static User_Profile user;
    public Container pane;
    public SSLSocket socket;

    public static User_Profile[] Online_users_list;
    public static ArrayList<User_Profile> choosen_users = new ArrayList<User_Profile>();
    public Destination destination;
    public String msg;
    public JScrollPane scroll;

    //*******CONNECTION
    ObjectOutputStream out_Server = null;
    ObjectInputStream in_Server = null;

    private ObjectInputStream sInput;
    private ObjectOutputStream sOutput;

    public static int k = 0;
    public static int m = 0;
    public int number_of_user = 0;
    public int times_called = 0;
    public static boolean boolean_connected = false;

    public static I2PServerSocket serverSocket;
    public static I2PSocket[] clientSocket;
    public static I2PSession session;
    public static I2PSocketManager manager;
    public static ObjectInputStream in_server;
    public static ObjectOutputStream out_server;
    public static ObjectInputStream[] in_client;
    public static ObjectOutputStream[] out_client;
    public static I2P_Session i2p_session;
    public static String what_are_you = null;

    public static ArrayList<Send_Message> messagesList;

    public Chat_Room_Panel(I2P_Session p_s, User_Profile p_user, User_Profile[] p_a, ObjectOutputStream p_obo, ObjectInputStream p_obi, ArrayList<Send_Message> messagesList) throws IOException {

        super("Chat Form");

        setSize(750, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        //*************************************************
        //******************VARIABLE***********************
        this.messagesList = messagesList;
        times_called = times_called + 1;
        user = p_user;
        i2p_session = p_s;
        Online_users_list = p_a;
        out_Server = p_obo;
        in_Server = p_obi;

        chat_label = new JLabel("Chat Room", JLabel.RIGHT);
        chat_label.setFont(new Font("Segoe UI", Font.BOLD, 27));
        chat_label.setBounds(-75, 10, 500, 30);

        user_online = new JLabel("Your nickname is: " + user.get_nickname(), JLabel.RIGHT);
        user_online.setHorizontalAlignment(JLabel.LEFT);
        user_online.setForeground(Color.red);
        user_online.setFont(new Font("Segoe UI", Font.BOLD, 22));
        user_online.setBounds(25, 60, 500, 30);

        connection_label = new JLabel("Chat with: ", JLabel.RIGHT);
        connection_label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        connection_label.setBounds(-20, 90, 150, 30);

        chat_with_field = new JTextField(20);
        chat_with_field.setBackground(Color.LIGHT_GRAY);
        chat_with_field.setFont(new Font("Segoe UI", Font.BOLD, 16));
        chat_with_field.setBounds(130, 90, 380, 30);

        connect_button = new JButton("Connect");
        connect_button.setBackground(Color.lightGray);
        connect_button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        connect_button.setBounds(520, 90, 150, 30);

        if (times_called == 1) {
            messages_area = new JTextArea(10, 10);
            messages_area.setBackground(Color.LIGHT_GRAY);
            messages_area.setFont(new Font("Segoe UI", Font.BOLD, 18));
            messages_area.setBounds(30, 150, 400, 350);
            messages_area.setEditable(false);
            messages_area.setLineWrap(true);
            messages_area.setWrapStyleWord(true);

        } else {
            messages_area = new JTextArea(10, 10);
            messages_area.setBackground(Color.LIGHT_GRAY);
            messages_area.setFont(new Font("Segoe UI", Font.BOLD, 16));
            messages_area.setBounds(30, 150, 400, 350);
            messages_area.setEditable(false);
            messages_area.setLineWrap(true);
            messages_area.setWrapStyleWord(true);
        }
        scroll = new JScrollPane(messages_area);

        online_users_area = new JTextArea(10, 10);
        for (int i = 0; i < Online_users_list.length; i++) {
            if (!user.get_nickname().equals(Online_users_list[i].get_nickname())) {
                online_users_area.append(Online_users_list[i].get_nickname() + "\n");
                online_users_area.setForeground(Color.green);
                online_users_area.setBackground(Color.LIGHT_GRAY);
                online_users_area.setFont(new Font("Segoe UI", Font.BOLD, 20));
                online_users_area.setBounds(450, 150, 220, 350);
                online_users_area.setEditable(false);
                online_users_area.setLineWrap(true);
                online_users_area.setWrapStyleWord(true);
            } else if (user.get_nickname().equals(Online_users_list[i].get_nickname()) && Online_users_list.length == 1) {
                JOptionPane.showMessageDialog(rootPane, "No one to chat!");
            }
        }

        send_messages_area = new JTextArea(2, 2);
        send_messages_area.setBackground(Color.LIGHT_GRAY);
        send_messages_area.setFont(new Font("Segoe UI", Font.BOLD, 15));
        send_messages_area.setBounds(30, 520, 400, 60);
        send_messages_area.setEditable(true);
        send_messages_area.setLineWrap(true);
        send_messages_area.setWrapStyleWord(true);

        send_button = new JButton("Send");
        send_button.setBackground(Color.lightGray);
        send_button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        send_button.setBounds(450, 520, 90, 60);

        disconnect_button = new JButton("Disconect");
        disconnect_button.setBackground(Color.lightGray);
        disconnect_button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        disconnect_button.setBounds(30, 600, 150, 30);

        refresh = new JButton("Refresh");
        refresh.setBackground(Color.lightGray);
        refresh.setFont(new Font("Segoe UI", Font.BOLD, 20));
        refresh.setBounds(550, 600, 150, 30);

        copyright = new JLabel("@Copyright 2016-2017", JLabel.RIGHT);
        copyright.setFont(new Font("Segoe UI", Font.BOLD, 10));
        copyright.setBounds(280, 630, 120, 30);

        refresh.addActionListener(this);
        connect_button.addActionListener(this);
        send_button.addActionListener(this);
        disconnect_button.addActionListener(this);
        pane = getContentPane();

        pane.setLayout(null);
        pane.add(chat_label);
        pane.add(user_online);
        pane.add(connection_label);
        pane.add(chat_with_field);
        pane.add(connect_button);
        pane.add(messages_area);
        pane.add(online_users_area);
        pane.add(send_messages_area);
        pane.add(send_button);
        pane.add(disconnect_button);
        pane.add(copyright);
        pane.add(refresh);
        pane.add(scroll);
        setContentPane(pane);
        ChatServer();

    }

    public void neo(ArrayList<Send_Message> list) {

        pane = getContentPane();

        pane.setLayout(null);
        pane.add(chat_label);
        pane.add(user_online);
        pane.add(connection_label);
        pane.add(chat_with_field);
        pane.add(connect_button);
        pane.add(messages_area);
        pane.add(online_users_area);
        pane.add(send_messages_area);
        pane.add(send_button);
        pane.add(disconnect_button);
        pane.add(copyright);
        pane.add(refresh);
        setContentPane(pane);
    }

    //*************************************************
    //*********ACTIONLISTENERS FOR BUTTONS*************
    public void actionPerformed(ActionEvent e) {

        Object o = e.getSource();

        //*************************************************
        //***************REFRESH BUTTONS*******************
        if (o == refresh) {
            try {
                System.out.println(" ");
                System.out.println("Client sends a message to server,to refresh his list.");

                //*************************************************
                //******SEND TO SERVER REFRESH MESSAGE*************
                out_Server.writeObject(new Send_Message("REFRESH"));
                out_Server.flush();

                k = k + 1;

                //*************************************************
                //******SEND TO SERVER REFRESH MESSAGE*************
                Send_Message servers_msg = (Send_Message) in_Server.readObject();

                //*************************************************             
                //****WE REFRESH THE PANEL WITH THE NEW LIST*******
                Online_users_list = servers_msg.getlist();

                online_users_area.setText("");
                for (int i = 0; i < servers_msg.getlist().length; i++) {
                    if (!user.get_nickname().equals(servers_msg.getlist()[i].get_nickname())) {
                        online_users_area.append(servers_msg.getlist()[i].get_nickname() + "\n");
                        online_users_area.setForeground(Color.green);
                        online_users_area.setBackground(Color.LIGHT_GRAY);
                        online_users_area.setFont(new Font("Segoe UI", Font.BOLD, 20));
                        online_users_area.setBounds(450, 150, 220, 350);
                        online_users_area.setEditable(false);
                        online_users_area.setLineWrap(true);
                        online_users_area.setWrapStyleWord(true);
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(Chat_Room_Panel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Chat_Room_Panel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //*************************************************
        //***************CONNECT BUTTON********************
        if (o == connect_button) {

            //*************************************************
            //**************VARIABLES**************************
            String users_to_chat = chat_with_field.getText();
            String[] users_to_check = users_to_chat.split(",");
            boolean flag = false;
            String ip = null;

            System.out.println(" ");
            System.out.println("Client press connected button to chat with someone.");

            //*************************************************
            //*******CHECK WHAT MODE WE HAVE SELECTED**********
            boolean ok = false;

            if (user.getoption() == 1 && users_to_check.length > 2) {
                JOptionPane.showMessageDialog(null,
                        "You have select Unicast!",
                        "Not Accepted", JOptionPane.INFORMATION_MESSAGE);
                chat_with_field.setText(" ");
            } else if (user.getoption() == 2 && users_to_check.length < 2) {
                JOptionPane.showMessageDialog(null,
                        "You have select Multicast!",
                        "Not Accepted", JOptionPane.INFORMATION_MESSAGE);
                chat_with_field.setText(" ");
            } else {
                ok = true;
            }

            //***********************************************************************************
            //******IF WE HAVE THE RIGHT MODE AND THE RIGHT NUMBER OF USERS CONTINUE*************
            if (users_to_check.length > 0 && ok == true) {

                System.out.println(" ");

                boolean are_all = false;
                int counter = 0;

                //*************************************************
                //******COMPARE TWO ARRAYS TO FIND THE USERS*******
                for (int i = 0; i < Online_users_list.length; i++) {
                    for (int j = 0; j < users_to_check.length; j++) {

                        //*************************************************
                        //******CHECK THE USER ONE BY ONE******************
                        if (users_to_check[j].equals(Online_users_list[i].get_nickname())) {
                            //*************************************************
                            //********ADD THE USERS INTO A NEW LIST************
                            choosen_users.add(Online_users_list[i]);
                            counter++;
                            //*********************************************************************
                            //***********WE CHECK IF ALL CHOOSEN USERS ARE IN THE LIST*************
                            if (counter == users_to_check.length) {
                                are_all = true;
                                System.out.println("All users are correctely writed." + "\n");
                            }
                        }
                    }
                }

                //*********************************************************************
                //*******IF EVERYTHING IS OK THEN CONTINUE TO MAKE A CONNECTION********
                if (are_all == true && (user.getoption() == 1 || user.getoption() == 2)) {
                    try {
                        //*********************************************************************
                        //***********CALL THE SUITABLE FUNCTION TO MAKE CONNECTION*************
                        System.out.println("Try to connect with:");
                        Connect_To_Other_User(choosen_users);

                    } catch (I2PException ex) {
                        Logger.getLogger(Chat_Room_Panel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ConnectException ex) {
                        Logger.getLogger(Chat_Room_Panel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NoRouteToHostException ex) {
                        Logger.getLogger(Chat_Room_Panel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedIOException ex) {
                        Logger.getLogger(Chat_Room_Panel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Chat_Room_Panel.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Chat_Room_Panel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                System.out.println("Someone is not in the list");
                JOptionPane.showMessageDialog(null,
                        "Someone is not in the list!",
                        "Not Accepted", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        //*****************************************
        //*****************SEND BUTTON*************
        if (o == send_button) {

            //************************************************************
            //**********ADD THE NEW MESSAGE INTO AN ARRAYLIST*************
            String msg = send_messages_area.getText();
            String who_msg = user.get_nickname();
            Chat_Room_Panel.messagesList.add(new Send_Message(msg, who_msg));
            messages_area.append(who_msg + " > " + msg + "\n");

            //*************************************************************************************************
            //**********CHECK WHAT WE ARE LEADER OR NOT LEADER OF THE CONVESATION AND ACT DIFFEREN*************
            try {
                //*************************************************************************
                //**********IF WE ARE LEADER WE SHOULD SEND TO ALL OUR MESSAGE*************
                if (choosen_users.size() > 1) {

                    for (int i = 0; i < choosen_users.size(); i++) {
                        System.out.println("SEND" + out_client[i]);
                        out_client[i].writeObject(new Send_Message(msg, who_msg));
                        out_client[i].flush();
                        send_messages_area.setText(" ");
                        System.out.println("The message has been send to"+ choosen_users.get(i).get_nickname());
                    }
                    System.out.println(" ");

                    //***********************************************************************************
                    //**********IF WE ARE NOT LEADER WE SEND TO THE OTHER CLIENT THE MESSAGE*************
                } else if (user.getwhat().equals("not leader")) {

                    out_client[0].writeObject(new Send_Message(Chat_Room_Panel.send_messages_area.getText(), user.get_nickname()));
                    out_client[0].flush();
                    send_messages_area.setText(" ");
                    System.out.println("The message has been send");
                }
            } catch (IOException ex) {
                Logger.getLogger(Chat_Room_Panel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //****************************************
        //**********DISCONNECT BUTTON*************
        if (o == disconnect_button) {
            try {
                out_Server.writeObject(new Send_Message("EXIT"));
                out_Server.flush();
                System.out.println("Client has disconnected!");
                dispose();
            } catch (IOException ex) {
                Logger.getLogger(Chat_Room_Panel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

//*************************************************
//*********CONNECT WITH THE OTHER USER*************
    void Connect_To_Other_User(ArrayList<User_Profile> choosen_users) throws I2PException, ConnectException, NoRouteToHostException, InterruptedIOException, IOException, ClassNotFoundException {

        //************************************************************
        //**********VARIABLES FOR THE FUNCTIONALITY*******************
        I2PThread[] t = new I2PThread[choosen_users.size()];
        clientSocket = new I2PSocket[choosen_users.size()];
        out_client = new ObjectOutputStream[choosen_users.size()];
        in_client = new ObjectInputStream[choosen_users.size()];
        int want_all_to_chat = 0;
        Send_Message send;

        //************************************************************
        //**********FOR EVERYONE WE START THE CONNECTION**************
        for (int i = 0; i < choosen_users.size(); i++) {
            try {
                //************************************************************
                //**********TAKE EACH DESTINATION FOR EVERY USER**************
                destination = new Destination(choosen_users.get(i).getip());
            } catch (DataFormatException ex) {
                System.out.println("Destination string incorrectly formatted." + "\n");
                return;
            }

            //************************************************************
            //**********OPEN A SOCKET FOR THIS DESTINATION****************
            clientSocket[i] = i2p_session.getmanager().connect(destination);

            //************************************************************
            //**********CREATE THE RESPECTIVELY OBJECT STREAMS************
            out_client[i] = new ObjectOutputStream((clientSocket[i].getOutputStream()));
            in_client[i] = new ObjectInputStream((clientSocket[i].getInputStream()));

            //************************************************************************************
            //**********WE SENT THE FIRST MESSAGE TO THE OTHER USER WE WANT TO CONNECT************
            out_client[i].writeObject(new Send_Message("LOGIN", "Start"));
            out_client[i].flush();

            System.out.println("Waiting the other client to accept or reject our request.");
            chat_with_field.setText(" ");
            
            //***************************************************************
            //**********WE RECIEVE THE ANSWER FROM THE OTHER USER************
            send = (Send_Message) in_client[i].readObject();
            System.out.println("The other client sends: " + send.getmessage());

            //********************************************************************
            //**********IF SENT EXIT THEN WE INTERRUPT THE CONNECTION*************
            if (send.getmessage().equals("EXIT")) {

                JOptionPane.showMessageDialog(null,
                        "The other user has not accepted our request!",
                        "Not Accepted", JOptionPane.INFORMATION_MESSAGE);
                        

                //*****************************************************************
                //**********IF SENT ACCEPT THEN CONTINUE THE CONNECTION************
            } else {
                System.out.println("The chat has started ");

                //*****************************************************************
                //**********OPEN A NEW THREAD FOR ALL THE NEW USERS****************
                t[i] = new I2PThread(new ManageUser(i2p_session.serverSocket, clientSocket[i], i));
                t[i].setName("cManageServer2");
                t[i].setDaemon(false);
                t[i].start();
                want_all_to_chat++;
            }
        }

        //*****************************************************************************
        //**********IF ALL IS OK THEN WE OPEN A NEW PANEL TO START CHATTING************
        if (want_all_to_chat > 0) {

            messages_area.append("Start Chatting..." + "\n");
            connect_button.setVisible(false);
            refresh.setVisible(false);
        }

    }
    //*********************************************************************
    //****************************SERVER MODE******************************

    public void ChatServer() throws IOException {

        //*********************************************************************
        //********CREATE A SOCKET FOR THE CLIENT THAT IS CONNECTED TO US*******
        I2PThread t = new I2PThread(new ManageUser(i2p_session.serverSocket));
        t.setName("ManageServer1");
        t.setDaemon(false);

        //******************************************************************
        //*************************START RUN FUNCTION***********************
        t.start();

    }

    public static class ManageUser extends Thread implements Serializable {

        //******************************
        //*********VARIABLES************
        boolean connected;
        I2PSocket clientSocket;
        int number_client = 0;

        //*********************************************************************
        //**********WE HAVE 2 CONSTRUCTORS FOR DIFFERENT SITUATIONS************
        public ManageUser(I2PServerSocket socket) {
            serverSocket = socket;
            connected = false;
        }

        //*********************************************************************
        //****************IF WE HAVE CREATED THE SOCKET************************
        public ManageUser(I2PServerSocket socket, I2PSocket clientSocket, int i) {
            serverSocket = socket;
            this.clientSocket = clientSocket;
            connected = true;
            number_client = i;
        }

        //**********************************
        //**********RUN FUNCTION************
        public void run() {
            Send_Message message;
            try {
                //*********************************************************************
                //******************ACCEPT THE SOCKET FROM USER************************
                Send_Message moo;

                //*********************************************************************
                //******IF WE RECIEVE A REQUEST FOR CONNECT WE ARE NOT CONNECTED*******
                if (!connected) {

                    user.setwhat("not leader");
                    clientSocket = serverSocket.accept();
                    in_client = new ObjectInputStream[1];
                    out_client = new ObjectOutputStream[1];
                    in_client[0] = new ObjectInputStream((clientSocket.getInputStream()));
                    out_client[0] = new ObjectOutputStream((clientSocket.getOutputStream()));

                    if (clientSocket != null) {

                        //*********************************************************************
                        //***************RECIEVE THE MESSAGE FROM OTHER USER*******************
                        moo = (Send_Message) in_client[0].readObject();

                        //*********************************************************************
                        //***********************CHECK THE MESSAGE FROM USER*******************
                        if (moo.getmessage().equals("LOGIN")) {

                            //*********************************************************************
                            //***************************ASK FOR CONNECTION************************
                            int selectedOption = JOptionPane.showConfirmDialog(null,
                                    "Do you wanna chat?",
                                    "Choose",
                                    JOptionPane.YES_NO_OPTION);

                            //***********************************************
                            //****************CHECK OUR DECISION*************
                            if (!(selectedOption == 1)) {

                                //*********************************************************************
                                //*******************Continue the protocoll****************************
                                System.out.println(" ");
                                System.out.println("The user accept the request to.");

                                //*********************************************************************
                                //**************IF WE ACCEPT THE REQUEST WE SHOULD SEND ACCEPT*********
                                out_client[0].writeObject(new Send_Message("ACCEPT"));
                                out_client[0].flush();

                                System.out.println("You are:" + user.get_nickname() + "\n");
                                Chat_Room_Panel.messages_area.append("Start Chatting..." + "\n");

                                connect_button.setVisible(false);
                                refresh.setVisible(false);

                                //*********************************************************************
                                //****************THE WHILE TO CONNECT EACH OTHER**********************
                                while ((moo = (Send_Message) in_client[0].readObject()) != null) {

                                    //***********************************************
                                    //**********IF THE OTHER USER SENT EXIT**********
                                    if (moo.getmessage().equals("EXIT")) {
                                        break;
                                    }
                                    messages_area.append(moo.getmeg() + " > " + moo.getmessage() + "\n");
                                    System.out.println(moo.getmeg() + " > " + moo.getmessage());
                                    Chat_Room_Panel.messagesList.add(moo);
                                }
                            } else {
                                System.out.println("We dont accept the request.");

                                out_client[0].writeObject(new Send_Message("EXIT"));
                                out_client[0].flush();
                            }
                            //*********************************************************************
                            //****************IF WE DONT ACCEPT THE REQUEST************************   
                        }

                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Chat_Room_Panel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Chat_Room_Panel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (I2PException ex) {
                Logger.getLogger(Chat_Room_Panel.class.getName()).log(Level.SEVERE, null, ex);

            }
            //*********************************************************************************
            //*******IF WERE ARE ALREADY CONNECTED AND IF WE ARE LEADER OF THE MULTICAST*******
            if (connected) {

                System.out.println("We are already connected " + user.get_nickname());
                Send_Message moo;
                int exit_number = 0;
                //***********************************************************
                //*****OPEN THE WHILE TO WAIT FOR OTHER TO SEND MESSAGES*****

                try {
                    while ((moo = (Send_Message) in_client[number_client].readObject()) != null) {

                        messages_area.append(moo.getmeg() + " > " + moo.getmessage() + "\n");
                        System.out.println(moo.getmeg() + " > " + moo.getmessage());
                        Chat_Room_Panel.messagesList.add(moo);

                        //*****************************************************************************
                        //****IF SOMEONE SEND A MESSAGE WE SHOULD INFORM ALL THE OTHERS FOR THIS*******
                        if (choosen_users.size() > 1) {
                            //*******************************************************
                            //**********FOR TO INFORM ALL THE OTHER USERS************
                            for (int i = 0; i < choosen_users.size(); i++) {
                                if (number_client == i) {

                                } else {
                                    out_client[i].writeObject(new Send_Message(moo.getmessage(), moo.getmeg()));
                                    out_client[i].flush();
                                }
                            }
                        }
                        //***************************************************
                        //**********IF ALL IS OUT, WE SHOULD STOP************
                        if (moo.getmessage().equals("EXIT") && exit_number == choosen_users.size()) {
                            break;
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Chat_Room_Panel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Chat_Room_Panel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }
}
