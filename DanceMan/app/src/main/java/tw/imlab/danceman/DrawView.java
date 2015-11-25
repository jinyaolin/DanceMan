package tw.imlab.danceman;

import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class DrawView extends GLSurfaceView {
    private final DrawViewRenderer mRenderer;
    private DrawController drawController;
    private DrawModel drawModel;

    public DrawView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView

        mRenderer = new DrawViewRenderer();
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        drawModel = new DrawModel(this);
        drawController = new DrawController(drawModel);
    }

    public void onModelUpdate() {
        Point p = drawModel.getDrawPoint();
        mRenderer.drawPoint(p.x, p.y);
        requestRender();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        Point p = new Point(Math.round(x), Math.round(y));
        drawController.handleInput(p);

        return true;
    }

}