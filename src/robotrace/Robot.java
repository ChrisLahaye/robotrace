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
    public void draw(GL2 gl, GLU glu, GLUT glut, float tAnim) {
        double rotationAngle = Math.acos( Vector.X.dot(direction) ) * (180/Math.PI);
        Color[] colors = new Color[] {Color.BLUE, Color.ORANGE, Color.RED, Color.LIGHT_GRAY};

        if (Vector.X.cross(direction).z <= 0) {
            rotationAngle *= -1;
        }

        // LEGS
        gl.glPushMatrix();
        gl.glColor3fv(Color.GRAY.getRGBComponents(null), 0);
        gl.glTranslated(position.x, position.y, position.z+5);
        gl.glRotated(rotationAngle, 0, 0, 1);
        gl.glScaled(0.5, 1, 1);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        // TORSO
        gl.glPushMatrix();

        gl.glTranslated(position.x, position.y, position.z + 0.5);
        gl.glRotated(rotationAngle, 0, 0, 1);
        gl.glScaled(0.5,1,2);
        gl.glColor3fv(Color.CYAN.getRGBComponents(null), 0);
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
        Textures.head.bind(gl);

        gl.glPopMatrix();

        // HEAD
        gl.glPushMatrix();

            gl.glLineWidth(2.5f);
            gl.glBegin(GL_LINES);
                gl.glVertex3d(position.add(direction).x, position.add(direction).y, 2);
                gl.glVertex3d(position.x, position.y, 2);
            gl.glEnd();

            gl.glTranslated(position.x, position.y, position.z + 2);
            gl.glRotated(rotationAngle,0,0,1);
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

            Textures.torso.bind(gl);

        gl.glPopMatrix();
    }
}
