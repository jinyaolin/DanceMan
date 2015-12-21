package tw.imlab.danceman;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class InputHandler implements Runnable {
    private DrawController controller;
    private List<Point> inputList;
    private int type; //1=Press 0=Release
    private Point[] tempPoint;
    boolean handled;

    public InputHandler (DrawController controller) {
        inputList = new ArrayList<>();
        this.controller = controller;
        tempPoint = new Point[4];
        handled = false;
    }

    public void queueInput (Point p, int t) {
        type = t;
        if (type == 1 && (inputList.isEmpty() || !inputList.get(inputList.size()-1).equals(p.x,p.y))) {
            inputList.add(p);
            if (inputList.size() == 5)
                inputList.remove(0);
            handled = false;
        }
    }

    public void clearQueue() {
        inputList.clear();
    }

    private void imageCurveTempMouse(int n){
        if (n==4) {
            tempPoint[0] = inputList.get(0);
            tempPoint[1] = inputList.get(1);
            tempPoint[2] = inputList.get(2);
            tempPoint[3] = inputList.get(3);
        } else if (n == 2){
            tempPoint[1] = inputList.get(0);
            tempPoint[2] = inputList.get(1);
            tempPoint[0] = new Point(tempPoint[1].x,tempPoint[1].y);
            tempPoint[3] = new Point(tempPoint[2].x,tempPoint[2].y);
            tempPoint[0].set(tempPoint[1].x+tempPoint[1].x-tempPoint[2].x, tempPoint[1].y+tempPoint[1].y-tempPoint[2].y);
            tempPoint[3].set(tempPoint[2].x + tempPoint[2].x - tempPoint[1].x, tempPoint[2].y + tempPoint[2].y - tempPoint[1].y);
            /*-.d("cood:", tempPoint[0].x + ","+tempPoint[0].y+" "+
                    tempPoint[1].x + ","+tempPoint[1].y+" "
             +tempPoint[2].x + ","+tempPoint[2].y+" "
              +tempPoint[3].x + ","+tempPoint[3].y+" ");*/
            //} else if (n == 3){
            //USELESS CASE
            //tempPoint[0] = inputList.get(0);
            //tempPoint[1] = inputList.get(0);
            //tempPoint[2] = inputList.get(1);
            //tempPoint[3] = inputList.get(2);
        }
    }

    private void DrawCurve(){ //Centripetal Catmull–Rom spline
        imageCurveTempMouse(inputList.size());
        double alpha = 0.5;
        double t0 = 0;
        int density = 2;

        double t1 = Math.pow(Math.sqrt((tempPoint[1].x - tempPoint[0].x) * (tempPoint[1].x - tempPoint[0].x) + (tempPoint[1].y - tempPoint[0].y) * (tempPoint[1].y - tempPoint[0].y)), alpha)+t0;
        double t2 = Math.pow(Math.sqrt((tempPoint[2].x - tempPoint[1].x) * (tempPoint[2].x - tempPoint[1].x) + (tempPoint[2].y - tempPoint[1].y) * (tempPoint[2].y - tempPoint[1].y)), alpha)+t1;
        double t3 = Math.pow(Math.sqrt((tempPoint[3].x - tempPoint[2].x) * (tempPoint[3].x - tempPoint[2].x) + (tempPoint[3].y - tempPoint[2].y) * (tempPoint[3].y - tempPoint[2].y)), alpha)+t2;
        double t = t1;
        int nPoint=(int)(t2-t1)*density+1;

        double unit = (t2-t1)/nPoint;
        for (int i=0; i<nPoint; ++i){
            double A1x,A1y,A2x,A2y,A3x,A3y,B1x,B1y,B2x,B2y,Cx,Cy;
            A1x = (t1-t)/(t1-t0)*tempPoint[0].x + (t-t0)/(t1-t0)*tempPoint[1].x;
            A2x = (t2-t)/(t2-t1)*tempPoint[1].x + (t-t1)/(t2-t1)*tempPoint[2].x;
            A3x = (t3-t)/(t3-t2)*tempPoint[2].x + (t-t2)/(t3-t2)*tempPoint[3].x;
            A1y = (t1-t)/(t1-t0)*tempPoint[0].y + (t-t0)/(t1-t0)*tempPoint[1].y;
            A2y = (t2-t)/(t2-t1)*tempPoint[1].y + (t-t1)/(t2-t1)*tempPoint[2].y;
            A3y = (t3-t)/(t3-t2)*tempPoint[2].y + (t-t2)/(t3-t2)*tempPoint[3].y;

            B1x = (t2-t)/(t2-t0)*A1x + (t-t0)/(t2-t0)*A2x;
            B2x = (t3-t)/(t3-t1)*A2x + (t-t1)/(t3-t1)*A3x;
            B1y = (t2-t)/(t2-t0)*A1y + (t-t0)/(t2-t0)*A2y;
            B2y = (t3-t)/(t3-t1)*A2y + (t-t1)/(t3-t1)*A3y;

            Cx  = (t2-t)/(t2-t1)*B1x + (t-t1)/(t2-t1)*B2x;
            Cy  = (t2-t)/(t2-t1)*B1y + (t-t1)/(t2-t1)*B2y;
            Point output = new Point((int)Cx, (int)Cy);

            controller.updateModel(output);
            t+=unit;
        }
    }

    /*private void DrawLine(){
        imageCurveTempMouse(inputList.size());
        double dist = Math.sqrt((tempPoint[1].x - tempPoint[0].x) * (tempPoint[1].x - tempPoint[0].x) + (tempPoint[1].y - tempPoint[0].y) * (tempPoint[1].y - tempPoint[0].y));
        int nPoint=(int)(dist)+1;
        double unitX = (double)(tempPoint[1].x - tempPoint[0].x)/nPoint;
        double unitY = (double)(tempPoint[1].y - tempPoint[0].y)/nPoint;
        Point output = new Point(tempPoint[0].x,tempPoint[0].y);
        Log.d("tag!", nPoint + " " + unitX + " " +unitY);
        for (int i=0; i<nPoint; ++i){
            controller.updateModel(output);
            output.x += unitX;
            output.y += unitY;
        }
    }*/

    public void run () {
        while (true) {
            if (!inputList.isEmpty()) {
                if (type == 0){
                    inputList.clear();
                } else if( handled == false ){
                    if (inputList.size() == 1) {
                        controller.updateModel(inputList.get(0));
                    } else if (inputList.size()!=3){
                        DrawCurve();
                    }
                    handled = true;
                }

            }
        }
    }

}
