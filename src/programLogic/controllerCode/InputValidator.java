package programLogic.controllerCode;

import java.util.Scanner;

public class InputValidator {

    private static final Scanner scanner = new Scanner(System.in);

    public static boolean isNumberBetween(short number, short from, short to) {
        return (number >= from && number <= to);
    }

    public static boolean isNumberBetween(int number , int from, int to) {
        return (number >= from && number <= to);
    }

    public static boolean isNumberBetween(float number, float from, float to) {
        return (number >= from && number <= to);
    }

    public static boolean isNumberBetween(double number, double from, double to) {
        return (number >= from && number <= to);
    }

    public static char readChar(String errorMessage) {
        return scanner.nextLine().charAt(0);
    }

    public static int readIntNumber(String errorMessage) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException ignore) {}
            System.out.println(errorMessage); // why not in catch?
        }
    }

    public static int readIntNumberBetween(int from, int to, String errorMessage) {
        int number = readIntNumber(errorMessage);

        while (!isNumberBetween(number, from, to))
        {
            System.out.println(errorMessage);
            number = readIntNumber(errorMessage);
        }
        return number;
    }

    public static double readDoubleNumber(String errorMessage) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException ignore) {}
            System.out.println(errorMessage); // why not in catch?
        }
    }

    public static double readDoubleNumberBetween(double from, double to, String errorMessage) {
        double number = readDoubleNumber(errorMessage);

        while (!isNumberBetween(number, from, to)) {
            System.out.println(errorMessage);
            number = readDoubleNumber(errorMessage);
        }
        return number;
    }

    public static short readShortNumber(String errorMessage) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Short.parseShort(line);
            } catch (NumberFormatException ignore) {}
            System.out.println(errorMessage); // why not in catch?
        }
    }

    public static float readFloatNumber(String errorMessage) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                return Float.parseFloat(line);
            } catch (NumberFormatException ignore) {}
            System.out.println(errorMessage); // why not in catch?
        }
    }

    public static boolean readBooleanValue(String errorMessage) {
        while (true) {
            String line = scanner.nextLine().trim();
            if (Character.toLowerCase(line.charAt(0)) == 'y') {
                return true;
            } else if((Character.toLowerCase(line.charAt(0)) == 'n')) {
                return false;
            } else {
                System.out.println(errorMessage);
            }
        }
    }

    public static short readShortNumberBetween(short from, short to, String errorMessage) {
        short number = readShortNumber(errorMessage);

        while (!isNumberBetween(number, from, to))
        {
            System.out.println(errorMessage);
            number = readShortNumber(errorMessage);
        }
        return number;
    }

    public static float readFloatNumberBetween(float from, float to, String errorMessage) {
        float number = readFloatNumber(errorMessage);

        while (!isNumberBetween(number, from, to))
        {
            System.out.println(errorMessage);
            number = readShortNumber(errorMessage);
        }
        return number;
    }

    public static String readString() {
        return scanner.nextLine().trim();
    }

    public static String readPassword() {
        String pass;
        while (true) {
            pass = readString();

            System.out.println("Confirm Password:");
            if (readString().equals(pass)) {
                return pass;
            }

            System.out.println("Error: Passwords do not match. Try again:");
        }

    }

    public static String readLine() {
        return scanner.nextLine();
    }

}
