import ServerSide.WebServiceAndDBMS;

import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final Scanner CONSOLE = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\nJAVA VERSION");

        String nameFirst = promptNameFirst();
        String nameLast = promptNameLast();

        int int1 = prompt2Ints1();
        int int2 = prompt2Ints2();

        File inFile = promptInFileName();
        File outFile = promptOutFileName(inFile);

        promptPassword();
        promptPasswordRetype();

        writeOutput(nameFirst, nameLast, int1, int2, inFile, outFile);

        System.exit(0);
    }

    private static final String REGEX_NAME = "^(?=.*[a-z]+.*)[a-z0-9\\s'\\.\\-]{1,50}$";
    /**
     * First name
     */
    private static String promptNameFirst() {
        return doRetrieveInput(
                "Enter a first name.",
                "50 chars max: [A-Z, 0-9, ', ., -]",
                REGEX_NAME,
                Pattern.CASE_INSENSITIVE
        );
    }

    /**
     * Last name
     */
    private static String promptNameLast() {
        return doRetrieveInput(
                "Enter a last name.",
                "50 chars max: [A-Z, 0-9, ', ., -]",
                REGEX_NAME,
                Pattern.CASE_INSENSITIVE
        );
    }

    /**
     * 2 Ints 1st
     */
    private static int prompt2Ints1() {
        return doRetrieveInputInt(
                "Enter the 1st int.",
                "Range: (" + Integer.MIN_VALUE + ", " + Integer.MAX_VALUE + ")"
        );
    }

    /**
     * 2 Ints 2nd
     */
    private static int prompt2Ints2() {
        return doRetrieveInputInt(
                "Enter the 2nd int.",
                "Range: (" + Integer.MIN_VALUE + ", " + Integer.MAX_VALUE + ")"
        );
    }

    /**
     * Input file name/path
     */
    private static File promptInFileName() {
        File file = null;
        while (file == null) {
            System.out.print("\nSelect the input file.\n(A File Browser was opened.)");

            FileDialog fd = new java.awt.FileDialog((Frame) null, "Select the input file.", FileDialog.LOAD);
            setupFD(fd);

            //if user cancels
            if (fd.getFile() == null) {
                System.out.println("\n> null\nNo file selected.");
                continue;
            }

            //test for valid file
            file = new File(fd.getDirectory() + fd.getFile());
            System.out.println("\n> " + file.getAbsolutePath());
            if (!file.canRead()) { //can't read
                System.out.println("File cannot be read.");
                file = null;
            }
        }

        return file;
    }

    /**
     * Output file name/path
     *
     * @return
     */
    private static File promptOutFileName(File inFile) {
        File file = null;
        while (file == null) {
            System.out.print("\nSelect the output file.\n(A File Browser was opened.)");

            FileDialog fd = new java.awt.FileDialog((java.awt.Frame) null, "Select the output file.", FileDialog.SAVE);
            setupFD(fd);

            //if user cancels
            if (fd.getFile() == null) {
                System.out.println("\n> null\nNo file selected.");
                continue;
            }

            file = new File(fd.getDirectory() + fd.getFile()); //User can overwrite non-admin files TODO fix or leave?
            System.out.println("\n> " + file.getAbsolutePath());
            //check if same as input
            if (file.equals(inFile)) {
                System.out.println("Output file was the same as input.");
                file = null;
                continue;
            }
            //test for valid file
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    file = null;
                }
            }
            if (!file.canWrite()) { //can't write
                System.out.println("File cannot be written to.");
                file = null;
            }
        }

        return file;
    }

    private static void setupFD(FileDialog fd) {
        fd.setDirectory("./");
        fd.setMultipleMode(false);
        fd.setAlwaysOnTop(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        fd.setLocation(screenSize.width / 2, screenSize.height / 2);
        fd.setAutoRequestFocus(true);
        fd.setVisible(true);
    }

    private static final String REGEX_PASSWORD = "^(?=.*[A-Z]+)(?=.*[a-z]+)(?=.*[\\d]+)(?=.*[^A-za-z\\d]+)[ -~]{8,50}$";
    /**
     * Password, then send to server
     */
    private static void promptPassword() {
        boolean success = false;
        while (!success) {
            String password = doRetrieveInput(
                    "Enter a password.",
                    "8 to 50 chars & must include:\n\t-Only ASCII\n\t-An uppercase letter\n\t-An lowers letter\n\t-A number\n\t-A special char",
                    REGEX_PASSWORD,
                    null
            );

            //Super real async HTTPS POST request
            String response = WebServiceAndDBMS.authPOST(password);

            //parse response for success
            if (response.contains("\"success\": true") && response.contains("\"message\": \"Password stored.\"")) {
                success = true; //should default to this unless WebServer is broken
            } else {
                System.out.println("No match found.");
            }
        }
    }

    /**
     * Password verification by sending to server
     */
    private static void promptPasswordRetype() {
        boolean success = false;
        while (!success) {
            String password = doRetrieveInput(
                    "Retype your password.",
                    "",
                    REGEX_PASSWORD,
                    null
            );

            //Super real async HTTPS POST request
            String response = WebServiceAndDBMS.authGET(password);

            //parse response for success
            if (response.contains("\"success\": true") && response.contains("\"message\": \"Password matched.\"")) {
                success = true; //should default to this unless WebServer is broken
            } else {
                System.out.println("No match found.");
            }
        }
    }

    /**
     * - writes the user's name <br>
     * - writes the result of adding the two integer values (no overflow should occur) <br>
     * - writes the result of multiplying the two integer values (no overflow should occur) <br>
     * - writes the contents of the input file <br>
     * - Each thing written should be clearly labeled (e.g. First name, Last name, First Integer, Second Integer, Sum, Product, Input File Name, Input file contents)
     */
    private static void writeOutput(String nameFirst, String nameLast, int int1, int int2, File inFile, File outFile) {
        try {
            FileWriter fw = new FileWriter(outFile);
            fw.write("First Name: " + nameFirst + "\n\n");
            fw.write("Last Name: " + nameLast + "\n\n");
            fw.write("int1 + int2: " + add2IntsOverflow(int1,int2) + "\n\n");
            fw.write("int1 * int2: " + multiply2IntsOverflow(int1,int2) + "\n\n");
            fw.write("Copy of " + inFile.getName() + ":\n");
            fileWriteBytePerByte(fw, inFile);
            fw.close();
        } catch (IOException e) {
            System.out.println("Error while writing to " + outFile.getAbsolutePath());
            System.out.println(e.getMessage());
        }

        //Open Output file for preview
        try {
            Desktop.getDesktop().open(outFile);
        } catch (IOException e) {
            System.out.println("Error opening a preview of " + outFile.getAbsolutePath());
            System.out.println(e.getMessage());
        }
    }

    private static String add2IntsOverflow(int int1, int int2) {
        try {
            return "" + Math.addExact(int1, int2);
        } catch (ArithmeticException e) {
            return ((long)int1 + (long)int2) + " (Overflow Detected)";
        }
    }
    private static String multiply2IntsOverflow(int int1, int int2) {
        try {
            return String.valueOf(Math.multiplyExact(int1, int2));
        } catch (ArithmeticException e) {
            return new BigInteger(String.valueOf(int1)).multiply(new BigInteger(String.valueOf(int2))) + " (Overflow Detected)";
        }
    }
    private static void fileWriteBytePerByte(FileWriter fw, File inFile) { //https://howtodoinjava.com/java/io/read-file-content-into-byte-array/
        try {
            byte[] bytes = Files.readAllBytes(Path.of(inFile.getAbsolutePath()));
            for (int i = 0; i < bytes.length; i++) {
                fw.write(bytes[i]);
            }
        } catch (IOException e) {
            System.out.println("Error while copying " + inFile.getAbsolutePath());
            System.out.println(e.getMessage());
        }
    }

    /**
     * Helper to use regex. <br>
     * From Java tutorials.
     *
     * @param matcher
     * @return
     */
    private static int doRegex(Matcher matcher) {
        int found = 0;
        if (matcher.find()) {
            found++;
        }
        if (found == 0) {
            System.out.println("No match found.");
        }

        return found;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static String doRetrieveInput(String prompt, String description, String regex, Integer patternParam) {
        String result = "";
        while (result.length() < 1) {
            String in = doInputPrompting(prompt, description);
            if ((patternParam != null ? doRegex(Pattern.compile(regex, patternParam).matcher(in)) : doRegex(Pattern.compile(regex).matcher(in))) == 1) {
                result = in;
            }
        }
        return result;
    }
    private static String doInputPrompting(String prompt, String description) {
        //print
        System.out.println();
        System.out.println(prompt);
        if (!description.equals("")) { System.out.println(description); }
        System.out.print("> ");

        //read
        String result = CONSOLE.nextLine();

        return result.trim();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static int doRetrieveInputInt(String prompt, String description) {
        Integer result = null;
        while (result == null) {
            result = doInputPromptingInt(prompt, description);
        }
        return result;
    }
    private static Integer doInputPromptingInt(String prompt, String description) {
        //print
        System.out.println();
        System.out.println(prompt);
        System.out.println(description);
        System.out.print("> ");

        //read
        String line = CONSOLE.nextLine().trim();
        Scanner lineScanner = new Scanner(line);
        Integer result = null;
        try {
            result = lineScanner.nextInt();
        } catch (Exception e) {
            System.out.println("No match found.");
        }
        lineScanner.close();

        return result;
    }
}
