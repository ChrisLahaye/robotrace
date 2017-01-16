package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 * Represents the terrain, to be implemented according to the Assignments.
 */
class Terrain {

    private final int fragments = 40;
    private final int minX = -20;
    private final int maxX = 20;
    private final int minY = -20;
    private final int maxY = 20;    
    
    private float xInterval;
    private float yInterval;
    
    public Terrain() {
        xInterval = (maxX - minX) / fragments;
        yInterval = (maxY - minY) / fragments;
    }

    /**
     * Draws the terrain.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        int xStart = minX;
        int yStart = minY;
        for(int fragment = 0; fragment < fragments; fragment++) {
            for(int fragment2 = 0; fragment2 < fragments; fragment2++) {
                gl.glBegin(GL2.GL_TRIANGLE_STRIP);
                    gl.glTexCoord2d(0, 0);
                    gl.glVertex3d(xStart, yStart, 0);
                    gl.glTexCoord2d(0, 1);
                    gl.glVertex3d(xStart, yStart + yInterval, 0);
                    gl.glTexCoord2d(1, 0);
                    gl.glVertex3d(xStart + xInterval, yStart, 0);
                    gl.glTexCoord2d(1, 1);
                    gl.glVertex3d(xStart + xInterval, yStart + yInterval, 0);
                gl.glEnd();
                xStart += xInterval;
            }
            xStart = minX;
            yStart += yInterval;
        }
    }
    
}