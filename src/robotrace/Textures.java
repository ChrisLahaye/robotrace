package robotrace;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 *
 * @author Niels Rood
 */
public class Textures {
    
    public static Texture head = null;
    public static Texture torso = null;
    public static Texture legs = null;
    public static Texture track = null;
    public static Texture brick = null;
    public static Texture pole = null;
        
    public static void loadTextures() {
        head = loadTexture("textures/bert_head.jpg");
        torso = loadTexture("textures/suit_torso.jpg");
        legs = loadTexture("textures/suit_pants.jpg");
        track = loadTexture("textures/track_run.jpg");
        brick = loadTexture("textures/brick.jpg");
        pole = loadTexture("textures/screen_pole.jpg");
    }
    
    /**
    * Try to load a texture from the given file. The file
    * should be located in the same folder as RobotRace.java.
    */
    private static Texture loadTexture(String file) {
        Texture result = null;

        try {
            // Try to load from local folder.
            result = TextureIO.newTexture(Textures.class.getResource(file), false, null);
        } catch(Exception e1) {
            e1.printStackTrace();
        }       
        return result;
    }
}
