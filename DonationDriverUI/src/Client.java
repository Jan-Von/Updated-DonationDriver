import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Client {

    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String sendRequest(String requestXml) throws IOException {
        try (Socket socket = new Socket(host, port);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"))
        ) {
            out.write(requestXml);
            out.newLine();
            out.flush();

            String responseXml = in.readLine();
            if (responseXml == null) {
                return null;
            }
            return responseXml.trim();
        }
    }

    static void main() {
        Client client = new Client("localhost", 5000);

        String request =
                "<request>" +
                        "<action>PING</action>" +
                        "<userId>test-user</userId>" +
                        "</request>";
        try {
            String response = client.sendRequest(request);
            System.out.println("Server response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

