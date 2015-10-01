package ass2.spec;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;


/**
 * A GameObject is an object that can move around in the game world.
 * 
 * GameObjects form a scene tree. The root of the tree is the special ROOT object.
 * 
 * Each GameObject is offset from its parent by a rotation, a translation and a scale factor. 
 *
 * @author malcolmr
 */
public class GameObject {

    // the list of all GameObjects in the scene tree
    public final static List<GameObject> ALL_OBJECTS = new ArrayList<GameObject>();
    
    // the root of the scene tree
    public final static GameObject ROOT = new GameObject();
    
    // the links in the scene tree
    private GameObject myParent;
    private List<GameObject> myChildren;

    // the local transformation
    //myRotation should be normalised to the range (-180..180)
    private double[] myRotation;
    private double myScale;
    private double[] myTranslation;
    
    // is this part of the tree showing?
    private boolean amShowing;

    /**
     * Special private constructor for creating the root node. Do not use otherwise.
     */
    private GameObject() {
        myParent = null;
        myChildren = new ArrayList<GameObject>();

        init_transforms();
        amShowing = true;
        ALL_OBJECTS.add(this);
    }

    /**
     * Public constructor for creating GameObjects, connected to a parent (possibly the ROOT).
     *  
     * New objects are created at the same location, orientation and scale as the parent.
     *
     * @param parent
     */
    public GameObject(GameObject parent) {
        myParent = parent;
        myChildren = new ArrayList<GameObject>();

        parent.myChildren.add(this);
        init_transforms();
        // initially showing
        amShowing = true;
        ALL_OBJECTS.add(this);
    }
    
    private void init_transforms() {
    	myRotation = new double[3];
        myRotation[0] = 0;
        myRotation[1] = 0;
        myRotation[2] = 0;
        myScale = 1;
        myTranslation = new double[3];
        myTranslation[0] = 0;
        myTranslation[1] = 0;
        myTranslation[2] = 0;
    }

    /**
     * Remove an object and all its children from the scene tree.
     */
    public void destroy() {
        for (GameObject child : myChildren) {
            child.destroy();
        }
        
        myParent.myChildren.remove(this);
        ALL_OBJECTS.remove(this);
    }

    /**
     * Get the parent of this game object
     * 
     * @return
     */
    public GameObject getParent() {
        return myParent;
    }

    /**
     * Get the children of this object
     * 
     * @return
     */
    public List<GameObject> getChildren() {
        return myChildren;
    }

    /**
     * Get the local rotation (in degrees)
     * 
     * @return
     */
    public double[] getRotation() {
        return myRotation;
    }

    /**
     * Set the local rotation (in degrees)
     * 
     * @return
     */
    public void setRotation(double[] rotation) {
//        myRotation = MathUtil.normaliseAngle(rotation);
    }

    /**
     * Rotate the object by the given angle (in degrees)
     * 
     * @param angle
     */
    public void rotate(double[] angle) {
    	myRotation[0] += angle[0];
    	myRotation[1] += angle[1];
    	myRotation[2] += angle[2];
//        myRotation = MathUtil.normaliseAngle(myRotation);
    }

    /**
     * Get the local scale
     * 
     * @return
     */
    public double getScale() {
        return myScale;
    }

    /**
     * Set the local scale
     * 
     * @param scale
     */
    public void setScale(double scale) {
        myScale = scale;
    }

    /**
     * Multiply the scale of the object by the given factor
     * 
     * @param factor
     */
    public void scale(double factor) {
        myScale *= factor;
    }

    /**
     * Get the local position of the object 
     * 
     * @return
     */
    public double[] getPosition() {
        return myTranslation;
    }

    /**
     * Set the local position of the object
     * 
     * @param x
     * @param y
     */
    public void setPosition(double x, double y, double z) {
        myTranslation[0] = x;
        myTranslation[1] = y;
        myTranslation[2] = z;
    }

    /**
     * Move the object by the specified offset in local coordinates
     * 
     * @param dx
     * @param dy
     */
    public void translate(double dx, double dy, double dz) {
    	myTranslation[0] += dx;
        myTranslation[1] += dy;
        myTranslation[2] += dz;
    }

    /**
     * Test if the object is visible
     * 
     * @return
     */
    public boolean isShowing() {
        return amShowing;
    }

    /**
     * Set the showing flag to make the object visible (true) or invisible (false).
     * This flag should also apply to all descendents of this object.
     * 
     * @param showing
     */
    public void show(boolean showing) {
        amShowing = showing;
    }

    /**
     * Update the object. This method is called once per frame. 
     * 
     * This does nothing in the base GameObject class. Override this in subclasses.
     * 
     * @param dt The amount of time since the last update (in seconds)
     */
    public void update(double dt) {
        // do nothing
    }

    /**
     * 
     * This does nothing in the base GameObject class. Override this in subclasses.
     * 
     * @param gl
     */
    public void drawSelf(GL2 gl) {
        // do nothing
    }

    /**
     * Draw the object and all of its descendants recursively.
     * 
     * @param gl
     */
    public void draw(GL2 gl) {
        
        // don't draw if it is not showing
        if (!amShowing) {
            return;
        }

        // Perform matrix operations
        // TRS Ordering
        gl.glTranslated(myTranslation[0], myTranslation[1], myTranslation[2]);
        gl.glRotated   (myRotation[0], myRotation[1], myRotation[2], 1);
        gl.glScaled	   (myScale, myScale, myScale);
        // Draw self, then draw children
        drawSelf(gl);
        for (GameObject child: myChildren) {
        	// Store parent's coordinate system
        	gl.glPushMatrix();
        	child.draw(gl);
        	// Restore parent's coordinate system for the next children
        	gl.glPopMatrix();
        }
        
        
    }

    /**
     * Compute the object's position in world coordinates
     * 
     * @return a point in world coordinates in [x,y] form
     */
    public double[] getGlobalPosition() {
        double[] p = new double[3];
        // Go up the scene tree, my global position should be my local translation surmounted by my 
        // parents TRS operations
       
        // Start with this node
        GameObject parent = this;
        double[][] m = MathUtil.identity();
        while (parent != null) {
        	double[][] parent_m = MathUtil.multiply(MathUtil.translationMatrix(parent.getPosition()),
        						  MathUtil.multiply(MathUtil.rotationMatrix(parent.getRotation()),
        								  			MathUtil.scaleMatrix(parent.getScale())));
        	// Multiplying in this order means the parent transforms are applied last
        	m = MathUtil.multiply(parent_m, m);
        	parent = parent.getParent();
        }
        p[0] = m[0][3];
        p[1] = m[1][3];
        p[2] = m[2][3];
        return p; 
    }

    /**
     * Compute the object's rotation in the global coordinate frame
     * 
     * @return the global rotation of the object (in degrees) and 
     * normalized to the range (-180, 180) degrees. 
     */
    public double[] getGlobalRotation() {
        // Start with this node
        GameObject parent = this;
        double[][] m = MathUtil.identity();
        while (parent != null) {
        	double[][] parent_m = MathUtil.multiply(MathUtil.translationMatrix(parent.getPosition()),
					  			  MathUtil.multiply(MathUtil.rotationMatrix(parent.getRotation()),
					  					  			MathUtil.scaleMatrix(parent.getScale())));
        	// Multiplying in this order means the parent transforms are applied last
        	m = MathUtil.multiply(parent_m, m);
        	parent = parent.getParent();
        }

        return MathUtil.decompose_rotation(m);
    }

    /**
     * Compute the object's scale in global terms
     * 
     * @return the global scale of the object 
     */
    public double getGlobalScale() {
    	double scale;
        GameObject parent = this;
        double[][] m = MathUtil.identity();
        while (parent != null) {
        	double[][] parent_m = MathUtil.multiply(MathUtil.translationMatrix(parent.getPosition()),
        						  MathUtil.multiply(MathUtil.rotationMatrix(parent.getRotation()),
        								  			MathUtil.scaleMatrix(parent.getScale())));
        	// Multiplying in this order means the parent transforms are applied last
        	m = MathUtil.multiply(parent_m, m);
        	parent = parent.getParent();
        }
        scale = Math.sqrt(m[0][0]*m[0][0] + m[0][1]*m[0][1] + m[0][2]*m[0][2]);
        return scale;
    }

    /**
     * Change the parent of a game object.
     * 
     * @param parent
     */
    public void setParent(GameObject parent) {
        double[] p;
        double[] r;
        double s;
        
        // Find what my current global coordinates are
        p 	 = getGlobalPosition();
        r 	 = getGlobalRotation();
        s 	 = getGlobalScale();
        // Original matrix
        double[][] m0 = MathUtil.multiply(MathUtil.multiply(MathUtil.translationMatrix(p),
												    		MathUtil.rotationMatrix(r)),
												    		MathUtil.scaleMatrix(s));
        myParent.myChildren.remove(this);
        myParent = parent;
        myParent.myChildren.add(this);

        // Find what my current global settings are, and construct the opposite
        p[0] = -parent.getGlobalPosition()[0];
        p[1] = -parent.getGlobalPosition()[1];
        p[2] = -parent.getGlobalPosition()[2];
        
        r[0] = -parent.getGlobalRotation()[0];
        r[1] = -parent.getGlobalRotation()[1];
        r[2] = -parent.getGlobalRotation()[2];
        
        s 	 = 1/parent.getGlobalScale();
        double[][] m1 = MathUtil.multiply(MathUtil.multiply(MathUtil.scaleMatrix(s),
												    		MathUtil.rotationMatrix(r)),
												    		MathUtil.translationMatrix(p));
        // Undo the new transformation from parent and apply the old one
        // M = P0 *P1 * P2 .... *PN
        // M != m0
        // To make M = m0, find PN.
        // PN = (P0*P1*P2...PN-1)^-1 * M
        double [][] m = MathUtil.multiply(m1, m0);
        // Origin point
        this.setPosition(m[0][3], m[1][3], m[2][3]);
        // ???
        this.setRotation(MathUtil.decompose_rotation(m));
        // modulus of the i vector
        this.setScale(Math.sqrt(m[0][0]*m[0][0] + m[1][0]*m[1][0] + m[2][0]*m[2][0]));
    }
}
