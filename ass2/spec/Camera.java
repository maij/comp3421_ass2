package ass2.spec;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

public class Camera extends GameObject {

	 	private float[] myBackground;
	    
	    public Camera(GameObject parent) {
	        super(parent);

	        myBackground = new float[]{1,1,1,1}; // Default to white background
	    }

	    public Camera() {
	        this(GameObject.ROOT);
	    }
	    
	    public float[] getBackground() {
	        return myBackground;
	    }

	    public void setBackground(float[] background) {
	        myBackground = background;
	    }
	    
	    public void setView(GL2 gl) {
	    	gl.glClearColor(myBackground[0], myBackground[1],
	    				    myBackground[2], myBackground[3]);
	    	gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
	    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	    	gl.glLoadIdentity();
	        gl.glRotated(-getGlobalRotation()[0],1,0,0); //rotate about x
	        gl.glRotated(-getGlobalRotation()[1],0,1,0); //rotate about y
	        gl.glRotated(-getGlobalRotation()[2],0,0,1); //rotate about z
	        gl.glScaled(1/getGlobalScale(), 1/getGlobalScale(), 1/getGlobalScale());
	        gl.glTranslated(-getGlobalPosition()[0],-getGlobalPosition()[1], -getGlobalPosition()[2]);
	        
	    }

	    public void reshape(GL2 gl, int x, int y, int width, int height) {
	        
	        // match the projection aspect ratio to the viewport
	        // to avoid stretching
	        gl.glMatrixMode(GL2.GL_PROJECTION);

	        double top, bottom, left, right;
	        
	        if (width > height) {
	            double aspect = (1.0 * width) / height;
	            top = 1.0;
	            bottom = -1.0;
	            left = -aspect;
	            right = aspect;            
	        }
	        else {
	            double aspect = (1.0 * height) / width;
	            top = aspect;
	            bottom = -aspect;
	            left = -1;
	            right = 1;                        
	        }        
	        GLU myGLU = new GLU();
	        // coordinate system (left, right, bottom, top)
	        myGLU.gluOrtho2D(left, right, bottom, top);   
//	        myGLU.gluLookAt(3, 3, 3, 0.01, 0.01, 0.01, 0, 1, 0);
	    }

	    @Override
		public void update(double dt) {
//	    	this.rotate(new double[]{dt*50,dt*50,dt*50});
//	    	this.translate(dt*0.01, dt*0.01, dt*0.01);
	    }
}
