/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package activationFunctions;

/**
 *
 * @author shale
 */
public class sigmoid implements Function{
    
    public double compute(double x){
        return (1/(1+Math.pow(Math.E,-x)));
    }
    
    public double computeDir(double x){
        return compute(x) * (1 - compute(x));
    }
    
}
