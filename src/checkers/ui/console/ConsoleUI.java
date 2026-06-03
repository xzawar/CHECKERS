package checkers.ui.console;

import checkers.logic.*;
import checkers.logic.GameState;
import checkers.model.*;
import java.util.*;

public class ConsoleUI {
    private CheckersGame game;
    private Scanner sc = new Scanner(System.in);

    public ConsoleUI(String p1, String p2){
        game = new CheckersGame(p1, p2);
    }

    public void start(){
        System.out.println("=== CHECKERS (6x6) ===");
        System.out.println(game.getP1().getName() + " plays RED   |   "
                         + game.getP2().getName() + " plays BLACK");

        while (!game.getEngine().getState().isOver()){
            printBoard();
            Player cur = game.currentPlayer();
            String colorLabel = (cur.getColor() == Piece.Color.RED) ? "Red" : "Black";
            System.out.println("\n>>> " + cur.getName() + "'s turn (" + colorLabel + " pieces)");

            List<Move> moves = game.getEngine().getValidMoves();
            printMoves(moves);

            Move chosen = pickMove(moves);
            game.getEngine().applyMove(chosen);
        }

        printBoard();

        // Resolve winner by name
        GameState.Status result = game.getEngine().getState().getStatus();
        String winnerName = (result == GameState.Status.RED_WINS)
                          ? game.getP1().getName()
                          : game.getP2().getName();
        String winnerColor = (result == GameState.Status.RED_WINS) ? "Red" : "Black";
        System.out.println("\n=============================");
        System.out.println("  GAME OVER!");
        System.out.println("  " + winnerName + " (" + winnerColor + ") wins!");
        System.out.println("=============================");
    }

    private void printBoard(){
        Board b = game.getEngine().getBoard();
        System.out.print("\n  ");
        for (int c = 0; c < Board.SIZE; c++) System.out.print(c + " ");
        System.out.println();
        
        for (int r = 0; r < Board.SIZE; r++){
            System.out.print(r + " ");
            for (int c = 0; c < Board.SIZE; c++){
                Piece p = b.getPiece(r, c);
                System.out.print((p == null ? "." : p.toString()) + " ");
            }
            System.out.println();
        }
    }

    private void printMoves(List<Move> moves){
        System.out.println("Valid moves:");
        for (int i = 0; i < moves.size(); i++)
            System.out.println("  " + i + ": " + moves.get(i));
    }

    private Move pickMove(List<Move> moves){
        System.out.print("Choose move number: ");
        int idx = -1;
        while (idx < 0 || idx >= moves.size()){
            try {
                idx = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid, try again: ");
            }
        }
        return moves.get(idx);
    }
}
