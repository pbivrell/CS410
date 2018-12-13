package cs410.matrix;

import Jama.Matrix;

public class Helper {
    
    public static String printMatrix(Matrix m){
        double[][] raw =  m.getArray();
        String res = "";
        res += m.getRowDimension() + "," + m.getColumnDimension() + "\n";
        for(double[] col: raw){
            for(double item: col){
                res += String.format("%.3f ", item);
            }
            res += "\n";
        }
        return res;
    }
}

