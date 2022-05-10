package com.vian4.tetris;

import com.vian4.tetris.gamepiece.GamePiece;
import com.vian4.tetris.gamepiece.LPiece;
import com.vian4.tetris.gamepiece.Square;

public class Main {

    public static void main(String[] args) {
        GameBoard board = new GameBoard();
        board.setCurrentPiece(new Square(board, 3, 8));
        while (true) {
            try {
                Thread.sleep(2000);
                printBoard(board);
                if (!board.currentPiece().moveDown()) {
                    //reached end
                    printBoard(board);
                    
                    
                    board.setCurrentPiece(new LPiece(board, 2, 5));
                }
                board.currentPiece().rotate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void printBoard(GameBoard board) {
        System.out.println("\n\n\n\n\n\n\n");
        for (int y = board.getBoard().length-1; y >= 0; y--) {
            for (int x = 0; x < board.getBoard()[y].length; x++) {
                if ( (board.getBoard()[y][x].getColor() != null && board.getBoard()[y][x].getColor().a == 1) || pieceContainsPt(board.currentPiece(), x, y)) {
                    System.out.print("x");
                } else {
                    System.out.print("-");
                }
            }
            System.out.print("\n");
        }
    }

    public static boolean pieceContainsPt(GamePiece piece, int x, int y) {
        if (piece == null) return false;
        for (Point point: piece.getPoints()) {
            if (point.getX() == x && point.getY() == y) return true;
        }
        return false;
    }
}
