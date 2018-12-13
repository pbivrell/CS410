package cs410.raytracer;

public class Image{
    
    private int height;
    private int width;
    private Color[][] image;

    /**
     * Constructor of Image object, creates 2D array of pixels
     * size height and width.
     */

    public Image(int height, int width){
        this.height = height;
        this.width = width;
        image = new Color[height][width];
    }


    /**
     * Sets internal image array at position i,j to pixel p.
     *
     * @param i the row index of the pixel
     * @param j the col index of the pixel
     * @param p the pixel to be added at position i,j
     */
    public void set(int i, int j, Color p){
        image[i][j] = p;
    }

    /**
     * toString for an Image object
     * @return String representation of Image object
     */
    public String toString(){
        String res = "P3\n" + height + " " + width + " 255\n";
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                res += image[i][j] + " ";
            }
            res += "\n";
        }
        return res;
    }
}

