package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
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



    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material

    ) {
        this.material = material;


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
            drawOrientationLine(gl, glut, rotationAngle);
        }

        gl.glPushMatrix();

        // Translate (move robot to track) + rotate while moving for correct orientation
        gl.glTranslated(position.x, position.y, position.z + 1);
        gl.glRotated(rotationAngle, 0, 0, 1);

        Textures.head.bind(gl);
        drawHead(gl, glut, rotationAngle);

        Textures.torso.bind(gl);
        drawTorso(gl, glut, rotationAngle);
        drawArms(gl, glut, rotationAngle, tAnim);

        Textures.legs.bind(gl);
        drawLegs(gl, glut, rotationAngle, tAnim);

        gl.glPopMatrix();

    }

    private void drawArms(GL2 gl, GLUT glut, double rotationAngle, float tAnim) {
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

    private void drawLegs(GL2 gl, GLUT glut, double rotationAngle, float tAnim) {
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

    private void drawTorso(GL2 gl, GLUT glut, double rotationAngle) {
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

    private void drawHead(GL2 gl, GLUT glut, double rotationAngle) {
        gl.glPushMatrix();

        gl.glTranslated(0, 0, 1);
        gl.glScaled(0.5,1,1);

        glut.glutSolidCube(1);

        // gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        // gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

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

    private void drawOrientationLine(GL2 gl, GLUT glut, double rotationAngle) {
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
