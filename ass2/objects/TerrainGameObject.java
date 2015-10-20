package ass2.objects;

import javax.media.opengl.GL2;

import ass2.spec.Mesh;
import ass2.spec.Terrain;

//import com.jogamp.opengl.util.texture.Texture;

public class TerrainGameObject extends GameObject {
	private Terrain myTerrain = null;
	private Mesh myMesh = null;
	
	public TerrainGameObject(GameObject parent) {
		super(parent);
	}
	
	public void setTerrain(Terrain t) {
		myTerrain = t;
	}
	
	public void generateMesh(Terrain t) {
		Mesh m = new Mesh();
		int width = (int)t.size().getWidth();
		int height = (int)t.size().getHeight();
		
		// Add all the vertices and normals
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				m.addVertex(new double[]{i, t.getGridAltitude(i, j), j});
			}
		}
		
		// TODO: Add all the normals
		m.addNormal(new double[]{0, 0, 0});
		
		// Add all the faces
		for (int i = 0; i < t.size().getWidth(); i++) {
			for (int j = 0; j < t.size().getHeight(); j++) {
				m.addFace(new int[]{(int) (Integer.min(i+1, width-1)*width+j), 
									(int) (i*width+j), 
									(int) (Integer.min(i+1, width-1)*width+Integer.min(j+1, height-1))}, 
									// Face normal
									0);
				m.addFace(new int[]{(int) (i*width+j), 
									(int) (i*width+Integer.min(j+1, height-1)), 
									(int) (Integer.min(i+1, width-1)*width+Integer.min(j+1, height-1))}, 
									// Face normal
									0);
			}
		}
		myMesh = m;
	}
	
	@Override 
	public void drawSelf(GL2 gl){
		if (myTerrain == null) {
			System.err.println("Terrain not set");
			return;
		}

        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_POINTS);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, super.getTextureID());
//		gl.glBegin(GL2.GL_POINTS);
//		for (int i = 0; i < this.myTerrain.size().getWidth(); i++) {
//			for (int j = 0; j < myTerrain.size().getWidth(); j++) {
//				gl.glVertex3d(i, myTerrain.getGridAltitude(i, j), j);
//			}
//		}
//		gl.glEnd();
		myMesh.draw(gl);
		
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	}

}
