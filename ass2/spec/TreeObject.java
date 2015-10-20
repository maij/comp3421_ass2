package ass2.spec;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import ass2.spec.GameObject;

public class TreeObject extends GameObject {
	private Mesh m;
	private double[][] vertices;
	private SphereObject top;
	private CylinderObject trunk;
	
	public TreeObject(GameObject parent) {
		super(parent);
		// TODO Auto-generated constructor stub
		top =  new SphereObject(this);
		trunk = new CylinderObject(this);
	}
	// Does nothing, draw top and sphere
	@Override
	public void drawSelf(GL2 gl) {
//		m.draw(gl);
		top.generateBuffers(gl);
	}
	
	public void setBushTexture(Texture t) {
		top.setTexture(t);
	}
	public void setTrunkTexture(Texture t) {
		trunk.setTexture(t);
	}
	
}
