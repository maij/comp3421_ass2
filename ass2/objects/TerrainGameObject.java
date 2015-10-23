package ass2.objects;

import java.util.List;
import java.util.function.Function;

import javax.media.opengl.GL2;

import ass2.spec.MathUtil;
import ass2.spec.Mesh;
import ass2.spec.Road;
import ass2.spec.Terrain;
import ass2.spec.Texture;
import ass2.spec.Tree;

//import com.jogamp.opengl.util.texture.Texture;

public class TerrainGameObject extends GameObject {
	private Terrain myTerrain = null;
	private Mesh myMesh = new Mesh();
	TreeObject[] trees;
	RoadObject[] roads;
	
	private static final double trunkRadius = 0.3;
	private static final double trunkHeight = 3;
	
	public TerrainGameObject(GameObject parent, Terrain t) {
		super(parent);
		myTerrain = t;
		generateMesh();
		drawTrees(myTerrain.trees());
		drawRoads(myTerrain.roads());
		
	}
	
	public void setRoadTexture(Texture t) {
		for (RoadObject r: roads) {
			r.setTexture(t);
		}
	}
	
	public void setTreeTextures(Texture bush, Texture trunk) {
		for (TreeObject t: trees) {
			t.setBushTexture(bush);
			t.setTrunkTexture(trunk);
		}
	}
	
	private void drawRoads(List<Road> r) {
		// Give 
		roads = new RoadObject[r.size()];
		for (int i = 0; i < r.size(); i++) {
			roads[i] = new RoadObject(GameObject.ROOT, r.get(i), new Function<double[], Double>() {

				@Override
				public Double apply(double[] t) {
					return myTerrain.altitude(t[0],t[1]);
				}
				
			});
		}
	}
	
	private void drawTrees(List<Tree> t) {
    	trees = new TreeObject[t.size()];
    	for (int i = 0; i < t.size(); i++) {
    		trees[i] = new TreeObject(GameObject.ROOT, trunkRadius, trunkHeight);
    		trees[i].translate(t.get(i).getPosition()[0],
			    				 t.get(i).getPosition()[1],
			    				 t.get(i).getPosition()[2]);
    		
    	}
    }
	
	private void generateMesh() {
		Mesh m = myMesh;
		Terrain t = myTerrain;
		int width = (int)t.size().getWidth();
		int height = (int)t.size().getHeight();
		
		// Add all the vertices and normals
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
//				System.out.printf("T: %d %f %d\n", i, t.getGridAltitude(i, j), j);
				m.addVertex(new double[]{i, t.getGridAltitude(i, j), j});
				m.addUVCoord(new double[]{i,j});
			}
		}
		
		// Add all the faces
		for (int i = 0; i < width-1; i++) {
			for (int j = 0; j < height-1; j++) {
				double x1, y1, z1, x2, y2, z2, x3, y3, z3;
				//P1
				x1 = i;
				y1 = t.getGridAltitude(i, j);
				z1 = j;
				// P2
				x2 = i+1;
				y2 = t.getGridAltitude(i+1, j);
				z2 = j;
				// P3
				x3 = i+1;
				y3 = t.getGridAltitude(i+1, j+1);
				z3 = j+1;

				
				m.addNormal(MathUtil.surfaceNormal(new double[]{x1,y1,z1}, 
												   new double[]{x2,y2,z2}, 
												   new double[]{x3,y3,z3}));
				
				m.addFace(new int[]{(int) ((i+1)*width+j), 
									(int) (i*width+j), 
									(int) (i*width+j+1), 
									(int) ((i+1)*width+j+1)}, 
									// Face normal
									(i*(width-1)+j));
			}
		}
		myMesh = m;
	}
	
	@Override 
	public void drawSelf(GL2 gl){
		if (myTerrain == null) {
			System.err.println("Terrain not set");
			return;
		}
		
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, super.getTextureID());
		myMesh.draw(gl);
		
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	}

}
