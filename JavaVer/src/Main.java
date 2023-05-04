import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final boolean DEBUG_ECHO = false;
    private static final Scanner CONSOLE = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\nJAVA VERSION");

        String nameFirst = promptNameFirst();
        String nameLast = promptNameLast();

        int int1 = prompt2Ints1();
        int int2 = prompt2Ints2();

        File inFile = promptInFileName();
        File outFile = promptOutFileName(inFile);

        System.exit(0);
    }

    private static final String NAME_REGEX = "^(?=.*[a-z]+.*)[a-z0-9\\s'\\.\\-]{1,50}$";

    /**
     * First name
     */
    private static String promptNameFirst() {
        return doRetrieveInput(
                "Enter a first name.",
                "50 chars max: [A-Z, 0-9, ', ., -]",
                NAME_REGEX,
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
                NAME_REGEX,
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
            System.out.print("\nSelect the input file.\n> ");

            FileDialog fd = new java.awt.FileDialog((Frame) null, "Select the input file.", FileDialog.LOAD);
            setupFD(fd);

            //if user cancels
            if (fd.getFile() == null) {
                System.out.println("null\nNo file selected.");
                continue;
            }

            //test for valid file
            file = new File(fd.getDirectory() + fd.getFile());
            System.out.println(file.getAbsolutePath());
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
            System.out.print("\nSelect the output file.\n> ");

            FileDialog fd = new java.awt.FileDialog((java.awt.Frame) null, "Select the output file.", FileDialog.SAVE);
            setupFD(fd);

            //if user cancels
            if (fd.getFile() == null) {
                System.out.println("null\nNo file selected.");
                continue;
            }

            file = new File(fd.getDirectory() + fd.getFile()); //User can overwrite non-admin files TODO fix or leave?
            System.out.println(file.getAbsolutePath());
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

    /**
     * Password, hashed and salted
     */
    private static void promptPassword() {

    }

    /**
     * Password verification by hashed and salted then compare
     */
    private static void promptPasswordRetype() {

    }

    /**
     * - writes the user's name <br>
     * - writes the result of adding the two integer values (no overflow should occur) <br>
     * - writes the result of multiplying the two integer values (no overflow should occur) <br>
     * - writes the contents of the input file <br>
     * - Each thing written should be clearly labeled (e.g. First name, Last name, First Integer, Second Integer, Sum, Product, Input File Name, Input file contents)
     */
    private static void writeOutput() {

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
            if (DEBUG_ECHO) {
                System.out.printf("\"%s\": Found match from index %d to %d.\n",
                        matcher.group(), matcher.start(), matcher.end());
            }
            found++;
        }
//        while (matcher.find()) {
//            System.out.printf("\"%s\": Found match \"%s\" from index %d to %d.\n",
//                    matcherString, matcher.group(), matcher.start(), matcher.end());
//            found++;
//        }
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
        System.out.println(description);
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
