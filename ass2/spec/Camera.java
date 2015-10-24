package ass2.spec;

import java.util.function.Function;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.gl2.GLUT;

import ass2.objects.*;

public class Camera extends GameObject {
	
	 	private float[] myBackground;

	 	private static final double transBaseSpeed = 0.1;
	 	private static final double rotBaseSpeed = 1;
	 	private double transSpeed = 1;
	 	private double rotSpeed = 1;
	    
	 	private int transDir = 1;
	 	private int rotDir = 1;
	 	
	    private boolean isMoving;
	    private boolean isTurning;
	    
	    private double myHeight = 1.2;
	    private boolean isFirstPerson = true;
	    private Function<double[], Double> altFun;
	    private PlayerObject myBody;
	    
	    public Camera(GameObject parent, Function<double[], Double> f) {
	        super(parent);
	        this.translate(0, myHeight, 0);
	        myBackground = new float[]{1,1,1,1}; // Default to white background
	        myBody = new PlayerObject(this, f);
	        myBody.translate(0, -myHeight, -1.5);
	        myBody.toggleShowing();
	        altFun = f;
	        // Note: Replace this by parent's matrix
	    }
	    
	    @Override
	    public void setTexture(Texture t) {
	    	myBody.setTexture(t);
	    }
	    
	    public void setTransDirection(int dir) {
	    	transDir = (int)(dir/Math.abs((double)dir));
	    }

	    public void setRotDirection(int dir) {
	    	rotDir = (int)(dir/Math.abs((double)dir));
	    }
	    
	    public void enableMovement() {
	    	isMoving = true;
	    }
	    
	    public void disableMovement() {
	    	isMoving = false;
	    }

	    public void enableTurning() {
	    	isTurning = true;
	    }
	    
	    public void disableTurning() {
	    	isTurning= false;
	    }
	    
	    public void setTransSpeed(double s) {
	    	transSpeed = s;
	    }

	    public void setRotSpeed(double r) {
	    	rotSpeed = r;
	    }
	    
	    public void togglePerspective() {
	    	if (isFirstPerson) {
	    		this.translate(0, 1, 0);
//	    		this.translate(-2*Math.cos(getRotation()[1]*Math.PI/180.0), 1, -2*Math.sin(getRotation()[1]*Math.PI/180.0));
//	    		this.rotafte(new double[]{-26,0,0});
	    	} else {
	    		this.translate(0, -1, 0);
//	    		this.translate(2*Math.cos(getRotation()[1]*Math.PI/180.0), -1, 2*Math.sin(getRotation()[1]*Math.PI/180.0));
//	    		this.rotate(new double[]{26,0,0});
//	    		this.rotate(new double[]{0,0,0});
	    	}
	    	myBody.toggleShowing();
	    	isFirstPerson = !isFirstPerson;
	    	
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
			if (!isFirstPerson) {
//				y += 1;
//				x -= 2*Math.cos(getRotation()[1]*Math.PI/180.0);
//				z -= 2*Math.sin(getRotation()[1]*Math.PI/180.0);
				centre[1] -= 0.2;
			}
			myGLU.gluLookAt(
	    					x, // x
	    					y, // y
    						z, // z
//	    			2,2,2,
    						x + centre[0],
    						y + centre[1],
    						z + centre[2],	// k vector (z-axis)
	    			0,1,0);
//					Math.cos(getRotation()[2]*Math.PI/180), Math.sin(getRotation()[2]*Math.PI/180), 0); // up vector
			// x = cos(z_rot), y = sin(z_rot)
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
	    	myGLU.gluPerspective(40, aspect, 0.1, 160);
	    }
	    
	    @Override
		public void update(double dt) {
	    	if (isMoving) {
	    		double[] p = getGlobalPosition();
	    		double[] xz = new double[]{p[0], p[2]}; 
	    		// Calculate altitude, translate by difference between old and new values
	    		this.translate(0,(altFun.apply(xz) + myHeight - p[1]),transSpeed*transBaseSpeed*transDir);
	    	}
	    	if (isTurning)
    			this.rotate(new double[]{0,rotSpeed*rotBaseSpeed*rotDir,0});
	    }
}
