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
	
	static public double[] normaliseVectord(double[] v) {
		double[] nv = new double[3];
		double len = Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
		nv[0] = v[0]/len;
		nv[1] = v[1]/len;
		nv[2] = v[2]/len;
		return nv;
	}
	
	static public float[] normaliseVectorf(float[] v) {
		float[] nv = new float[3];
		float len = (float)Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
		nv[0] = v[0]/len;
		nv[1] = v[1]/len;
		nv[2] = v[2]/len;
		return nv;
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
	
	static public double[] surfaceNormal(double[] p1, double[] p2, double[] p3) {
		double x1, y1, z1, x2, y2, z2, x3, y3, z3;
		x1 = p1[0];
		y1 = p1[1];
		z1 = p1[2];
		x2 = p2[0];
		y2 = p2[1];
		z2 = p2[2];
		x3 = p3[0];
		y3 = p3[1];
		z3 = p3[2];
		
		// Found from cross product of vectors P_12, P_13
		return new double[]{
				(y2-y1)*(z3-z1) - (y3-y1)*(z2-z1),
				(z2-z1)*(x3-x1) - (x2-x1)*(z3-z1),
				(x2-x1)*(y3-y1) - (x3-x1)*(y2-y1)
		};
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
                for (int k = 0; k < 4; k++) {
                   m[i][j] += p[i][k] * q[k][j]; 
                }
            }
        }

        return m;
    }

    /**
     * Multiply a vector by a matrix
     * 
     * @param m A 4x4 matrix
     * @param v A 4x1 vector
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
    /**
     * Extracts the x, y and z rotations from a matrix
     * @return Double array of x, y, z rotations.
     */
    public static double[] decompose_rotation(double[][] m) {
    	double thetaX = 0.0, thetaY = 0.0, thetaZ = 0.0;
	    
    	thetaX = Math.atan2(m[2][1], m[2][2]);
    	thetaY = Math.atan2(-m[2][0], Math.sqrt(m[2][1]*m[2][1] + m[2][2]*m[2][2]));
    	thetaZ = Math.atan2(m[1][0], m[0][0]);
    	return normaliseAngleArray(new double[]{thetaX*180/Math.PI,
    											thetaY*180/Math.PI,
    											thetaZ*180/Math.PI});
    }

    /**
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
     * 
     * @param angle in degrees
     * @return
     */
    public static double[][] rotationMatrix(double[] angle) {
    	// From  Slide 20 in Geom.
    	double radx = Math.PI/180*angle[0];
    	double rady = Math.PI/180*angle[1];
    	double radz = Math.PI/180*angle[2];

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
