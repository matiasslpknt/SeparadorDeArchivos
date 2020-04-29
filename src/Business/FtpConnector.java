package Business;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;


public class FtpConnector {
    public FTPClient connect(String url, String usuario, String password){
        FTPClient client = new FTPClient();
        String sFTP = url;
        String sUser = usuario;
        String sPassword = password;
        try {
            client.connect(sFTP);
            boolean login = client.login(sUser,sPassword);
        } catch (IOException ioe) {}
        return client;
    }

    public void disconnect(FTPClient client) throws IOException {
        client.logout();
        client.disconnect();
    }
}
