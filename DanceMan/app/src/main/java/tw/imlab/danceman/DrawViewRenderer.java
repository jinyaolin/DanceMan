package tw.imlab.danceman;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DrawViewRenderer implements GLSurfaceView.Renderer {

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private DrawCanvas canvas;
    private List<Button> button;
    private Button eraser;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);


        button = new ArrayList<>();
        int buttonSize = 70;
        int offset = 8;
        int dist = 15;
        Button tempButton;
        eraser = new Button(offset ,0, buttonSize, Color.WHITE);
        tempButton = new Button(offset+1 ,1, buttonSize-2, Color.BLACK);
        offset+=buttonSize+dist;
        button.add(tempButton);
        tempButton = new Button(offset ,0, buttonSize, Color.WHITE);
        offset+=buttonSize+dist;
        button.add(tempButton);
        tempButton = new Button(offset ,0, buttonSize, Color.rgb(255, 153, 0));
        offset+=buttonSize+dist;
        button.add(tempButton);
        tempButton = new Button(offset ,0, buttonSize, Color.rgb(176, 48, 96));
        offset+=buttonSize+dist;
        button.add(tempButton);
        tempButton = new Button(offset ,0, buttonSize, Color.rgb(8, 120, 48));
        offset+=buttonSize+dist;
        button.add(tempButton);
        tempButton = new Button(offset ,0, buttonSize, Color.rgb(83, 104, 149));
        offset+=buttonSize+dist;
        button.add(tempButton);
        tempButton = new Button(offset ,0, buttonSize, Color.rgb(255, 216, 0));
        offset+=buttonSize+dist;
        button.add(tempButton);
        tempButton = new Button(offset ,0, buttonSize, Color.rgb(183, 0, 0));
        offset+=buttonSize+dist;
        button.add(tempButton);
        tempButton = new Button(offset ,0, buttonSize, Color.rgb(33, 66, 30));
        offset+=buttonSize+dist;
        button.add(tempButton);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        canvas.draw(mMVPMatrix);
        eraser.draw(mMVPMatrix);
        for (int i=0; i<button.size();++i)
            button.get(i).draw(mMVPMatrix);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 1, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.orthoM(mProjectionMatrix, 0, 0f, width, height, 0f , 0f, 50f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        canvas = new DrawCanvas(0, 80, width, height - 80);
    }

    public void drawPoint(int x, int y) {
        canvas.drawPoint(x, y);
    }

    public void saveFrame(String name) {
        canvas.saveSketch(name);
    }

    /* very brutal way */
    public boolean buttonClick(float x, float y) {
        for (int i = 0; i < button.size(); ++i) {
            if (button.get(i).isClicked(x,y)){
                canvas.setBrushColor(button.get(i).getColor());
                return true;
            }
        }
        return false;
    }


}