package checkers;

import checkers.ui.console.ConsoleUI;
import checkers.ui.gui.CheckersGUI;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("       CHECKERS - Welcome!       ");
        System.out.println("=================================");
        System.out.println("Choose your interface:");
        System.out.println("  1. Console (text-based)");
        System.out.println("  2. GUI (graphical window)");
        System.out.print("Enter 1 or 2: ");

        String choice = "";
        while (!choice.equals("1") && !choice.equals("2")) {
            choice = scanner.nextLine().trim();
            if (!choice.equals("1") && !choice.equals("2")) {
                System.out.print("Invalid input. Please enter 1 or 2: ");
            }
        }

        if (choice.equals("1")) {
            System.out.print("Enter Player 1 name (or press Enter for 'Player 1'): ");
            String p1 = scanner.nextLine().trim();
            if (p1.isEmpty()) p1 = "Player 1";

            System.out.print("Enter Player 2 name (or press Enter for 'Player 2'): ");
            String p2 = scanner.nextLine().trim();
            if (p2.isEmpty()) p2 = "Player 2";

            new ConsoleUI(p1, p2).start();
        } else {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // fall back to default look and feel
            }
            SwingUtilities.invokeLater(() -> new CheckersGUI());
        }

        scanner.close();
    }
}
