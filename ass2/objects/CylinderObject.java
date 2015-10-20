package ass2.objects;

import javax.media.opengl.GL2;

public class CylinderObject extends GameObject {
	private final static int numEdges = 32;
	private final static int numVertices = numEdges*2;
	
	private double[][] vertices = new double[numVertices][3];
	private double[][] normals = new double[numVertices/2][3];
	public CylinderObject(GameObject parent, double radius, double height) {
		super(parent);
		// TODO Auto-generated constructor stub'
		for (int i = 0; i < numEdges; i++) {
			double x = radius*Math.cos(2*Math.PI*i/numEdges);
			double z = radius*Math.sin(2*Math.PI*i/numEdges);
			double nx = Math.cos(2*Math.PI/numEdges*(i+0.5));
			double nz = Math.sin(2*Math.PI/numEdges*(i+0.5));
			
			normals[i][0] = nx;
			normals[i][1] = 0;
			normals[i][2] = nz;
			vertices[i*2][0] = x;
			vertices[i*2][1] = 0;
			vertices[i*2][2] = z;
			vertices[i*2+1][0] = x;
			vertices[i*2+1][1] = height;
			vertices[i*2+1][2] = z;
		}
		
	}
	
	
	@Override
	public void drawSelf(GL2 gl) {
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, super.getTextureID());
		gl.glBegin(GL2.GL_QUADS);
		{
			for (int i = 0; i < numVertices; i += 2) {
				gl.glNormal3d(normals[i/2][0], normals[i/2][1], normals[i/2][2]);
//				System.out.printf("i = %d; u = %f\n", i, (double)i/2/(numEdges+1));
				gl.glTexCoord2d((double)i/2/(numEdges+1), 0);
				gl.glVertex3d(vertices[i][0],
						      vertices[i][1],
						      vertices[i][2]);
				
				gl.glTexCoord2d((double)i/2/(numEdges+1), 1);
				gl.glVertex3d(vertices[(i+1)%numVertices][0], 
							  vertices[(i+1)%numVertices][1],
							  vertices[(i+1)%numVertices][2]);
				
				gl.glTexCoord2d((double)(i+2)/2/(numEdges+1), 1);
				gl.glVertex3d(vertices[(i+3)%numVertices][0], 
						  	  vertices[(i+3)%numVertices][1],
					  	  	  vertices[(i+3)%numVertices][2]);
				
				gl.glTexCoord2d((double)(i+2)/2/(numEdges+1), 0);
				gl.glVertex3d(vertices[(i+2)%numVertices][0], 
							  vertices[(i+2)%numVertices][1],
							  vertices[(i+2)%numVertices][2]);
			}
		}
		gl.glEnd();
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		
	}
}
