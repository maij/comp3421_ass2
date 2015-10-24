package ass2.objects;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;

import ass2.spec.Shader;
import ass2.spec.Texture;

// Must use VBOs
public class BeastObject extends GameObject {
	private static final String VERTEX_SHADER = "ass2/shaders/PhongVertexTex.glsl";
    private static final String FRAGMENT_SHADER = "ass2/shaders/PhongFragmentTex.glsl";
    private static final int NUM_BODY_SEGMENTS = 6;
    private static final float LEG_INSET = 0.2f;
    private static final float LEG_WIDTH = 0.5f;
    private static final float LEG_HEIGHT = 0.5f;
    
    private static final float BODY_WIDTH = 1.0f;
    private static final float BODY_LENGTH = 2.0f;
    private static final float BODY_HEIGHT = 1.0f;
    
    private static final float FACE_WIDTH = 0.8f;
    private static final float FACE_LENGTH = 1.0f;
    private static final float FACE_HEIGHT = 1.0f;
    private static final float FACE_INSET = 0.2f;
    
    
    private int shaderprogram;
    private float[] vertices = {
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
    		BODY_WIDTH - LEG_INSET,			 		  0,LEG_INSET,			// 0,0,0
    		BODY_WIDTH - LEG_INSET,			 		  0,LEG_INSET+LEG_WIDTH, // 0,0,1
    		BODY_WIDTH - LEG_INSET-LEG_WIDTH,		  0,LEG_INSET+LEG_WIDTH, // 1,0,1
    		BODY_WIDTH - LEG_INSET-LEG_WIDTH,		  0,LEG_INSET, 		   	// 1,0,0
    		BODY_WIDTH - LEG_INSET,			 LEG_HEIGHT,LEG_INSET,			 // 0,0,0
    		BODY_WIDTH - LEG_INSET,			 LEG_HEIGHT,LEG_INSET+LEG_WIDTH, // 0,0,1
    		BODY_WIDTH - LEG_INSET-LEG_WIDTH,LEG_HEIGHT,LEG_INSET+LEG_WIDTH, // 1,0,1
    		BODY_WIDTH - LEG_INSET-LEG_WIDTH,LEG_HEIGHT,LEG_INSET, 		   	 // 1,0,0
    		// Third leg
    		LEG_INSET		   ,		  0, BODY_LENGTH - LEG_INSET, 		   // 0,0,0
    		LEG_INSET		   ,		  0, BODY_LENGTH - LEG_INSET-LEG_WIDTH, // 0,0,1
    		LEG_INSET+LEG_WIDTH,		  0, BODY_LENGTH - LEG_INSET-LEG_WIDTH, // 1,0,1
    		LEG_INSET+LEG_WIDTH,		  0, BODY_LENGTH - LEG_INSET, 		   // 1,0,0
    		LEG_INSET		   , LEG_HEIGHT, BODY_LENGTH - LEG_INSET, 		   // 0,0,0
    		LEG_INSET		   , LEG_HEIGHT, BODY_LENGTH - LEG_INSET-LEG_WIDTH, // 0,0,1
    		LEG_INSET+LEG_WIDTH, LEG_HEIGHT, BODY_LENGTH - LEG_INSET-LEG_WIDTH, // 1,0,1
    		LEG_INSET+LEG_WIDTH, LEG_HEIGHT, BODY_LENGTH - LEG_INSET, 		   // 1,0,0
			// Fourth Leg
    		BODY_WIDTH - LEG_INSET,			 		 0,BODY_LENGTH - LEG_INSET, 		   // 0,0,0
    		BODY_WIDTH - LEG_INSET,			 		 0,BODY_LENGTH - LEG_INSET-LEG_WIDTH, // 0,0,1
    		BODY_WIDTH - LEG_INSET-LEG_WIDTH,		 0,BODY_LENGTH - LEG_INSET-LEG_WIDTH, // 1,0,1
    		BODY_WIDTH - LEG_INSET-LEG_WIDTH,		 0,BODY_LENGTH - LEG_INSET, 		   // 1,0,0
    		BODY_WIDTH - LEG_INSET,			LEG_HEIGHT,BODY_LENGTH - LEG_INSET, 		   // 0,0,0
    		BODY_WIDTH - LEG_INSET,			LEG_HEIGHT,BODY_LENGTH - LEG_INSET-LEG_WIDTH,  // 0,0,1
    		BODY_WIDTH - LEG_INSET-LEG_WIDTH,LEG_HEIGHT,BODY_LENGTH - LEG_INSET-LEG_WIDTH,  // 1,0,1
    		BODY_WIDTH - LEG_INSET-LEG_WIDTH,LEG_HEIGHT,BODY_LENGTH - LEG_INSET, 		   // 1,0,0

    		// Cow Face
    		BODY_WIDTH/2-FACE_WIDTH/2, BODY_HEIGHT - FACE_INSET	+ LEG_HEIGHT, 0,
    		BODY_WIDTH/2+FACE_WIDTH/2, BODY_HEIGHT - FACE_INSET	+ LEG_HEIGHT				, 0,
    		BODY_WIDTH/2+FACE_WIDTH/2, BODY_HEIGHT - FACE_INSET	+ LEG_HEIGHT				, -FACE_LENGTH,
    		BODY_WIDTH/2-FACE_WIDTH/2, BODY_HEIGHT - FACE_INSET	+ LEG_HEIGHT				, -FACE_LENGTH,
    		BODY_WIDTH/2-FACE_WIDTH/2, BODY_HEIGHT - FACE_INSET -FACE_HEIGHT	+ LEG_HEIGHT, 0,
    		BODY_WIDTH/2+FACE_WIDTH/2, BODY_HEIGHT - FACE_INSET -FACE_HEIGHT	+ LEG_HEIGHT, 0,
    		BODY_WIDTH/2+FACE_WIDTH/2, BODY_HEIGHT - FACE_INSET -FACE_HEIGHT	+ LEG_HEIGHT, -FACE_LENGTH,
    		BODY_WIDTH/2-FACE_WIDTH/2, BODY_HEIGHT - FACE_INSET -FACE_HEIGHT	+ LEG_HEIGHT, -FACE_LENGTH,
    		
    };
    
    private float texCoords[] =  {
    		// 8 Vertices for all rectangular prisms
    		0,0, 0,1, 1,1, 1,0,
    		0,0, 0,1, 1,1, 1,0,
    		
    		0,0, 0,1, 1,1, 1,0,
    		0,0, 0,1, 1,1, 1,0,

    		0,0, 0,1, 1,1, 1,0,
    		0,0, 0,1, 1,1, 1,0,
    		
    		0,0, 0,1, 1,1, 1,0,
    		0,0, 0,1, 1,1, 1,0,
    		
    		0,0, 0,1, 1,1, 1,0,
    		0,0, 0,1, 1,1, 1,0,
    		
    		0,0, 0,1, 1,1, 1,0,
    		0,0, 0,1, 1,1, 1,0,

	};
    
    private short[] vertex_indices;
    
    private FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(vertices);
    private FloatBuffer texBuffer = Buffers.newDirectFloatBuffer(texCoords);
    private ShortBuffer vertIndicesBuffer; //= Buffers.newDirectShortBuffer(vertex_indices);
	
    int bufferIds[];
    int texUnitLoc;
    int texUnitLoc2;
    
    GameObject[] myBody = new GameObject[0];
    
    public BeastObject(GameObject parent) {
		super(parent);
//		for (int i = 0; i < vertices.length; i++) {
//			System.out.printf("%f\n", vertices[i]);
//		}
		vertex_indices = new short[NUM_BODY_SEGMENTS*6*4];
		for (short i = 0; i < NUM_BODY_SEGMENTS; i++) {
			short current_segment = (short) (i*24);
			int current_face = current_segment;
			vertex_indices[current_face+0] = (short) (current_segment + 0);
			vertex_indices[current_face+1] = (short) (current_segment + 1);
			vertex_indices[current_face+2] = (short) (current_segment + 2);
			vertex_indices[current_face+3] = (short) (current_segment + 3);
			current_face++;
			vertex_indices[current_face+4] = (short) (current_segment + 4);
			vertex_indices[current_face+5] = (short) (current_segment + 5);
			vertex_indices[current_face+6] = (short) (current_segment + 6);
			vertex_indices[current_face+7] = (short) (current_segment + 7);
			current_face++;
			vertex_indices[current_face+0] = (short) (current_segment + 0);
			vertex_indices[current_face+1] = (short) (current_segment + 1);
			vertex_indices[current_face+4] = (short) (current_segment + 4);
			vertex_indices[current_face+5] = (short) (current_segment + 5);
			current_face++;
			vertex_indices[current_face+3] = (short) (current_segment + 3);
			vertex_indices[current_face+7] = (short) (current_segment + 7);
			vertex_indices[current_face+6] = (short) (current_segment + 6);
			vertex_indices[current_face+2] = (short) (current_segment + 2);
			current_face++;
			vertex_indices[current_face+0] = (short) (current_segment + 0);
			vertex_indices[current_face+3] = (short) (current_segment + 3);
			vertex_indices[current_face+7] = (short) (current_segment + 7);
			vertex_indices[current_face+4] = (short) (current_segment + 4);
			current_face++;
			vertex_indices[current_face+1] = (short) (current_segment + 1);
			vertex_indices[current_face+2] = (short) (current_segment + 2);
			vertex_indices[current_face+6] = (short) (current_segment + 6);
			vertex_indices[current_face+5] = (short) (current_segment + 5);
		}
		vertIndicesBuffer = Buffers.newDirectShortBuffer(vertex_indices);
		System.out.println("Made index buffer...");
//		myBody = new CubeObject[NUM_BODY_SEGMENTS];
//		for (int i = 0; i < NUM_BODY_SEGMENTS; i++) {
//			myBody[i] = new CubeObject(this);
//		}
//		// Four legs
//		myBody[0].translate(-0.4, 0, -0.4);
//		myBody[1].translate(-0.4, 0,  0.4);
//		myBody[2].translate( 0.4, 0, -0.4);
//		myBody[3].translate( 0.4, 0,  0.4);
//		myBody[0].scale(0.25);
//		myBody[1].scale(0.25);
//		myBody[2].scale(0.25);
//		myBody[3].scale(0.25);
//		// main body
//		myBody[4].scale(1);	
//		myBody[4].translate(0, 0.25, 0);
//		// The head
//		myBody[5].translate(0.5, 0.75, 0);
//		myBody[5].scale(0.5);
	}
    
    @Override
    public void setTexture(Texture t) {
    	for(GameObject go: myBody) {
    		go.setTexture(t);
    	}
    }
    
    @Override
    public void drawSelf(GL2 gl) {
    	 try {
   		 shaderprogram = Shader.initShaders(gl,VERTEX_SHADER,FRAGMENT_SHADER);
   		
   		 
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
     	//Say that is the current array buffer
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER,bufferIds[1]);
        gl.glBindBuffer(GL2.GL_TEXTURE_BUFFER,bufferIds[2]);
        //Is 4 right?
        //just ask for the space.
     	gl.glBufferData(GL.GL_ARRAY_BUFFER,vertices.length*Float.BYTES+texCoords.length*Float.BYTES, vertexBuffer,GL2.GL_STATIC_DRAW);
     	gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, vertex_indices.length*Short.BYTES , vertIndicesBuffer, GL2.GL_STATIC_DRAW);
     	gl.glBufferData(GL2.GL_TEXTURE_BUFFER, texCoords.length*Float.BYTES , texBuffer, GL2.GL_STATIC_DRAW);
     	
//     	gl.glBufferSubData(GL.GL_ARRAY_BUFFER,0,vertices.length*Float.BYTES, vertexBuffer);
//     	gl.glBufferSubData(GL.GL_ARRAY_BUFFER,vertices.length*Float.BYTES,texCoords.length*Float.BYTES, texBuffer);
     	 
     	 
     	 
		// Enable two vertex arrays: co-ordinates and color.
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		
		// Specify locations for the co-ordinates and color arrays.
//		gl.glVertexPointer(4, GL.GL_FLOAT, 0, 0); //last num is the offset
//		gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, vertices.length*4);
   
    	gl.glUseProgram(shaderprogram);
    	gl.glUniform1i(texUnitLoc , 0);
    	

    	// Set current texture
//    	gl.glActiveTexture(GL2.GL_TEXTURE0);
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, this.getTextureID());        
    	      
    	//Set wrap mode for texture in S direction
    	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT); 
    	//Set wrap mode for texture in T direction
    	gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
//
    	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

    	gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,bufferIds[0]);
    	gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER,bufferIds[1]);
    	// Specify locations for the co-ordinates and color arrays.
    	gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0); //last num is the offset
    	gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, 0);

//    	gl.glDrawArrays(GL2.GL_QUADS,0,4);
    	gl.glDrawElements(GL2.GL_QUADS, vertex_indices.length, GL2.GL_UNSIGNED_SHORT, 0);
    	gl.glBindBuffer(GL2.GL_ARRAY_BUFFER,0);
    	gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER,0);
    	gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

    }
	
}
