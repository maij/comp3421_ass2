package ass2.objects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Random;
import java.util.function.Function;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;

import ass2.spec.Shader;

// Must use VBOs
public class BeastObject extends GameObject {
	private static final String VERTEX_SHADER = "ass2/shaders/PhongVertexTex.glsl";
    private static final String FRAGMENT_SHADER = "ass2/shaders/PhongFragmentTex.glsl";
    private static final int NUM_BODY_SEGMENTS = 6;
    private static final float LEG_INSET = 0.15f;
    private static final float LEG_WIDTH = 0.3f;
    private static final float LEG_HEIGHT = 0.5f;
    static double updateTimeout = 0;
    private static final float BODY_WIDTH = 1.0f;
    private static final float BODY_LENGTH = 2.0f;
    private static final float BODY_HEIGHT = 1.0f;
    
    private static final float FACE_WIDTH = 0.8f;
    private static final float FACE_LENGTH = 1.0f;
    private static final float FACE_HEIGHT = 0.8f;
    private static final float FACE_INSET = 0.2f;
    
    private Function<double[], Double> altFun;
    private int shaderprogram;
    private static final float[] vertices = {
    		// Main Body
    		// Bottom
    		0		  ,LEG_HEIGHT ,0,
    		0		  ,LEG_HEIGHT		  ,BODY_LENGTH,
    		BODY_WIDTH,LEG_HEIGHT		  ,BODY_LENGTH,
    		BODY_WIDTH,LEG_HEIGHT		  ,0,
    		// Top
    		0		  ,BODY_HEIGHT + LEG_HEIGHT,0,
    		0		  ,BODY_HEIGHT + LEG_HEIGHT,BODY_LENGTH,
    		BODY_WIDTH,BODY_HEIGHT + LEG_HEIGHT,BODY_LENGTH,
    		BODY_WIDTH,BODY_HEIGHT + LEG_HEIGHT,0,
    		
    		// Legs
    		
    		// First leg
    		LEG_INSET,			0,LEG_INSET, 		   // 0,0,0
    		LEG_INSET,			0,LEG_INSET+LEG_WIDTH, // 0,0,1
    		LEG_INSET+LEG_WIDTH,0,LEG_INSET+LEG_WIDTH, // 1,0,1
    		LEG_INSET+LEG_WIDTH,0,LEG_INSET, 		   // 1,0,0
    		LEG_INSET,			LEG_HEIGHT,LEG_INSET, 		   // 0,0,0
    		LEG_INSET,			LEG_HEIGHT,LEG_INSET+LEG_WIDTH, // 0,0,1
    		LEG_INSET+LEG_WIDTH,LEG_HEIGHT,LEG_INSET+LEG_WIDTH, // 1,0,1
    		LEG_INSET+LEG_WIDTH,LEG_HEIGHT,LEG_INSET, 		   // 1,0,0
    		// Second leg
    		BODY_WIDTH - LEG_INSET-LEG_WIDTH,		  0,LEG_INSET, 		   	// 0,0,0
    		BODY_WIDTH - LEG_INSET-LEG_WIDTH,		  0,LEG_INSET+LEG_WIDTH, // 0,0,1
    		BODY_WIDTH - LEG_INSET,			 		  0,LEG_INSET+LEG_WIDTH, // 1,0,1
    		BODY_WIDTH - LEG_INSET,			 		  0,LEG_INSET,			// 1,0,0
    		BODY_WIDTH - LEG_INSET-LEG_WIDTH,LEG_HEIGHT,LEG_INSET, 		   	// 0,0,0
    		BODY_WIDTH - LEG_INSET-LEG_WIDTH,LEG_HEIGHT,LEG_INSET+LEG_WIDTH, // 0,0,1
    		BODY_WIDTH - LEG_INSET,			 LEG_HEIGHT,LEG_INSET+LEG_WIDTH, // 1,0,1
    		BODY_WIDTH - LEG_INSET,			 LEG_HEIGHT,LEG_INSET,			// 1,0,0
    		// Third leg
    		LEG_INSET		   ,		  0, BODY_LENGTH - LEG_INSET-LEG_WIDTH, // 0,0,0
    		LEG_INSET		   ,		  0, BODY_LENGTH - LEG_INSET, 		   // 0,0,1
    		LEG_INSET+LEG_WIDTH,		  0, BODY_LENGTH - LEG_INSET, 		   // 1,0,1
    		LEG_INSET+LEG_WIDTH,		  0, BODY_LENGTH - LEG_INSET-LEG_WIDTH, // 1,0,0
    		LEG_INSET		   , LEG_HEIGHT, BODY_LENGTH - LEG_INSET-LEG_WIDTH, // 0,0,0
    		LEG_INSET		   , LEG_HEIGHT, BODY_LENGTH - LEG_INSET, 		   // 0,0,1
    		LEG_INSET+LEG_WIDTH, LEG_HEIGHT, BODY_LENGTH - LEG_INSET, 		   // 1,0,1
    		LEG_INSET+LEG_WIDTH, LEG_HEIGHT, BODY_LENGTH - LEG_INSET-LEG_WIDTH, // 1,0,0
			// Fourth Leg
    		BODY_WIDTH - LEG_INSET-LEG_WIDTH,		 0,BODY_LENGTH - LEG_INSET-LEG_WIDTH, // 0,0,0
    		BODY_WIDTH - LEG_INSET-LEG_WIDTH,		 0,BODY_LENGTH - LEG_INSET, 		   // 0,0,1
    		BODY_WIDTH - LEG_INSET,			 		 0,BODY_LENGTH - LEG_INSET, 		   // 1,0,1
    		BODY_WIDTH - LEG_INSET,			 		 0,BODY_LENGTH - LEG_INSET-LEG_WIDTH, // 0,0,1
    		
    		BODY_WIDTH - LEG_INSET-LEG_WIDTH, LEG_HEIGHT,BODY_LENGTH - LEG_INSET-LEG_WIDTH, // 0,0,0
    		BODY_WIDTH - LEG_INSET-LEG_WIDTH, LEG_HEIGHT,BODY_LENGTH - LEG_INSET, 		   // 0,0,1
    		BODY_WIDTH - LEG_INSET, 		  LEG_HEIGHT,BODY_LENGTH - LEG_INSET, 		   // 1,0,1
    		BODY_WIDTH - LEG_INSET,			  LEG_HEIGHT,BODY_LENGTH - LEG_INSET-LEG_WIDTH, // 0,0,1
    		
    		// Cow Face

    		BODY_WIDTH/2-FACE_WIDTH/2, BODY_HEIGHT - FACE_INSET -FACE_HEIGHT	+ LEG_HEIGHT, -FACE_LENGTH,
    		BODY_WIDTH/2-FACE_WIDTH/2, BODY_HEIGHT - FACE_INSET -FACE_HEIGHT	+ LEG_HEIGHT, 0,
    		BODY_WIDTH/2+FACE_WIDTH/2, BODY_HEIGHT - FACE_INSET -FACE_HEIGHT	+ LEG_HEIGHT, 0,
    		BODY_WIDTH/2+FACE_WIDTH/2, BODY_HEIGHT - FACE_INSET -FACE_HEIGHT	+ LEG_HEIGHT, -FACE_LENGTH,
    		BODY_WIDTH/2-FACE_WIDTH/2, BODY_HEIGHT - FACE_INSET	+ LEG_HEIGHT, -FACE_LENGTH,
    		BODY_WIDTH/2-FACE_WIDTH/2, BODY_HEIGHT - FACE_INSET + LEG_HEIGHT, 0,
    		BODY_WIDTH/2+FACE_WIDTH/2, BODY_HEIGHT - FACE_INSET + LEG_HEIGHT, 0,
    		BODY_WIDTH/2+FACE_WIDTH/2, BODY_HEIGHT - FACE_INSET + LEG_HEIGHT, -FACE_LENGTH,
    		
    };
    
    private static final float texCoords[] =  {
    		// 8 Vertices for all rectangular prisms
    		0, 0.5f, 1,0.5f, 1,0.25f, 0,0.35f,
    		0,0.75f, 1,0.75f, 1,1, 0,0,
    		
    		0, 0.5f, 1,0.5f, 1,0.25f, 0,0.35f,
    		0,0.75f, 1,0.75f, 1,1, 0,0,

    		0, 0.5f, 1,0.5f, 1,0.25f, 0,0.35f,
    		0,0.75f, 1,0.75f, 1,1, 0,0,
    		
    		0, 0.5f, 1,0.5f, 1,0.25f, 0,0.35f,
    		0,0.75f, 1,0.75f, 1,1, 0,0,
    		
    		0, 0.5f, 1,0.5f, 1,0.25f, 0,0.35f,
    		0,0.75f, 1,0.75f, 1,1, 0,0,
    		
    		0, 0.5f, 1,0.5f, 1,0.25f, 0,0.35f,
    		0,0.75f, 1,0.75f, 1,1, 0,0,
	};
    
    private short[] vertex_indices;
    
    private FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(vertices);
    private FloatBuffer texBuffer = Buffers.newDirectFloatBuffer(texCoords);
    private ShortBuffer vertIndicesBuffer;
	
    int bufferIds[];
    int texUnitLoc;
    int texUnitLoc2;
    
    public BeastObject(GameObject parent, Function<double[], Double> f) {
		super(parent);
		altFun = f;
		vertex_indices = new short[NUM_BODY_SEGMENTS*6*4];
		for (short i = 0; i < NUM_BODY_SEGMENTS; i++) {
			int seg_vert_offset = i*8;
			int current_face = i*24;
			// Bottom
			vertex_indices[current_face]   = (short) (seg_vert_offset + 0);
			vertex_indices[current_face+1] = (short) (seg_vert_offset + 1);
			vertex_indices[current_face+2] = (short) (seg_vert_offset + 2);
			vertex_indices[current_face+3] = (short) (seg_vert_offset + 3);
			current_face+=4;
			// Top
			vertex_indices[current_face]   = (short) (seg_vert_offset + 4);
			vertex_indices[current_face+1] = (short) (seg_vert_offset + 5);
			vertex_indices[current_face+2] = (short) (seg_vert_offset + 6);
			vertex_indices[current_face+3] = (short) (seg_vert_offset + 7);
			current_face+=4;
			// Left
			vertex_indices[current_face]   = (short) (seg_vert_offset + 0);
			vertex_indices[current_face+1] = (short) (seg_vert_offset + 1);
			vertex_indices[current_face+2] = (short) (seg_vert_offset + 5);
			vertex_indices[current_face+3] = (short) (seg_vert_offset + 4);
			current_face+=4;
			// Right
			vertex_indices[current_face]   = (short) (seg_vert_offset + 3);
			vertex_indices[current_face+1] = (short) (seg_vert_offset + 7);
			vertex_indices[current_face+2] = (short) (seg_vert_offset + 6);
			vertex_indices[current_face+3] = (short) (seg_vert_offset + 2);
			current_face+=4;
			// Front
			vertex_indices[current_face]   = (short) (seg_vert_offset + 0);
			vertex_indices[current_face+1] = (short) (seg_vert_offset + 3);
			vertex_indices[current_face+2] = (short) (seg_vert_offset + 7);
			vertex_indices[current_face+3] = (short) (seg_vert_offset + 4);
			current_face+=4;
			// Back
			vertex_indices[current_face]   = (short) (seg_vert_offset + 1);
			vertex_indices[current_face+1] = (short) (seg_vert_offset + 2);
			vertex_indices[current_face+2] = (short) (seg_vert_offset + 6);
			vertex_indices[current_face+3] = (short) (seg_vert_offset + 5);
		}
		
		vertIndicesBuffer = Buffers.newDirectShortBuffer(vertex_indices);
	}
    
    private void setMaterial(GL2 gl) {
    	// Default Material property vectors.
    	float matAmbAndDif1[] = {1.0f, 1.0f, 1.0f, 0.0f};
    	float matAmbAndDif2[] = {0.0f, 0.0f, 0.0f, 0.0f};
    	float matSpec[] = { 0.0f, 0.0f, 0.0f, 0.0f };
    	float matShine[] = { 10.0f };
    	// Material properties.
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif1,0);
    	gl.glMaterialfv(GL2.GL_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif2,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);
    }
    
    @Override
    public void drawSelf(GL2 gl) {
    	setMaterial(gl);
    	try {
   		 shaderprogram = Shader.initShaders(gl, VERTEX_SHADER, FRAGMENT_SHADER);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    	texUnitLoc = gl.glGetUniformLocation(shaderprogram,"texUnit1");
    	texUnitLoc2 = gl.glGetUniformLocation(shaderprogram,"texUnit2");
    	
    	
    	bufferIds = new int[3];
        gl.glGenBuffers(3,bufferIds,0);
        //would use GL.GL_ELEMENT_ARRAY_BUFFER for indices
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER,bufferIds[1]);
        gl.glBindBuffer(GL2.GL_TEXTURE_BUFFER,bufferIds[2]);

        gl.glBufferData(GL.GL_ARRAY_BUFFER,vertices.length*Float.BYTES, vertexBuffer, GL2.GL_STATIC_DRAW);
     	gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, vertex_indices.length*Short.BYTES , vertIndicesBuffer, GL2.GL_STATIC_DRAW);
     	gl.glBufferData(GL2.GL_TEXTURE_BUFFER, texCoords.length*Float.BYTES , texBuffer, GL2.GL_STATIC_DRAW);
     	
    	gl.glUseProgram(shaderprogram);
    	gl.glUniform1i(texUnitLoc , 0);
    	int vPos = gl.glGetAttribLocation(shaderprogram, "vertexPos");
    	gl.glEnableVertexAttribArray(vPos);
    	

    	// Set current texture
//    	gl.glActiveTexture(GL2.GL_TEXTURE0);
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, this.getTextureID());        
    	      
    	//Set wrap mode for texture in S direction
    	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT); 
    	//Set wrap mode for texture in T direction
    	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);

    	// Enable two vertex arrays: co-ordinates and color.
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		
    	gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,bufferIds[0]);
    	gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER,bufferIds[1]);
    	// Specify locations for the co-ordinates and color arrays.
    	gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0); //last num is the offset
    	gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, 0);
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    	gl.glDrawElements(GL2.GL_QUADS, vertex_indices.length, GL2.GL_UNSIGNED_SHORT, 0);

    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    	gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,0);
    	gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER,0);
    	gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

    }
    double x = 0, y = 0, z = 0;
    @Override
    public void update(double dt) {
    	updateTimeout += dt;
    	if (updateTimeout > 1) { 
    		updateTimeout = 0;
	    	Random r = new Random(System.currentTimeMillis());
	    	x = r.nextDouble() - 0.5;
	    	z = r.nextDouble() - 0.5;
    	}

    	double[] p = this.getGlobalPosition();
    	y = altFun.apply(new double[]{p[0],p[2]});
//    	this.translate(x*dt*4, y-p[1], z*dt*4);
    }
	
}
