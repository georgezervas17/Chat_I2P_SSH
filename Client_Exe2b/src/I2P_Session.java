//Γεώργιος Ζέρβας icsd13055
//Νικόλαος Φουρτούνης icsd13195
//Παύλος Σκούπρας icsd13171

import net.i2p.client.I2PSession;
import net.i2p.client.streaming.I2PServerSocket;
import net.i2p.client.streaming.I2PSocketManager;

public class I2P_Session {

    I2PSocketManager manager;
    I2PServerSocket serverSocket;
    I2PSession session;

    I2P_Session(I2PSocketManager p_m, I2PServerSocket p_sock, I2PSession p_sess) {
        manager = p_m;
        serverSocket = p_sock;
        session = p_sess;
    }

    I2PSocketManager getmanager() {
        return manager;
    }

    I2PServerSocket getsocker() {
        return serverSocket;
    }

    I2PSession getsession() {
        return session;
    }

}
