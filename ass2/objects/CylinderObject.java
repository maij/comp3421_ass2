package ass2.objects;

import javax.media.opengl.GL2;

import ass2.spec.Mesh;

public class CylinderObject extends GameObject {
	private Mesh shaft_mesh;
	private final static int numEdges = 32;
	private final static int numVertices = numEdges*2;
	public CylinderObject(GameObject parent, double radius, double height) {
		super(parent);
		// TODO Auto-generated constructor stub'
		shaft_mesh = new Mesh();
		for (int i = 0; i < numEdges; i++) {
			double x = radius*Math.cos(2*Math.PI*i/numEdges);
			double z = radius*Math.sin(2*Math.PI*i/numEdges);
			double nx = Math.cos(2*Math.PI/numEdges*(i+0.5));
			double nz = Math.sin(2*Math.PI/numEdges*(i+0.5));
			
			shaft_mesh.addVertex(new double[]{x,0,z});
			shaft_mesh.addVertex(new double[]{x,height,z});
			
			shaft_mesh.addNormal(new double[]{nx,0,nz});
		}
		
		for (int i = 0; i < numVertices; i += 2) {
			shaft_mesh.addFace(new int[]{i, (i+2)%numVertices, (i+3)%numVertices, (i+1)%numVertices}, i/2);
		}
	}
	
	
	@Override
	public void drawSelf(GL2 gl) {
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, super.getTextureID());
		shaft_mesh.draw(gl);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		
	}
}
