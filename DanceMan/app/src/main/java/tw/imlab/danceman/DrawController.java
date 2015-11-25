package tw.imlab.danceman;

import android.graphics.Point;
import android.util.Log;


public class DrawController {
    private DrawModel drawModel;
    private InputHandler handler;
    private Thread handlerThread;

    public DrawController (DrawModel model) {
        drawModel = model;
        handler = new InputHandler(this);

        handlerThread = new Thread(handler);
    }

    public void handleInput (Point p) {
        handler.queueInput(p);
        handlerThread.run();
    }

    public void updateModel(Point p) {
        drawModel.updateDrawPoint(p);
    }


}
