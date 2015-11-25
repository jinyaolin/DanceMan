package tw.imlab.danceman;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class InputHandler implements Runnable {
    private DrawController controller;
    private List<Point> inputList;

    public InputHandler (DrawController controller) {
        inputList = new ArrayList<>();
        this.controller = controller;
    }

    public void queueInput (Point p) {
        inputList.add(p);
    }

    public void run () {
        if (!inputList.isEmpty()) {
            for (int i = 0; i < inputList.size(); ++i) {
                controller.updateModel(inputList.remove(i));
            }
        }
    }

}
