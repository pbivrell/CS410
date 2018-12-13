package cs410.matrix;

public class Vector {

    public double[] raw;

    public Vector(double... d){
        raw = d;
    }

    public String toString(){
        String res = "";
        for(int i = 0; i < raw.length; i++){
            res += raw[i] + "\n";
        }
        return res;
    }

    public Vector add(Vector v){
        double[] res = new double[raw.length];
        
        for(int i = 0; i < raw.length; i++){
            res[i] = raw[i] + v.raw[i];
        }
        return new Vector(res);
    }

    public Vector subtract(Vector v){
        double[] res = new double[raw.length];
        
        for(int i = 0; i < raw.length; i++){
            res[i] = raw[i] - v.raw[i];
        }
        return new Vector(res);
    }

    public double[] getArray(){
        return this.raw;
    }

    public Vector crossProduct(Vector v){
        double[] res = new double[raw.length];

        res[0] = raw[1] * v.raw[2] - v.raw[1] * raw[2];
        res[1] = v.raw[0] * raw[2] - raw[0] * v.raw[2];
        res[2] = raw[0] * v.raw[1] - v.raw[0] * raw[1];
        return new Vector(res);
    }

    public double dotProduct(Vector v){
        //System.out.println(this);
        //System.out.println(v);
        double result = 0;
        for(int i = 0; i < raw.length; i++){
            result += raw[i] * v.raw[i];
        }
        return result;
    }

    public double norm(){
        double res = 0;
        for(int i = 0; i < raw.length; i++){
            res += raw[i] * raw[i];
        }
        return Math.sqrt(res);
    }

    public Vector makeUnitLength(){
        double norm = this.norm();
        if(norm == 0){
            return new Vector(raw);
        }
        return this.multiplyScalar(1/ norm);
    }

    public Vector times(Vector v){
        double[] res = new double[raw.length];
        for(int i = 0; i < raw.length; i++){
            res[i] = raw[i] * v.raw[i];
        }
        return new Vector(res);
    }

    public Vector copy(){
        return new Vector(raw.clone());
    }

    public Vector multiplyScalar(double d){
        double[] res = new double[raw.length];
        for(int i = 0; i < raw.length; i++){
            res[i] = raw[i] * d;
        }
        return new Vector(res);
    }

    public double get(int i){
        return raw[i];
    }

    public void set(int i, double d){
        raw[i] = d;
    }
}
