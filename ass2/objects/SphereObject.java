package ass2.objects;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

public class SphereObject extends GameObject {
	private int maxStacks = 20;
    private int maxSlices = 30;
    private int maxVertices = maxStacks*(maxSlices+1)*2;
	FloatBuffer verticesBuffer = FloatBuffer.allocate(maxVertices*3);
	FloatBuffer normalsBuffer = FloatBuffer.allocate(maxVertices*3);
	DoubleBuffer texBuffer = DoubleBuffer.allocate(maxVertices*4*2);
      
    private int numStacks = maxStacks;
    private int numSlices = maxSlices;
    private int bufferIds[] = new int[1];
    private double radius = 1;
    
	public SphereObject(GameObject parent, double radius) {
		super(parent);
		// TODO Auto-generated constructor stub
		this.radius = radius;
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
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);

    	// Enable two vertex arrays: co-ordinates and color.
    	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);

    	// Specify locations for the co-ordinates and color arrays.
    	gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0); //last num is the offset
    	gl.glNormalPointer(GL.GL_FLOAT, 0, maxVertices*3*Float.BYTES);
    	
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, super.getTextureID());
//    	gl.glTexCoordPointer(4, GL2.GL_FLOAT, 0, texBuffer);
		for(int i=0; i < numStacks; i++ ){
//    		u = 0.5 + Math.atan2(2, 1)/2/Math.PI;
//    		v = 0.5 - Math.asin(1)/Math.PI;
//    		gl.glTexCoord2d(u, v);
    		gl.glDrawArrays(GL2.GL_TRIANGLE_STRIP,i*(maxSlices+1)*2,(numSlices+1)*2);        
    	}
	}

	public void generateData(){
	    
    	double deltaT;
    	deltaT = 0.5/maxStacks;
    	int ang;  
    	int delang = 360/maxSlices;
    	double x1,x2,z1,z2,y1,y2;
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

    			verticesBuffer.put((float)x1);
    			verticesBuffer.put((float)y1);
    			verticesBuffer.put((float)z1);
    			normalsBuffer.put((float)normal[0]);
    			normalsBuffer.put((float)normal[1]);
    			normalsBuffer.put((float)normal[2]);


    			//gl.glNormal3dv(normal,0);         
    			//gl.glVertex3d(x1,y1,z1);
    			normal[0] = x2;
    			normal[1] = y2;
    			normal[2] = z2;

    			normalize(normal);    
    			//gl.glNormal3dv(normal,0); 
    			//gl.glVertex3d(x2,y2,z2); 

    			verticesBuffer.put((float)x2);
    			verticesBuffer.put((float)y2);
    			verticesBuffer.put((float)z2);
    			normalsBuffer.put((float)normal[0]);
    			normalsBuffer.put((float)normal[1]);
    			normalsBuffer.put((float)normal[2]);
//    			System.out.println(i*maxSlices + j);
//    			texBuffer.put(0);
//    			texBuffer.put(0);
//    			texBuffer.put(0);
//    			texBuffer.put(1);

    		}; 
    	}
    	verticesBuffer.rewind();
    	normalsBuffer.rewind();
    	texBuffer.rewind();
}
    
    public void generateBuffers(GL2 gl){
    	
    	 generateData();
    	 gl.glGenBuffers(1,bufferIds,0);
         gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);
         
         gl.glBufferData(GL2.GL_ARRAY_BUFFER,      
        	        maxVertices *3 * Float.BYTES +  
        	        maxVertices *3 * Float.BYTES,
        	        null, GL2.GL_STATIC_DRAW);


         gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0,
        		 maxVertices*3 *Float.BYTES,verticesBuffer);

         gl.glBufferSubData(GL2.GL_ARRAY_BUFFER,
        		 maxVertices*3*Float.BYTES, 
        		 maxVertices*3*Float.BYTES,normalsBuffer);

         gl.glBufferSubData(GL2.GL_ARRAY_BUFFER,
        		 maxVertices*4*Float.BYTES, 
        		 maxVertices*4*Float.BYTES,texBuffer);
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
