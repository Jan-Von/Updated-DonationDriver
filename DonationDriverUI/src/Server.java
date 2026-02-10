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
                    case "CREATE_TICKET": {
                        OperationResult result = createTicket(userId, requestXml);  // NEW
                        if (result.success) {
                            status = "OK";
                        }
                        message = result.message;
                        break;
                    }
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

        private static final String TICKETS_DIR = "tickets";
        private static final Object TICKET_LOCK = new Object();
        private static long nextTicketId = System.currentTimeMillis();

        private static class OperationResult {
            final boolean success;
            final String message;

            OperationResult(boolean success, String message) {
                this.success = success;
                this.message = message;
            }
        }

        private OperationResult createTicket(String userId, String requestXml) {
            if (userId == null || userId.trim().isEmpty()) {
                return new OperationResult(false, "UserId is required to create a donation ticket.");
            }

            String itemCategory = extractTagValue(requestXml, "itemCategory");
            if (itemCategory == null || itemCategory.isEmpty()) {
                itemCategory = extractTagValue(requestXml, "type");
            }

            String quantityStr     = extractTagValue(requestXml, "quantity");
            String condition       = extractTagValue(requestXml, "condition");
            String expirationDate  = extractTagValue(requestXml, "expirationDate");
            String pickupDateTime  = extractTagValue(requestXml, "pickupDateTime");
            String pickupLocation  = extractTagValue(requestXml, "pickupLocation");
            String photoPath       = extractTagValue(requestXml, "photoPath");
            String notes           = extractTagValue(requestXml, "details");


            if (itemCategory == null || itemCategory.trim().isEmpty()) {
                return new OperationResult(false, "Item category (food, clothes, books, etc.) is required.");
            }

            int quantity = 0;
            if (quantityStr != null && !quantityStr.trim().isEmpty()) {
                try {
                    quantity = Integer.parseInt(quantityStr.trim());
                } catch (NumberFormatException ex) {
                    return new OperationResult(false, "Quantity must be a whole number.");
                }
                if (quantity <= 0) {
                    return new OperationResult(false, "Quantity must be greater than zero.");
                }
            }

            if (expirationDate != null && !expirationDate.trim().isEmpty()) {
                try {
                    Date exp = new SimpleDateFormat("yyyy-MM-dd").parse(expirationDate.trim());
                    if (exp.before(new Date())) {
                        return new OperationResult(false,
                                "Donation items appear expired based on the provided expiration date.");
                    }
                } catch (Exception ignored) {
                }
            }

            String ticketId;

            synchronized (TICKET_LOCK) {
                ticketId = String.valueOf(nextTicketId++);

                File dir = new File(TICKETS_DIR);
                if (!dir.exists() && !dir.mkdirs()) {
                    return new OperationResult(false, "Server error: unable to create tickets directory.");
                }

                File file = new File(dir, ticketId + ".xml");

                StringBuilder sb = new StringBuilder();
                sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                sb.append("<ticket>");
                sb.append("<ticketId>").append(escapeXml(ticketId)).append("</ticketId>");
                sb.append("<userId>").append(escapeXml(userId)).append("</userId>");
                sb.append("<status>").append("PENDING").append("</status>");
                sb.append("<createdAt>").append(escapeXml(
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                )).append("</createdAt>");
                sb.append("<itemCategory>").append(escapeXml(itemCategory)).append("</itemCategory>");
                sb.append("<quantity>").append(escapeXml(quantityStr != null ? quantityStr : "")).append("</quantity>");
                sb.append("<condition>").append(escapeXml(condition != null ? condition : "")).append("</condition>");
                sb.append("<expirationDate>").append(escapeXml(expirationDate != null ? expirationDate : ""))
                        .append("</expirationDate>");
                sb.append("<pickupDateTime>").append(escapeXml(pickupDateTime != null ? pickupDateTime : ""))
                        .append("</pickupDateTime>");
                sb.append("<pickupLocation>").append(escapeXml(pickupLocation != null ? pickupLocation : ""))
                        .append("</pickupLocation>");
                sb.append("<photoPath>").append(escapeXml(photoPath != null ? photoPath : "")).append("</photoPath>");
                sb.append("<notes>").append(escapeXml(notes != null ? notes : "")).append("</notes>");
                sb.append("</ticket>");

                try (FileWriter fw = new FileWriter(file, false)) {
                    fw.write(sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    return new OperationResult(false, "Server error: failed to save donation ticket.");
                }
            }

            String msg = "Donation ticket created successfully. Ticket ID: " + ticketId;
            return new OperationResult(true, msg);
        }

        private void log(String transaction, String userId, String data) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String user = (userId == null || userId.isEmpty()) ? "ANONYMOUS" : userId;

            System.out.printf("[%s] %s %s%n", timestamp, user, transaction);

            StringBuilder sb = new StringBuilder();
            sb.append("[").append(timestamp).append("]").append(System.lineSeparator());
            sb.append("User: ").append(user).append(System.lineSeparator());
            sb.append("Transaction: ").append(transaction).append(System.lineSeparator());
            sb.append("Data:").append(System.lineSeparator());
            sb.append(data).append(System.lineSeparator());
            sb.append("----").append(System.lineSeparator());

            try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
                fw.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
