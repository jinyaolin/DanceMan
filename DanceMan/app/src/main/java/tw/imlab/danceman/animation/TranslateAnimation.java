package tw.imlab.danceman.animation;

import tw.imlab.danceman.Dancer;
import tw.imlab.danceman.tweener.Tweener;

public class TranslateAnimation extends Animation {
    private float startPointX, startPointY;
    private float destinationX, destinationY;

    public TranslateAnimation(Dancer dancer, Tweener tweener, int elapsedTime, float destinationX, float destinationY) {
        super(dancer, tweener, elapsedTime);
        this.destinationX = destinationX;
        this.destinationY = destinationY;
        startPointX = dancer.getPositionX();
        startPointY = dancer.getPositionY();
    }

    public void update (){
        float t = (System.nanoTime() - startTime);
        float st = tweener.getInterpolation(t, 0, 1, elapsedTime);
        if (st > 1) {
            dancer.setPosition(destinationX, destinationY);
            isFinish = true;
        }
        else {
            float currentPointX = startPointX + st * (destinationX - startPointX);
            float currentPointY = startPointY + st * (destinationY - startPointY);
            dancer.setPosition(currentPointX, currentPointY);
        }
    }
}
