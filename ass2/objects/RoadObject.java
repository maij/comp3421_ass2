package ass2.objects;

import java.util.concurrent.Callable;
import java.util.function.Function;

import javax.media.opengl.GL2;

import ass2.spec.Mesh;
import ass2.spec.Road;

public class RoadObject extends GameObject {
	private Road myRoad;
	private static final double delta_t = 0.01;
	private static final double y_offset = 0.05;
	private static final int numSplines = 100;
	private Mesh m;
	// f is a function to return the altitude of the road
	public RoadObject(GameObject parent, Road r, Function<double[], Double> f) {
		super(parent);
		m = new Mesh();
		myRoad = r;
		
		double width = myRoad.width();
		// Add vertices
		for (double i = 0; i < 1; i += delta_t) {
			double root8 = 2*Math.sqrt(2);
			
			for (int j = 0; j < numSplines; j++) {
				double[] p = r.point(i*r.size());
				p[0] += width/root8*(2*(double)j/((double)numSplines - 1) - 1);
				p[1] += width/root8*(2*(double)j/((double)numSplines - 1) - 1);
//				System.out.printf("%d: %f\n",j, width/root2*(2*(double)j/numSplines - 1));
//				System.out.printf("p%d : %f %f\n",j, p[0], p[1]);

//				System.out.printf("%f\n", (double)j/((double)(numSplines-1)));
				m.addUVCoord(new double[]{(double)j/((double)(numSplines-1)), (1-i/(1-delta_t)) });
				m.addVertex(new double[]{p[0],
										 f.apply( p  ) + y_offset,
										 p[1]});
			}
//			System.out.printf("%f\n", (1-i*(1/(1-delta_t))));
		}
		
		
		
		m.addNormal(new double[]{0,1,0});

		// Add faces
		for (int i = 0;  i < 1/delta_t - 1; i++) {
			for (int j = 0; j < numSplines-1; j++) {
				m.addFace(new int[]{i*numSplines + j, i*numSplines+j+1,(i+1)*numSplines+j+1, (i+1)*numSplines+j}, 0);
			}
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
