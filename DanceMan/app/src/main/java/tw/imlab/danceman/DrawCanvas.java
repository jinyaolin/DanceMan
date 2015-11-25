package tw.imlab.danceman;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

public class DrawCanvas {
    // number of coordinates per vertex in this array
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
    private final short indices[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices

    private final FloatBuffer vertexBuffer;
    private final FloatBuffer texCoordBuffer;
    private final ShortBuffer indexBuffer;

    private Shader shader;
    private final int mProgram;
    private int[] textureHandle = new int[1];

    private Brush brush;
    private Paint paint;
    private Bitmap sketch;
    private Canvas canvas;

    public int positionX, positionY;
    public int width, height;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public DrawCanvas(int positionX, int positionY, int width, int height) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;

        brush = new Brush();
        paint = new Paint();
        sketch = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(sketch);
        sketch.eraseColor(Color.GREEN);

        // initialize byte buffer for shape coordinates
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
     *                  this shape.
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
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, sketch, 0);

        int mSampler = GLES20.glGetUniformLocation (mProgram, shader.UNIFORM_TEXTURE);
        GLES20.glUniform1i (mSampler, 0);

        // get handle to shape's transformation matrix
        int mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, shader.UNIFORM_MVP_MATRIX);

        // Apply the projection and view transformation
        float transformMVPMatrix[] = new float[16];
        float transformMatrix[] = new float[16];
        Matrix.setIdentityM(transformMatrix, 0);
        Matrix.translateM(transformMatrix, 0, positionX, positionY, 0);
        Matrix.scaleM(transformMatrix, 0, width, height, 1);
        Matrix.multiplyMM(transformMVPMatrix, 0, mvpMatrix, 0, transformMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, transformMVPMatrix, 0);

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordHandle);

        Log.d("DEBUG", Integer.toString(sketch.getPixel(20, 20)));
    }

    public void drawPoint(int x, int y) {
        Log.d("DEBUG", "draw " + x + " " + y);

        canvas.drawBitmap(brush.getStroke(), x - brush.getStrokeSize()/2, y - brush.getStrokeSize()/2, paint);
    }

    public void setPosition(int x, int y) {
        positionX = x;
        positionY = y;
    }

    public void setResolution(int w, int h) {
        width = w;
        height = h;
        sketch.setWidth(width);
        sketch.setHeight(height);
    }
}