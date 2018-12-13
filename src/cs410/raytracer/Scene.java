package cs410.raytracer;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Arrays;

import java.io.IOException;

import cs410.debugging.Debug;
import cs410.io.LineReader;
import cs410.io.Error;
import cs410.matrix.MatrixApache;
import cs410.matrix.Matrix;
import cs410.matrix.Vector;

public class Scene{

    // Debug stuff
    private final boolean DEBUG_ENABLED = true;
    private final Debug d; 

    // Objects in scene
    private ArrayList<Obj> objects;
    
    // Lights
    private ArrayList<Light> lights;
    private Vector ambient;

    // Camera model stuff
    private Vector eye;
    private Vector look;
    private Vector up;
    private int[] res;
    private double di; // Distance from focal point to image plane
    private double[] bounds; // left bottom right top
    private int height;
    private int width;
    private int recursionLevel;

    // Image object representing resultant image
    private Image result;  

    /**
     * Constructs and initalizes a Scene object
     */
    public Scene(){
        // Init debug object
        d = new Debug(DEBUG_ENABLED);
        // Init collection of objects
        objects = new ArrayList<Obj>();
        lights = new ArrayList<Light>();
        // Init camera stuff
        eye = null;
        look = null;
        up = null;
        di = 0;
        bounds = new double[4];
        recursionLevel = 0;
        res = new int[2];
    }


    public Color getColor(Intersection i){
        // Ambient Light
        Vector kA = i.material.kA().color().times(ambient);
        // Difuse

        Vector kD = new Vector(0,0,0);
        for(Light light : lights){
            Vector l = light.position.subtract(i.pointOnSurface).makeUnitLength();
            //System.out.println("TO LIGHT: " + l);
            //System.out.println("TEMP:"+ i.normal == null);
            //System.out.println("TEMP2:" + l == null);
            double cosAngle = l.dotProduct(i.normal);
            if(cosAngle >= 0){
                //System.out.println(i.normal);
                //System.out.println(i.material.kD().color());
                //System.out.println(light.brightness);
                kD = kD.add((i.material.kD().color().times(light.brightness)).multiplyScalar(cosAngle));
            }
        }

        Vector k = kA.add(kD);
        return new Color(k);
    }

    public Image raytrace(){
        Image result = new Image(height, width);
        Ray[] rays = makeRays();
        System.out.println("Made Rays: " + rays.length);
        int hit_count = 0;
        for(int j = 0; j < rays.length; j++){
            double percent = ((j+1)/(double)rays.length) * 100.0;
            if(percent % 5 == 0){
                System.out.printf("- %.1f%% Complete\n", percent);
            }
            Ray ray = rays[j];
            //System.out.println(ray);
            //System.out.print(".");
            //d.print("New Ray");
            boolean hit = false;
            double distance = 999999;
            Intersection hit_intersect = null;

            for(Obj obj : objects){
                //d.print("New Object");
                Intersection i = obj.intersect(ray);
                if(i.hit && i.distance < distance){
                    hit_intersect = i;
                    hit = true;
                    distance = i.distance;
                }
            }
            if(hit){
                hit_count++;
            }
            //System.out.println(((hit) ? "" : "Not ") + "Hit");
            Color p = hit ? getColor(hit_intersect) : new Color();
            result.set(ray.getCol(), ray.getRow(), p);
        }
        System.out.printf("Hit %d rays\n", hit_count);
        return result;   
    }

    private Ray[] makeRays(){
        // Build camera
        up = up.makeUnitLength();
        Vector WV = eye.subtract(look).makeUnitLength();
        Vector UV = up.crossProduct(WV).makeUnitLength();
        Vector VV = WV.crossProduct(UV).makeUnitLength();

        Ray[] rays = new Ray[height*width];
        d.print("Making image:", width, "x", height);
        // PARALLELIZE HERE
        for(int i = 0; i <width; i++){
            for(int j = 0; j <height; j++){
                //http://www.cs.colostate.edu/~cs410/yr2018fa/more_resources/sage/cs410lec08n01.html
                // WHY ARE i and j flipped here?
                double px = (double)i / (width-1) * (bounds[2] - bounds[0]) + bounds[0];
                double py = (double)j /(height-1) * (bounds[1] - bounds[3]) + bounds[3];
                
                //System.out.println("X:" + i + " " + px);
                //System.out.println("Y:" + j + " " + py);
                //System.out.println(px + " " + py);
                //System.out.println(eye);
                //System.out.println(WV);
                //System.out.println(WV.multiplyScalar(-di));
                //System.out.println(UV);
                //System.out.println(UV.multiplyScalar(px));
                //System.out.println(VV);
                //System.out.println(VV.multiplyScalar(py));
                //System.out.println(eye.add(WV.multiplyScalar(-di)).add(UV.multiplyScalar(px)).add(VV.multiplyScalar(py)));
                Vector pixpt = eye.add(WV.multiplyScalar(-di)).add(UV.multiplyScalar(px)).add(VV.multiplyScalar(py));
                //System.out.println("Pixpt:\n" + pixpt);
                //System.out.println(pixpt.subtract(eye));
                //System.out.println(pixpt.subtract(eye).makeUnitLength());
                Vector shoot = (pixpt.subtract(eye));
                //System.out.println("Shoot:\n" + shoot); 
                shoot = shoot.makeUnitLength();
                //System.out.println("Shoot:\n" + shoot); 
                //System.out.println(i * height + j);
                rays[i * height + j] = new Ray(pixpt, shoot,i,j);
            }
        }
        //System.out.println(Arrays.toString(rays));
        return rays;
    }

    /**
     * This method takes a scanner object open to a driver file
     * and populates a scene as described by the driver file.
     *
     * Sample driver:
     * eye 4 4 4 
     * look 0 0 0 
     * up 0 0 1 
     * d 2 
     * bounds -1 -1 1 1 
     * res 256 256 
     * recursionLevel 3
     * ambient 0.1 0.1 0.1 
     * light 0 4 4 1 0.50 0.25 0.25 
     * light 4 0 4 1 0.25 0.50 0.25 
     * sphere -1 -1 -1 1 0.2 0.2 0.2 0.5 0.5 0.5 0.5 0.5 0.5 0.9 0.9 0.9 
     * model 0.0 1.0 0.0 45 1.0 1.0 0.0 1.0 cube.obj
     *
     * 
     * @param scan a Scanner that has been opened to a driver file
     */

    public void processDriver(String filename){ 
        try {
            LineReader.read(filename, this::processLines);
        }catch(IOException e){
            Error.fatal("could not read driver file [%s]: reason [%s]\n",filename, e);
        }
        System.out.println("Made Objects: " + objects.size());
        System.out.println("Made Lights: " + lights.size());
    }

    public void processLines(String line){
        if(line.startsWith("model")){
            objects.add(makeModel(line));
        }else if(line.startsWith("sphere")){
            objects.add(makeSphere(line));
        }else if(line.startsWith("eye")){
            eye = new Vector(processNumbers(line));
        }else if(line.startsWith("look")){
            look = new Vector(processNumbers(line));
        }else if(line.startsWith("up")){
            up = new Vector(processNumbers(line));
        }else if(line.startsWith("d")){
            di = processNumber(line);
        }else if(line.startsWith("bounds")){
            bounds = processNumbers(line);
        }else if(line.startsWith("res")){
            double[] temp = processNumbers(line);
            d.print("H, W",temp[0], temp[1]);
            height = (int)temp[1]; width = (int)temp[0];
            result = new Image(height, width);
        }else if(line.startsWith("recursionLevel")){
            recursionLevel = (int)processNumber(line);
        }else if(line.startsWith("ambient")){
            ambient = new Vector(processNumbers(line));
        }else if(line.startsWith("light")){
            double[] temp = processNumbers(line);
            if(temp.length != 7){
                Error.fatal("invalid line in driver file [%s]\n", line);
            }
            lights.add(new Light(temp));
        }
    }

    /**
     * This method takes a line containing n numbers, then
     * updates an array with the values from the line as
     * doubles or ints, will stop program if the line is formatted 
     * incorrectly.
     * 
     * @param numberLine String containing n number of doubles/ints
     * @param container Array of doubles/ints which will be populated
     */

    private double[] processNumbers(String numbersLine){
        String[] parts = numbersLine.split(" ");
        double[] res = new double[parts.length-1];
        //System.out.println(Arrays.toString(parts));
        try {
            for(int i = 1; i < parts.length; i++){
                res[i-1] = Double.parseDouble(parts[i]);
            }
        } catch(NumberFormatException e){
            Error.fatal("Invalid [%s] line in driver file.\n",parts[0]);
        }
        return res;
    }

    /**
     * This method takes a line containing a single double,
     * then returns the double value, will stop program if
     * the line is foramtted incorrectly.
     *
     * @param numberLine line containing a single double
     * @return double value from the line
     */

    private double processNumber(String numberLine){
        String[] parts = numberLine.split(" ");
        try {
            return Double.parseDouble(parts[1]);
        } catch(NumberFormatException e){
            Error.fatal("Invalid [%s] line in driver file.\n", parts[0]);
        }
        return -1;
    }

    /**
     * This method takes a string describing a model and
     * returns a Model object in world coordinates, the
     * constructed model is read from the .obj file.
     * 
     * Sample modelLine:
     * model 1.0 0.0 0.0 45 1.0 10.0 10.0 10.0 cube.obj
     * 
     * modelLine Labled:
     * model wx wy wz theta scale tx ty tz model.obj 
     * model (wx, wy, wz, theta) = rotation, scale, (tx, ty, tz) = translation, obj file
     *
     * @param modelLine String that describes a model
     * @return Model Object
     */
    private Model makeModel(String modelLine){
        d.print("Making model with params:", modelLine);
        Scanner scan = new Scanner(modelLine);
        // Throw away model tag
        scan.next();
        try {
            return new Model(scan.nextFloat(), scan.nextFloat(), scan.nextFloat(), 
                    scan.nextInt(),
                    scan.nextFloat(), scan.nextFloat(), scan.nextFloat(), scan.nextFloat(),
                    scan.next()); 
        }catch(InputMismatchException e){
            Error.fatal("model input line formatted incorrectly: [%s]\n",modelLine);
        }
        return null;    
    }

    /**
     * This method takes a line describing a sphere and
     * produces a sphere object matching the description
     * from the line.
     *
     * Sample Sphere line
     * sphere -1 -1 -1 1 0.2 0.2 0.2 0.5 0.5 0.5 0.5 0.5 0.5 0.9 0.9 0.9 
     *
     * sphereLine labeled:
     * sphere x, y, z, radius, Ka_red, Ka_green, Ka_blue, Kd_red, Kd_green, Kd_blue, Ks_red, Ks_green, Ks_blue, Kr_red, Kr_green, Kr_blue
     * sphere (x,y,z) = world coords, radius, (Ka_red, Ka_green, Ka_blue) = ambient, (Kd_red, Kd_green, Kd_blue) = diffuse, (Ks_red, Ks_green, Ks_blue) = specular, (Kr_red, Kr_green, Kr_blue) = attenuation
     *
     * @param sphereLine line describing a new sphere object
     * @return Sphere object
     */

    private Sphere makeSphere(String sphereLine){
        return new Sphere(processNumbers(sphereLine));
    }

    /**
     * This method wrote the model files back to an output directory,
     * it has been deprecated because after assignment 1 we won't be
     * writing the models back to disk.
     *
     * @deprecated
     * @param path String representing path to output directory
     */
    @Deprecated
    public void writeFile(String path){
        System.err.println("[Warning]: Functionality for writing objects to file has been removed");
        return;
        /*
           String filename = path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("/")+9);
           try{    
        // Create Directory
        File dir = new File(filename);
        boolean madeDir = dir.mkdir();
        if(!madeDir){
        d.print("[Warning]: Replacing exisiting files @", filename);
        }
        d.print("Making Dir:",filename + "/");

        // Create Obj files
        PrintWriter writer;
        for(int i = models.size()-1; i >= 0; i-- ){
        Model m = models.get(i);
        //String fileExtension = String.format("_mw%02d.obj", modelLabels.get(m.getModelFile()));
        //modelLabels.put(m.getModelFile(), modelLabels.get(m.getModelFile()) - 1);
        d.print("Writing file:", filename + "/" + m.getModelFile() + fileExtension);
        writer = new PrintWriter(new File(filename + "/" + m.getModelFile() + fileExtension));
        writer.print(m);
        writer.close();
        }
        }catch(IOException e){
        System.err.printf("[Fatal Error]: Filed to create writer to [%s]: %s",filename, e);
        }*/
    }
}
