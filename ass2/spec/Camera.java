package ass2.spec;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

public class Camera extends GameObject {
	
	 	private float[] myBackground;
	    
	    public Camera(GameObject parent) {
	        super(parent);

	        myBackground = new float[]{1,1,1,1}; // Default to white background
	        // Note: Replace this by parent's matrix
	    }

	    public Camera() {
	        this(GameObject.ROOT);
	    }
	    
	    public float[] getBackground() {
	        return myBackground;
	    }
	    
	    // Pre-multiplying all operations
	    @Override
	    public void translate(double dx, double dy, double dz) {
	    	double[] pos = getPosition();
	    	double[][] m = MathUtil.multiply(MathUtil.rotationMatrix(getRotation()),
								MathUtil.translationMatrix(new double[]{dx,dy,dz}));
	    	double[] newPos = new double[]{m[0][3],m[1][3],m[2][3]};
//	    	System.out.printf("%f %f %f\n", pos[0] + newPos[0], pos[1] + newPos[1], pos[2] + newPos[2]);
	    	setPosition(pos[0] + newPos[0], pos[1] + newPos[1], pos[2] + newPos[2]);
	    }

	    public void setBackground(float[] background) {
	        myBackground = background;
	    }
	    
	    public void setView(GL2 gl) {
	    	gl.glClearColor(myBackground[0], myBackground[1],
	    				    myBackground[2], myBackground[3]);
	    	gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
	    	
	    	gl.glMatrixMode(GL2.GL_MODELVIEW);
	    	gl.glLoadIdentity();
	    	GLU myGLU = new GLU();
	    	double x,y,z;
	    	x = getGlobalPosition()[0];
	    	y = getGlobalPosition()[1];
			z = getGlobalPosition()[2];
			double[] centre = MathUtil.multiply(MathUtil.rotationMatrix(getRotation()), new double[]{0,0,-1,0});

			myGLU.gluLookAt(
	    					x, // x
	    					y, // y
    						z, // z
//	    			2,2,2,
    						x + centre[0],
    						y + centre[1],
    						z + centre[2],	// k vector (z-axis)
//	    			0,0,0,
	    			0,1,0); // up vector
			
	    }

	    public void reshape(GL2 gl, int x, int y, int width, int height) {
	        
	        double aspect;
	        if (width > height) {
	            aspect = (1.0 * width) / height;
	        }
	        else {
	            aspect = (1.0 * height) / width;
	        }

	        // match the projection aspect ratio to the viewport
	        // to avoid stretching
	        gl.glMatrixMode(GL2.GL_PROJECTION);
	    	gl.glLoadIdentity();

	        GLU myGLU = new GLU();
	    	myGLU.gluPerspective(60, aspect, 0.1, 40);
	    }

	    @Override
		public void update(double dt) {
//	    	this.rotate(new double[]{0,dt*50,0});
//	    	this.translate(dt*0.01, dt*0.01, dt*0.01);
	    }
}
