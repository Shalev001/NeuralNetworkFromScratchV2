/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package activationFunctions;

/**
 *
 * @author shale
 */
public class ReLU implements Function{
    
    public double compute(double x){
        if (x < 0){
            return 0;
        }
        return x;
    }
    
    public double computeDir(double x){
        if (x < 0){
            return 0;
        }
        return 1;
    }
}
