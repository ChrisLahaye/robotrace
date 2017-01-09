package robotrace;

import com.jogamp.graph.geom.SVertex;
import com.jogamp.graph.geom.Vertex;
import com.jogamp.opengl.math.VectorUtil;

import java.awt.*;
import static javax.media.opengl.GL.GL_LINES;

import static javax.media.opengl.GL2.*;
import static robotrace.ShaderPrograms.*;

/**
 * Handles all of the RobotRace graphics functionality,
 * which should be extended per the assignment.
 * 
 * OpenGL functionality:
 * - Basic commands are called via the gl object;
 * - Utility commands are called via the glu and
 *   glut objects;
 * 
 * GlobalState:
 * The gs object contains the GlobalState as described
 * in the assignment:
 * - The camera viewpoint angles, phi and theta, are
 *   changed interactively by holding the left mouse
 *   button and dragging;
 * - The camera view width, vWidth, is changed
 *   interactively by holding the right mouse button
 *   and dragging upwards or downwards; (Not required in this assignment)
 * - The center point can be moved up and down by
 *   pressing the 'q' and 'z' keys, forwards and
 *   backwards with the 'w' and 's' keys, and
 *   left and right with the 'a' and 'd' keys;
 * - Other settings are changed via the menus
 *   at the top of the screen.
 * 
 * Textures:
 * Place your "track.jpg", "brick.jpg", "head.jpg",
 * and "torso.jpg" files in the folder textures. 
 * These will then be loaded as the texture
 * objects track, bricks, head, and torso respectively.
 * Be aware, these objects are already defined and
 * cannot be used for other purposes. The texture
 * objects can be used as follows:
 * 
 * gl.glColor3f(1f, 1f, 1f);
 * Textures.track.bind(gl);
 * gl.glBegin(GL_QUADS);
 * gl.glTexCoord2d(0, 0);
 * gl.glVertex3d(0, 0, 0);
 * gl.glTexCoord2d(1, 0);
 * gl.glVertex3d(1, 0, 0);
 * gl.glTexCoord2d(1, 1);
 * gl.glVertex3d(1, 1, 0);
 * gl.glTexCoord2d(0, 1);
 * gl.glVertex3d(0, 1, 0);
 * gl.glEnd(); 
 * 
 * Note that it is hard or impossible to texture
 * objects drawn with GLUT. Either define the
 * primitives of the object yourself (as seen
 * above) or add additional textured primitives
 * to the GLUT object.
 */
public class RobotRace extends Base {
    
    /** Array of the four robots. */
    private final Robot[] robots;
    
    /** Instance of the camera. */
    private final Camera camera;
    
    /** Instance of the race track. */
    private final RaceTrack[] raceTracks;
    
    /** Instance of the terrain. */
    private final Terrain terrain;
        
    /**
     * Constructs this robot race by initializing robots,
     * camera, track, and terrain.
     */
    public RobotRace() {
        
        // Create a new array of four robots
        robots = new Robot[4];
        
        // Initialize robot 0
        robots[0] = new Robot(Material.GOLD
                
        );
        
        // Initialize robot 1
        robots[1] = new Robot(Material.SILVER
              
        );
        
        // Initialize robot 2
        robots[2] = new Robot(Material.WOOD
              
        );

        // Initialize robot 3
        robots[3] = new Robot(Material.ORANGE
                
        );
        
        // Initialize the camera
        camera = new Camera();
        
        // Initialize the race tracks
        raceTracks = new RaceTrack[2];
        
        // Track 1
        raceTracks[0] = new ParametricTrack();
        
        // Track 2
        float g = 3.5f;
        raceTracks[1] = new BezierTrack(
                
            new Vector[] {}
       
        );
        
        // Initialize the terrain
        terrain = new Terrain();
    }
    
    /**
     * Called upon the start of the application.
     * Primarily used to configure OpenGL.
     */
    @Override
    public void initialize() {
		
        // Enable blending.
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                
        // Enable depth testing.
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS);
		
        // Enable face culling for improved performance
        // gl.glCullFace(GL_BACK);
        // gl.glEnable(GL_CULL_FACE);
        
	    // Normalize normals.
        gl.glEnable(GL_NORMALIZE);
        
	// Try to load four textures, add more if you like in the Textures class         
        Textures.loadTextures();
        reportError("reading textures");
        
        // Try to load and set up shader programs
        ShaderPrograms.setupShaders(gl, glu);
        reportError("shaderProgram");
        
    }
   
    /**
     * Configures the viewing transform.
     */
    @Override
    public void setView() {
        // Select part of window.
        gl.glViewport(0, 0, gs.w, gs.h);
        
        // Set projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        // Set the perspective.
        glu.gluPerspective(45, (float)gs.w / (float)gs.h, 0.1*gs.vDist, 10*gs.vDist);
        
        // Set camera.
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
        
        // Add light source
        gl.glLightfv(GL_LIGHT0, GL_POSITION, new float[]{0f,0f,0f,1f}, 0);
               
        // Update the view according to the camera mode and robot of interest.
        // For camera modes 1 to 4, determine which robot to focus on.
        camera.update(gs, robots[0]);
        glu.gluLookAt(camera.eye.x(),    camera.eye.y(),    camera.eye.z(),
                      camera.center.x(), camera.center.y(), camera.center.z(),
                      camera.up.x(),     camera.up.y(),     camera.up.z());
    }
    
    /**
     * Draws the entire scene.
     */
    @Override
    public void drawScene() {
        
        gl.glUseProgram(defaultShader.getProgramID());
        reportError("program");
        
        // Background color.
        gl.glClearColor(1f, 1f, 1f, 0f);
        
        // Clear background.
        gl.glClear(GL_COLOR_BUFFER_BIT);
        
        // Clear depth buffer.
        gl.glClear(GL_DEPTH_BUFFER_BIT);
        
        // Set color to black.
        gl.glColor3f(0f, 0f, 0f);
        
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        

    // Draw hierarchy example.
        //drawHierarchy();
        
        // Draw the axis frame.
        if (gs.showAxes) {
            drawAxisFrame();
        }
        
        // Draw the (first) robot.
        gl.glUseProgram(robotShader.getProgramID());
        
        Color[] noob = new Color[] { Color.BLACK, Color.GREEN, Color.YELLOW, Color.PINK};
        Vector P;
        Vector T;
        for(int i = 0; i < 4; i++) {
            gl.glPushMatrix();
                
                gl.glColor3fv(noob[i].getRGBColorComponents(null), 0);
                P = raceTracks[gs.trackNr].getLanePoint(i, (0.1 * gs.tAnim) % 1); // 
                T = raceTracks[gs.trackNr].getLaneTangent(i, (0.1 * gs.tAnim) % 1); 
                gl.glTranslated(P.x, P.y, P.z + 2);

                
                
                Vector rotationCross = Vector.X.cross(T);
                if (rotationCross.z > 0) {
                    gl.glRotated(Math.acos( Vector.X.dot(T) ) * (180/Math.PI),0,0,1);
                }
                else {
                    gl.glRotated(-Math.acos( Vector.X.dot(T) ) * (180/Math.PI),0,0,1);
                }
                
                
                robots[i].draw(gl, glu, glut, gs.tAnim);
            gl.glPopMatrix();
            
            gl.glPushMatrix();
            // Set line width to a better visible value
            gl.glLineWidth(2.5f);
            gl.glBegin(GL_LINES);
            gl.glVertex3d(P.add(T).x, P.add(T).y, 2);
            gl.glVertex3d(P.subtract(T).x, P.subtract(T).y, 2);
            gl.glEnd();
            gl.glPopMatrix();
        }
   
        robots[0].draw(gl, glu, glut, 0);
        
        // Draw the race track.
        gl.glUseProgram(trackShader.getProgramID());
        raceTracks[gs.trackNr].draw(gl, glu, glut);
        reportError("robot:");
        
        // Draw the terrain.
        gl.glUseProgram(terrainShader.getProgramID());
        terrain.draw(gl, glu, glut);
        reportError("terrain:");
        
        
    }

    public void drawAxisFrame() {
        drawArrow(Vector.O, Vector.X, Color.RED); // draw X-axis arrow
        drawArrow(Vector.O, Vector.Y, Color.GREEN); // draw Y-axis arrow
        drawArrow(Vector.O, Vector.Z, Color.BLUE); // draw Z-axis arrow
        drawOriginSphere(); // draw yellow sphere centered around origin
    }

    private void drawArrow(Vector fromVertex, Vector toVertex, Color arrowColor) {
        gl.glPushMatrix();
        // May of course be changed to drawArrowHeads() as well if preferred - pop & push matrix not needed in that case
        drawArrowCone(toVertex, arrowColor);
        // Pop & push matrix again since drawing the arrow cone involves rotation + translation
        gl.glPopMatrix();

        gl.glPushMatrix();

        // Set line width to a better visible value
        gl.glLineWidth(2.5f);

        /*
         * Start drawing lines between specified vertices, GL_LINES treats every pair of vertices as a line segment.
         * Vertices 2n - 1 and 2n define a line n.
         * N / 2 lines are drawn where N is the total amount of vertices specified.
         *
         * Note on 3fv: second parameter i specifies offset index for xyz, e.g. x = i, y = i+1, z = i+2.
        */
        gl.glBegin(GL_LINES);

        // Color line between specified vertices based on param arrowColor (r/g/b for x/y/z in case of axis arrow)
        gl.glColor3fv(arrowColor.getRGBColorComponents(null), 0);

        // Specify points from (in case of axis arrow - from origin to (x or y or z) + 1)
        gl.glVertex3d(fromVertex.x(), fromVertex.y(), fromVertex.z());
        gl.glVertex3d(toVertex.x(), toVertex.y(), toVertex.z());

        // Ending delimiter for line vertices specification
        gl.glEnd();

        // Pop current matrix before drawing lines
        gl.glPopMatrix();
    }

    /**
     * Draws the origin-sphere, a solid yellow sphere centered around the origin of the axis-frame.
     */
    private void drawOriginSphere() {
        // Color origin sphere yellow
        gl.glColor3d(1.0,1.0,0.0); // yellow (RGb)

        /*
         * Draw a sphere centered around the origin with radius 0.1 (10% of the axis frame) and 10 slices (latitude)
         * + stacks (longitude).
         */
        glut.glutSolidSphere(0.1, 10, 10);
    }

    public void drawArrowCone(Vector axisEndPoint, Color color) {
        // Color cone to given color
        gl.glColor3fv(color.getRGBComponents(null), 0);

        // Translate cone from origin to axisEndPoint
        gl.glTranslated(axisEndPoint.x(), axisEndPoint.y(), axisEndPoint.z());

        // Rotate cone 90 degrees around correct axis to point it in the axis direction
        if(axisEndPoint.x() == 1) {
            gl.glRotated(90, 0, 1, 0); // Rotate 90 degrees counter clockwise around Y-axis
        } else if(axisEndPoint.y() == 1) {
            gl.glRotated(-90, 1, 0, 0); // Rotate 90 degrees clockwise around X-axis
        }

        // Cone -> base, height, slices (latitude), stacks (longitude)
        glut.glutSolidCone(0.03, 0.15,10, 10);
    }

    /**
     * Draws arrow head from given axisEndPoint with given length & width in given (rgb) color.
     *
     * Vectors/vertices involved:
     *
     * Vector V - unit vector from origin to the axisEndPoint
     * Vector U - vector perpendicular to the axis (axisEndPoint)
     * Vertex M - midpoint of the arrowhead (axisEndPoint + (V * length))
     * Vertex C - left/right side of arrow head, connected with M and axisEndPoint (M - (U * (width/2)))
     * Vertex D - left/right side of arrow head, connected with M and axisEndPoint (M + (U * (width/2)))
     *
     * @param axisEndPoint
     * @param length
     * @param width
     * @param color
     */
    public void drawAxisArrowHeads(Vector axisEndPoint, double length, double width, Color color) {
        gl.glColor3fv(color.getRGBComponents(null), 0);

        gl.glBegin(GL_LINES);

        Vector V, U, M, C, D;

        V = (Vector.O.subtract(axisEndPoint)).normalized();

        if(axisEndPoint.x() == 1) {
            U = new Vector(0,0,1);
        } else {
            U = new Vector(1,0,0);
        }

        M = axisEndPoint.add(V.scale(length));

        C = M.subtract(U.scale(width/2.0));
        D = M.add(U.scale(width/2.0));

        gl.glVertex3d(axisEndPoint.x(), axisEndPoint.y(), axisEndPoint.z());
        gl.glVertex3d(C.x(), C.y(), C.z());

        gl.glVertex3d(axisEndPoint.x(), axisEndPoint.y(), axisEndPoint.z());
        gl.glVertex3d(D.x(), D.y(), D.z());

        gl.glEnd();

    }
 
    /**
     * Drawing hierarchy example.
     * 
     * This method draws an "arm" which can be animated using the sliders in the
     * RobotRace interface. The A and B sliders rotate the different joints of
     * the arm, while the C, D and E sliders set the R, G and B components of
     * the color of the arm respectively. 
     * 
     * The way that the "arm" is drawn (by calling {@link #drawSecond()}, which 
     * in turn calls {@link #drawThird()} imposes the following hierarchy:
     * 
     * {@link #drawHierarchy()} -> {@link #drawSecond()} -> {@link #drawThird()}
     */
    private void drawHierarchy() {
        gl.glColor3d(gs.sliderC, gs.sliderD, gs.sliderE);
        gl.glPushMatrix(); 
            gl.glScaled(2, 1, 1);
            glut.glutSolidCube(1);
            gl.glScaled(0.5, 1, 1);
            gl.glTranslated(1, 0, 0);
            gl.glRotated(gs.sliderA * -90.0, 0, 1, 0);
            drawSecond();
        gl.glPopMatrix();
    }
    
    private void drawSecond() {
        gl.glTranslated(1, 0, 0);
        gl.glScaled(2, 1, 1);
        glut.glutSolidCube(1);
        gl.glScaled(0.5, 1, 1);
        gl.glTranslated(1, 0, 0);
        gl.glRotated(gs.sliderB * -90.0, 0, 1, 0);
        drawThird();
    }
    
    private void drawThird() {
        gl.glTranslated(1, 0, 0);
        gl.glScaled(2, 1, 1);
        glut.glutSolidCube(1);
    }
    
    
    /**
     * Main program execution body, delegates to an instance of
     * the RobotRace implementation.
     */
    public static void main(String args[]) {
        RobotRace robotRace = new RobotRace();
        robotRace.run();
    }
}
