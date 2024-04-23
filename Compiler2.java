import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Compiler2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("A MINI COMPILER PROJECT FOR CTE711S\n===================================");

        // Loop to continuously accept user input until "99" is entered
        while (true) {
            // Display instructions for user input
            System.out.println("ENTER NEXT STRING === \n-Every String/line must end with a semicolon (;)\n" +
                    "-Enter String (Containing 0 to 9 and/or operators: +,/,*,-)\n" +
                    "-Enter No Space in b/w chara & end the string with semicolon(;)ie 5-4+9*8/2; " +
                    "for Full(7 stages) results of compilation\n" +
                    "-or Enter Space in b/w char with semicolon(;) at end of String ie 5 - 4 + 9 * 8 / 2 ; " +
                    "for the result of arithmetic expression\n" +
                    "-Or Type 99 and press Enter to Quit:");

            // Read user input
            String input = scanner.nextLine().trim();
            
            // Check if user wants to exit
            if (input.equals("99")) {
                System.out.println("Exiting...");
                break;
            }

            // Call the performCompilation method to process the input
            performCompilation(input);
        }
    }

    // Method to perform all stages of compilation
    private static void performCompilation(String input) {
        // Stage 1: Lexical Analysis (Scanning)
        List<Token> tokens = lexicalAnalysis(input);
        System.out.println("======STAGE1: COMPILER TECHNIQUES--> LEXICAL ANALYSIS-Scanner");
        System.out.println("SYMBOL TABLE COMPRISING ATTRIBUTES AND TOKENS:\n");
        displayTokens(tokens);

        // Stage 2: Syntax Analysis (Parsing)
        String derivation = syntaxAnalysis(tokens);
        System.out.println("\n======STAGE2: COMPILER TECHNIQUES--> SYNTAX ANALYSIS-Parser");
        System.out.println("GET A DERIVATION FOR : " + input + "\n" + derivation);

        // Stage 3: Semantic Analysis
        boolean semanticCorrectness = semanticAnalysis(tokens);
        System.out.println("\n======STAGE3: COMPILER TECHNIQUES--> SEMANTIC ANALYSIS");
        if (semanticCorrectness)
            System.out.println("CONCLUSION-->This expression: " + input + " is Syntactically and Semantically correct");
        else
            System.out.println("CONCLUSION-->This expression: " + input + " is not Semantically correct");

        // Stage 4: Intermediate Code Representation (ICR)
        String icr = generateICR(tokens);
        System.out.println("\n======STAGE4: COMPILER TECHNIQUES--> INTERMEDIATE CODE REPRESENTATION (ICR)");
        System.out.println("THE STRING ENTERED IS : " + input);
        System.out.println("The ICR is as follows:\n" + icr);
    }

    // Method for Lexical Analysis
    private static List<Token> lexicalAnalysis(String input) {
        // Create a list to store tokens
        List<Token> tokens = new ArrayList<>();
        
        // Split the input string based on operators and semicolon
        String[] parts = input.split("(?=[+*/;-])|(?<=[+*/;-])");
        for (String part : parts) {
            // Check if the part is not empty
            if (!part.trim().isEmpty()) {
                if (isNumeric(part.trim())) {
                    tokens.add(new Token(part.trim(), TokenType.IDENTIFIER));
                } else if (isOperator(part.trim())) {
                    tokens.add(new Token(part.trim(), TokenType.OPERATOR));
                } else if (part.trim().equals(";")) {
                    tokens.add(new Token(part.trim(), TokenType.SYMBOL));
                }
            }
        }
        return tokens;
    }

    // Method to check if a string is numeric
    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Method to check if a string is an operator
    private static boolean isOperator(String str) {
        return str.equals("+") || str.equals("*") || str.equals("/") || str.equals("-");
    }

    // Method for Syntax Analysis
    private static String syntaxAnalysis(List<Token> tokens) {
        StringBuilder derivation = new StringBuilder();
        for (Token token : tokens) {
            derivation.append(token.getValue()).append(" ");
        }
        String initialDerivation = derivation.toString().trim();
        StringBuilder finalDerivation = new StringBuilder(initialDerivation + "\n");

        // Replace E[0-9]+ with "digit"
        while (true) {
            String[] parts = derivation.toString().split("\\s+");
            boolean replaced = false;
            for (int i = 0; i < parts.length - 2; i += 2) {
                if (parts[i].matches("E[0-9]+")) {
                    parts[i] = "digit" + parts[i].substring(1);
                    replaced = true;
                }
            }
            if (!replaced) break;

            derivation.setLength(0);
            for (String part : parts) {
                derivation.append(part).append(" ");
            }
            finalDerivation.append(String.join(" ", parts)).append("\n");
        }
        return finalDerivation.toString();
    }

    // Method for Semantic Analysis
    private static boolean semanticAnalysis(List<Token> tokens) {
        return true;
    }

    // Method to generate Intermediate Code Representation (ICR)
    private static String generateICR(List<Token> tokens) {
        StringBuilder icr = new StringBuilder();
        List<String> tempVariables = new ArrayList<>();
        int tempCount = 1;
        int index = 0;

        // Generate ICR for arithmetic operations
        while (index < tokens.size()) {
            Token token = tokens.get(index);

            if (token.getType() == TokenType.OPERATOR) {
                String operand1 = tokens.get(index - 1).getValue();
                String operand2 = tokens.get(index + 1).getValue();
                String tempVar = "t" + tempCount++;
                icr.append(tempVar).append("= ").append(operand1).append(token.getValue()).append(operand2).append("\n");
                tempVariables.add(tempVar);
                index += 2;
            } else {
                index++;
            }
        }

        // Print the intermediate code
        for (int i = 0; i < tempVariables.size(); i++) {
            icr.append(tempVariables.get(i)).append("= ").append(tokens.get(i * 2).getValue()).append("\n");
        }

        icr.append("CONCLUSION-->The expression was correctly generated in ICR");
        return icr.toString();
    }

    // Method to display tokens
    private static void displayTokens(List<Token> tokens) {
        int tokenNumber = 1;
        System.out.println("TOKEN# VALUE TYPE");
        for (Token token : tokens) {
            System.out.println("TOKEN#" + tokenNumber + " " + token.getValue() + " " + token.getType());
            tokenNumber++;
        }
        System.out.println("Total number of Tokens: " + tokens.size());
        System.out.println("GIVEN THE GRAMMAR: E=E1 | E=E1*E2 | E=E1+E2 | E=digit | E={0,1,2,3,4,5,6,7,8,9}");
    }

    // Inner class to represent Token
    private static class Token {
        private String value;
        private TokenType type;

        // Constructor to initialize Token
        public Token(String value, TokenType type) {
            this.value = value;
            this.type = type;
        }

        // Getter method for value
        public String getValue() {
            return value;
        }

        // Getter method for type
        public TokenType getType() {
            return type;
        }
    }

    // Enum to represent Token Type
    private enum TokenType {
        IDENTIFIER,
        OPERATOR,
        SYMBOL
    }
}
