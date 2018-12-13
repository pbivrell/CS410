package cs410.raytracer;

import cs410.matrix.Vector;

class Color{
    
    private final float R;
    private final float G;
    private final float B;
    private final Vector color;

    public Color(){
        this(0,0,0);
        //this((float)239/255, (float)239/255, (float)239/255);
    }


    public Color(Vector v){
        this((float) v.get(0), (float) v.get(1), (float) v.get(2));
    }

    /**
     * Constructor of a Color
     * 
     * @param red the amount of red in the color [0-1]
     * @param green the amount of red in the color [0-1]
     * @param blue the amount of red in the color [0-1]
     */
    public Color(float red, float green, float blue){
        R = normalizeColor(red);
        G = normalizeColor(green);
        B = normalizeColor(blue);
        color = new Vector(R,G,B);
    }

    public float normalizeColor(float color){
        color = Math.max(0, color);
        color = Math.min(1, color);
        return color;
    }

    public Vector color(){
        return color;
    }

    public float red(){
        return R;
    }

    public float green(){
        return G;
    }

    public float blue(){
        return B;
    }

    /**
     * toString of Color object
     * @return String representation of a color 
     */
    public String toString(){
        
        return (int)(R*255) + " " + (int)(G*255) + " " + (int)(B*255);
    }

    public static void main(String[] args){
        Color p = new Color();
        System.out.println(p);
    }
}
