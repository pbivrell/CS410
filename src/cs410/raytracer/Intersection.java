package cs410.raytracer;

import cs410.matrix.Vector;

public class Intersection {

    public final Material material;
    public final Vector normal;
    public final Vector pointOnSurface;
    public final double distance;
    public final boolean hit;

    public Intersection(Material material, Vector normal, Vector pointOnSurface, double distance, boolean hit){
        this.material = material;
        this.normal = normal;
        this.pointOnSurface = pointOnSurface;
        this.distance = distance;
        this.hit = hit;
    }

}
