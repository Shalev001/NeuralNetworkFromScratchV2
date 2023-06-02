/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mains;

import activationFunctions.Function;
import activationFunctions.ReLU;
import chess.ChessBoard;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import neuralnetwork.Network;

/**
 *
 * @author shale
 */
public class DataCollector {// this code is intended to generate training data
    
    public static void main(String[] args) {
        
        File location = new File("C:\\Users\\shale\\OneDrive\\Desktop\\neuralNetworks\\chessBots\\chessBot1");
        
        Network nnet = null;
        ChessBoard cb = new ChessBoard();
        Function actiFunc = new ReLU();
        
        try {
            nnet = Network.importf(location);
        } catch (IOException ex) {
            Logger.getLogger(DataCollector.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        nnet.setInput(cb.toNNetInput());
        
        nnet.compute(actiFunc);
        
        System.out.println(nnet.getOutput());
    }
    
}
