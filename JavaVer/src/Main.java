import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final boolean DEBUG_ECHO = false;
    private static final Scanner CONSOLE = new Scanner(System.in);

    public static void main(String[] args) {
        String nameFirst = promptNameFirst();
        String nameLast = promptNameLast();
    }

    private static final String NAME_REGEX = "^[a-z0-9\\s'\\.\\-]+$";
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
     * 2 Ints
     */
    private static void prompt2Ints() {

    }

    /**
     * Input file name/path
     */
    private static void promptInFileName() {

    }

    /**
     * Output file name/path
     */
    private static void promptOutFileName() {

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

    private static String doRetrieveInput(String prompt, String description, String regex, Integer patternParam) {
        String result = "";
        while (result.length() < 1) {
            String in = doInputPrompting(prompt, description);
            if ((patternParam != null ? doRegex(Pattern.compile(regex, patternParam).matcher(in)) : doRegex(Pattern.compile(regex).matcher(in)) )== 1) {
                result = in;
            }
        }
        return result;
    }
}
