import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Client {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5000;

    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static Client getDefault() {
        return new Client(DEFAULT_HOST, DEFAULT_PORT);
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

    public static Response parseResponse(String responseXml) {
        if (responseXml == null || responseXml.isEmpty()) {
            return null;
        }
        String status = extractTagValue(responseXml, "status");
        String message = extractTagValue(responseXml, "message");
        return new Response(status != null ? status : "", message != null ? message : "");
    }

    private static String extractTagValue(String xml, String tag) {
        String open = "<" + tag + ">";
        String close = "</" + tag + ">";
        int i = xml.indexOf(open);
        int j = xml.indexOf(close);
        if (i == -1 || j == -1 || j <= i) {
            return null;
        }
        return xml.substring(i + open.length(), j).trim();
    }

    private static String escapeXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    public static class Response {
        public final String status;
        public final String message;

        public Response(String status, String message) {
            this.status = status != null ? status : "";
            this.message = message != null ? message : "";
        }

        public boolean isOk() {
            return "OK".equalsIgnoreCase(status);
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

