package ass2.objects;
import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

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
	
	@Override
	public void drawSelf(GL2 gl) {
		// Tree Materials (applies to both bush and trunk)
    	float matAmbAndDif1[] = {0.8f, 1.0f, 0.8f, 0.0f};
    	float matAmbAndDif2[] = {0f, 0f, 0f, 0f};
    	float matSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    	float matShine[] = { 0.0f };
    	// Material properties.
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif1,0);
    	gl.glMaterialfv(GL2.GL_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif2,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);
    	
    	GLUT glut =  new GLUT();
//    	gl.glTranslated(5, -2, 5);
//    	glut.glutSolidSphere(1, 100, 100);
	}
	
	public void setBushTexture(Texture t) {
		top.setTexture(t);
	}
	public void setTrunkTexture(Texture t) {
		trunk.setTexture(t);
	}
	
}
