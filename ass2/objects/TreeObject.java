package ass2.objects;
import javax.media.opengl.GL2;

import ass2.objects.GameObject;
import ass2.spec.Texture;

public class TreeObject extends GameObject {
	private SphereObject top;
	private CylinderObject trunk;
	
	public TreeObject(GameObject parent, double trunkRadius, double trunkHeight) {
		super(parent);
		// TODO Auto-generated constructor stub
		top =  new SphereObject(this, trunkRadius*3);
		top.translate(0, trunkHeight, 0);
		trunk = new CylinderObject(this, trunkRadius, trunkHeight);
	}
	
	public void setBushTexture(Texture t) {
		top.setTexture(t);
	}
	public void setTrunkTexture(Texture t) {
		trunk.setTexture(t);
	}
	
}
