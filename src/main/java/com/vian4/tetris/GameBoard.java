package com.vian4.tetris;

import com.vian4.tetris.gamepiece.GamePiece;

public class GameBoard {

    public class GBPoint {
        private boolean occupied = false;
        private Color color;
        private int x, y;

        public GBPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void setOccupied(Color color) {
            this.occupied = true;
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        public boolean isOccupied() {
            return occupied;
        }

    }

    private GBPoint[][] board;
    private GamePiece currentPiece = null;

    public GameBoard(int r, int c) {
        board = new GBPoint[r][c];
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[y][x] = new GBPoint(x, y);
            }
        }
    }
    
    public boolean currentPieceSelected() {
        return currentPiece != null;
    }

    public GamePiece currentPiece() {
        return currentPiece;
    }

    public void setCurrentPiece(GamePiece piece) {
        currentPiece = piece;
    }

    public GBPoint[][] getBoard() {
        return board;
    }

}
