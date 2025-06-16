import java.util.Scanner;

public class USStatesSearch {

    // List of 50 states concatenated as one string with spaces separating them
    private static final String statesText = String.join(" ",
        "Alabama Alaska Arizona Arkansas California Colorado Connecticut Delaware Florida Georgia " +
        "Hawaii Idaho Illinois Indiana Iowa Kansas Kentucky Louisiana Maine Maryland Massachusetts Michigan " +
        "Minnesota Mississippi Missouri Montana Nebraska Nevada New Hampshire New Jersey New Mexico New York " +
        "North Carolina North Dakota Ohio Oklahoma Oregon Pennsylvania Rhode Island South Carolina South Dakota " +
        "Tennessee Texas Utah Vermont Virginia Washington West Virginia Wisconsin Wyoming"
    ).toLowerCase();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1) Display the text");
            System.out.println("2) Search");
            System.out.println("3) Exit program");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            switch (choice) {
                case 1:
                    System.out.println("\nText of 50 US states:\n" + statesText);
                    break;
                case 2:
                    System.out.print("Enter pattern to search: ");
                    String pattern = scanner.nextLine().toLowerCase();
                    int[] matches = boyerMooreSearch(statesText, pattern);
                    if (matches.length == 0) {
                        System.out.println("No matches found.");
                    } else {
                        System.out.print("Pattern found at indices: ");
                        for (int idx : matches) {
                            System.out.print(idx + " ");
                        }
                        System.out.println();
                    }
                    break;
                case 3:
                    System.out.println("Exiting program.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Boyer-Moore bad character rule search
    private static int[] boyerMooreSearch(String text, String pattern) {
        if (pattern.length() == 0) return new int[0];

        int[] badChar = buildBadCharTable(pattern);

        int s = 0; // shift of the pattern with respect to text
        int n = text.length();
        int m = pattern.length();
        java.util.List<Integer> result = new java.util.ArrayList<>();

        while (s <= (n - m)) {
            int j = m - 1;

            // Move backwards while characters of pattern and text are matching at this shift s
            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
                j--;
            }

            // If pattern matched fully
            if (j < 0) {
                result.add(s);
                // Shift pattern so that next character in text aligns with last occurrence in pattern
                s += (s + m < n) ? m - badChar[text.charAt(s + m)] : 1;
            } else {
                // Shift pattern so bad character in text aligns with last occurrence in pattern
                int shift = Math.max(1, j - badChar[text.charAt(s + j)]);
                s += shift;
            }
        }

        // Convert list to array
        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    // Build bad character table: stores last occurrence of each character in pattern
    private static int[] buildBadCharTable(String pattern) {
        final int ASCII_SIZE = 256;
        int[] badChar = new int[ASCII_SIZE];
        for (int i = 0; i < ASCII_SIZE; i++) {
            badChar[i] = -1; // Initialize all occurrences as -1
        }
        for (int i = 0; i < pattern.length(); i++) {
            badChar[pattern.charAt(i)] = i; // Record last index of char in pattern
        }
        return badChar;
    }
}
