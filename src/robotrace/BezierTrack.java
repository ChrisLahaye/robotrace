
package robotrace;

/**
 * Implementation of RaceTrack, creating a track from control points for a 
 * cubic Bezier curve
 */
public class BezierTrack extends RaceTrack {
    
    public Vector[] controlPoints;
    
    BezierTrack(Vector[] controlPoints) {
        this.controlPoints = controlPoints;
        this.parametricInterval = 1f / controlPoints.length;
    }
    
    private Vector getControlPoint(int i) {
        if (i >= this.controlPoints.length) {
            i = this.controlPoints.length - 1;
        }
        
        return controlPoints[i];
    }
    
    @Override
    protected Vector getPoint(double t) {
        int index = (int)Math.floor(t * (controlPoints.length - 1));
        Vector P0 = getControlPoint(index);
        Vector P1 = getControlPoint(index + 1);
        Vector P2 = getControlPoint(index + 2);
        Vector P3 = getControlPoint(index + 3);
        
        // P(t) = (1 - t)^3 * P0 + 3t(1-t)^2 * P1 + 3t^2 (1-t) * P2 + t^3 * P3
        return P0.scale(Math.pow(1 - t, 3))
                .add(P1.scale(3 * t * Math.pow(1 - t, 2)))
                .add(P2.scale(3 * Math.pow(t, 2) * (1 - t)))
                .add(P3.scale(Math.pow(t, 3)));
    }

    @Override
    protected Vector getTangent(double t) {
        int index = (int)Math.floor(t * (controlPoints.length - 1));
        Vector P0 = getControlPoint(index);
        Vector P1 = getControlPoint(index + 1);
        Vector P2 = getControlPoint(index + 2);
        Vector P3 = getControlPoint(index + 3);
        
        // dP(t) / dt =  -3(1-t)^2 * P0 + 3(1-t)^2 * P1 - 6t(1-t) * P1 - 3t^2 * P2 + 6t(1-t) * P2 + 3t^2 * P3 
        return P0.scale(-3 * Math.pow(1 - t, 2))
            .add(P1.scale(3 * Math.pow(1 - t, 2) - 6 * t * (1 - t)))
            .add(P2.scale(-3 * Math.pow(t, 2) + 6 * t * (1 - t)))
            .add(P3.scale(3 * Math.pow(t, 2))).normalized();
    }
}
