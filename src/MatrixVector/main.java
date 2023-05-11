/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MatrixVector;

/**
 *
 * @author shale
 */
public class main {
    
    public static void main(String[] args){
        
        double[][] contents1 = {{1,2,3,4},{2,3,4,5},{3,4,5,6},{4,5,6,7}};
        double[] contents2 = {1,2,3,4};
        
        Matrix mat1 = new Matrix(contents1);
        
        Vector vec1 = new Vector(contents2);
        
        System.out.println(mat1.multiply(vec1).toString());
    }
}
