package ass2.spec;

import javax.media.opengl.GL2;

public class Axes extends GameObject {

	public Axes(GameObject parent) {
		super(parent);
	}
	
	@Override
	public void drawSelf (GL2 gl) {
		gl.glBegin(GL2.GL_LINES);
		{
			// x
			gl.glColor4d(0.2, 1, 0, 1);
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(1, 0, 0);

			// y
			gl.glColor4d(1, 0, 0.2, 1);
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(0, 1, 0);

			// z
			gl.glColor4d(0.2, 0, 1, 1);
			gl.glVertex3d(0, 0, 0);
			gl.glVertex3d(0, 0, 1);
		}
		gl.glEnd();
	}
}
