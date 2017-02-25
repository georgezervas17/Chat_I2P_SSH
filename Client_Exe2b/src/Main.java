//Γεώργιος Ζέρβας icsd13055
//Νικόλαος Φουρτούνης icsd13195
//Παύλος Σκούπρας icsd13171

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;



public class Main implements Serializable {

    public static String modifiedSentence;
    public static BufferedReader inFromUser = null;
    public static DataOutputStream outToServer = null;
    public static BufferedReader inFromServer = null;

    public static void main(String[] args) throws IOException, ClassNotFoundException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException, InterruptedException {

        Start_Chat sc = new Start_Chat();
    }
}
