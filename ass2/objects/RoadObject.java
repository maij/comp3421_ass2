package ass2.objects;

import java.util.concurrent.Callable;

import javax.media.opengl.GL2;

import ass2.spec.Mesh;
import ass2.spec.Road;
import ass2.spec.Terrain;

public class RoadObject extends GameObject {
	private Road myRoad;
	private static final double delta_t = 0.001;
	private Mesh m;
	
	public RoadObject(GameObject parent, Road r, Terrain t) {
		super(parent);
		m = new Mesh();
		myRoad = r;
		
		double width = myRoad.width();
		
		// Add vertices
		for (double i = 0; i < r.size(); i += delta_t*r.size()) {
			double[] p = myRoad.point(i);
			System.out.printf("%f %f\n",p[0], p[1]);
			m.addUVCoord(new double[]{i/r.size(), 0});
			m.addVertex(new double[]{p[0],
									 t.altitude(p[0], p[1]-width/2),
									 p[1]-width/2});
			
			m.addUVCoord(new double[]{i/r.size(), 0.5});
			m.addVertex(new double[]{p[0],
				 					 t.altitude(p[0], p[1]),
								 	 p[1]});

			m.addUVCoord(new double[]{i/r.size(), 1});
			m.addVertex(new double[]{p[0],
					 				 t.altitude(p[0], p[1]+width/2),
					 				 p[1]+width/2});
		}
		
		m.addNormal(new double[]{0,0,0});
		
		// Add faces
		for (int i = 0; i < (r.size()/delta_t)-1; i++) {
			m.addFace(new int[]{3*i+1,3*i+3,3*i  }, 0);
			m.addFace(new int[]{3*i+4,3*i+3,3*i+1}, 0);
			m.addFace(new int[]{3*i+5,3*i+4,3*i+1}, 0);
			m.addFace(new int[]{3*i+2,3*i+5,3*i+1}, 0);
		}
	}
	
	
	
	@Override
	public void drawSelf(GL2 gl) {
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, super.getTextureID());
		m.draw(gl);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
	}

}
