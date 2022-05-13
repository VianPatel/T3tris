package com.vian4.tetris;

import com.jme3.math.ColorRGBA;
import com.vian4.tetris.gamepiece.GamePiece;

public class GameBoard {

    public class GBPoint extends AbstractPoint {
        private ColorRGBA color;

        public GBPoint(int x, int y, int z) {
            super(x, y, z);
        }
        
        public void setOccupied(ColorRGBA color) {
            this.color = color;
        }

        public void setUnoccupied() {
            this.color = null;
        }

        public ColorRGBA getColor() {
            return color;
        }

        public boolean isOccupied() {
            return color != null;
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

    public void clearYPlain(int y) {
        for (y = y + 1; y < board.length; y ++) {
            for (int x = 0; x < board[y].length; x++) {
                for (int z = 0; z < board[y][x].length; z++) {
                    board[y-1][x][z] = board[y][x][z];
                }
            }
        }
        
        for (int x = 0; x < board[y - 1].length; x++) {
            for (int z = 0; z < board[y - 1][x].length; z++) {
                board[board.length - 1][x][z].setUnoccupied();
            }
        }
    }

    public void clearXPlain(int x) {
        for (int y = 0; y < board.length; y++) {
            for (int z = 0; z < board[y][x].length; z++) {
                board[y][x][z].setUnoccupied();
            }
        }
    }

    public void clearZPlain(int z) {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[y][x][z].setUnoccupied();
            }
        }
    }

    public boolean isYPlainFilled(int y) {
        for (int x = 0; x < board[y].length; x++) {
            for (int z = 0; z < board[y][x].length; z++) {
                if (!board[y][x][z].isOccupied()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isXPlainFilled(int x) {
        for (int y = 0; y < board.length; y++) {
            for (int z = 0; z < board[y][x].length; z++) {
                if (!board[y][x][z].isOccupied()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isZPlainFilled(int z) {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (!board[y][x][z].isOccupied()) {
                    return false;
                }
            }
        }
        return true;
    }

}
