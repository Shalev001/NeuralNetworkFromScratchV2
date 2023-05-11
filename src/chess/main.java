/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

import java.util.Scanner;

/**
 *
 * @author shale
 */
public class main {
    
    public static void main(String[] args) {
        
        ChessBoard cb = new ChessBoard();
        
        Scanner input = new Scanner(System.in);
        
        int[] vals = new int[4];
        
        System.out.println(cb.toString());
        
        while(!cb.checkMate(1) && !cb.checkMate(0)){
            if (cb.getTurn() == 0){
                System.out.println("black's turn, enter the [x,y] of the piece you want to meet followed by the new [x,y] (seperated by spaces)");
            }else{
                System.out.println("white's turn, enter the [x,y] of the piece you want to meet followed by the new [x,y] (seperated by spaces)");
            }
            for (int i = 0; i < 4; i++) {
                vals[i] = input.nextInt();
            }
            
            if (!cb.takeNextTurn(vals[0], vals[1], vals[2], vals[3])){
                System.out.println(cb.toString());
                System.out.println("an impossible move was played, please redo the turn");
            }else{
                System.out.println(cb.toString());
            }
        }
        if (cb.checkMate(0)){
            System.out.println("white wins!");
        }else if (cb.checkMate(1)){
            System.out.println("black wins!");
        }
    }
}
