import java.util.Scanner;

public class CompilerTask1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Stage 1: Lexical Analysis (Scanner)
        String line;
        System.out.println("Enter the program:");
        line = scanner.nextLine().trim();

        // Stage 2: Syntax Analysis (Parser)
        if (!line.startsWith("START") || !line.endsWith("STOP")) {
            System.out.println("Error at Syntax Analysis: Missing START or STOP keyword");
            return;
        }

        // Stage 3: Semantic Analysis (Syntactic Analysis)
        int M = 0, N = 0, K = 0, P = 0, R = 0, H = 0, g = 0, k = 0, m = 0;

        // Stage 4: Intermediate Code Representation (ICR)
        String[] parts = line.split(" ");
        try {
            M = Integer.parseInt(parts[1]);
            N = Integer.parseInt(parts[2]);
            K = Integer.parseInt(parts[3]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Error at Semantic Analysis: Invalid input format");
            return;
        }

        // Stage 5: Code Generation (CG)
        try {
            N = M - K;
            P = g / (H - 1) + m * N / k;
            R = M + N / K;
        } catch (ArithmeticException e) {
            System.out.println("Error at Semantic Analysis: ArithmeticException - Division by zero");
            return;
        }

        // Stage 6: Code Optimization (CO)
        // No optimization is performed in this simplified version

        // Stage 7: Target Machine code (TMC) in Binary
        System.out.println("Binary representation of P: " + Integer.toBinaryString(P));
        System.out.println("Binary representation of R: " + Integer.toBinaryString(R));

        scanner.close();
    }
}
