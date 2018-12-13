package cs410.raytracer;

import cs410.io.LineReader;
import cs410.io.Error;

public class Material {
    
    private Color A;
    private Color D;
    private Color S;
    private float alpha;

    public Material(double[] parts){
        this.A = new Color((float) parts[4], (float) parts[5], (float) parts[6]);
        this.D = new Color((float) parts[7], (float) parts[8], (float) parts[9]);
        this.S = new Color((float) parts[10], (float) parts[11], (float) parts[12]);
    }

    public Material(){
        this.A = new Color();
        this.D = new Color(); 
        this.S = new Color();
        this.alpha = 1;
    }

    public Material(float ar, float ag, float ab, float dr, float dg, float db, float sr, float sg, float sb, float alpha){
        this.A = new Color(ar, ag, ab);
        this.D = new Color(dr, dg, db);
        this.S = new Color(sr, sg, sb);
        this.alpha = alpha;
    }

    public Color kA(){
        return A;
    }

    public Color kD(){
        return D;
    }

    public Color kS(){
        return S;
    }

    public float[] getNumbers(String line){
        String[] parts = line.split(" ");
        if(parts.length != 4){
            Error.fatal("invalid line in material file [%s]\n", line);
        }
        float[] res = new float[3];
        for(int i = 1; i < parts.length; i++){
            try{
                res[i-1] = Float.parseFloat(parts[i]);
            }catch (NumberFormatException e){
                Error.fatal("invalid line in material file [%s]\n", line);
            } 
        }
        return res;
    }

    public void processLines(String line){
        //System.out.println(line);
        if(line.startsWith("Ka ")){
            float[] K = getNumbers(line);
            A = new Color(K[0], K[1], K[2]);
        }else if(line.startsWith("Kd ")){
            float[] K = getNumbers(line);
            D = new Color(K[0], K[1], K[2]);
        }else if(line.startsWith("Ks ")){
            float[] K = getNumbers(line);
            S = new Color(K[0], K[1], K[2]);
        }else if(line.startsWith("d ")){
            alpha = Float.parseFloat(line.split(" ")[1]);
        }

    }

    public static Material fromFile(String filename){
        Material m = new Material();
        try{
            LineReader.read(filename, m::processLines);
        }catch(Exception e){
            Error.fatal("could not read Material file [%s]: reason: %s\n",filename, e);
        }
        return m;
    }
}
