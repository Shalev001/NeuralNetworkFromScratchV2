/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mains;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import neuralnetwork.Network;

/**
 *
 * @author shale
 */
public class ChessBotGenerator {
    
    public static void main(String[] args){
        
        //enter the name of the network here
        String name = "chessBot1";
        //enter the desired folder address here
        String address = "C:\\Users\\shale\\OneDrive\\Desktop\\neuralNetworks\\chessBots";
        
        // "\\" only works for windows
        File file = new File(address + "\\" + name + ".nnet");
        
        int[] NetworkInfo = {-2,81920,512,32,32,1};
        
        Network nnet = new Network(NetworkInfo);
        
        nnet.InitializeWeightsAsIdentities();
        nnet.InitializeRandomBiases();
        
        try {
            nnet.export(file);
        } catch (IOException ex) {
            System.out.println("network did not export properly");
            Logger.getLogger(ChessBotGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
