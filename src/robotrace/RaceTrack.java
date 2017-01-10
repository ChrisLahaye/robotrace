package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import static javax.media.opengl.GL.GL_LINES;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static javax.media.opengl.GL2.*;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
abstract class RaceTrack {
    
    /** The width of one lane. The total width of the track is 4 * laneWidth. */
    private final static float laneWidth = 1.22f;
    private final static float laneWidthTotal = 4 * laneWidth;
    private final static float parametricInterval = 1f / 100;
    
    
    
    /**
     * Constructor for the default track.
     */
    public RaceTrack() {
    }


    
    /**
     * Draws this track, based on the control points.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        for(float t = 0; t < 1; t += parametricInterval) {
            Vector P = getPoint(t); // P.z = 1
            Vector TN = getTangent(t).cross(Vector.Z).normalized(); // Normal on tangent
            
            Vector Pout = P.add(TN.scale(laneWidthTotal / 2)); // Point projected on track furthest from O
            Vector Pin = P.subtract(TN.scale(laneWidthTotal / 2)); // Point projected on track closest to O
               
            Vector Pnext = getPoint(t + parametricInterval);
            Vector Pnexttn = getTangent(t + parametricInterval).cross(Vector.Z).normalized(); // Normal on tangent of next point
            Vector Pnextout = Pnext.add(Pnexttn.scale(laneWidthTotal / 2)); // Point projected on track furthest from O
            Vector Pnextin = Pnext.subtract(Pnexttn.scale(laneWidthTotal / 2)); // Point projected on track closest to O
                 
            gl.glBegin(GL2.GL_TRIANGLE_STRIP);
                // https://en.wikipedia.org/wiki/Triangle_strip
                gl.glColor3f(0.1f, 0.2f, 0.3f);
                
                // Top horizontal triangle
                gl.glVertex3d(Pin.x, Pin.y, 1);
                gl.glVertex3d(Pout.x, Pout.y, 1);
                gl.glVertex3d(Pnextin.x, Pnextin.y, 1);
                gl.glVertex3d(Pnextout.x, Pnextout.y, 1);
                
                // Inside vertical triangle
                gl.glVertex3d(Pin.x, Pin.y, -1);
                gl.glVertex3d(Pin.x, Pin.y, 1);
                gl.glVertex3d(Pnextin.x, Pnextin.y, -1);
                gl.glVertex3d(Pnextin.x, Pnextin.y, 1);
                
                // Outside vertical triangle                
                gl.glVertex3d(Pout.x, Pout.y, -1);
                gl.glVertex3d(Pout.x, Pout.y, 1);
                gl.glVertex3d(Pnextout.x, Pnextout.y, -1);
                gl.glVertex3d(Pnextout.x, Pnextout.y, 1);
            gl.glEnd();
        }  
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
        Vector L2 = getLanePoint(lane, t + 0.001f);
        
        Vector T = new Vector(1, (L2.y - L1.y) / (L2.x - L1.x), 0).normalized();
        if (Vector.Z.cross(L1).x > 0) {
            return T;
        } else {
            return T.scale(-1);
        }
    }
    
    // Returns a point on the test track at 0 <= t < 1.
    protected abstract Vector getPoint(double t);

    // Returns a tangent on the test track at 0 <= t < 1.
    protected abstract Vector getTangent(double t);
}
