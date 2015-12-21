package tw.imlab.danceman;

import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCPortIn;

public class DrawView extends GLSurfaceView {
    private final DrawViewRenderer mRenderer;
    private DrawController drawController;
    private DrawModel drawModel;
    private OSCPortIn receiver;
    private OSCListener uploadListener;
    private Stitch stitch;

    public DrawView(Context context, OSCPortIn receiver, Stitch stitch) {
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

        this.receiver = receiver;

        uploadListener = new UploadListener(this);
        receiver.addListener(uploadListener.getAddress(), uploadListener);
        //drawController.startController();

        this.stitch = stitch;
    }

    public void saveFrame() {
        mRenderer.saveFrame(Integer.toString(stitch.getDeviceID()));
        mRenderer.saveFrame("sketch");
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

        /* very brutal way */
        if (mRenderer.buttonClick(x, y))
            return true;

        Point p = new Point(Math.round(x), Math.round(y));

        if(e.getAction() == MotionEvent.ACTION_UP)
            drawController.handleInput(p, 0);
        else
            drawController.handleInput(p, 1);

        return true;
    }


}