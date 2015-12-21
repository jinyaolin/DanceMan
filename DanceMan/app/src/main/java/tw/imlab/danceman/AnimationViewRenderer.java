package tw.imlab.danceman;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.List;

import tw.imlab.danceman.animation.*;
import tw.imlab.danceman.tweener.*;

public class AnimationViewRenderer implements GLSurfaceView.Renderer {

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private List<Dancer> dancers;
    private List<Animation> animations;
    private Stitch stitch;

    public AnimationViewRenderer (Stitch stitch) {
        this.stitch = stitch;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        dancers = new ArrayList<>();
        animations = new ArrayList<>();
        for (int i = 0; i < 12; ++i) {
            Dancer dancer = null;
            try {
                dancer = new Dancer(i, stitch.getStitchX(i), stitch.getStitchY(i));
            } catch (Exception e) {
                dancer = null;
            }

            if (dancer != null)
                dancers.add(dancer);
        }
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        for (int i = 0; i < animations.size(); ++i) {
            if (animations.get(i).isFinished())
                animations.remove(i);
        }

        for (int i = 0; i < animations.size(); ++i)
            animations.get(i).update();

        for (int i = 0; i < dancers.size(); ++i) {
            dancers.get(i).draw(mMVPMatrix);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        // Set the camera position (View matrix)
        float eyeX = stitch.getStitchX(stitch.getDeviceID());
        float eyeY = stitch.getStitchY(stitch.getDeviceID());
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, 1, eyeX, eyeY, 0f, 0f, 1.0f, 0.0f);

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.orthoM(mProjectionMatrix, 0, 0f, width, height, 0f, 0f, 50f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        for (int i = 0; i < dancers.size(); ++i)
            dancers.get(i).setResolution(width, height);
    }

    public  Dancer getDancer (int i) {
        if (i >= 0 && i < dancers.size()) {
            return dancers.get(i);
        }
        return null;
    }

    public void addAnimation(Animation animation) {
        animations.add(animation);
    }
}