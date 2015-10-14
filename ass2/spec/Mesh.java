package ass2.spec;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;

public class Mesh {
	private ArrayList<double[]> vertices;
	private ArrayList<double[]> normals;
	private ArrayList<int[]> face_verts;
	private ArrayList<Integer> face_norms;
	
	public Mesh() {
		vertices = new ArrayList<double[]>();
		normals  = new ArrayList<double[]>();
		face_verts = new ArrayList<int[]>();
		face_norms= new ArrayList<Integer>();
	}
	
	public void setVertices(List<double[]> l) {
		vertices = new ArrayList<double[]>(l);
	}
	
	public void setNormals(List<double[]> l) {
		normals = new ArrayList<double[]>(l);
	}
	
	public void setFaceVerts(List<int[]> l) {
		face_verts = new ArrayList<int[]>(l);
	}
	
	public void setFaceNorms(List<Integer> l) {
		face_norms = new ArrayList<Integer>(l);
	}
	
	public void addVertex(double[] v) {
		vertices.add(v);
	}
	
	public void addNormal(double[] n) {
		normals.add(n);
	}
	
	public void addFace(int[] vert_indices, int norm_index) {
		face_verts.add(vert_indices);
		face_norms.add(norm_index);
	}
	
	public void draw (GL2 gl) {
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		if (face_verts.size() != face_norms.size()) {
			System.err.println("Number of faces does not match number of normals");
			return;
		}
		// Draw each face as a separate polygon
		for (int i = 0; i < face_verts.size(); i++) {
			gl.glBegin(GL2.GL_POLYGON);
			{
				double nx, ny, nz;
				nx = normals.get(face_norms.get(i))[0];
				ny = normals.get(face_norms.get(i))[1];
				nz = normals.get(face_norms.get(i))[2];
				gl.glNormal3d(nx, ny, nz);
				for (int v_index : face_verts.get(i)) {
					double x, y, z;
					x = vertices.get(v_index)[0];
					y = vertices.get(v_index)[1];
					z = vertices.get(v_index)[2];
					gl.glVertex3d(x, y, z);
	
//					System.out.printf("%d\n", v_index);
				}
//				System.out.println();
			} 
			gl.glEnd();
		}
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	}
}
