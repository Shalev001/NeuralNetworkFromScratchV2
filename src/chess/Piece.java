package chess;

import java.util.ArrayList;



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author shale
 */
public abstract class Piece {
    
    int pieceNum;// 0 = pawn, 1 = bishop, 2 = knight, 3 = rook, 4 = queen, 5 = king
    int pieceValue;
    int[] pieceLocation;//the x,y location of the piece
    int moveCount;
    int pieceColour;// 0 is black, 1 is white
    
    public Piece(int xLoc,int yLoc,int colour){
        
        pieceLocation = new int[2];
        
        pieceColour = colour;
        
        pieceLocation[0] = xLoc;
        pieceLocation[1] = yLoc;
        
        moveCount = 0;
    }
    
    public abstract Piece clone();

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public int getPieceColour() {
        return pieceColour;
    }
    
    /**
     *
     * @param xLoc
     * @param yLoc
     * @return
     */
    protected boolean onBoard(int xLoc, int yLoc) {
        if ((xLoc >= 1 && xLoc <= 8) && (yLoc >= 1 && yLoc <= 8)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * static version
     * @param xLoc
     * @param yLoc
     * @return 
     */
    public static boolean SonBoard(int xLoc, int yLoc) {
        if ((xLoc >= 1 && xLoc <= 8) && (yLoc >= 1 && yLoc <= 8)) {
            return true;
        } else {
            return false;
        }
    }

    protected int enemyPieceThere(int xLoc, int yLoc, ArrayList<Piece> enemyPieces) {
        for (int i = 0; i < enemyPieces.size(); i++) {
            if (enemyPieces.get(i).getPieceLocation()[0] == xLoc && enemyPieces.get(i).getPieceLocation()[1] == yLoc) {
                return i;
            }
        }
        return -1;
    }
    protected boolean alliedPieceThere(int xLoc, int yLoc, ArrayList<Piece> alliedPieces) {
        for (int i = 0; i < alliedPieces.size(); i++) {
            if (alliedPieces.get(i).getPieceLocation()[0] == xLoc && alliedPieces.get(i).getPieceLocation()[1] == yLoc) {
                return true;
            }
        }
        return false;
    }
    
    public abstract boolean move(int xLoc, int yLoc, ArrayList<Piece> enemyPieces, ArrayList<Piece> alliedPieces);
    
    public abstract boolean canMove(int xLoc, int yLoc, ArrayList<Piece> enemyPieces, ArrayList<Piece> alliedPieces);
    
    public abstract int[][] spacesBetween(int xLoc,int yLoc);

    public int getPieceValue() {
        return pieceValue;
    }

    public void setLocation(int xLoc, int yLoc){
        pieceLocation[0] = xLoc;
        pieceLocation[1] = yLoc;
    }
    
    public int[] getPieceLocation() {
        return pieceLocation;
    }

    public int getPieceNum() {
        return pieceNum;
    }

    public int getMoveCount() {
        return moveCount;
    }
    
    public void movecountdown(int num){
        moveCount -= num;
    }
       
}
