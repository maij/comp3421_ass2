package ass2.spec;

/**
 * A collection of useful math methods 
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 */
public class MathUtil {
	/**
	 * Returns a 4x4 identity matrix
	 */
	static public double[][] identity() {
		return new double[][]{{1, 0, 0, 0},
							  {0, 1, 0, 0},
							  {0, 0, 1, 0},
							  {0, 0, 0, 1}};
	}
	/**
	 * Print the transformation matrix, for debugging purposes.
	 * @param p A 4x4 matrix.
	 */
	static public void printMatrix(double[][] p) {
		int i, j;
		for (i = 0; i < 4; i++) {
			System.out.print("[ ");
			for (j = 0; j < 4; j++) {
				System.out.printf("%f ", p[i][j]);
			}
			System.out.print("]\n");
		}
	}
	
	/**
     * Normalise an angle to the range (-180, 180]
     * 
     * @param angle 
     * @return
     */
	static public double normaliseAngle(double angle) {
    	return ((angle + 180.0) % 360.0 + 360.0) % 360.0 - 180.0;
	}
	
    /**
     * Normalise an array of angles to the range (-180, 180]
     * 
     * @param angles
     * @return
     */
    static public double[] normaliseAngleArray(double[] angles) {
        double[] normal = new double[3];
        for (int i = 0; i < 3; i++) {
        	normal[i] = normaliseAngle(angles[i]);
        }
    	return normal;
    }

    /**
     * Clamp a value to the given range
     * 
     * @param value
     * @param min
     * @param max
     * @return
     */

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * Multiply two matrices, p * q
     * 
     * @param p A 4x4 matrix
     * @param q A 4x4 matrix
     * @return
     */
    public static double[][] multiply(double[][] p, double[][] q) {

        double[][] m = new double[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                m[i][j] = 0;
                for (int k = 0; k < 3; k++) {
                   m[i][j] += p[i][k] * q[k][j]; 
                }
            }
        }

        return m;
    }

    /**
     * Multiply a vector by a matrix
     * 
     * @param m A 3x3 matrix
     * @param v A 3x1 vector
     * @return
     */
    public static double[] multiply(double[][] m, double[] v) {

        double[] u = new double[4];

        for (int i = 0; i < 4; i++) {
            u[i] = 0;
            for (int j = 0; j < 4; j++) {
                u[i] += m[i][j] * v[j];
            }
        }

        return u;
    }



    // ===========================================
    // COMPLETE THE METHODS BELOW
    // ===========================================
    

    /**
     * TODO: A 3D translation matrix for the given offset vector
     * 
     * @param v origin offset vector.
     * @return
     */
    public static double[][] translationMatrix(double[] v) {
    	// From  Slide 18 in Geom.
    	double[][] m = new double[][]{{1, 0, 0, v[0]},
    			 					  {0, 1, 0, v[1]},
    			 					  {0, 0, 1, v[2]},
    			 					  {0, 0, 0, 1}};
        return m;
    }

    /**
     * TODO: A 3D rotation matrix for the given angle
     * 
     * @param angle in degrees
     * @return
     */
    public static double[][] rotationMatrix(double[] angle) {
    	// From  Slide 20 in Geom.
    	double radx = Math.PI/180*angle[0];
    	double rady = Math.PI/180*angle[1];
    	double radz = Math.PI/180*angle[2];
    	// TODO: Make these the correct 4x4 matrices
    	double[][] mx = new double[][]{{1, 0		     ,	0			  , 0},
            						   {0, Math.cos(radx), -Math.sin(radx), 0},
					   				   {0, Math.sin(radx),  Math.cos(radx), 0},
					   				   {0, 0			 , 	0			  ,	1}};

    	double[][] my = new double[][]{{ Math.cos(rady), 0,	Math.sin(rady), 0},
            						   { 0			   , 1, 0			  , 0},
					   				   {-Math.sin(rady), 0, Math.cos(rady), 0},
					   				   { 0			   , 0, 0			  ,	1}};

		double[][] mz = new double[][]{{Math.cos(radz), -Math.sin(radz), 0, 0},
		    						   {Math.sin(radz),  Math.cos(radz), 0, 0},
					   				   {0			  ,  0			   , 1, 0},
					   				   {0			  ,  0			   , 0,	1}};
    	return multiply(mx, multiply(my,mz));    
    }

    /**
     * TODO: A 3D scale matrix that scales both all by the same factor
     * 
     * @param scale
     * @return
     */
    public static double[][] scaleMatrix(double scale) {
    	// From  Slide 24 in Geom.
    	double[][] m = new double[][]{{scale, 0    , 0    , 0},
									  {0    , scale, 0    , 0},
									  {0    , 0    , scale, 0},
    			 					  {0    , 0    , 0    , 1}};
        return m;
    }

    
}
