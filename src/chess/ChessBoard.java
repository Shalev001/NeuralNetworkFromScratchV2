/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

import java.util.ArrayList;
import java.util.Collections;
import MatrixVector.Vector;
import java.util.Arrays;

/**
 *
 * @author shale
 */
public class ChessBoard {

    ArrayList<Piece> white;
    ArrayList<Piece> black;
    int turn;// 0 = black turn, 1 = white turn

    public ChessBoard() {

        white = new ArrayList();
        black = new ArrayList();
        turn = 1;

        white.add(new King(5, 1, 1));// kings will always be at index 0

        for (int i = 1; i <= 8; i++) {
            white.add(new Pawn(i, 2, 1));
        }

        white.add(new Rook(1, 1, 1));
        white.add(new Rook(8, 1, 1));
        ((King) white.get(0)).setQSideR((Rook) white.get(9));
        ((King) white.get(0)).setKSideR((Rook) white.get(10));

        white.add(new Knight(2, 1, 1));
        white.add(new Knight(7, 1, 1));

        white.add(new Bishop(3, 1, 1));
        white.add(new Bishop(6, 1, 1));

        white.add(new Queen(4, 1, 1));

        black.add(new King(5, 8, 0));

        for (int i = 1; i <= 8; i++) {
            black.add(new Pawn(i, 7, 0));
        }

        black.add(new Rook(1, 8, 0));
        black.add(new Rook(8, 8, 0));
        ((King) black.get(0)).setQSideR((Rook) black.get(9));
        ((King) black.get(0)).setKSideR((Rook) black.get(10));

        black.add(new Knight(2, 8, 0));
        black.add(new Knight(7, 8, 0));

        black.add(new Bishop(3, 8, 0));
        black.add(new Bishop(6, 8, 0));

        black.add(new Queen(4, 8, 0));
    }

    public boolean legalMove(int x1, int y1, int x2, int y2) {

        ArrayList<Piece> team;
        ArrayList<Piece> otherTeam;

        if (turn == 0) {
            team = black;
            otherTeam = white;
        } else {
            team = white;
            otherTeam = black;
        }

        for (Piece piece : team) {
            if (piece.getPieceLocation()[0] == x1 && piece.getPieceLocation()[1] == y1 && piece.canMove(x2, y2, otherTeam, team)) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<int[]> getLegalMoves() {//the problem is probably here

        ArrayList<Piece> team = (turn == 0) ? black : white;
        ArrayList<Piece> otherTeam = (turn == 0) ? white : black;

        ArrayList<int[]> moves = new ArrayList<>();

        for (int i = 0; i < team.size(); i++) {
            Piece piece = team.get(i);
            for (int j = 1; j <= 8; j++) {
                for (int k = 1; k <= 8; k++) {
                    if (piece.canMove(j, k, otherTeam, team)) {

                        ArrayList<Piece> preTurnAllies = new ArrayList<>();
                        for (Piece alliedPiece : team) {
                            preTurnAllies.add(alliedPiece.clone());
                        }

                        ArrayList<Piece> preTurnEnemy = new ArrayList<>();
                        for (Piece enemyPiece : otherTeam) {
                            preTurnEnemy.add(enemyPiece.clone());
                        }

                        int[] move = {piece.getPieceLocation()[0], piece.getPieceLocation()[1], j, k};
                        boolean turnSuccess = takeNextTurn(move[0],move[1],move[2],move[3]);
                        
                        if (turnSuccess && inCheck() == -1) {//this is probably the problem
                            moves.add(move);
                        }
                        piece.setLocation(move[0], move[1]);
                        piece.movecountdown(1);

                        if (turn == 0) {
                            black = preTurnAllies;
                            team = preTurnAllies;
                            white = preTurnEnemy;
                            otherTeam = preTurnEnemy;
                        } else {
                            white = preTurnAllies;
                            team = preTurnAllies;
                            black = preTurnEnemy;
                            otherTeam = preTurnEnemy;
                        }
                    }
                }
            }
        }

        return moves;
    }

    public void setWhite(ArrayList<Piece> white) {
        this.white = white;
    }

    public void setBlack(ArrayList<Piece> black) {
        this.black = black;
    }

    /**
     * returns the index of the threatening piece
     *
     * @param colour
     * @return
     */
    public int inCheck() {

        ArrayList<Piece> enemyPieces;
        ArrayList<Piece> alliedPieces;
        int xLoc;
        int yLoc;

        if (turn == 0) {
            enemyPieces = white;
            alliedPieces = black;
            xLoc = black.get(0).getPieceLocation()[0];
            yLoc = black.get(0).getPieceLocation()[1];
        } else {
            enemyPieces = black;
            alliedPieces = white;
            xLoc = white.get(0).getPieceLocation()[0];
            yLoc = white.get(0).getPieceLocation()[1];
        }

        for (int i = 0; i < enemyPieces.size(); i++) {
            if (enemyPieces.get(i).canMove(xLoc, yLoc, alliedPieces, enemyPieces)) {
                ((King) alliedPieces.get(0)).setInCheck(true);
                return i;
            }
        }
        ((King) alliedPieces.get(0)).setInCheck(false);
        return -1;
    }

    private boolean wouldBeInCheck(int xLoc, int yLoc, int colour) {

        ArrayList<Piece> enemyPieces;
        ArrayList<Piece> alliedPieces;
        ArrayList<Piece> preTurnAllies = new ArrayList<>();

        if (colour == 0) {
            enemyPieces = white;
            alliedPieces = black;
        } else {
            enemyPieces = black;
            alliedPieces = white;
        }

        for (Piece alliedPiece : alliedPieces) {
            preTurnAllies.add(alliedPiece.clone());
        }

        boolean flag = false;
        alliedPieces.remove(0);

        for (int i = 0; i < enemyPieces.size(); i++) {
            if (enemyPieces.get(i).canMove(xLoc, yLoc, alliedPieces, enemyPieces)) {
                flag = true;
            }
        }

        if (colour == 0) {
            black = preTurnAllies;

        } else {
            white = preTurnAllies;
        }

        return flag;
    }

    private boolean KingUnableToMove(int colour) {

        int kingxLoc;
        int kingyLoc;

        if (colour == 0) {
            kingxLoc = black.get(0).getPieceLocation()[0];
            kingyLoc = black.get(0).getPieceLocation()[1];
        } else {
            kingxLoc = white.get(0).getPieceLocation()[0];
            kingyLoc = white.get(0).getPieceLocation()[1];
        }

        boolean flag = true;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((j != 0 || i != 0) && (Piece.SonBoard(kingxLoc + j, kingyLoc + i))) {

                    if (!wouldBeInCheck(kingxLoc + j, kingyLoc + i, colour)) {
                        flag = false;
                    }
                }
            }
        }
        return flag;
    }

    public boolean takeNextTurn(int pxLoc, int pyLoc, int nxLoc, int nyLoc) {
        if (turn == 0) {
            ArrayList<Piece> preTurnEnemy = new ArrayList<>();
            for (Piece white1 : white) {
                preTurnEnemy.add(white1.clone());
            }
            for (int i = 0; i < black.size(); i++) {
                if (black.get(i).getPieceLocation()[0] == pxLoc && black.get(i).getPieceLocation()[1] == pyLoc) {
                    if (black.get(i).move(nxLoc, nyLoc, white, black)) {
                        if (inCheck() != -1) {
                            black.get(i).setLocation(pxLoc, pyLoc);
                            black.get(i).movecountdown(1);
                            white = preTurnEnemy;
                            return false;
                        }
                        turn = 1;
                        if (black.get(i).getPieceNum() == 0 && nyLoc == 1) {//if the piece moved is now a pawn on the bottom rank
                            black.remove(i);//replace it with a queen
                            black.add(new Queen(nxLoc, nyLoc, 0));
                        }
                        return true;
                    }
                }
            }

        } else if (turn == 1) {
            ArrayList<Piece> preTurnEnemy = new ArrayList<>();
            for (Piece black1 : black) {
                preTurnEnemy.add(null);
            }
            Collections.copy(preTurnEnemy, black);
            for (int i = 0; i < white.size(); i++) {
                if (white.get(i).getPieceLocation()[0] == pxLoc && white.get(i).getPieceLocation()[1] == pyLoc) {
                    if (white.get(i).move(nxLoc, nyLoc, black, white)) {
                        if (inCheck() != -1) {
                            white.get(i).setLocation(pxLoc, pyLoc);
                            white.get(i).movecountdown(1);
                            black = preTurnEnemy;
                            return false;
                        }
                        turn = 0;
                        if (white.get(i).getPieceNum() == 0 && nyLoc == 8) {//if the piece moved is now a pawn on the top rank
                            white.remove(i);//replace it with a queen
                            white.add(new Queen(nxLoc, nyLoc, 0));
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean takeRandomMove() {

        ArrayList<int[]> moves = getLegalMoves();

        if (moves.isEmpty()) {
            return false;
        }

        int rand = (int) (Math.random() * moves.size());

        int[] move = moves.get(rand);
        return takeNextTurn(move[0], move[1], move[2], move[3]);
    }

    public boolean checkMate() {//########## this is the problem

        //this if statement is broken
        if (!KingUnableToMove(turn)) {// if the king can move, not checkmate
            return false;
        }

        ArrayList<Piece> enemyPieces;
        ArrayList<Piece> alliedPieces;
        int xLoc;
        int yLoc;

        if (turn == 0) {
            enemyPieces = white;
            alliedPieces = black;
            xLoc = black.get(0).getPieceLocation()[0];
            yLoc = black.get(0).getPieceLocation()[1];
        } else {
            enemyPieces = black;
            alliedPieces = white;
            xLoc = white.get(0).getPieceLocation()[0];
            yLoc = white.get(0).getPieceLocation()[1];
        }

        //finding the piece that is threataning the king
        if (inCheck() == -1) {
            return false;
        }
        Piece threataningPiece = enemyPieces.get(inCheck());

        //if the piece is a pawn or knight the only option is to take it
        if (threataningPiece.getPieceNum() == 0 || threataningPiece.getPieceNum() == 2) {
            //checking if it can be taken
            for (int i = 1; i < alliedPieces.size(); i++) {//excluding king since it must be stuck
                if (alliedPieces.get(i).canMove(threataningPiece.getPieceLocation()[0],
                        threataningPiece.getPieceLocation()[0], enemyPieces, alliedPieces)) {
                    return false;
                }
            }
            return true;
        } //if the piece is any other piece it can either be blocked or taken
        else {
            //checking if it can be taken
            for (int i = 1; i < alliedPieces.size(); i++) {//excluding king since it must be stuck
                if (alliedPieces.get(i).canMove(threataningPiece.getPieceLocation()[0],
                        threataningPiece.getPieceLocation()[0], enemyPieces, alliedPieces)) {
                    return false;
                }
            }
            for (int[] spaceBetween : threataningPiece.spacesBetween(xLoc, yLoc)) {
                for (int i = 1; i < alliedPieces.size(); i++) {//excluding king since it must be stuck
                    if (alliedPieces.get(i).canMove(spaceBetween[0],
                            spaceBetween[1], enemyPieces, alliedPieces)) {
                        return false;
                    }
                }
            }
            return true;
        }

    }

    public boolean staleMate() {
        return ((black.size() == 1 && white.size() == 1) || (getLegalMoves().isEmpty()) && !checkMate()) ;
    }

    public ArrayList<Piece> getWhite() {
        return white;
    }

    public ArrayList<Piece> getBlack() {
        return black;
    }

    /**
     * a method to take the current board state and convert it into input for a
     * neural network. follows the format outlined in the design document
     *
     * @param colour
     * @return
     */
    public Vector toNNetInput_Outdated() {

        double[] output = new double[65];//every piece on the board plus an entry for the colour

        output[64] = turn; //the last entry is the colour;

        for (Piece piece : white) {
            //             xLocation              +          yLocation * 8    adding one since the first index should be the colour
            output[(piece.getPieceLocation()[0] - 1 + (piece.getPieceLocation()[1] - 1) * 8)] = piece.getPieceNum() + 1;//entering the piece code for the specific piece
        }
        for (Piece piece : black) {
            output[(piece.getPieceLocation()[0] - 1 + (piece.getPieceLocation()[1] - 1) * 8)] = piece.getPieceNum() + 7;
        }

        return new Vector(output);
    }

    /**
     * a method to convert an integer indexing a 4 dimensional space with 8
     * entries per dimension into coordinates the first two dimensions are the
     * original x/y and the second two are the new x/y
     *
     * @param index
     * @return coordinates in the format: x1, y1, x2, y2
     */
    public static int[] indexToCoordinates_Outdated(int index) {

        int[] output = new int[4];

        output[1] = index / 512;

        output[0] = (index % 512) / 64;

        output[3] = ((index % 512) % 64) / 8;

        output[2] = ((index % 512) % 64) % 8;

        return output;
    }

    /**
     * a method to convert the current board state into a network input
     * following the NNUE format
     *
     * @return
     */
    public Vector toNNetInput() {
        double[] indexedBoard = new double[40960 * 2];
        int[] WKingLoc = white.get(0).getPieceLocation();
        int[] BKingLoc = black.get(0).getPieceLocation();

        if (turn == 1) {// encoded from whites perspective
            //own king
            for (int i = 1; i < white.size() + black.size() - 1; i++) {
                Piece piece = (i < white.size()) ? white.get(i) : black.get((i - white.size()) + 1);
                int pieceNum = (i < white.size()) ? piece.getPieceNum() : piece.getPieceNum() + 5;// a king will never be encountered so its piece value should be skipped
                int[] pieceLoc = piece.getPieceLocation();
                indexedBoard[(((WKingLoc[0] - 1 + (WKingLoc[1] - 1) * 8)) * 640 + pieceNum * 64 + ((pieceLoc[0] - 1 + (pieceLoc[1] - 1) * 8)))] = 1;
            }
            //enemyKing
            for (int i = 1; i < white.size() + black.size() - 1; i++) {
                Piece piece = (i < white.size()) ? white.get(i) : black.get((i - white.size()) + 1);
                int pieceNum = (i < white.size()) ? piece.getPieceNum() : piece.getPieceNum() + 5;// a king will never be encountered so its piece value should be skipped
                int[] pieceLoc = piece.getPieceLocation();
                indexedBoard[(((-BKingLoc[0] + 7 + (-BKingLoc[1] + 7) * 8)) * 640 + pieceNum * 64 + ((-pieceLoc[0] + 7 + (-pieceLoc[1] + 7) * 8)))] = 1;
            }
            return new Vector(indexedBoard);

        } else {//encoded from blacks perspective
            //own king
            for (int i = 1; i < white.size() + black.size() - 1; i++) {
                Piece piece = (i < black.size()) ? black.get(i) : white.get((i - black.size()) + 1);
                int pieceNum = (i < black.size()) ? piece.getPieceNum() : piece.getPieceNum() + 5;// a king will never be encountered so its piece value should be skipped
                int[] pieceLoc = piece.getPieceLocation();
                indexedBoard[(((-WKingLoc[0] + 7 + (-WKingLoc[1] + 7) * 8)) * 640 + pieceNum * 64 + ((-pieceLoc[0] + 7 + (-pieceLoc[1] + 7) * 8)))] = 1;
            }
            //enemyKing
            for (int i = 1; i < white.size() + black.size() - 1; i++) {
                Piece piece = (i < black.size()) ? black.get(i) : white.get((i - black.size()) + 1);
                int pieceNum = (i < black.size()) ? piece.getPieceNum() : piece.getPieceNum() + 5;// a king will never be encountered so its piece value should be skipped
                int[] pieceLoc = piece.getPieceLocation();
                indexedBoard[(((BKingLoc[0] - 1 + (BKingLoc[1] - 1) * 8)) * 640 + pieceNum * 64 + ((pieceLoc[0] - 1 + (pieceLoc[1] - 1) * 8)))] = 1;
            }
            return new Vector(indexedBoard);
        }
    }

    public int getEnemyPointTotal() {

        int sum = 0;

        if (turn == 0) {
            for (Piece piece : white) {
                sum += piece.getPieceValue();
            }
        } else {
            for (Piece piece : black) {
                sum += piece.getPieceValue();
            }
        }

        return sum;
    }

    public int getAlliedPointTotal() {

        int sum = 0;

        if (turn == 0) {
            for (Piece piece : black) {
                sum += piece.getPieceValue();
            }
        } else {
            for (Piece piece : white) {
                sum += piece.getPieceValue();
            }
        }

        return sum;
    }

    public int getTurn() {
        return turn;
    }

    public String toString() {
        String str = "";
        boolean flag;

        for (int k = 8; k >= 1; k--) {
            for (int i = 1; i <= 8; i++) {
                str += "|";
                flag = false;
                for (int j = 0; j < black.size(); j++) {
                    if (black.get(j).getPieceLocation()[0] == i && black.get(j).getPieceLocation()[1] == k) {
                        switch (black.get(j).getPieceNum()) {
                            case 0:
                                str += "BPn";
                                break;
                            case 1:
                                str += "BBp";
                                break;
                            case 2:
                                str += "BKn";
                                break;
                            case 3:
                                str += "BRk";
                                break;
                            case 4:
                                str += "BQn";
                                break;
                            case 5:
                                str += "BKg";
                                break;
                        }
                        flag = true;
                    }
                }
                for (int j = 0; j < white.size(); j++) {

                    if (white.get(j).getPieceLocation()[0] == i && white.get(j).getPieceLocation()[1] == k) {
                        switch (white.get(j).getPieceNum()) {
                            case 0:
                                str += "WPn";
                                break;
                            case 1:
                                str += "WBp";
                                break;
                            case 2:
                                str += "WKn";
                                break;
                            case 3:
                                str += "WRk";
                                break;
                            case 4:
                                str += "WQn";
                                break;
                            case 5:
                                str += "WKg";
                                break;
                        }
                        flag = true;
                    }
                }
                if (!flag) {
                    if ((i + k) % 2 == 0) {
                        str += " x ";
                    } else {
                        str += "   ";
                    }
                }
            }
            str += "|\n --- --- --- --- --- --- --- ---\n";
        }
        return str;
    }

}
