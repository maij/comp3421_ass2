package ass2.objects;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

public class SphereObject extends GameObject {
	private int maxStacks = 20;
    private int maxSlices = 30;
    private int maxVertices = maxStacks*(maxSlices+1)*2;
	double[] vertexArray = new double[maxVertices*3];
	double[] textureMapArray = new double[maxVertices*2];
	double[] normalArray	= new double[maxVertices*3];
	
    private double radius = 1;
    
	public SphereObject(GameObject parent, double radius) {
		super(parent);
		// TODO Auto-generated constructor stub
		this.radius = radius;
		double deltaT;
    	deltaT = 0.5/maxStacks;
    	int ang;  
    	int delang = 360/maxSlices;
    	double x1,x2,z1,z2,y1,y2;
    	int texCount = 0;
    	int arrayCounter = 0;
    	for (int i = 0; i < maxStacks; i++) 
    	{ 
    		double t = -0.25 + i*deltaT;

    		for(int j = 0; j <= maxSlices; j++)  
    		{  
    			ang = j*delang;
    			x1=radius * r(t)*Math.cos((double)ang*2.0*Math.PI/360.0); 
    			x2=radius * r(t+deltaT)*Math.cos((double)ang*2.0*Math.PI/360.0); 
    			y1 = radius * getY(t);
    			
    			z1=radius * r(t)*Math.sin((double)ang*2.0*Math.PI/360.0);  
    			z2= radius * r(t+deltaT)*Math.sin((double)ang*2.0*Math.PI/360.0);  
    			y2 = radius * getY(t+deltaT);

    			double normal[] = {x1,y1,z1};
    			normalize(normal);
    			
//        		System.out.printf("%f %f\n", u, v);
//    			gl.glNormal3d(x1, y1, z1);
    			normalArray[arrayCounter] = x1;
    			normalArray[arrayCounter+1] = y1;
    			normalArray[arrayCounter+2] = z1;
    			
    			vertexArray[arrayCounter++] = x1;
    			vertexArray[arrayCounter++] = y1;
    			vertexArray[arrayCounter++] = z1;
    			
    			vertexArray[arrayCounter++] = x2;
    			vertexArray[arrayCounter++] = y2;
    			vertexArray[arrayCounter++] = z2;
    			
    			double u = 0.5 + Math.atan2(z1, x1)/2/Math.PI;
        		double v = 0.5 - Math.asin(y1)/Math.PI;
        		textureMapArray[texCount++] = u;
        		textureMapArray[texCount++] = v;
    			u = 0.5 + Math.atan2(z2, x2)/2/Math.PI;
        		v = 0.5 - Math.asin(y2)/Math.PI;
        		textureMapArray[texCount++] = u;
        		textureMapArray[texCount++] = v;
    		}
    	}
	} 
	
	double r(double t){
    	double x  = Math.cos(2 * Math.PI * t);
        return x;
    }
    
    double getY(double t){
    	
    	double y  = Math.sin(2 * Math.PI * t);
        return y;
    }

	
	@Override
	public void drawSelf(GL2 gl) {
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, super.getTextureID());

    	int vertexCount = 0;
    	int texCount = 0;
    	for(int i=0; i < maxStacks; i++ ){
    		gl.glBegin(GL2.GL_TRIANGLE_STRIP);
    		for (int j = 0; j <= maxSlices; j++) {
    	
    			// x1, y1, z1
	    		gl.glNormal3d(normalArray[vertexCount],
	    					  normalArray[vertexCount+1],
	    					  normalArray[vertexCount+2]);
	    		// u1, v1
	    		gl.glTexCoord2d(textureMapArray[texCount++], textureMapArray[texCount++]);
	    		// x1, y1, z1
	    		gl.glVertex3d(vertexArray[(vertexCount++)],
	    					  vertexArray[(vertexCount++)],
	    					  vertexArray[(vertexCount++)]);
	    		// u2, v2
	    		gl.glTexCoord2d(textureMapArray[texCount++], textureMapArray[texCount++]);
	    		// x2, y2, z2
	    		gl.glVertex3d(vertexArray[(vertexCount++)], 
	    					  vertexArray[(vertexCount++)], 
	    					  vertexArray[(vertexCount++)]);
	    		
    		}
        	gl.glEnd();
    	}
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
	}

    private void normalize(double v[])  
    {  
        double d = Math.sqrt(v[0]*v[0]+v[1]*v[1]+v[2]*v[2]);  
        if (d != 0.0) 
        {  
           v[0]/=d; 
           v[1]/=d;  
           v[2]/=d;  
        }  
    } 
}
