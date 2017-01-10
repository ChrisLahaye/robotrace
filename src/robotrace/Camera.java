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
        up = Vector.Z;
        eye = new Vector(
            Math.cos(gs.theta) * Math.cos(gs.phi),
            Math.cos(gs.theta) * Math.sin(gs.phi),
            Math.sin(gs.theta)
        ).scale(gs.vDist).add(center);
    }

    /**
     * Computes eye, center, and up, based on the first person mode.
     * The camera should view from the perspective of the robot.
     */
    private void setFirstPersonMode(GlobalState gs, Robot focus) {
        // Since focus.position x and y are the center of the robot,
        // we can not place the camera at this position, or else we are 
        // located inside the robat. Therefor we move outside in the robots
        // direction to get a position in front of the robot.
        // The robot's depth is 0.3, so we need to move at least 0.15000001
        // forward to be outside its body.
        Vector robotFront = focus.position.add(focus.direction.scale(0.2));
        
        eye = new Vector(robotFront.x, robotFront.y, 3.25); // Eye height is around 3.25
        center = eye.add(focus.direction.scale(1)); // Place the center in front of the eyes
        up = Vector.Z;
    }
}
