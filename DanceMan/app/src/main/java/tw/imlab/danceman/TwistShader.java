package tw.imlab.danceman;

import android.opengl.GLES20;

public class TwistShader {
    private static int programID;
    public final String UNIFORM_MVP_MATRIX = "uMVPMatrix";
    public final String ATTRIBUTE_POSITION = "aPosition";
    public final String ATTRIBUTE_TEXCOORD = "aTexCoord";
    public final String UNIFORM_TEXTURE = "uTexture";

    private final static String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 aPosition;" +
                    "attribute vec2 aTexCoord;"+
                    "varying vec2 vTexCoord;" +
                    "void main() {" +
                    "  vTexCoord = aTexCoord;"+
                    "  gl_Position = uMVPMatrix * aPosition;" +
                    "}";
    private final static String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D uTexture;" +
                    //"uniform mat3 uDeformMatrix;" +
                    "varying vec2 vTexCoord;" +
                    "void main() {" +
                    "  vec3 uv = vec3(vTexCoord, 1.0);" +
                    "  mat3 uDeformMatrix = mat3(1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0);" +
                    "  vec3 duv = uDeformMatrix * uv;" +
                    "  vec2 deformUV = duv.xy;" +
                    "  gl_FragColor = texture2D(uTexture, deformUV);" +
                    "}";

    public static int loadVertexShader() {
        int shader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, vertexShaderCode);
        GLES20.glCompileShader(shader);

        // return the shader
        return shader;
    }

    public static int loadFragmentShader() {
        int shader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, fragmentShaderCode);
        GLES20.glCompileShader(shader);

        // return the shader
        return shader;
    }
}