package cs410.raytracer;

import cs410.matrix.Vector;
import cs410.matrix.Matrix;

public class Face {
    private final int i_a;
    private final int i_b;
    private final int i_c;
    private final int material;
    private Vector a;
    private Vector b;
    private Vector c;
    private Vector normal;
    private boolean facesBuilt;

    public Face(int i_a, int i_b, int i_c, int material){
        this.i_a = i_a - 1;
        this.i_b = i_b - 1;
        this.i_c = i_c - 1;
        this.material = material;
        this.a = null;
        this.b = null;
        this.c = null;
        facesBuilt = false;
    }

    public boolean built(){
        return facesBuilt;
    }

    public void buildFace(Matrix vertices){
        if(facesBuilt){
            return;
        }
        //System.out.println(vertices);
        //System.out.println(i_a + " " + i_b + " " + i_c);
        a = vertices.getColumn(i_a);
        b = vertices.getColumn(i_b);
        c = vertices.getColumn(i_c);
        //System.out.println(a);
        //System.out.println(b);
        //System.out.println(c);
        //normal = 
        facesBuilt = true;
    }

    public Vector a(){
        return a;
    }

    public Vector b(){
        return b;
    }

    public Vector c(){
        return c;
    }

    public Vector normal(){
        return normal;
    }

    public int material(){
        return material;
    }

    public String toString(){
        return i_a + " " + i_b + " " + i_c + " " + material;
    }
}
