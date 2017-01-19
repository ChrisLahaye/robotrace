package robotrace;


import java.awt.*;

import static java.lang.Math.sin;
import static javax.media.opengl.GL.GL_LINES;

import static javax.media.opengl.GL2.*;
import static robotrace.ShaderPrograms.*;

import javax.media.opengl.GL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_FRONT_AND_BACK;
import static javax.media.opengl.GL.GL_NEAREST;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_WRAP_S;
import static javax.media.opengl.GL.GL_TEXTURE_WRAP_T;
import javax.media.opengl.GL2ES1;
import  javax.media.opengl.GL2GL3;
import static javax.media.opengl.GL2GL3.GL_FILL;
import javax.media.opengl.GLAutoDrawable;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

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
    
    /** Render-to-texture variables. */
    private int[] texID = {0};
    private int[] fboID = {0};
    private int[] depthID = {0};
    private int fboTexSize = 512;
    private int width = 1024;
    private int height = 768; 
     
    /**
     * Constructs this robot race by initializing robots,
     * camera, track, and terrain.
     */
    public RobotRace() {
        
        // Create a new array of four robots
        robots = new Robot[4];
        
        // Initialize robot 0
        robots[0] = new Robot(Material.GOLD, 0, 6
                
        );
        
        // Initialize robot 1
        robots[1] = new Robot(Material.SILVER, 1, 3
              
        );
        
        // Initialize robot 2
        robots[2] = new Robot(Material.WOOD, 2, 3
              
        );

        // Initialize robot 3
        robots[3] = new Robot(Material.ORANGE, 3, 3
                
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
            new Vector[] {
                
                    
new Vector(3, 16, 1),
new Vector(-8, 17, 1),
new Vector(-14, 14.6, 1),
new Vector(-15.5, 10.5, 1),

new Vector(-15.5, 10.5, 1), 
new Vector(-17, 7, 1),
new Vector(-15.5, 4, 1),
new Vector(-14, 0.5, 1),

new Vector(-14, 0.5, 1), 
new Vector(-12.5, -2, 1),
new Vector(-11.5, -5, 1),
new Vector(-12, -8.5, 1),

new Vector(-12, -8.5, 1),
new Vector(-13.5, -12, 1),
new Vector(-12.5, -14.5, 1),
new Vector(-3.5, -17.5, 1),

new Vector(-3.5, -17.5, 1),
new Vector(1.5, -19, 1),
new Vector(4.5, -17,1),
new Vector(7.5, -15, 1),

new Vector(7.5, -15, 1),
new Vector(11,-11,1),
new Vector(12, -7.5, 1),
new Vector(10,-5, 1),

new Vector(10,-5, 1),
new Vector(7, -2.5, 1),
new Vector(7.5, 2, 1),
new Vector(11.5,4.5,1),

new Vector(11.5,4.5,1),
new Vector(14,6,1),
new Vector(16,8,1),
new Vector(14,12,1),

new Vector(14,12,1),
new Vector(12,16,1),
new Vector(6, 17, 1),
new Vector(3, 16, 1)

            }
        );
        
        
        
        // Initialize the terrain
        terrain = new Terrain(gs);
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
        
        initializeFob();

        gs.vDist = 30;
        gs.phi = 0.2f;
    }
    
    private void initializeFob() {
         // create a frame buffer object
        gl.glGenFramebuffers(1, fboID, 0);

        // bind the frame buffer
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fboID[0]);

        // Generate render texture
        gl.glGenTextures (1, texID, 0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texID[0]);

        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
        int level = 0;

        ByteBuffer buffer = ByteBuffer.allocateDirect(fboTexSize * fboTexSize * 4);
        buffer.order(ByteOrder.nativeOrder());

        gl.glTexImage2D(GL.GL_TEXTURE_2D, level, GL.GL_RGBA, fboTexSize, fboTexSize, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, buffer);
        gl.glTexEnvf(GL2ES1.GL_TEXTURE_ENV, GL2ES1.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);

        // Attach the texture to the fbo
        gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, GL.GL_TEXTURE_2D, texID[0], level);

        // generate a renderbuffer for the depth buffer
        gl.glGenRenderbuffers(1, depthID, 0);
        gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, depthID[0]);
        gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL2GL3.GL_DEPTH_COMPONENT, fboTexSize, fboTexSize);

        // Attach the depth buffer to the fbo
        gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT,
                             GL.GL_RENDERBUFFER, depthID[0]);

        // unbind texture
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
        // unbind fbo
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0); 
        
        int status = gl.glCheckFramebufferStatus(GL_FRAMEBUFFER);
        switch(status) {
            case GL.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT: 
                throw new RuntimeException("Framebuffer incomplete, incomplete attachment");
            case GL.GL_FRAMEBUFFER_UNSUPPORTED:
                throw new RuntimeException("Unsupported framebuffer format");
            case GL.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
                throw new RuntimeException("Framebuffer incomplete, missing attachment");
            case GL2GL3.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
                throw new RuntimeException("Framebuffer incomplete, missing draw buffer");
            case GL2GL3.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER: 
                throw new RuntimeException("Framebuffer incomplete, missing read buffer");
        }
    }
    
    private void renderToTexture() {
        int camMode = gs.camMode;
        float vDist = gs.vDist;
        gs.camMode = 1;
        gs.vDist = 17f;
        
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fboID[0]);
    
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
        
        drawScene();
        
        // unbind frame buffer
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
        
        gs.camMode = camMode;
        gs.vDist = vDist;
    }
    
    
    
 
    /**
     * Configures the viewing transform.
     */
    @Override
    public void setView() {
        renderToTexture();
        
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

        robots[0].height = ((float)(sin(gs.tAnim) + 1 ) / 2 + 1) * 3;
        gl.glUseProgram(defaultShader.getProgramID());
        reportError("program");

        // Draw hierarchy example.
        //drawHierarchy();
        // Background color.
        gl.glClearColor(1f, 1f, 1f, 0f);
        
        // Clear background.
        gl.glClear(GL_COLOR_BUFFER_BIT);
        
        // Clear depth buffer.
        gl.glClear(GL_DEPTH_BUFFER_BIT);
        
        // Set color to black.
        gl.glColor3f(0f, 0f, 0f);
        
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        
        drawTelevision();
        
        // Draw the axis frame.
        if (gs.showAxes) {
            drawAxisFrame();
        }
        
        // Draw the (first) robot.
        gl.glUseProgram(robotShader.getProgramID());
        for(int i = 0; i < 4; i++) {
            double t = (0.05 * (i + 0.2) * gs.tAnim) % 1;
            robots[i].position = raceTracks[gs.trackNr].getLanePoint(robots[i].lane, t);
            robots[i].direction = raceTracks[gs.trackNr].getLaneTangent(robots[i].lane, t);
            robots[i].draw(gl, glu, glut, gs.tAnim, true);
            
            boolean canMoveLeft = robots[i].lane > 0; // The robot could move to the left if it is not already on the inner most lane.
            for(int j = 0; j < 4; j++) { // Check the positions of the other robots
                if (i == j) continue;
                
                double distance = Math.abs(robots[i].position.subtract(robots[j].position).length());
                if (distance < 4 && robots[i].lane - 1 == robots[j].lane) {
                    // The lane on the next is already taken and a switch is not possible
                    canMoveLeft = false;
                }
                
                if (distance < 4 && robots[i].lane == robots[j].lane) {
                    // The fastest robot must move to the right or they hit eachother
                    int movingRobot = i > j ? i : j; // Higher number walks faster
                    robots[movingRobot].lane++;
                }
            }
            
            if(canMoveLeft) {
                robots[i].lane--;
            }
        }
   
        robots[0].draw(gl, glu, glut, 0);
        
        // Draw the race track.
        gl.glUseProgram(trackShader.getProgramID());
        raceTracks[gs.trackNr].draw(gl, glu, glut, Material.WOOD);
        reportError("robot:");
                
        // Draw the terrain.
        gl.glUseProgram(terrainShader.getProgramID());
        terrain.draw(gl, glu, glut);
        reportError("terrain:");
    }
    
    public void drawTelevision() {
        //Vector P = raceTracks[gs.trackNr].getLanePoint(4, 0); 
        //Vector T = raceTracks[gs.trackNr].getLaneTangent(4,0);

        gl.glUseProgram(terrainShader.getProgramID());
        Textures.pole.bind(gl);

        gl.glPushMatrix();

        gl.glTranslated(12.5, 0, -1);

        // Draw pole carrying screen (bottom of pole centered at origin)
        gl.glPushMatrix();
        gl.glTranslated(0.5, 0, 0);
        glut.glutSolidCylinder(0.5, 10, 10, 10);
        gl.glPopMatrix();

        // Enable standard textures
        gl.glUseProgram(0);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texID[0]);
        gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);

        // Draw screen side one
        gl.glPushMatrix();
            gl.glTranslated(1, 0, 11.5);
            gl.glScaled(0,8,3);

            gl.glBegin(GL.GL_TRIANGLE_STRIP);

                gl.glTexCoord2d(0, 0);
                gl.glVertex3d(-0.5, -0.5, -0.5);
                gl.glTexCoord2d(0, 1);
                gl.glVertex3d(-0.5, -0.5, 0.5);
                gl.glTexCoord2d(1, 0);
                gl.glVertex3d(-0.5, 0.5, -0.5);
                gl.glTexCoord2d(1, 1);
                gl.glVertex3d(-0.5, 0.5, 0.5);

            gl.glEnd();

        gl.glPopMatrix();

        // Draw screen side two
        gl.glPushMatrix();
            gl.glTranslated(0,0,11.5);
            gl.glScaled(0,8,3);

            gl.glBegin(GL.GL_TRIANGLE_STRIP);

                gl.glTexCoord2d(0, 0);
                gl.glVertex3d(-0.5, -0.5, -0.5);
                gl.glTexCoord2d(0, 1);
                gl.glVertex3d(-0.5, -0.5, 0.5);
                gl.glTexCoord2d(1, 0);
                gl.glVertex3d(-0.5, 0.5, -0.5);
                gl.glTexCoord2d(1, 1);
                gl.glVertex3d(-0.5, 0.5, 0.5);

            gl.glEnd();

        gl.glPopMatrix();

        gl.glDisable(GL.GL_TEXTURE_2D);

        // Draw "billboard" holding the screen sides
        gl.glPushMatrix();
        gl.glTranslated(0.5, 0, 11.5);
        gl.glScaled(1, 8, 3);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        gl.glPopMatrix();

    }

    public void drawAxisFrame() {
        drawArrow(Vector.O, Vector.X, Color.RED); // draw X-axis arrow
        drawArrow(Vector.O, Vector.Y, Color.GREEN); // draw Y-axis arrow
        drawArrow(Vector.O, Vector.Z, Color.BLUE); // draw Z-axis arrow
        drawOriginSphere(); // draw yellow sphere centered around origin
    }

    private void drawArrow(Vector fromVertex, Vector toVertex, Color arrowColor) {
        gl.glPushMatrix();
        gl.glScaled(1, 1, 1);
        // May of course be changed to drawArrowHeads() as well if preferred - pop & push matrix not needed in that case
        drawArrowCone(toVertex, arrowColor);
        // Pop & push matrix again since drawing the arrow cone involves rotation + translation
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glScaled(1, 1, 1);

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
    public void drawArrowHeads(Vector axisEndPoint, double length, double width, Color color) {
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
