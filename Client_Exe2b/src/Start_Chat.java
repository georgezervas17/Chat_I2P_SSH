//Γεώργιος Ζέρβας icsd13055
//Νικόλαος Φουρτούνης icsd13195
//Παύλος Σκούπρας icsd13171

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import net.i2p.client.I2PSession;
import net.i2p.client.streaming.I2PServerSocket;
import net.i2p.client.streaming.I2PSocketManager;
import net.i2p.client.streaming.I2PSocketManagerFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Start_Chat extends JFrame implements Serializable {

    private JLabel copyright = null;
    private JLabel chat = null;
    private JLabel options_label = null;
    private JLabel nickname_label = null;
    private JButton continue_button = null;
    private JButton exit_button = null;
    private JTextField nickname_field;
    private JComboBox combo;
    private String[] options = {" ", "Unicast", "Multicast"};
    public static ObjectOutputStream out_Server;
    public static ObjectInputStream in_Server;
    public static ArrayList<User_Profile> arraylist;
    public static User_Profile[] user1;

    public Start_Chat() {

        //*************************************************
        //**************SET UP THE PANEL*******************
        super("Chat Room");
        setSize(650, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        //*************************************************
        //******************GRAPHICS***********************
        chat = new JLabel("Chat Room", JLabel.RIGHT);
        chat.setFont(new Font("Segoe UI", Font.BOLD, 27));
        chat.setBounds(-75, 10, 500, 30);

        nickname_label = new JLabel("Nickname: ", JLabel.RIGHT);
        nickname_label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nickname_label.setBounds(-10, 90, 200, 30);
        nickname_label.setOpaque(true);

        nickname_field = new JTextField(20);
        nickname_field.setBackground(Color.LIGHT_GRAY);
        nickname_field.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nickname_field.setBounds(200, 90, 330, 30);

        options_label = new JLabel("Choose: ", JLabel.RIGHT);
        options_label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        options_label.setBounds(-10, 130, 200, 30);

        combo = new JComboBox(options);
        combo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        combo.setBackground(Color.lightGray);
        combo.setBounds(200, 130, 150, 30);

        continue_button = new JButton("Continue");
        continue_button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        continue_button.setBackground(Color.lightGray);
        continue_button.setBounds(200, 170, 150, 30);

        exit_button = new JButton("Exit");
        exit_button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        exit_button.setBackground(Color.lightGray);
        exit_button.setBounds(360, 170, 150, 30);

        copyright = new JLabel("@Copyright 2016-2017", JLabel.RIGHT);
        copyright.setFont(new Font("Segoe UI", Font.BOLD, 10));
        copyright.setBounds(280, 270, 120, 30);

        continue_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //************************************************
                //*******************I2P SERVER*******************
                int p = combo.getSelectedIndex();

                if (!(nickname_field.getText().equals(" ")) && !(p == 0)) {

                    Security.addProvider(new BouncyCastleProvider());
                    I2PSocketManager manager = I2PSocketManagerFactory.createManager();
                    I2PServerSocket serverSocket = manager.getServerSocket();
                    I2PSession session = manager.getSession();
                    System.out.println("This is Server Destination in form Base64: \n"
                            + session.getMyDestination().toBase64() + "\n");
                    System.out.println("IP CLIENT socket" + serverSocket + "\n");
                    I2P_Session s = new I2P_Session(manager, serverSocket, session);

                    //*************************************************
                    //*********CREATE USER_PROFILE OBJECT**************
                    User_Profile user = new User_Profile(nickname_field.getText(), session.getMyDestination().toBase64(), p);

                    try {
                        //*************************************************
                        //*********SSL HANDSHAKE WITH SERVER***************
                        SSLSocket socket = null;

                        socket = SSL_HandShake(user);

                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Start_Chat.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    try {
                        //*************************************************
                        //*********CALL THE PANEL CHAT_ROOM****************
                        dispose();
                        String msg = null;
                        ArrayList<Send_Message> messagesList = new ArrayList<Send_Message>();
                        Chat_Room_Panel panel = new Chat_Room_Panel(s, user, user1, out_Server, in_Server, messagesList);

                    } catch (IOException ex) {
                        Logger.getLogger(Start_Chat.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    JOptionPane.showMessageDialog(rootPane, "Fill the empty fields!");
                }
            }
        });
        exit_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        Container pane = getContentPane();
        pane.setLayout(null);
        pane.add(nickname_label);
        pane.add(nickname_field);
        pane.add(continue_button);
        pane.add(exit_button);
        pane.add(options_label);
        pane.add(combo);
        pane.add(copyright);
        setContentPane(pane);
    }

    private static SSLSocket SSL_HandShake(User_Profile user) throws ClassNotFoundException {

        //*************************************************
        //****LOAD THE CERTIFICATE WITH HIS KEYSTORE*******
        System.setProperty("javax.net.ssl.trustStore", "C:\\killek\\KeyStore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");

        PrintStream out = System.out;
        //*************************************************
        //*********CREATE A SSLSOCKET FACTORY*****************

        SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket c = null;
        try {

            //*************************************************
            //*********CONNECT TO PORT 9999,LOCALHOST**********       
            c = (SSLSocket) f.createSocket("localhost", 9999);
            

            //**********************************************
            //*********TRY TO MAKE A HANDSHAKE************** 
            c.addHandshakeCompletedListener(new MyHandshakeListener());

            //*************************************************
            //*********TRY TO MAKE A HANDSHAKE*****************
            c.startHandshake();
            System.out.println("Info SSLSocket: "+c.toString());
            printSocketInfo(c);

            //*************************************************
            //*********OBJECT INPUT/OUTPUT STREAM**************
            out_Server = new ObjectOutputStream(c.getOutputStream());
            in_Server = new ObjectInputStream(c.getInputStream());

            //*************************************************
            //****DATE TO MAKE A SIGNATURE FOR EVERY MESSAGE***
            Date k = new Date();

            //*************************************************
            //********SEND THE FIRST MESSAGE TO SERVER*********
            out_Server.writeObject(new Send_Message("START", k, user));
            out_Server.flush();
            System.out.println("Client want to connect to server.");

              //*************************************************
            //*******CREATE AN OBJECT FOR CLIENTS TIMER**********
            new Timer(c,out_Server,in_Server).start();
            
            //*************************************************
            //********RECIEVE A MESSAGE FROM SERVER************
            Send_Message m = (Send_Message) in_Server.readObject();

            user1 = new User_Profile[m.getnumber()];
            user1 = m.getlist();

            if (m.getmessage().equals("list")) {
                System.out.println("Client is connected to server and the list is recieved!");
            } else {
                System.out.println("Client isn't connected to server and the list isn't recieved!");
            }

            //w.close();
            //r.close();
            // c.close();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
        //*************************************************
        //***********RETURN SSL SOCKET TO CLASS************
        return c;
    }

    //*************************************************
    //*********FUNCTION TO PRINT SOCKET INFO***********
    private static void printSocketInfo(SSLSocket s) {
        System.out.println("SOCKET INFORMATION " + "\n");
        System.out.println("Socket class: " + s.getClass());
        System.out.println("   Remote address = "
                + s.getInetAddress().toString());
        System.out.println("   Remote port = " + s.getPort());
        System.out.println("   Local socket address = "
                + s.getLocalSocketAddress().toString());
        System.out.println("   Local address = "
                + s.getLocalAddress().toString());
        System.out.println("   Local port = " + s.getLocalPort());
        System.out.println("   Need client authentication = "
                + s.getNeedClientAuth());
        SSLSession ss = s.getSession();
        System.out.println("   Cipher suite = " + ss.getCipherSuite());
        System.out.println("   Protocol = " + ss.getProtocol() + "\n");

        System.out.println("END OF SOCKET INFORMATION " + "\n");

    }

    //*************************************************
    //********MESSAGES WHEN WE HAVE HANDSHAKE**********
    public static class MyHandshakeListener implements HandshakeCompletedListener {

        public void handshakeCompleted(HandshakeCompletedEvent e) {
            System.out.println("Handshake succesful!");
            System.out.println("Using cipher suite: " + e.getCipherSuite());
            System.out.println(" ");
        }
    }

    //*************************************************
    //*****FUNCTION TO SEND A HEARTBEAT TO SERVER******
    public static ArrayList<User_Profile> heartbeat(String heartbeat, SSLSocket socket) throws IOException, ClassNotFoundException {

        out_Server = new ObjectOutputStream(socket.getOutputStream());
        in_Server = new ObjectInputStream(socket.getInputStream());

        out_Server.writeObject(new Send_Message("HEARTBEAT"));
        out_Server.flush();

        Send_Message ss = (Send_Message) in_Server.readObject();
        return ss.getArrayList();

    }

}
