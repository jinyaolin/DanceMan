package tw.imlab.danceman;

import android.graphics.Point;

public class DrawModel {
    private Point drawPoint;
    private DrawView drawView;

    public DrawModel(DrawView view) {
        drawView = view;
    }

    public void notifyUpdate() {
        drawView.onModelUpdate();
    }

    public void updateDrawPoint(Point p) {
        drawPoint = p;
        notifyUpdate();
    }

    public Point getDrawPoint() {
        return drawPoint;
    }
}
