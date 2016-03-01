package ass2.objects;

import javax.media.opengl.GL2;

public class RainDroplet extends GameObject {
	
	public RainDroplet(GameObject parent) {
		super(parent);
	}
	
	
	@Override
	public void drawSelf (GL2 gl) {
		
		float matAmbAndDif1[] = {0.1f, 0.2f, 0.8f, 0.3f};
    	float matAmbAndDif2[] = {0.0f, 0.0f, 0.0f, 0.0f};
    	float matSpec[] = { 0.0f, 0.0f, 1.0f, 0.8f };
    	float matShine[] = { 10.0f };
    	
//    	// Material properties.
//    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif1,0);
//    	gl.glMaterialfv(GL2.GL_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif2,0);
//    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
//    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);
//    	
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
//    	gl.glDisable(GL2.GL_CULL_FACE);
    	gl.glEnable(GL2.GL_CULL_FACE);
    	gl.glCullFace(GL2.GL_BACK);
		
    	gl.glBegin(GL2.GL_POLYGON);
    	{
			gl.glNormal3d(0, 0, 1);
			gl.glVertex3d(1, 0, 0);
			gl.glVertex3d(0.5, 0.5, 0);
			gl.glVertex3d(0, 2, 0);
			gl.glVertex3d(-0.5, 0.5, 0);
			gl.glVertex3d(-1, 0, 0);
			gl.glVertex3d(-0.5, -0.5, 0);
			gl.glVertex3d(0.5, -0.5, 0);
    	}
		gl.glEnd();
		
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		
	}

}
