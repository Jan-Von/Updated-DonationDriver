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
                        OperationResult result = createTicket(userId, requestXml);
                        if (result.success) {
                            status = "OK";
                        }
                        message = result.message;
                        break;
                    }
                    case "READ_TICKETS": {
                        String ticketsXml = readTickets(userId, requestXml);
                        status = "OK";
                        message = ticketsXml;
                        break;
                    }
                    case "UPDATE_TICKET": {
                        OperationResult result = updateTicket(userId, requestXml);
                        if (result.success) {
                            status = "OK";
                        }
                        message = result.message;
                        break;
                    }
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

        private String readTickets(String requesterUserId, String requestXml) {
            String filterStatus = extractTagValue(requestXml, "status");

            File dir = new File(TICKETS_DIR);
            File[] files = dir.listFiles((d, name) -> name.endsWith(".xml"));

            StringBuilder ticketsBuilder = new StringBuilder();
            ticketsBuilder.append("<tickets>");

            if (files != null) {
                for (File file : files) {
                    String xml = readWholeFile(file);
                    if (xml == null || xml.trim().isEmpty()) {
                        continue;
                    }

                    String ticketUserId = extractTagValue(xml, "userId");
                    String ticketStatus = extractTagValue(xml, "status");

                    boolean matchesUser =
                            (requesterUserId == null || requesterUserId.isEmpty())
                                    || (ticketUserId != null && requesterUserId.equals(ticketUserId));

                    boolean matchesStatus =
                            (filterStatus == null || filterStatus.isEmpty())
                                    || (ticketStatus != null && filterStatus.equalsIgnoreCase(ticketStatus));

                    if (!matchesUser || !matchesStatus) {
                        continue;
                    }

                    String ticketXml = xml.trim();
                    int start = ticketXml.indexOf("<ticket>");
                    if (start >= 0) {
                        ticketXml = ticketXml.substring(start);
                    }

                    ticketsBuilder.append(ticketXml);
                }
            }

            ticketsBuilder.append("</tickets>");
            return ticketsBuilder.toString();
        }

        private String readWholeFile(File file) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return sb.toString();
        }

        private OperationResult updateTicket(String requesterUserId, String requestXml) {
            String ticketId = extractTagValue(requestXml, "ticketId");
            if (ticketId == null || ticketId.trim().isEmpty()) {
                return new OperationResult(false, "ticketId is required to update a ticket.");
            }

            String newStatus       = extractTagValue(requestXml, "status");
            String qualityStatus   = extractTagValue(requestXml, "qualityStatus");
            String qualityReason   = extractTagValue(requestXml, "qualityReason");
            String newPickupTime   = extractTagValue(requestXml, "pickupDateTime");

            File dir = new File(TICKETS_DIR);
            File file = new File(dir, ticketId + ".xml");
            if (!file.exists()) {
                return new OperationResult(false, "Ticket " + ticketId + " not found.");
            }

            String xml = readWholeFile(file);
            if (xml == null || xml.trim().isEmpty()) {
                return new OperationResult(false, "Ticket " + ticketId + " is empty or unreadable.");
            }

            String ticketUserId     = extractTagValue(xml, "userId");
            String oldStatus        = extractTagValue(xml, "status");
            String createdAt        = extractTagValue(xml, "createdAt");
            String itemCategory     = extractTagValue(xml, "itemCategory");
            String quantityStr      = extractTagValue(xml, "quantity");
            String condition        = extractTagValue(xml, "condition");
            String expirationDate   = extractTagValue(xml, "expirationDate");
            String pickupDateTime   = extractTagValue(xml, "pickupDateTime");
            String pickupLocation   = extractTagValue(xml, "pickupLocation");
            String photoPath        = extractTagValue(xml, "photoPath");
            String notes            = extractTagValue(xml, "notes");
            String existingQuality  = extractTagValue(xml, "qualityStatus");
            String existingReason   = extractTagValue(xml, "qualityReason");
            String statusHistory    = extractTagValue(xml, "statusHistory");


            if (ticketUserId != null && requesterUserId != null
                    && !requesterUserId.isEmpty()
                    && requesterUserId.equals(ticketUserId)) {
            }

            String nowTs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            StringBuilder historyBuilder = new StringBuilder();
            if (statusHistory != null && !statusHistory.isEmpty()) {
                historyBuilder.append(statusHistory).append(" | ");
            }

            String finalStatus = oldStatus;
            if (newStatus != null && !newStatus.trim().isEmpty()) {
                String oldNormalized = oldStatus != null ? oldStatus.toUpperCase() : "";
                String newNormalized = newStatus.toUpperCase();

                boolean allowed = false;
                if ("PENDING".equals(oldNormalized) && ("ACCEPTED".equals(newNormalized) || "REJECTED".equals(newNormalized))) {
                    allowed = true;
                } else if ("ACCEPTED".equals(oldNormalized) && ("PICKED_UP".equals(newNormalized) || "REJECTED".equals(newNormalized))) {
                    allowed = true;
                } else if ("PICKED_UP".equals(oldNormalized) && "DELIVERED".equals(newNormalized)) {
                    allowed = true;
                } else if (oldNormalized.equals(newNormalized)) {
                    allowed = true;
                }

                if (!allowed) {
                    return new OperationResult(false,
                            "Invalid status transition: " + oldNormalized + " -> " + newNormalized);
                }

                finalStatus = newNormalized;
                historyBuilder.append(nowTs)
                        .append(" ").append(requesterUserId != null ? requesterUserId : "SYSTEM")
                        .append(" changed status from ")
                        .append(oldStatus != null ? oldStatus : "(none)")
                        .append(" to ")
                        .append(newNormalized);
            }

            String finalQualityStatus = existingQuality;
            String finalQualityReason = existingReason;

            if (qualityStatus != null && !qualityStatus.trim().isEmpty()) {
                finalQualityStatus = qualityStatus.toUpperCase();
                finalQualityReason = qualityReason != null ? qualityReason : existingReason;

                if (historyBuilder.length() > 0) {
                    historyBuilder.append(" | ");
                }
                historyBuilder.append(nowTs)
                        .append(" ").append(requesterUserId != null ? requesterUserId : "SYSTEM")
                        .append(" set quality to ").append(finalQualityStatus);
                if (finalQualityReason != null && !finalQualityReason.isEmpty()) {
                    historyBuilder.append(" (reason: ").append(finalQualityReason).append(")");
                }
            }

            String finalPickupTime = pickupDateTime;
            if (newPickupTime != null && !newPickupTime.trim().isEmpty()) {
                finalPickupTime = newPickupTime.trim();

                if (historyBuilder.length() > 0) {
                    historyBuilder.append(" | ");
                }
                historyBuilder.append(nowTs)
                        .append(" ").append(requesterUserId != null ? requesterUserId : "SYSTEM")
                        .append(" rescheduled pickup to ").append(finalPickupTime);
            }

            String finalHistory = historyBuilder.toString();

            synchronized (TICKET_LOCK) {
                StringBuilder sb = new StringBuilder();
                sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                sb.append("<ticket>");
                sb.append("<ticketId>").append(escapeXml(ticketId)).append("</ticketId>");
                sb.append("<userId>").append(escapeXml(ticketUserId != null ? ticketUserId : "")).append("</userId>");
                sb.append("<status>").append(escapeXml(finalStatus != null ? finalStatus : "")).append("</status>");
                sb.append("<createdAt>").append(escapeXml(createdAt != null ? createdAt : "")).append("</createdAt>");
                sb.append("<lastUpdatedAt>").append(escapeXml(nowTs)).append("</lastUpdatedAt>");
                sb.append("<itemCategory>").append(escapeXml(itemCategory != null ? itemCategory : "")).append("</itemCategory>");
                sb.append("<quantity>").append(escapeXml(quantityStr != null ? quantityStr : "")).append("</quantity>");
                sb.append("<condition>").append(escapeXml(condition != null ? condition : "")).append("</condition>");
                sb.append("<expirationDate>").append(escapeXml(expirationDate != null ? expirationDate : ""))
                        .append("</expirationDate>");
                sb.append("<pickupDateTime>").append(escapeXml(finalPickupTime != null ? finalPickupTime : ""))
                        .append("</pickupDateTime>");
                sb.append("<pickupLocation>").append(escapeXml(pickupLocation != null ? pickupLocation : ""))
                        .append("</pickupLocation>");
                sb.append("<photoPath>").append(escapeXml(photoPath != null ? photoPath : "")).append("</photoPath>");
                sb.append("<notes>").append(escapeXml(notes != null ? notes : "")).append("</notes>");
                sb.append("<qualityStatus>").append(escapeXml(finalQualityStatus != null ? finalQualityStatus : ""))
                        .append("</qualityStatus>");
                sb.append("<qualityReason>").append(escapeXml(finalQualityReason != null ? finalQualityReason : ""))
                        .append("</qualityReason>");
                sb.append("<statusHistory>").append(escapeXml(finalHistory != null ? finalHistory : ""))
                        .append("</statusHistory>");
                sb.append("</ticket>");

                try (FileWriter fw = new FileWriter(file, false)) {
                    fw.write(sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    return new OperationResult(false, "Server error: failed to update ticket " + ticketId + ".");
                }
            }

            return new OperationResult(true, "Ticket " + ticketId + " updated successfully.");
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
