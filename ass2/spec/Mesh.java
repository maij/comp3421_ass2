package ass2.spec;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;

public class Mesh {
	private ArrayList<double[]> vertices;
	private ArrayList<double[]> normals;
	private ArrayList<int[]> face_verts;
	private ArrayList<Integer> face_norms;
	
	private int[] textureID = new int[1];
	
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
	
	public void draw (GL2 gl, TextureData data) {
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		if (face_verts.size() != face_norms.size()) {
			System.err.println("Number of faces does not match number of normals");
			return;
		}
		if (data != null) {
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

			gl.glGenTextures(1, textureID, 0);
			//The first time bind is called with the given id,
			//an openGL texture object is created and bound to the id
			//It also makes it the current texture.
			gl.glBindTexture(GL.GL_TEXTURE_2D, textureID[0]);

			 // Build texture initialised with image data.
	        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0,
	        				data.getInternalFormat(),
	        				data.getWidth(),
	        				data.getHeight(),
	        				0,
	        				data.getPixelFormat(),
	        				data.getPixelType(),
	        				data.getBuffer());
			// Mipmap stuff
	        // Set texture parameters to enable automatic mipmap generation and bilinear/trilinear filtering
//    		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
//    		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
    		//float fLargest[] = new float[1];

    		//gl.glGetFloatv(GL.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, fLargest,0);
    		//System.out.println(fLargest[0]);
    		//gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAX_ANISOTROPY_EXT, fLargest[0]);
//    		gl.glGenerateMipmap(GL2.GL_TEXTURE_2D); 		    

//			gl.glTexImage2D(
//					GL2.GL_TEXTURE_2D,
//					0,// level of detail: 0 = base
//					data.getInternalFormat(),
//					data.getWidth(),
//					data.getHeight(),
//					0, // border (must be 0)
//					data.getPixelFormat(),
//					data.getPixelType(),
//					data.getBuffer());
//			texture.enable(gl);
//			texture.bind(gl);
//			texture.enable(arg0);
//			gl.glGenerateMipmap(GL2.GL_TEXTURE_2D);
//			gl.glTexParameteri(
//					GL2.GL_TEXTURE_2D,
//					GL2.GL_TEXTURE_MIN_FILTER,
//					GL2.GL_NEAREST_MIPMAP_NEAREST);
//			gl.glBindTexture(GL2.GL_TEXTURE_2D,0);
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
				}
			} 
			gl.glEnd();
		}
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	}
}
