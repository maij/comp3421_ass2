package ass2.objects;

import java.util.concurrent.Callable;

import javax.media.opengl.GL2;

import ass2.spec.Mesh;
import ass2.spec.Road;
import ass2.spec.Terrain;

public class RoadObject extends GameObject {
	private Road myRoad;
	private static final double delta_t = 0.01;
	private static final double y_offset = 0.01;
	private Mesh m;
	
	public RoadObject(GameObject parent, Road r, Terrain t) {
		super(parent);
		m = new Mesh();
		myRoad = r;
		
		double width = myRoad.width();
		
		// Add vertices
		for (double i = 0; i < r.size(); i += delta_t*r.size()) {
			double[] p = myRoad.point(i);
			System.out.printf("%f %f\n", p[0], p[1]);
//			System.out.printf("L: %f %f %f\n",p[0], t.altitude(p[0], p[1]-width/2) ,p[1]-width/2);
//			System.out.printf("C: %f %f %f\n",p[0], t.altitude(p[0], p[1]) ,p[1]);
//			System.out.printf("R: %f %f %f\n",p[0], t.altitude(p[0], p[1]+width/2) ,p[1]+width/2);
//			System.out.println();
			m.addUVCoord(new double[]{0, (r.size()-i)/r.size()});
			m.addVertex(new double[]{p[0]-width/2,
									 t.altitude(p[0], p[1]) + y_offset,
									 p[1]-width/2});
			
			m.addUVCoord(new double[]{0.5, (r.size()-i)/r.size()});
			m.addVertex(new double[]{p[0],
				 					 t.altitude(p[0], p[1]) + y_offset,
								 	 p[1]});

			m.addUVCoord(new double[]{1, (r.size()-i)/r.size()});
			m.addVertex(new double[]{p[0]+width/2,
					 				 t.altitude(p[0], p[1]) + y_offset,
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
