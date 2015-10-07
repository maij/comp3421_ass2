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
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        // Transform the object before drawing it
        gl.glTranslated(getPosition()[0], getPosition()[1], getPosition()[2]);
		gl.glRotated(getRotation()[0], 1, 0, 0);
		gl.glRotated(getRotation()[1], 0, 1, 0);
		gl.glRotated(getRotation()[2], 0, 0, 1);
		gl.glScaled(getScale(), getScale(), getScale());
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
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	}

}
