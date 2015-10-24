package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

import com.jogamp.newt.event.InputEvent;
import com.jogamp.opengl.util.FPSAnimator;

import ass2.objects.*;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private Terrain myTerrain;
    private static Camera myCamera;
    private long myTime;
    private Texture[] myTextures;
    private BeastObject myBeast;
    private TerrainGameObject terrain;
    private CubeObject[] cubes = new CubeObject[]{};
    private SphereObject[] spheres = new SphereObject[]{};
    private float[] mySun;
    private float[] mySunColour = new float[]{1.0f,1.0f,1.0f};
    private static double secInDay = 10;
    private boolean isNight = false;
    
    String[] textureFiles = new String[]{
    		"./textures/grass.png",
    		"./textures/wood.png",
    		"./textures/branches.png",
    		"./textures/road.png",
    		"./textures/brushed_gold.png",
    		"./textures/gradient_gold.png",
    		"./textures/cow.png"
    };
    

    private static final int NUM_TEXTURES = 7;
    
    private boolean isTimePassing = false;
    
    private static final double myHeight 	= 1.2;
    
    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
    }
    
    public void setCamera(Camera camera) {
    	myCamera = camera;
    }
    
    /** 
     * Run the game.
     */
    public void run() {
		  GLProfile glp = GLProfile.getDefault();
		  GLCapabilities caps = new GLCapabilities(glp);
		  GLJPanel panel = new GLJPanel(caps);
		  panel.addGLEventListener(this);
		  
		  terrain = new TerrainGameObject(GameObject.ROOT, myTerrain);
//		  drawWorldObjects();
//		  drawTrees(myTerrain.trees());
		  
		  myBeast = new BeastObject(GameObject.ROOT);
		  myBeast.translate(-5, 0, -5);
//		  terrain.translate(5, 0, 5);
		  
		  myCamera = new Camera(GameObject.ROOT, new Function<double[], Double>() {
			@Override
			public Double apply(double[] t) {
				return myTerrain.altitude(t[0], t[1]);
			}
		  });
		  myCamera.rotate(new double[]{0,-90,0});	//face in the +x direction
		  myCamera.setBackground(new float[]{1f,1f,1f,1f});

		  // Add the keyListener
		  panel.addKeyListener(this);
		  
          // Add an animator to call 'display' at 60fps        
          FPSAnimator animator = new FPSAnimator(60);
          animator.add(panel);
          animator.start();
//          myCamera.setPosition(0, 0, 0);
//          myCamera.setRotation(new double[]{0,-270,0});
          getContentPane().add(panel);
          setSize(640, 480);        
          setVisible(true);
          setDefaultCloseOperation(EXIT_ON_CLOSE);        
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
    	game.run();
    }
    
    public void drawWorldObjects() {
    	CubeObject cubeFront = new CubeObject(GameObject.ROOT);
    	CubeObject cubeBack = new CubeObject(GameObject.ROOT);
    	CubeObject cubeLeft = new CubeObject(GameObject.ROOT);
    	CubeObject cubeRight = new CubeObject(GameObject.ROOT);
        cubeFront.translate(0, 0, -3);
        cubeBack.translate(0, 0, 3);
        cubeLeft.translate(-3, 0, 0);
        cubeRight.translate(3, 0, 0);
        cubes = new CubeObject[]{cubeFront, cubeBack, cubeLeft, cubeRight};
//        GameObject axes = new Axes(GameObject.ROOT);
//        axes.scale(2);
        
//        SphereObject sphere = new SphereObject(GameObject.ROOT);
//        sphere.translate(0, 2, 0);
//        sphere.scale(4);
//        spheres = new SphereObject[]{sphere};
    }
        
    private void update() {
        // compute the time since the last frame
        long time = System.currentTimeMillis();
        double dt = (time - myTime) / 1000.0;
        myTime = time;
        if (isTimePassing) {
	        mySun[0] = (float)Math.sin(time/1000.0 /secInDay  *Math.PI*2)*(-100);
	        mySun[1] = (float)Math.cos(time/1000.0 /secInDay  *Math.PI*2)*(-100);
	        mySun[2] = (1)*(-100);
	        mySunColour[0] = (float)Math.cos(time/1000.0 /secInDay  *Math.PI*2)/2 + 0.3f;
	        mySunColour[1] = (float)Math.cos(time/1000.0 /secInDay  *Math.PI*2)/2 + 0.2f;
	        mySunColour[2] = (float)Math.cos(time/1000.0 /secInDay  *Math.PI*2);
	        
//	        mySun = MathUtil.normaliseAngleArray(mySun);
	        System.out.printf("x = %f, y= %f\n", mySun[0], mySun[1]);
        }
        // take a copy of the ALL_OBJECTS list to avoid errors 
        // if new objects are created in the update
        List<GameObject> objects = new ArrayList<GameObject>(GameObject.ALL_OBJECTS);
        
        // update all objects
        for (GameObject g : objects) {
            g.update(dt);
        }        
    }
    
	@Override
	public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        myCamera.setView(gl);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);
        update();
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION , mySun, 0);
		float[] ambient;
//		gl.glEnable(GL2.GL_LIGHT_MODEL_AMBIENT);
//		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[]{0.5f,0.5f,0.1f,0.1f}, 0);
		if (isNight) {
			gl.glDisable(GL2.GL_LIGHT0);
			gl.glEnable(GL2.GL_LIGHT1);
			double[] posd = myCamera.getGlobalPosition();
			float[] posf = new float[]{(float)posd[0], (float)posd[1], (float)posd[2]};
			double[] centre = MathUtil.multiply(MathUtil.rotationMatrix(myCamera.getRotation()), new double[]{0,0,-1,0});
			float[] dirf = new float[]{(float)centre[0], (float)centre[1], (float)centre[2]};
			gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[]{0.3f,0.3f,0.3f,1.0f}, 0);
			
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, posf, 0);
			gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, dirf, 0);
	        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_EXPONENT, new float[]{75.0f}, 0);
	        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, new float[]{70.0f}, 0);
	        
//			gl.glDisable(GL2.GL_LIGHTING);
//			gl.glEnable(GL2.GL_LIGHTING);
		} else {
			gl.glEnable(GL2.GL_LIGHT0);
			gl.glDisable(GL2.GL_LIGHT1);
			gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[]{0.0f,0.0f,0.0f,0.01f}, 0);
			
		}
		if (isTimePassing) {
			float sunStrength = (mySun[1]/100 + 1)/2;
			if (sunStrength < 0) {
				sunStrength = 0;
			}
			ambient = new float[]{mySunColour[0],mySunColour[1],mySunColour[2], 0};
			System.out.printf("amb: %f\n", sunStrength);
			
			gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, ambient, 0);
		}
		// Add textures to objects
        terrain.setTexture(myTextures[0]);
        terrain.setTreeTextures(myTextures[1], myTextures[2]);
        terrain.setRoadTexture(myTextures[3]);
        // My cow
        myBeast.setTexture(myTextures[6]);
        // My teapot.
		myCamera.setTexture(myTextures[4]);
        for (CubeObject c: cubes) {
        	c.setTexture(myTextures[1]);
        }
        for (SphereObject s: spheres) {
        	s.setTexture(myTextures[0]);
        }
        GameObject.ROOT.draw(gl);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
//		drawable.destroy();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		myTime = System.currentTimeMillis();
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glFrontFace( GL2.GL_CCW);
		gl.glPolygonOffset(1.0f, 1.0f);
		// Big points for debugging
		gl.glPointSize(5.0f);
		
		// Add all textures
		myTextures = new Texture[NUM_TEXTURES];
		int i = 0;
		for (String filename: textureFiles) {
			myTextures[i] = new Texture(gl, filename, "png", true);
			i++;
		}
		
//		gl.glEnable(GL2.GL_CULL_FACE);
//		gl.glCullFace(GL2.GL_BACK);
		
		gl.glEnable(GL2.GL_LIGHTING);
		// Specified as a direction
		mySun = new float[]{-100*myTerrain.getSunlight()[0], -100*myTerrain.getSunlight()[1], -100*myTerrain.getSunlight()[2], 0};
		
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[]{0.5f,0.5f,0.5f,0.5f}, 0);
		
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION , mySun, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[]{0.0f, 0.0f, 0.0f, 0.0f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 0);
        
        
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glDisable(GL2.GL_LIGHT1);
    	// Default Material property vectors.
    	float matAmbAndDif1[] = {1.0f, 1.0f, 1.0f, 0.0f};
    	float matAmbAndDif2[] = {1.0f, 1.0f, 1.0f, 0.0f};
    	float matSpec[] = { 1.0f, 1.0f, 1.0f, 1.0f };
    	float matShine[] = { 0.0f };
    	// Material properties.
    	gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif1,0);
    	gl.glMaterialfv(GL2.GL_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif2,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, matSpec,0);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine,0);
//
//    	// Specify how texture values combine with current surface color values.
    	gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE); 

    	// Turn on OpenGL texturing.
    	gl.glEnable(GL2.GL_TEXTURE_2D);
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
		myCamera.reshape(gl, x, y, width, height);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		double speed = 1;
		if (e.getModifiers() == InputEvent.SHIFT_MASK) {
			speed = 5;
		}
//		System.out.println(speed);
		myCamera.setTransSpeed(speed);
		myCamera.setRotSpeed(speed);
		switch(key) {
			case KeyEvent.VK_UP   : myCamera.enableMovement(); myCamera.setTransDirection(-1);	break;
			case KeyEvent.VK_DOWN : myCamera.enableMovement(); myCamera.setTransDirection(1); break;
			case KeyEvent.VK_LEFT : myCamera.enableTurning(); myCamera.setRotDirection(1);	break;
			case KeyEvent.VK_RIGHT: myCamera.enableTurning(); myCamera.setRotDirection(-1); break;
			case KeyEvent.VK_P	  : myCamera.togglePerspective(); break;
			case KeyEvent.VK_T	  : isTimePassing = !isTimePassing; break;
			case KeyEvent.VK_N	  : isNight = !isNight; break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		double speed = 1;
		if (e.getModifiers() == InputEvent.SHIFT_MASK) {
			speed = 5;
		}
		myCamera.setTransSpeed(speed);
		myCamera.setRotSpeed(speed);
		switch(key) {
			case KeyEvent.VK_UP   : 
			case KeyEvent.VK_DOWN : myCamera.disableMovement(); break;
			case KeyEvent.VK_LEFT : 
			case KeyEvent.VK_RIGHT: myCamera.disableTurning(); break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
