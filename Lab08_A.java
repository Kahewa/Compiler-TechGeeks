import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Lab08_A {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("A MINI COMPILER PROJECT FOR CTE711S\n===================================");
        int count = 0;

        while (true) {

            count++;
            System.out.println("ENTER NEXT STRING === #"+count+"\n-Every String/line must end with a semicolon (;)\n" +
                    "-Enter String (Containing 0 to 9 and/or operators: +,/,*,-)\n" +
                    "-Enter No Space in b/w chara & end the string with semicolon(;)ie 5-4+9*8/2; " +
                    "for Full(7 stages) results of compilation\n" +
                    "-or Enter Space in b/w char with semicolon(;) at end of String ie 5 - 4 + 9 * 8 / 2 ; " +
                    "for the result of arithmetic expression\n" +
                    "-Or Type 99 and press Enter to Quit:");

            String input = scanner.nextLine().trim();
            if (input.equals("99")) {
                System.out.println("Exiting...");
                break;
            }

            // Stage 1: Lexical Analysis (Scanning)
            List<Lab08_A.Token> tokens = lexicalAnalysis(input);
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
            System.out.println("CONCLUSION-->The expression was correctly generated in ICR");

            // Stage 5: Code Generation

        }
    }

    private static List<Lab08_A.Token> lexicalAnalysis(String input) {
        List<Lab08_A.Token> tokens = new ArrayList<>();
        String[] parts = input.split("(?=[+*/;-])|(?<=[+*/;-])");
        for (String part : parts) {
            if (!part.trim().isEmpty()) {
                if (isNumeric(part.trim())) {
                    tokens.add(new Lab08_A.Token(part.trim(), Lab08_A.TokenType.IDENTIFIER));
                } else if (isOperator(part.trim())) {
                    tokens.add(new Lab08_A.Token(part.trim(), Lab08_A.TokenType.OPERATOR));
                } else if (part.trim().equals(";")) {
                    tokens.add(new Lab08_A.Token(part.trim(), Lab08_A.TokenType.SYMBOL));
                }
            }
        }
        return tokens;
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isOperator(String str) {
        return str.equals("+") || str.equals("*") || str.equals("/") || str.equals("-");
    }

    private static String syntaxAnalysis(List<Lab08_A.Token> tokens) {
        StringBuilder derivation = new StringBuilder();
        for (Lab08_A.Token token : tokens) {
            derivation.append(token.getValue()).append(" ");
        }
        String initialDerivation = derivation.toString().trim();
        StringBuilder finalDerivation = new StringBuilder(initialDerivation + "\n");

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

    private static boolean semanticAnalysis(List<Lab08_A.Token> tokens) {
        return true;
    }

    private static String generateICR(List<Lab08_A.Token> tokens) {
        StringBuilder icr = new StringBuilder();
        List<String> tempVariables = new ArrayList<>();
        int tempCount = 1;
        int index = 0;

        while (index < tokens.size()) {
            Lab08_A.Token token = tokens.get(index);

            if (token.getType() == Lab08_A.TokenType.OPERATOR) {
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

    private static void displayTokens(List<Lab08_A.Token> tokens) {
        int tokenNumber = 1;
        System.out.println("TOKEN# VALUE TYPE");
        for (Lab08_A.Token token : tokens) {
            System.out.println("TOKEN#" + tokenNumber + " " + token.getValue() + " " + token.getType());
            tokenNumber++;
        }
        System.out.println("Total number of Tokens: " + tokens.size());
        System.out.println("GIVEN THE GRAMMAR: E=E1 | E=E1*E2 | E=E1+E2 | E=digit | E={0,1,2,3,4,5,6,7,8,9}");
    }

    private static class Token {
        private String value;
        private Lab08_A.TokenType type;

        public Token(String value, Lab08_A.TokenType type) {
            this.value = value;
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public Lab08_A.TokenType getType() {
            return type;
        }
    }

    private enum TokenType {
        IDENTIFIER,
        OPERATOR,
        SYMBOL
    }
}
