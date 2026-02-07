import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 5000;
    private static final String LOG_FILE = "server_log.txt";
    private final ExecutorService executor = Executors.newCachedThreadPool();

    static void main() {

    }

    public void start() {
        System.out.println("Server started on port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit()
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
        }

        private String handleRequest(String requestXml) {
            return "OK";
        }

        private String extractTagValue(String xml, String tag) {
            String open = "<" + tag + ">";
            String close = "</" + tag + ">";
            int i = xml.indexOf(open);
            int j = xml.indexOf(close);
            if (i == -1 || j == -1 || j <= i) {
                return null;
            }
            return xml.substring(i + open.length(), j).trim();
        }

        private String escapeXml(String s) {
            if (s == null) {
                return "";
            }
            return s.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
        }

        private void log(String transaction, String userId, String data) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String user = (userId == null || userId.isEmpty()) ? "ANONYMOUS" : userId;

            String line = String.format(
                    "[%s] user=%s transaction=%s data=%s",
                    timestamp, user, transaction, data
            );

            System.out.println(line);
            try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
                fw.write(line + System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
