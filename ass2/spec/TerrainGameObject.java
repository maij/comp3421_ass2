package ass2.spec;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

public class TerrainGameObject extends GameObject {
	private Terrain myTerrain;
	public TerrainGameObject(GameObject parent) {
		super(parent);
	}
	
	public void setTerrain(Terrain t) {
		myTerrain = t;
	}
	
	@Override 
	public void draw(GL2 gl) {
		if (myTerrain == null) {
			System.out.println("ERR: Terrain not set");
			return;
		}
		gl.glBegin(GL.GL_TRIANGLES);
		{
			gl.glColor4d(0.3, 0.2, 0.3, 1);
			// Use the 3 points around to draw triangles
			for (int i = 0; i < myTerrain.size().getWidth(); i++) {
				for (int j = 0; j  < myTerrain.size().getHeight(); j++) {
					gl.glVertex3d(i, myTerrain.getGridAltitude(i, j), j);
					
					if (i - 1 >= 0)
						gl.glVertex3d(i-1, myTerrain.getGridAltitude(i-1, j), j);
					else 
						gl.glVertex3d(i+1, myTerrain.getGridAltitude(i+1, j), j);

					if (j - 1 >= 0)
						gl.glVertex3d(i, myTerrain.getGridAltitude(i, j-1), j-1);
					else 
						gl.glVertex3d(i, myTerrain.getGridAltitude(i, j+1), j+1);
				}
			}
			
		}
		gl.glEnd();
	}

}
