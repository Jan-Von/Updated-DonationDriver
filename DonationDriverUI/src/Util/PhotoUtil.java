package Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Utility for converting JPG image files to base64 for inclusion in XML.
 */
public final class PhotoUtil {

    private PhotoUtil() {}

    /**
     * Reads a JPG/JPEG file and returns its content as a base64-encoded string.
     *
     * @param file the image file (should be .jpg or .jpeg)
     * @return base64 string, or null if the file is null, invalid, or cannot be read
     */
    public static String jpgFileToBase64(File file) {
        if (file == null || !file.isFile() || !file.canRead()) {
            return null;
        }
        String name = file.getName().toLowerCase();
        if (!name.endsWith(".jpg") && !name.endsWith(".jpeg")) {
            return null;
        }
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            return null;
        }
    }
}
