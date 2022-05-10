package com.vian4.tetris;

public class Main {

    private static class Rectangle extends GamePiece {
        public Rectangle(GameBoard gameBoard, int x, int y) {
            super(gameBoard);
            points = new Point[4];
            points[0] = new Point(x, y);
            points[1] = new Point(x, y + 1);
            points[2] = new Point(x + 1, y);
            points[3] = new Point(x + 1, y + 1);
        }

        public boolean rotate() {
            return false;
        }
    }

    private static class L extends GamePiece {
        public L(GameBoard gameBoard, int x, int y) {
            super(gameBoard);
            points = new Point[4];
            points[0] = new Point(x, y);
            points[1] = new Point(x, y + 1);
            points[2] = new Point(x, y + 2);
            points[3] = new Point(x + 1, y + 2);
        }

        public boolean rotate() {
            return false;
        }
    }

    public static void main(String[] args) {
        GameBoard board = new GameBoard();
        board.setCurrentPiece(new Rectangle(board, 3, 8));
        while (true) {
            try {
                Thread.sleep(500);
                printBoard(board);
                if (!board.currentPiece().moveDown()) {
                    System.out.println("Frozen");
                    printBoard(board);
                    
                    
                    board.setCurrentPiece(new L(board, 2, 5));
                }
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
        for (Point point: piece.points) {
            if (point.getX() == x && point.getY() == y) return true;
        }
        return false;
    }
}
