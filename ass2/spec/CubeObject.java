package ass2.spec;

import javax.media.opengl.GL2;

public class CubeObject extends GameObject {
	Mesh m;
	public CubeObject(GameObject parent) {
		super(parent);
		// TODO Auto-generated constructor stub
		m = new Mesh();
		m.addVertex(new double[]{-0.5, 0, -0.5}); // 0,0,0
		m.addVertex(new double[]{-0.5, 0, 0.5});  // 0,0,1
		m.addVertex(new double[]{0.5 , 0, 0.5});  // 1,0,1
		m.addVertex(new double[]{0.5 , 0, -0.5}); // 1,0,0
		m.addVertex(new double[]{-0.5, 1, -0.5});
		m.addVertex(new double[]{-0.5, 1, 0.5});
		m.addVertex(new double[]{0.5 , 1, 0.5});
		m.addVertex(new double[]{0.5 , 1, -0.5});
		
		m.addNormal(new double[]{ 1,  0,  0});
		m.addNormal(new double[]{ 0,  1,  0});
		m.addNormal(new double[]{ 0,  0,  1});
		m.addNormal(new double[]{-1,  0,  0});
		m.addNormal(new double[]{ 0, -1,  0});
		m.addNormal(new double[]{ 0,  0, -1});
		
		m.addFace(new int[]{0,3,2,1}, 4); // +y
		m.addFace(new int[]{4,7,6,5}, 1); // -y
		m.addFace(new int[]{3,7,6,2}, 0); // +x
		m.addFace(new int[]{0,4,5,1}, 3); // -x
		m.addFace(new int[]{1,5,6,2}, 2); // +z
		m.addFace(new int[]{0,4,7,3}, 5); // -z
	}
	
	@Override
	public void drawSelf(GL2 gl) {
//		m.draw(gl);
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
