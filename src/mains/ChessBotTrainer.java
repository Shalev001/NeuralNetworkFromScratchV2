/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mains;

import MatrixVector.Vector;
import activationFunctions.Function;
import activationFunctions.ReLU;
import chess.ChessBoard;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import neuralnetwork.Network;

/**
 *
 * @author shale
 */
public class ChessBotTrainer {
    
    public static void main(String[] args){
        
        File networkLocation = new File("C:\\Users\\shale\\OneDrive\\Desktop\\neuralNetworks\\chessBots\\chessBot1.nnet");
        
        Network nnet = null;
        
        Function actiFunc = new ReLU();
        
        try {
            nnet = Network.importf(networkLocation);
        } catch (IOException ex) {
            Logger.getLogger(ChessBotTrainer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (int i = 1; i <= 1000; i++) {
            
            BufferedReader reader = null;
            Label:
            try {
                String address = "C:\\Users\\shale\\OneDrive\\Desktop\\neuralNetworks\\chessBots\\RandomGameData\\game" + i +".txt";
                
                File gameLog = new File(address);
                
                reader = new BufferedReader(
                        new FileReader(gameLog));
                
                //the first line of each file begins with the winner of the game noted from -1 -> 1
                // modifying the range to be from 0 -> 1
                double[] winner = {(((double) Integer.parseInt(reader.readLine())) + 1) / 2};
                
                //at the beginning I don't want to include games that end in draws since they may slow down training so those are skipped
                if (winner[0] == 0.5){               
                    i++;
                    break Label;
                }
                System.out.println(i);
                
                ArrayList<String> boardStates = new ArrayList<>();
                
                String line = reader.readLine();
                
                while(line != null){
                    boardStates.add(line);
                    line = reader.readLine();
                }
                
                reader.close();
                
                for (int j = 0; j < boardStates.size(); j++) {
                    
                    ChessBoard cb = new ChessBoard(boardStates.get(j));
                    
                    double[] temp = winner.clone();
                    //since the output should represent the liklyhood of the opposing team winning it changes depending on who's turn it is
                    temp[0] = (cb.getTurn() == 1) ? -temp[0] : temp[0];
                    Vector expected = new Vector(temp);
                    
                    nnet.setInput(cb.toNNetInput());
                    
                    nnet.stocasticGradientDiscent(expected, 0.15, actiFunc);
                }
                
                
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ChessBotTrainer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ChessBotTrainer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(ChessBotTrainer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        try {
            nnet.export(networkLocation);
        } catch (IOException ex) {
            Logger.getLogger(ChessBotTrainer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
