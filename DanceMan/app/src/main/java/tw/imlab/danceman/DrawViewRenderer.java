package tw.imlab.danceman;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

public class DrawViewRenderer implements GLSurfaceView.Renderer {

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private DrawCanvas canvas;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        canvas.draw(mMVPMatrix);
    }

    public void drawPoint(int x, int y) {
        canvas.drawPoint(x, y);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);
        //Log.d("Debug", width + " " + height);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 1, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.orthoM(mProjectionMatrix, 0, 0f, width, height, 0f , 0f, 50f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        canvas = new DrawCanvas(0, 0, width, height);
    }

}