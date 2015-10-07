package ass2.spec;

import javax.media.opengl.GL2;

public class CubeObject extends GameObject {

	public CubeObject(GameObject parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void drawSelf(GL2 gl) {
		gl.glBegin(GL2.GL_QUADS);
		{	
			gl.glColor4f(0, 1, 0, 1);
			// Top (offset xz face)
			gl.glVertex3d(-0.5, 1, -0.5);
			gl.glVertex3d( 0.5, 1, -0.5);
			gl.glVertex3d( 0.5, 1,  0.5);
			gl.glVertex3d(-0.5, 1,  0.5);

			gl.glColor4f(0, 1, 1, 1);
			// Bottom (xz face)
			gl.glVertex3d(-0.5, 0, -0.5);
			gl.glVertex3d(0.5, 0, -0.5);
			gl.glVertex3d(0.5, 0, 0.5);
			gl.glVertex3d(-0.5, 0, 0.5);

			gl.glColor4f(1, 1, 0, 1);
			// Left (offset yz face)
			gl.glVertex3d(0.5, 0, -0.5);
			gl.glVertex3d(0.5, 0, 0.5);
			gl.glVertex3d(0.5, 1, 0.5);
			gl.glVertex3d(0.5, 1, -0.5);

			gl.glColor4f(1, 0, 1, 1);
			// Right (yz face)
			gl.glVertex3d(-0.5, 0, -0.5);
			gl.glVertex3d(-0.5, 0, 0.5);
			gl.glVertex3d(-0.5, 1, 0.5);
			gl.glVertex3d(-0.5, 1, -0.5);

			gl.glColor4f(0, 0, 1, 1);
			// Front (offset xy face)
			gl.glVertex3d(-0.5, 0, 0.5);
			gl.glVertex3d(-0.5, 1, 0.5);
			gl.glVertex3d(0.5, 1, 0.5);
			gl.glVertex3d(0.5, 0, 0.5);

			gl.glColor4f(1, 0, 0, 1);
			// Back (xy face)
			gl.glVertex3d(-0.5, 0, -0.5);
			gl.glVertex3d(-0.5, 1, -0.5);
			gl.glVertex3d(0.5, 1, -0.5);
			gl.glVertex3d(0.5, 0, -0.5);
			
		}
		gl.glEnd();
	}

		@Override
		public void update(double dt) {
//			this.rotate(new double[]{dt*10,dt*10,dt*10});
		}
}
