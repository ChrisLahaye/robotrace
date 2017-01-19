package robotrace;

/**
* Materials that can be used for the robots.
 * Used material properties for gold and silver from http://devernay.free.fr/cours/opengl/materials.html
*/
public enum Material {

    /** 
     * Gold material properties.
     * Modify the default values to make it look like gold.
     * todo: reconsider note below
     * Note: Set gold shininess to 25 instead of 0.4, since otherwise specular highlighting is a bit too much.
     */
    GOLD (
            
        new float[] {0.75164f, 0.60648f, 0.22648f, 1},
        new float[] {0.628281f, 0.555802f, 0.366065f, 1},
        25f

    ),

    /**
     * Silver material properties.
     * Modify the default values to make it look like silver.
     * todo: reconsider note below
     * Note: Set gold shininess to 25 instead of 0.4, since otherwise specular highlighting is a bit too much.
     */
    SILVER (
            
        new float[] {0.50754f, 0.50754f, 0.50754f, 1},
        new float[] {0.508273f, 0.508273f, 0.508273f, 1},
            25f

    ),

    /** 
     * Orange material properties.
     * Modify the default values to make it look like orange.
     */
    ORANGE (
            
        new float[] {0.992157f, 0.513726f, 0, 1},
        new float[] {0.0225f, 0.0225f, 0.0225f, 1},
        12.8f

    ),

    /**
     * Wood material properties.
     * Modify the default values to make it look like Wood.
     * We default to the assumption that wood does not reflect any light.
     */
    WOOD (

        new float[] {0.992157f, 0.513726f, 0, 1}, // todo: make it more woody - now using orange diffuse values
        new float[] {0.0225f, 0.0225f, 0.0225f, 1},
        100f

    );

    /** The diffuse RGBA reflectance of the material. */
    float[] diffuse;

    /** The specular RGBA reflectance of the material. */
    float[] specular;
    
    /** The specular exponent of the material. */
    float shininess;

    /**
     * Constructs a new material with diffuse and specular properties.
     */
    private Material(float[] diffuse, float[] specular, float shininess) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }
}
