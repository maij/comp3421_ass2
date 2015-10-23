package ass2.objects;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class PlayerObject extends GameObject {

	public PlayerObject(ass2.objects.GameObject parent) {
		super(parent);
	}
	
	@Override
	public void drawSelf(GL2 gl) {
//		if (isShowing()) {
			GLUT glut = new GLUT();
			gl.glBindTexture(GL2.GL_TEXTURE_2D, this.getTextureID());
			glut.glutSolidTeapot(0.1);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
//		}
	}

}
