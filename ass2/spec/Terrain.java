package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;



/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlight;

    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth) {
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
        mySunlight = new float[3];
    }
    
    public Terrain(Dimension size) {
        this(size.width, size.height);
    }

    public Dimension size() {
        return mySize;
    }

    public List<Tree> trees() {
        return myTrees;
    }

    public List<Road> roads() {
        return myRoads;
    }

    public float[] getSunlight() {
        return mySunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        mySunlight[0] = dx;
        mySunlight[1] = dy;
        mySunlight[2] = dz;        
    }
    
    /**
     * Resize the terrain, copying any old altitudes. 
     * 
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mySize = new Dimension(width, height);
        double[][] oldAlt = myAltitude;
        myAltitude = new double[width][height];
        
        for (int i = 0; i < width && i < oldAlt.length; i++) {
            for (int j = 0; j < height && j < oldAlt[i].length; j++) {
                myAltitude[i][j] = oldAlt[i][j];
            }
        }
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myAltitude[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, double h) {
        myAltitude[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * TO BE COMPLETED
     * 
     * @param x
     * @param z
     * @return
     */
    public double altitude(double x, double z) {
        double altitude = 0;
        double x_prop = x - (long)x;
        double z_prop = z - (long)z;
        int x_hi, x_lo, z_hi, z_lo;
        
        //  (x_hi,y_hl,z_lo)  (x_hi,y_hh,z_hi)   
        // 	          	+-----+  
        //     		  	|    /|  
        //        	  	|  /  |
        //      	  	|/    |
        //  	      	+-----+
        //  (x_lo,y_ll,z_lo)  (x_hi,y_lh,z_h)
        
        x_hi = (int) (x+1);
        z_hi = (int) (z+1);
        x_lo = (int) (x);
        z_lo = (int) (z);
//        System.out.printf("%d %d %d %d\n", x_hi, z_hi, x_lo, z_lo);
        double y_ll = 0, y_lh = 0, y_hl = 0, y_hh = 0;
        
        // If z_hi or x_hi are out of bounds, then the altitude is 0 (initialized above)
        y_ll = getGridAltitude(x_lo,z_lo);
//        System.out.printf("Out of Bounds?\t z_hi :: %b; x_hi :: %b\n", z_hi > size().getHeight(), x_hi > size().getWidth());
        if (z_hi < size().getHeight())
        	y_lh = getGridAltitude(x_lo,z_hi);
        if (x_hi < size().getWidth())
        	y_hl = getGridAltitude(x_hi,z_lo);
        if (x_hi < size().getWidth() && z_hi < size().getHeight())
        y_hh = getGridAltitude(x_hi,z_hi);
        
        // Bilinearly interpolating, first with x.
        // Low and high altitudes wrt the z axis (i.e. y_lo is at z = z_lo, y_hi is at z = z_hi)
        double y_lo, y_hi;
        y_lo = x_prop*y_hl + (1-x_prop)*y_ll;
        y_hi = x_prop*y_hh + (1-x_prop)*y_lh;
        // Now interpolate along z
        altitude = z_prop*y_hi + (1-z_prop)*y_lo;
//        System.out.printf("x  = %f z  = %f\n", x_prop, z_prop);
//        System.out.printf("xp = %f zp = %f\n", x_prop, z_prop);
        
        return altitude;
    }

    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(double x, double z) {
        double y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        myTrees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(double width, double[] spine) {
        Road road = new Road(width, spine);
        myRoads.add(road);        
    }


}
