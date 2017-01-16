package robotrace;

/**
 * Implementation of a camera with a position and orientation. 
 */
class Camera {

    /** The position of the camera. */
    public Vector eye = new Vector(3f, 6f, 5f);

    /** The point to which the camera is looking. */
    public Vector center = Vector.O;

    /** The up vector. */
    public Vector up = Vector.Z;

    /**
     * Updates the camera viewpoint and direction based on the
     * selected camera mode.
     */
    public void update(GlobalState gs, Robot focus) {

        switch (gs.camMode) {
            
            // First person mode    
            case 1:
                setFirstPersonMode(gs, focus);
                break;
                
            // Default mode    
            default:
                setDefaultMode(gs);
        }
    }

    /**
     * Computes eye, center, and up, based on the camera's default mode.
     */
    private void setDefaultMode(GlobalState gs) {
        center = gs.cnt;
        eye = new Vector(
            Math.cos(gs.theta) * Math.cos(gs.phi),
            Math.sin(gs.theta) * Math.cos(gs.phi),
            Math.sin(gs.phi)
        ).scale(gs.vDist).add(center);
        up = Vector.Z;
    }

    /**
     * Computes eye, center, and up, based on the first person mode.
     * The camera should view from the perspective of the robot.
     */
    private void setFirstPersonMode(GlobalState gs, Robot focus) {
        up = Vector.Z;
        
        // Since focus.position x and y are the center of the robot,
        // we can not place the camera at this position, or else we are 
        // located inside the robot. Therefor we move outside in the robots
        // direction to get a position just in front of the robot.
        // The robot's depth is 0.3, so we need to move at least 0.15000001
        // forward to be outside its body.
        eye = focus.position.add(focus.direction.scale(0.151));
        
        // The center we locate it further away based on viewing distance,
        // but depends on the same principle.
        center = focus.position.add(focus.direction.scale(gs.vDist));
        
        // Since focus.position z is the bottom of the robot at the race track,
        // we want to reposition the eye and center around eye height.
        eye.z = center.z = 3.25;

        gs.vDist = 10;
    }
}
