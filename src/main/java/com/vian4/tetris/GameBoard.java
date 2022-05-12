package com.vian4.tetris;

import com.jme3.math.ColorRGBA;
import com.vian4.tetris.gamepiece.GamePiece;

public class GameBoard {

    public class GBPoint extends AbstractPoint {
        private boolean occupied = false;
        private ColorRGBA color;

        public GBPoint(int x, int y, int z) {
            super(x, y, z);
        }
        
        public void setOccupied(ColorRGBA color) {
            this.occupied = true;
            this.color = color;
        }

        public ColorRGBA getColor() {
            return color;
        }

        public boolean isOccupied() {
            return occupied;
        }

    }

    private GBPoint[][][] board;
    private GamePiece currentPiece = null;

    public GameBoard(int r, int c, int d) {
        board = new GBPoint[r][c][d];
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                for (int z = 0; z < board[y][x].length; z++) {
                    board[y][x][z] = new GBPoint(x, y, z);
                }
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

    public GBPoint[][][] getBoard() {
        return board;
    }

}
