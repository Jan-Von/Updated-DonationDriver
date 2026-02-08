package Model;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class UserModel {

    private static final String USERS_XML_RELATIVE = "DonationDriverUI/users.xml";

    private String emailField;
    private String passField;

    // Additional registration details
    private String firstName;
    private String lastName;
    private String middleName;
    private String dateOfBirth;
    private String address;
    private String phoneNumber;

    public UserModel(String email, String password) {
        this.emailField = email;
        this.passField = password;
    }

    // Full constructor for registration with personal details
    public UserModel(String firstName,
                     String lastName,
                     String middleName,
                     String dateOfBirth,
                     String address,
                     String phoneNumber,
                     String email,
                     String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.emailField = email;
        this.passField = password;
    }

    /** Resolves users.xml to the project's DonationDriverUI folder (not the run/output folder). */
    private static File getUsersXmlFile() {
        File cwd = new File(System.getProperty("user.dir"));
        for (File dir = cwd; dir != null; dir = dir.getParentFile()) {
            File candidate = new File(dir, USERS_XML_RELATIVE);
            if (candidate.exists()) return candidate;
            File donationDriverDir = new File(dir, "DonationDriverUI");
            if (donationDriverDir.isDirectory()) return new File(donationDriverDir, "users.xml");
        }
        return new File(cwd, USERS_XML_RELATIVE);
    }

    // Authenticate user from users.xml
    public boolean authenticate() {
        try {
            File file = getUsersXmlFile();
            if (!file.exists()) return false;
            Document doc = loadDocument(file);
            NodeList users = doc.getElementsByTagName("user");
            for (int i = 0; i < users.getLength(); i++) {
                Element user = (Element) users.item(i);
                String email = getTextContent(user, "email");
                String password = getTextContent(user, "password");
                if (email != null && password != null
                        && email.trim().equals(emailField)
                        && password.equals(passField)) {
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Register user (append to users.xml)
    public boolean register() {
        try {
            File file = getUsersXmlFile();
            Document doc;
            if (file.exists()) {
                doc = loadDocument(file);
            } else {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.newDocument();
                doc.appendChild(doc.createElement("users"));
            }
            Element root = doc.getDocumentElement();
            if (root == null) {
                root = doc.createElement("users");
                doc.appendChild(root);
            }
            Element userEl = doc.createElement("user");
            appendChildText(doc, userEl, "email", emailField);
            appendChildText(doc, userEl, "password", passField);
            appendChildText(doc, userEl, "firstName", firstName);
            appendChildText(doc, userEl, "lastName", lastName);
            appendChildText(doc, userEl, "middleName", middleName);
            appendChildText(doc, userEl, "dateOfBirth", dateOfBirth);
            appendChildText(doc, userEl, "address", address);
            appendChildText(doc, userEl, "phoneNumber", phoneNumber);
            root.appendChild(userEl);
            writeDocument(doc, file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if an email is already registered
    public boolean emailExists() {
        try {
            File file = getUsersXmlFile();
            if (!file.exists()) return false;
            Document doc = loadDocument(file);
            NodeList users = doc.getElementsByTagName("user");
            for (int i = 0; i < users.getLength(); i++) {
                Element user = (Element) users.item(i);
                String email = getTextContent(user, "email");
                if (email != null && email.trim().equalsIgnoreCase(emailField)) {
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private static Document loadDocument(File file) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(file);
    }

    private static void writeDocument(Document doc, File file) throws Exception {
        file.getParentFile().mkdirs();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        try (FileOutputStream out = new FileOutputStream(file)) {
            t.transform(new DOMSource(doc), new StreamResult(out));
        }
    }

    private static String getTextContent(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() == 0) return null;
        Element el = (Element) list.item(0);
        return el.getTextContent() != null ? el.getTextContent().trim() : null;
    }

    private static void appendChildText(Document doc, Element parent, String tagName, String value) {
        Element el = doc.createElement(tagName);
        el.setTextContent(value != null ? value : "");
        parent.appendChild(el);
    }
}
