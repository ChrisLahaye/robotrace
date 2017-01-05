
package robotrace;

/**
 * Implementation of RaceTrack, creating a track from a parametric formula
 */
public class ParametricTrack extends RaceTrack {
    
    @Override
    protected Vector getPoint(double t) {
        // P(t) = (10 cos(2pi*t); 14 sin(2pi*t); 1):
        return new Vector(10 * Math.cos(2 * Math.PI * t), 14 * Math.sin(2 * Math.PI * t), 1);
    }

    @Override
    protected Vector getTangent(double t) {
        Vector P = getPoint(t);
        
        // Suppose P is a point on the parametric track, in other words
        // a point on a circle in the z=1 plane. Since the circle
        // is centered around O, the unit vector from O to P is the normal to P.
        // To get the tangent to this direction, we take the cross product with
        // any other vector, and optionally translate it to P. Now we have one
        // one of the following
        
        //        x  x                   x  x
        //     x        x             x        x      x
        //    x          x           x          x  x
        //    x          x x         x          x
        //     x        x    x        x        x
        //        x  x                   x  x
        
        return getPoint(t).normalized().cross(Vector.Z);
    }
    
}
