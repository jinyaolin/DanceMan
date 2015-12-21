package tw.imlab.danceman;

import android.graphics.Point;
import android.util.Log;


public class DrawController {
    private DrawModel drawModel;
    private InputHandler handler;

    public DrawController (DrawModel model) {
        drawModel = model;
        handler = new InputHandler(this);
        Thread thread = new Thread(handler);
        thread.start();
    }

    public void handleInput (Point p, int type) {
        handler.queueInput(p, type);

    }

    public void startController() {

    }

    public void reset() {
        handler.clearQueue();
    }

    public void updateModel(Point p) {
        drawModel.updateDrawPoint(p);
    }

    /*public void startController () {

        handler.startHandling();
    }

    public void stopController () {
        handler.stopHandling();
    }*/

}
