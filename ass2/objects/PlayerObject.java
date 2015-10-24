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
		if (isShowing()) {
			GLUT glut = new GLUT();
			gl.glBindTexture(GL2.GL_TEXTURE_2D, this.getTextureID());
			glut.glutSolidTeapot(0.1);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		}
	}

	@Override
	public void update(double dt) {
		this.translate(0, altFun.apply(new double[]{getPosition()[1], getPosition()[2]}) - getPosition()[1], 0);
	}

}
