package ass2.objects;

import java.util.function.Function;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class PlayerObject extends GameObject {
	Function<double[], Double> altFun;
	public PlayerObject(ass2.objects.GameObject parent, Function<double[], Double> f) {
		super(parent);
		altFun = f;
		
	}
	
	@Override
	public void drawSelf(GL2 gl) {
		// Default Material property vectors.
    	float matAmbAndDif1[] = {1.0f, 1.0f, 1.0f, 0.0f};
    	float matAmbAndDif2[] = {0f, 0f, 0f, 0f};
    	float matSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    	float matShine[] = { 100.0f };
    	// Material properties.
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif1,0);
    	gl.glMaterialfv(GL2.GL_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif2,0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShine, 0);
		if (isShowing()) {
			GLUT glut = new GLUT();
			gl.glBindTexture(GL2.GL_TEXTURE_2D, this.getTextureID());
			glut.glutSolidTeapot(0.1);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		}
	}

	@Override
	public void update(double dt) {
		this.translate(0, altFun.apply(new double[]{getGlobalPosition()[0], getGlobalPosition()[2]}) +1 - getGlobalPosition()[1], 0);
		this.rotate(new double[]{0,dt*360/6,0});
	}

}
