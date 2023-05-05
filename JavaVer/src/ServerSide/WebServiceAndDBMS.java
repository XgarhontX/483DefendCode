package ServerSide;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebServiceAndDBMS {
    //Real Database
    private static byte[] dbms_passwordHashed = null;
    private static byte[] dbms_passwordSalt = null;

    //regex
    private static final String REGEX_PASSWORD = "^(?=.*[A-Z]+)(?=.*[a-z]+)(?=.*[\\d]+)(?=.*[^A-za-z\\d]+)[ -~]{8,50}$";

    //https://www.baeldung.com/java-password-hashing
    public static String authPOST(String password) {
        //regex check
        if (doRegex(Pattern.compile(REGEX_PASSWORD).matcher(password)) != 1) {
            return "{\n" +
                    "  \"success\": false,\n" +
                    "  \"message\": \"Password doesn't meet requirements.\"\n" +
                    "}";
        }

        //salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        dbms_passwordSalt = salt;
        //hash
        dbms_passwordHashed = hashPassword(password, salt);

        return "{\n" +
                "  \"success\": true,\n" +
                "  \"message\": \"Password stored.\"\n" +
                "}";
    }

    public static String authGET(String password) {
        String failedResponse = "{\n" +
                "  \"success\": false,\n" +
                "  \"message\": \"Password doesn't match.\"\n" +
                "}";

        //regex check
        if (doRegex(Pattern.compile(REGEX_PASSWORD).matcher(password)) != 1) {
            return failedResponse;
        }

        //hash and check
        if (!Arrays.equals(hashPassword(password, dbms_passwordSalt), dbms_passwordHashed)) {
            return failedResponse;
        }

        //succeeded
        return "{\n" +
                "  \"success\": true,\n" +
                "  \"message\": \"Password matched.\"\n" +
                "}";
    }

    private static int doRegex(Matcher matcher) {
        int found = 0;
        if (matcher.find()) {
            found++;
        }
        return found;
    }

    public static void main(String[] args) {
        authPOST("Testestest1!111");
        System.out.println(new String(dbms_passwordHashed, StandardCharsets.US_ASCII) + ", " + new String(dbms_passwordSalt, StandardCharsets.US_ASCII));
    }

    private static byte[] hashPassword(String password, byte[] salt) {
        //make key spec
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        //hash password
        byte[] hash;
        try {
            hash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return hash;
    }
}