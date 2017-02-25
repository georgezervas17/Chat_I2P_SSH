//Γεώργιος Ζέρβας icsd13055
//Νικόλαος Φουρτούνης icsd13195
//Παύλος Σκούπρας icsd13171

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Chat_GUI extends JFrame {

    // will first hold "Username:", later on "Enter message"
    private JLabel connect_with;
    // to hold the Username and later on the messages
    private JTextField tf;
    // to hold the server address an the port number
    private JTextField tfServer, tfPort;
    // to Logout and get the list of the users
    private JButton login, logout, whoIsIn;
    // for the chat room
    private JTextArea ta;
    // if it is for connection
    private boolean connected;
    // the Client object
    private Chat_Room_Panel client;
    // the default port number
    private int defaultPort;
    private String defaultHost;
    private User_Profile user;

    Chat_GUI() {
        
        //User_Profile p_user
        super("Chat Client");
       // user = p_user;

        // The NorthPanel with:
        JPanel northPanel = new JPanel(new GridLayout(3, 2));
        // the server name anmd the port number
        JPanel serverAndPort = new JPanel(new GridLayout(1, 4, 1, 3));

        connect_with = new JLabel("Connect with", SwingConstants.CENTER);
        connect_with.setFont(new Font("Segoe UI", Font.BOLD, 27));
        
        tf = new JTextField(" ");
        tf.setBackground(Color.WHITE);
        northPanel.add(connect_with);
        northPanel.add(tf);
        add(northPanel, BorderLayout.NORTH);
        
        
        
    ta = new JTextArea("Welcome to the Chat room\n", 80, 80);
		JPanel centerPanel = new JPanel(new GridLayout(1,1));
		centerPanel.add(new JScrollPane(ta));
		ta.setEditable(false);
		add(centerPanel, BorderLayout.CENTER);

		// the 3 buttons
		login = new JButton("Login");
		login.addActionListener((ActionListener) this);
		logout = new JButton("Logout");
		logout.addActionListener((ActionListener) this);
		logout.setEnabled(false);		// you have to login before being able to logout
		whoIsIn = new JButton("Who is in");
		whoIsIn.addActionListener((ActionListener) this);
		whoIsIn.setEnabled(false);		// you have to login before being able to Who is in

		JPanel southPanel = new JPanel();
		southPanel.add(login);
		southPanel.add(logout);
		southPanel.add(whoIsIn);
		add(southPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
		tf.requestFocus();

	}

	
	
    
    // to start the whole thing the server
	public static void main(String[] args) {
		new Chat_GUI();
	}
}
