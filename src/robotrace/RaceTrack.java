package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
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
    private final static float parametricInterval = 1f / 50;
    
    
    
    /**
     * Constructor for the default track.
     */
    public RaceTrack() {
    }


    
    /**
     * Draws this track, based on the control points.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        Vector P;
        
        for(float t = 0; t < 1; t += parametricInterval) {
            P = getPoint(t); // P.z = 1
            
            gl.glPushMatrix();
            // Translate square such that:
            //  x is centered around P.x
            //  y is centered around P.y
            //  z is centered around 0, so D(z) = [-1, 1]
            gl.glTranslated(P.x, P.y, 0);
            gl.glScaled(laneWidthTotal, laneWidthTotal, P.z * 2);
            
            glut.glutSolidCube(1);
            gl.glPopMatrix(); 
        }  
    }
    
    /**
     * Returns the center of a lane at 0 <= t < 1.
     * Use this method to find the position of a robot on the track.
     */
    public Vector getLanePoint(int lane, double t){

        // Tracks are centered around P(t),
        // so we the center of the middle lane is P(t).
        // The track ranges from P(t) - laneWidthTotal/2 to
        // P(t) + laneWidthTotal + 2
        Vector P = getPoint(t);
        
        return new Vector(
            P.x - laneWidthTotal/2 + (lane + 1 * laneWidth)/2,
            P.y - laneWidthTotal/2 + (lane + 1 * laneWidth)/2,
            P.z
        );

    }
    
    /**
     * Returns the tangent of a lane at 0 <= t < 1.
     * Use this method to find the orientation of a robot on the track.
     */
    public Vector getLaneTangent(int lane, double t){
        
        return Vector.O;

    }
    
    
    
    // Returns a point on the test track at 0 <= t < 1.
    protected abstract Vector getPoint(double t);

    // Returns a tangent on the test track at 0 <= t < 1.
    protected abstract Vector getTangent(double t);
}
