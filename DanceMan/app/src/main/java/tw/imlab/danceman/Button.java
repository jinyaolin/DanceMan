package tw.imlab.danceman;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Button {
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    private final float vertices[] = {
            0f, 0f, 0f,  // top left
            0f, 1f, 0f, // bottom left
            1f, 1f, 0f,  // bottom right
            1f, 0f, 0.0f};  // top right
    private final float texCoords[] = {
            0f, 0f,
            0f, 1f,
            1f, 1f,
            1f, 0f};

    private final short indices[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    private final FloatBuffer vertexBuffer;
    private final FloatBuffer texCoordBuffer;
    private final ShortBuffer indexBuffer;

    private Shader shader;
    private final int mProgram;
    private int[] textureHandle = new int[1];


    private Bitmap buttonTexture;
    private float positionX = 0.0f, positionY = 0.0f;
    private float buttonSize = 30.0f;
    private int color;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Button(float x, float y,float size, int color) {
        buttonSize = size;
        positionX = x;
        positionY = y;
        this.color = color;

        buttonTexture = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        buttonTexture.eraseColor(color);

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        vertexByteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = vertexByteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for texture coordinates
        ByteBuffer texCoordByteBuffer = ByteBuffer.allocateDirect(texCoords.length * 4);
        texCoordByteBuffer.order(ByteOrder.nativeOrder());
        texCoordBuffer = texCoordByteBuffer.asFloatBuffer();
        texCoordBuffer.put(texCoords);
        texCoordBuffer.position(0);

        GLES20.glGenTextures(1, textureHandle, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);

        // initialize byte buffer for the draw order list
        ByteBuffer indexByteBuffer = ByteBuffer.allocateDirect(indices.length * 2);
        indexByteBuffer.order(ByteOrder.nativeOrder());
        indexBuffer = indexByteBuffer.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);

        shader = new Shader();

        int vertexShader = shader.loadVertexShader();
        int fragmentShader = shader.loadFragmentShader();

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executable
        GLES20.glUseProgram(mProgram);                   // Add program to OpenGL environment
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(float[] mvpMatrix) {
        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, shader.ATTRIBUTE_POSITION);

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        int mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, shader.ATTRIBUTE_TEXCOORD);

        GLES20.glEnableVertexAttribArray(mTexCoordHandle);

        GLES20.glVertexAttribPointer(
                mTexCoordHandle, 2,
                GLES20.GL_FLOAT, false,
                0, texCoordBuffer);


        // set up new frame
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, buttonTexture, 0);

        int mSampler = GLES20.glGetUniformLocation (mProgram, shader.UNIFORM_TEXTURE);
        GLES20.glUniform1i(mSampler, 0);

        // get handle to shape's transformation matrix
        int mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, shader.UNIFORM_MVP_MATRIX);

        // Apply the projection and view transformation
        float transformMVPMatrix[] = new float[16];
        float transformMatrix[] = new float[16];
        Matrix.setIdentityM(transformMatrix, 0);
        Matrix.translateM(transformMatrix, 0, positionX, positionY, 0);
        Matrix.scaleM(transformMatrix, 0, buttonSize, buttonSize, 1);
        Matrix.multiplyMM(transformMVPMatrix, 0, mvpMatrix, 0, transformMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, transformMVPMatrix, 0);

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordHandle);
    }

    public boolean isClicked(float x, float y) {
        if (x > positionX && y > positionY && x < positionX+buttonSize && y < positionY+buttonSize)
            return true;
        return false;
    }

    public int getColor() {
        return color;
    }

    public void setPostion(float x, float y) {
        positionX = x;
        positionY = y;
    }

    public void setButtonSize(float size) {
        buttonSize = size;
    }
}
