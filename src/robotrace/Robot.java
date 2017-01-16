package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.glsl.ShaderState;
import jogamp.opengl.glu.GLUquadricImpl;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import java.awt.*;
import java.nio.FloatBuffer;

import static javax.media.opengl.GL2.*;
import static robotrace.ShaderPrograms.robotShader;

/**
* Represents a Robot, to be implemented according to the Assignments.
*/
class Robot {

    /** The position of the robot. */
    public Vector position = new Vector(0, 0, 0);

    /** The direction in which the robot is running. */
    public Vector direction = new Vector(1, 0, 0);

    /** The material from which this robot is built. */
    private final Material material;

    public int lane;
    
    public double nextLaneSwitchAllowed = 0;
    

    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material, int lane

    ) {
        this.material = material;
        this.lane = lane;

    }

    /**
     * Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, float tAnim, boolean enableOrientationLine) {
        double rotationAngle = Math.acos( Vector.X.dot(direction) ) * (180/Math.PI);

        if (Vector.X.cross(direction).z <= 0) {
            rotationAngle *= -1;
        }

        if(enableOrientationLine) {
            drawOrientationLine(gl, glut);
        }

        gl.glPushMatrix();

        setMaterialPropertiesForRobot(gl);

        placeAndOrientRobotOnTrack(gl, rotationAngle);

        Textures.head.bind(gl);
        drawCubeHead(gl, glut);

        Textures.torso.bind(gl);
        drawTorso(gl, glut);
        drawArms(gl, glut, tAnim);

        Textures.legs.bind(gl);
        drawLegs(gl, glut, tAnim);

        gl.glPopMatrix();

    }

    /**
     * Set correct material properties for robot based on provided material.
     * Used for lighting calculations in shader.
     * @param gl
     */
    private void setMaterialPropertiesForRobot(GL2 gl) {
        gl.glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, FloatBuffer.wrap(material.diffuse));
        gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, FloatBuffer.wrap(material.specular));
        gl.glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, material.shininess);
    }

    /**
     * Translate (move robot to track) and rotate for correct orientation.
     * @param gl
     * @param rotationAngle
     */
    private void placeAndOrientRobotOnTrack(GL2 gl, double rotationAngle) {
        gl.glTranslated(position.x, position.y, position.z + 1);
        gl.glRotated(rotationAngle, 0, 0, 1);
    }

    private void drawArms(GL2 gl, GLUT glut, float tAnim) {
        double armMovement = ((Math.sin(((tAnim + 1) * 10)) * (180/Math.PI)) / 3.5);

        gl.glPushMatrix();

        gl.glRotated(-armMovement, 0, 1, 0);

        // LEFT LOWER ARM
        gl.glPushMatrix();
        gl.glTranslated(0,  0.7, -.11);
        gl.glScaled(0.5, 0.325, 0.5);

        gl.glRotated(112.5, 0, 1, 0); // initial rotation to orient lower arm correctly

        glut.glutSolidCylinder(0.25, 0.775, 10, 10);

        gl.glPopMatrix();

        // LEFT ELBOW
        gl.glPushMatrix();

        gl.glTranslated(0,  0.7, -.1);
        gl.glScaled(0.5, 0.375, 0.425);

        glut.glutSolidSphere(0.25, 10, 10);

        gl.glPopMatrix();

        // LEFT UPPER ARM
        gl.glPushMatrix();

        gl.glTranslated(0,  0.7, -.05);
        gl.glScaled(0.5, 0.325, 0.5);

        glut.glutSolidCylinder(0.25, 0.775, 10, 10);

        gl.glPopMatrix();

        // LEFT SHOULDER
        gl.glPushMatrix();

        gl.glTranslated(0,0.375,0.325);
        gl.glScaled(0.346, 0.855, 0.325);

        glut.glutSolidSphere(0.5, 10, 10);

        gl.glPopMatrix();

        gl.glPopMatrix();

        gl.glPushMatrix();

        gl.glRotated(armMovement, 0, 1, 0);

        // RIGHT LOWER ARM
        gl.glPushMatrix();

        gl.glTranslated(0,  -0.7, -.11);
        gl.glScaled(0.5, 0.325, 0.5);

        gl.glRotated(112.5, 0, 1, 0); // initial rotation to orient lower arm correctly

        glut.glutSolidCylinder(0.25, 0.775, 10, 10);

        gl.glPopMatrix();

        // RIGHT ELBOW
        gl.glPushMatrix();

        gl.glTranslated(0,  -0.7, -.1);
        gl.glScaled(0.5, 0.375, 0.425);

        glut.glutSolidSphere(0.25, 10, 10);

        gl.glPopMatrix();

        // RIGHT UPPER ARM
        gl.glPushMatrix();

        gl.glTranslated(0,  -0.7, -.05);
        gl.glScaled(0.5, 0.325, 0.5);

        glut.glutSolidCylinder(0.25, 0.775, 10, 10);

        gl.glPopMatrix();

        // RIGHT SHOULDER
        gl.glPushMatrix();

        gl.glTranslated(0,-0.375,0.325);
        gl.glScaled(0.346, 0.855, 0.325);

        glut.glutSolidSphere(0.5, 10, 10);

        gl.glPopMatrix();

        gl.glPopMatrix();
    }

    private void drawLegs(GL2 gl, GLUT glut, float tAnim) {
        gl.glPushMatrix();

        double legMovement = ((Math.sin(((tAnim + 1) * 10)) * (180/Math.PI)) / 4);

        // LEFT LEG
        gl.glRotated(-legMovement, 0, 1, 0);
        gl.glTranslated(0,0.275,-1);
        gl.glScaled(0.5, 0.45, 1);

        glut.glutSolidCube(1);

        // LEFT LEG TEXTURE QUAD
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);

        gl.glMultiTexCoord2d(GL_TEXTURE0, 0, 0);
        gl.glVertex3d(0.51, -0.51, -0.5);
        gl.glMultiTexCoord2d(GL_TEXTURE0, 0, 1);
        gl.glVertex3d(0.51, -0.51, 0.5);
        gl.glMultiTexCoord2d(GL_TEXTURE0, 1, 0);
        gl.glVertex3d(0.51, 0.51, -0.5);
        gl.glMultiTexCoord2d(GL_TEXTURE0, 1, 1);
        gl.glVertex3d(0.51, 0.51, 0.5);

        gl.glEnd();

        gl.glPopMatrix();

        gl.glPushMatrix();

        // RIGHT LEG
        gl.glRotated(legMovement, 0, 1, 0);
        gl.glTranslated(0,-0.275,-1);
        gl.glScaled(0.5, 0.45, 1);

        glut.glutSolidCube(1);

        // RIGHT LEG TEXTURE QUAD
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);

        gl.glMultiTexCoord2d(GL_TEXTURE0, 0, 0);
        gl.glVertex3d(0.51, -0.51, -0.5);
        gl.glMultiTexCoord2d(GL_TEXTURE0, 0, 1);
        gl.glVertex3d(0.51, -0.51, 0.5);
        gl.glMultiTexCoord2d(GL_TEXTURE0, 1, 0);
        gl.glVertex3d(0.51, 0.51, -0.5);
        gl.glMultiTexCoord2d(GL_TEXTURE0, 1, 1);
        gl.glVertex3d(0.51, 0.51, 0.5);

        gl.glEnd();

        gl.glPopMatrix();
    }

    private void drawTorso(GL2 gl, GLUT glut) {
        gl.glPushMatrix();

        gl.glScaled(0.5,1,1);

        glut.glutSolidCube(1);

        // TORSO QUAD
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);

        gl.glMultiTexCoord2d(GL_TEXTURE0, 0, 0);
        gl.glVertex3d(0.51, -0.51, -0.5);
        gl.glMultiTexCoord2d(GL_TEXTURE0, 0, 1);
        gl.glVertex3d(0.51, -0.51, 0.5);
        gl.glMultiTexCoord2d(GL_TEXTURE0, 1, 0);
        gl.glVertex3d(0.51, 0.51, -0.5);
        gl.glMultiTexCoord2d(GL_TEXTURE0, 1, 1);
        gl.glVertex3d(0.51, 0.51, 0.5);

        gl.glEnd();

        gl.glPopMatrix();
    }

    private void drawSphereHead(GL2 gl, GLU glu, GLUT glut) {
        gl.glPushMatrix();

        gl.glTranslated(0, 0, 1);
        gl.glScaled(0.25, 0.25, 0.45);

        glut.glutSolidSphere(1, 25, 25);

        gl.glPopMatrix();

        // DRAW NECK
        gl.glPushMatrix();

        gl.glTranslated(0, 0, .6);
        gl.glScaled(0.5, 0.325, 0.5);

        glut.glutSolidCylinder(0.5, 0.775, 10, 10);

        gl.glPopMatrix();
    }

    private void drawCubeHead(GL2 gl, GLUT glut) {
        gl.glPushMatrix();

        gl.glTranslated(0, 0, 1);
        gl.glScaled(0.5,1,1);

        glut.glutSolidCube(1);

        // HEAD QUAD
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);

        gl.glMultiTexCoord2d(GL_TEXTURE0, 0, 0);
        gl.glVertex3d(0.51, -0.51, -0.5);
        gl.glMultiTexCoord2d(GL_TEXTURE0, 0, 1);
        gl.glVertex3d(0.51, -0.51, 0.5);
        gl.glMultiTexCoord2d(GL_TEXTURE0, 1, 0);
        gl.glVertex3d(0.51, 0.51, -0.5);
        gl.glMultiTexCoord2d(GL_TEXTURE0, 1, 1);
        gl.glVertex3d(0.51, 0.51, 0.5);

        gl.glEnd();

        gl.glPopMatrix();
    }

    private void drawOrientationLine(GL2 gl, GLUT glut) {
        gl.glPushMatrix();

        gl.glColor3fv(Color.RED.getRGBColorComponents(null), 0);

        gl.glLineWidth(2.5f);
        gl.glBegin(GL_LINES);
        gl.glVertex3d(position.add(direction).x, position.add(direction).y, 2);
        gl.glVertex3d(position.x, position.y, 2);
        gl.glEnd();

        gl.glPopMatrix();
    }
}
