package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import static javax.media.opengl.GL.GL_LINES;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import java.nio.FloatBuffer;

import static javax.media.opengl.GL2.*;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
abstract class RaceTrack {
    
    /** The width of one lane. The total width of the track is 4 * laneWidth. */
    private final static float laneWidth = 1.22f;
    private final static float laneWidthTotal = 4 * laneWidth;
    
    protected float drawingInterval = 1f / 500;
    
    /**
     * Constructor for the default track.
     */
    public RaceTrack() {
    }
    
    /**
     * Draws this track, based on the control points.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, Material material) {
        gl.glPushMatrix();
        gl.glColor3f(0.1f, 0.2f, 0.3f);
        for(float t = 0; t < 1; t += drawingInterval) {
            Vector P = getPoint(t); // P.z = 1
            Vector TN = getTangent(t).cross(Vector.Z).normalized(); // Normal on tangent

            Vector Pout = P.add(TN.scale(laneWidthTotal / 2)); // Point projected on track furthest from O
            Vector Pin = P.subtract(TN.scale(laneWidthTotal / 2)); // Point projected on track closest to O
               
            Vector Pnext = getPoint(t + drawingInterval);
            Vector Pnexttn = getTangent(t + drawingInterval).cross(Vector.Z).normalized(); // Normal on tangent of next point
            Vector Pnextout = Pnext.add(Pnexttn.scale(laneWidthTotal / 2)); // Point projected on track furthest from O
            Vector Pnextin = Pnext.subtract(Pnexttn.scale(laneWidthTotal / 2)); // Point projected on track closest to O

            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, FloatBuffer.wrap(material.diffuse));
            gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, FloatBuffer.wrap(material.specular));
            gl.glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, material.shininess);

            gl.glLineWidth(2.5f);
            gl.glBegin(GL_LINES);
                Vector T = getTangent(t);
                gl.glColor3f(1f, 0f, 0f);
                // Tangent
                gl.glVertex3d(P.add(T).x, P.add(T).y, 2);
                gl.glVertex3d(P.subtract(T).x, P.subtract(T).y, 2);

                // Tangent Normal
                gl.glVertex3d(P.x, P.y, 2);
                gl.glVertex3d(P.add(TN).x, P.add(TN).y, 2);

                gl.glColor3f(0.1f, 0.2f, 0.3f);
            gl.glEnd();

            Textures.track.bind(gl);
            gl.glBegin(GL2.GL_TRIANGLE_STRIP); // https://en.wikipedia.org/wiki/Triangle_strip
            gl.glNormal3d(TN.x(), TN.y(), TN.z());

            // Top horizontal triangle
                gl.glMultiTexCoord2d(GL_TEXTURE0, 0, 0);
                gl.glVertex3d(Pin.x, Pin.y, 1);
                gl.glMultiTexCoord2d(GL_TEXTURE0, 1, 0);
                gl.glVertex3d(Pout.x, Pout.y, 1);
                gl.glMultiTexCoord2d(GL_TEXTURE0, 0, 1);
                gl.glVertex3d(Pnextin.x, Pnextin.y, 1);
                gl.glMultiTexCoord2d(GL_TEXTURE0, 1, 1);
                gl.glVertex3d(Pnextout.x, Pnextout.y, 1);
                gl.glEnd();

            Textures.brick.bind(gl);
            gl.glBegin(GL2.GL_TRIANGLE_STRIP); // https://en.wikipedia.org/wiki/Triangle_strip
            gl.glNormal3d(TN.x(), TN.y(), TN.z());

            // Inside vertical triangle
                gl.glMultiTexCoord2d(GL_TEXTURE0, 0, 0);
                gl.glVertex3d(Pin.x, Pin.y, -1);
                gl.glMultiTexCoord2d(GL_TEXTURE0, 0, 1);
                gl.glVertex3d(Pin.x, Pin.y, 1);
                gl.glMultiTexCoord2d(GL_TEXTURE0, 1, 0);
                gl.glVertex3d(Pnextin.x, Pnextin.y, -1);
                gl.glMultiTexCoord2d(GL_TEXTURE0, 1, 1);
                gl.glVertex3d(Pnextin.x, Pnextin.y, 1);
            gl.glEnd();

            gl.glBegin(GL2.GL_TRIANGLE_STRIP);
            gl.glNormal3d(TN.x(), TN.y(), TN.z());

            // Outside vertical triangle
                gl.glMultiTexCoord2d(GL_TEXTURE0, 0, 0);
                gl.glVertex3d(Pout.x, Pout.y, -1);
                gl.glMultiTexCoord2d(GL_TEXTURE0, 0, 1);
                gl.glVertex3d(Pout.x, Pout.y, 1);
                gl.glMultiTexCoord2d(GL_TEXTURE0, 1, 0);
                gl.glVertex3d(Pnextout.x, Pnextout.y, -1);
                gl.glMultiTexCoord2d(GL_TEXTURE0, 1, 1);
                gl.glVertex3d(Pnextout.x, Pnextout.y, 1);
            gl.glEnd();
        }  
        gl.glPopMatrix();
    }
    
    /**
     * Returns the center of a lane at 0 <= t < 1.
     * Use this method to find the position of a robot on the track.
     */
    public Vector getLanePoint(int lane, double t){
        Vector P = getPoint(t);
        Vector TN = getTangent(t).cross(Vector.Z).normalized(); // Normal on tangent
        
        Vector Pin = P.subtract(TN.scale(laneWidthTotal / 2)); // Point projected on track closest to O
        Vector L = Pin.add(TN.scale( (lane + 0.5) * laneWidth)); // Point at the center of the lane
                       
        return new Vector(L.x, L.y, P.z);
    }
    
    /**
     * Returns the tangent of a lane at 0 <= t < 1.
     * Use this method to find the orientation of a robot on the track.
     */
    public Vector getLaneTangent(int lane, double t){
        // Compute tangent between two near points
        Vector L1 = getLanePoint(lane, t);
        Vector L2 = getLanePoint(lane, t + 0.001);
        return L2.subtract(L1).normalized();
    }
    
    // Returns a point on the test track at 0 <= t < 1.
    protected abstract Vector getPoint(double t);

    // Returns a tangent on the test track at 0 <= t < 1.
    protected abstract Vector getTangent(double t);
}
