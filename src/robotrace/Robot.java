package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static javax.media.opengl.GL2.*;

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
        if (Vector.X.cross(direction).z <= 0) {
            rotationAngle *= -1;
        }
            
        gl.glPushMatrix();
            gl.glColor3d(0f,1,1f);
            gl.glLineWidth(2.5f);
            gl.glBegin(GL_LINES);
                gl.glVertex3d(position.add(direction).x, position.add(direction).y, 2);
                gl.glVertex3d(position.x, position.y, 2);
            gl.glEnd();
                
            gl.glTranslated(position.x, position.y, position.z + 2);
            gl.glRotated(rotationAngle,0,0,1);
            gl.glScaled(0.3, 1, 4);
            glut.glutSolidCube(1);
        gl.glPopMatrix();
    }
}
