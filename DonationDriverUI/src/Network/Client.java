package Network;

import java.io.*;
import java.net.Socket;

public class Client {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5267;

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


    public String login(String email, String password) throws IOException {
        String request = "<request><action>LOGIN</action><userId></userId>"
                + "<email>" + escapeXml(email) + "</email>"
                + "<password>" + escapeXml(password) + "</password></request>";
        return sendRequest(request);
    }

    public String register(String email, String password) throws IOException {
        String request = "<request><action>REGISTER</action><userId></userId>"
                + "<email>" + escapeXml(email) + "</email>"
                + "<password>" + escapeXml(password) + "</password></request>";
        return sendRequest(request);
    }

    public String createTicket(String userId, String type, String details) throws IOException {
        String request = "<request><action>CREATE_TICKET</action>"
                + "<userId>" + escapeXml(userId) + "</userId>"
                + "<type>" + escapeXml(type) + "</type>"
                + "<details>" + escapeXml(details) + "</details></request>";
        return sendRequest(request);
    }

    public String createTicket(
            String userId,
            String itemCategory,
            int quantity,
            String condition,
            String expirationDate,
            String pickupDateTime,
            String pickupLocation,
            String photoPath,
            String notes
    ) throws IOException {

        StringBuilder request = new StringBuilder();
        request.append("<request><action>CREATE_TICKET</action>");
        request.append("<userId>").append(escapeXml(userId)).append("</userId>");

        request.append("<itemCategory>").append(escapeXml(itemCategory)).append("</itemCategory>");
        request.append("<quantity>").append(quantity).append("</quantity>");
        request.append("<condition>").append(escapeXml(condition != null ? condition : ""))
                .append("</condition>");
        request.append("<expirationDate>").append(escapeXml(expirationDate != null ? expirationDate : ""))
                .append("</expirationDate>");

        request.append("<pickupDateTime>").append(escapeXml(pickupDateTime != null ? pickupDateTime : ""))
                .append("</pickupDateTime>");
        request.append("<pickupLocation>").append(escapeXml(pickupLocation != null ? pickupLocation : ""))
                .append("</pickupLocation>");

        request.append("<photoPath>").append(escapeXml(photoPath != null ? photoPath : ""))
                .append("</photoPath>");

        request.append("<details>").append(escapeXml(notes != null ? notes : "")).append("</details>");

        request.append("</request>");

        return sendRequest(request.toString());
    }

    public String readTickets(String userId) throws IOException {
        return readTickets(userId, null);
    }

    public String readTickets(String userId, String status) throws IOException {
        String request = "<request><action>READ_TICKETS</action>"
                + "<userId>" + escapeXml(userId) + "</userId>";
        if (status != null) {
            request += "<status>" + escapeXml(status) + "</status>";
        }
        request += "</request>";
        return sendRequest(request);
    }

    public String updateTicket(String userId, String ticketId, String status) throws IOException {
        String request = "<request><action>UPDATE_TICKET</action>"
                + "<userId>" + escapeXml(userId) + "</userId>"
                + "<ticketId>" + escapeXml(ticketId) + "</ticketId>"
                + "<status>" + escapeXml(status) + "</status></request>";
        return sendRequest(request);
    }

    public String deleteTicket(String userId, String ticketId) throws IOException {
        String request = "<request><action>DELETE_TICKET</action>"
                + "<userId>" + escapeXml(userId) + "</userId>"
                + "<ticketId>" + escapeXml(ticketId) + "</ticketId></request>";
        return sendRequest(request);
    }
    //Overload method for optional reason from client when cancelling a ticket
    public String deleteTicket(String userId, String ticketId, String reason) throws IOException {
        StringBuilder request = new StringBuilder();
        request.append("<request><action>DELETE_TICKET</action>");
        request.append("<userId>").append(escapeXml(userId)).append("</userId>");
        request.append("<ticketId>").append(escapeXml(ticketId)).append("</ticketId>");
        if (reason != null && !reason.trim().isEmpty()) {
            request.append("<deleteReason>").append(escapeXml(reason.trim())).append("</deleteReason>");
        }
        request.append("</request>");
        return sendRequest(request.toString());
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

    public static String escapeXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    public String ping() throws IOException {
        return sendRequest("<request><action>PING</action><userId></userId></request>");
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

    public static void main(String[] args) {
        Client client = getDefault();

        try {
            String donorId = "donor@gmail.com";
            String responseXml = client.readTickets(donorId);
            System.out.println("READ_TICKETS (donor) response: " + responseXml);

            Response r = parseResponse(responseXml);
            if (r != null) {
                System.out.println("Parsed status: " + r.status);
                System.out.println("Tickets XML from message:");
                System.out.println(r.message);
            }

            String responsePending = client.readTickets("", "PENDING");
            System.out.println("READ_TICKETS (all PENDING) response: " + responsePending);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

