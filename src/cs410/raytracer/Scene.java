package cs410.raytracer;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;

import cs410.debugging.Debug;


public class Scene{
    
    private HashMap<String, Integer> modelLabels;
    private ArrayList<Model> models;
    private final boolean DEBUG_ENABLED = true;
    private final Debug d; 

    public Scene(){
        d = new Debug(DEBUG_ENABLED);
        models = new ArrayList<Model>();
        modelLabels = new HashMap<String, Integer>();
    }

    public void writeFile(String path){
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
                String fileExtension = String.format("_mw%02d.obj", modelLabels.get(m.getModelFile()));
                modelLabels.put(m.getModelFile(), modelLabels.get(m.getModelFile()) - 1);
                d.print("Writing file:", filename + "/" + m.getModelFile() + fileExtension);
                writer = new PrintWriter(new File(filename + "/" + m.getModelFile() + fileExtension));
                writer.print(m);
                writer.close();
            }
        }catch(IOException e){
            System.err.printf("[Fatal Error]: Filed to create writer to [%s]: %s",filename, e);
        }
    }

    public void processFile(Scanner scan){ 
        while(scan.hasNextLine()){
            String line = scan.nextLine();
            d.print("Processing line:" + line);
            if(line.startsWith("model")){
                Model m = makeModel(line);
                models.add(m);
                modelLabels.put( m.getModelFile() , modelLabels.get(m.getModelFile()) == null ? 0 : modelLabels.get(m.getModelFile()) + 1);
            }
        }
        
    }

    public Model makeModel(String modelLine){
        d.print("Making model with params:", modelLine);
        // model 1.0 0.0 0.0 45 1.0 10.0 10.0 10.0 cube.obj
        // model wx wy wz theta scale tx ty tz model.obj 
        // model (wx, wy, wz, theta) = rotation, scale, (tx, ty, tz) = translation, obj file
        Scanner scan = new Scanner(modelLine);
        // Throw away model tag
        scan.next();
        try {
            return new Model(scan.nextFloat(), scan.nextFloat(), scan.nextFloat(), 
                             scan.nextInt(),
                             scan.nextFloat(), scan.nextFloat(), scan.nextFloat(), scan.nextFloat(),
                             scan.next()); 
        }catch(InputMismatchException e){
            System.err.printf("[Fatal Error]: model input line formatted incorrectly: [%s]\n",modelLine);
            System.exit(-1);
        }
        return null;
        
    }

}
