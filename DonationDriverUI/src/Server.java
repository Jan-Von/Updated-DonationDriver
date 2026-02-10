import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 5267;
    private static final String LOG_FILE = "server_log.txt";
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        System.out.println("Server started on port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String clientInfo = socket.getInetAddress() + ":" + socket.getPort();
            System.out.println("Client connected: " + clientInfo);

            try (
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    BufferedWriter out = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream(), "UTF-8"))
            ) {
                String line;
                while ((line = in.readLine()) != null) {
                    String requestXml = line.trim();
                    if (requestXml.isEmpty()) {
                        continue;
                    }
                    String responseXml = handleRequest(requestXml);
                    out.write(responseXml);
                    out.newLine();
                    out.flush();
                }
            } catch (IOException e) {
                System.out.println("Client disconnected: " + clientInfo);
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }

        private String handleRequest(String requestXml) {
            String action = extractTagValue(requestXml, "action");
            String userId = extractTagValue(requestXml, "userId");
            log("REQUEST", userId, requestXml);

            String status = "ERROR";
            String message;

            if (action == null) {
                message = "Missing <action> in request.";
            } else {
                //To do: return real data or errors. Initial code for switch-case
                switch (action) {
                    case "LOGIN": {
                        String email = extractTagValue(requestXml, "email");
                        String password = extractTagValue(requestXml, "password");
                        if (email == null || password == null) {
                            message = "Missing email or password.";
                        } else if (authenticateUser(email.trim(), password.trim())) {
                            status = "OK";
                            message = "Login successful.";
                            userId = email.trim();
                        } else {
                            message = "Invalid email or password.";
                        }
                        break;
                    }
                    case "REGISTER": {
                        String email = extractTagValue(requestXml, "email");
                        String password = extractTagValue(requestXml, "password");
                        String firstName = extractTagValue(requestXml, "firstName");
                        String lastName = extractTagValue(requestXml, "lastName");
                        String middleName = extractTagValue(requestXml, "middleName");
                        String dateOfBirth = extractTagValue(requestXml, "dateOfBirth");
                        String address = extractTagValue(requestXml, "address");
                        String phone = extractTagValue(requestXml, "phone");

                        if (email == null || password == null) {
                            message = "Missing email or password.";
                        } else if (userXmlExists(email.trim())) {
                            message = "Registration failed: email already exists.";
                        } else if (saveUserToXml(email.trim(), password.trim(),
                                firstName, lastName, middleName, dateOfBirth, address, phone)) {
                            status = "OK";
                            message = "Registration successful.";
                        } else {
                            message = "Registration failed due to server error.";
                        }
                        break;
                    }
                    case "CREATE_TICKET":
                        status = "OK";
                        message = "CREATE_TICKET received...";
                        break;
                    case "READ_TICKETS":
                        status = "OK";
                        message = "READ_TICKETS received...";
                        break;
                    case "UPDATE_TICKET":
                        status = "OK";
                        message = "UPDATE_TICKET received...";
                        break;
                    case "DELETE_TICKET":
                        status = "OK";
                        message = "DELETE_TICKET received...";
                        break;
                    case "PING":
                        status = "OK";
                        message = "PONG from DonationServer.";
                        break;
                    default:
                        message = "Unknown action: " + action;
                }
            }
            String responseXml =
                    "<response>" +
                            "<status>" + escapeXml(status) + "</status>" +
                            "<message>" + escapeXml(message) + "</message>" +
                            (userId != null && !userId.isEmpty()
                                    ? "<userId>" + escapeXml(userId) + "</userId>"
                                    : "") +
                            "</response>";
            log("RESPONSE", userId, responseXml);
            return responseXml;
        }

        private static final String USERS_CSV = "users.csv";

        private boolean authenticateUser(String email, String password) {
            File file = new File(USERS_CSV);
            if (!file.exists()) return false;
            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine().trim();
                    if (line.isEmpty()) continue;
                    String[] u = line.split(",", -1);
                    if (u.length >= 2) {
                        if (u[0].trim().equals(email) && u[1].trim().equals(password)) {
                            return true;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }


        private boolean registerUser(String email, String password) {
            File file = new File(USERS_CSV);
            if (file.exists()) {
                try (Scanner sc = new Scanner(file)) {
                    while (sc.hasNextLine()) {
                        String line = sc.nextLine().trim();
                        if (line.isEmpty()) continue;
                        String[] u = line.split(",", -1);
                        if (u.length >= 1 && u[0].trim().equalsIgnoreCase(email)) {
                            return false;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            try (FileWriter fw = new FileWriter(USERS_CSV, true)) {
                fw.write(email + "," + password + "\n");
                fw.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
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

        // Store each user as a separate XML file under "users"
        private static final String USERS_DIR = "users";

        private boolean userXmlExists(String email) {
            File dir = new File(USERS_DIR);
            File file = new File(dir, email + ".xml");
            return file.exists();
        }

        private boolean saveUserToXml(
                String email,
                String password,
                String firstName,
                String lastName,
                String middleName,
                String dateOfBirth,
                String address,
                String phone
        ) {
            try {
                File dir = new File(USERS_DIR);
                if (!dir.exists() && !dir.mkdirs()) {
                    return false;
                }

                File file = new File(dir, email + ".xml");

                StringBuilder sb = new StringBuilder();
                sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                sb.append("<user>");
                sb.append("<email>").append(escapeXml(email)).append("</email>");
                sb.append("<password>").append(escapeXml(password)).append("</password>");
                if (firstName != null) {
                    sb.append("<firstName>").append(escapeXml(firstName)).append("</firstName>");
                }
                if (lastName != null) {
                    sb.append("<lastName>").append(escapeXml(lastName)).append("</lastName>");
                }
                if (middleName != null) {
                    sb.append("<middleName>").append(escapeXml(middleName)).append("</middleName>");
                }
                if (dateOfBirth != null) {
                    sb.append("<dateOfBirth>").append(escapeXml(dateOfBirth)).append("</dateOfBirth>");
                }
                if (address != null) {
                    sb.append("<address>").append(escapeXml(address)).append("</address>");
                }
                if (phone != null) {
                    sb.append("<phone>").append(escapeXml(phone)).append("</phone>");
                }
                sb.append("</user>");

                try (FileWriter fw = new FileWriter(file, false)) {
                    fw.write(sb.toString());
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
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
