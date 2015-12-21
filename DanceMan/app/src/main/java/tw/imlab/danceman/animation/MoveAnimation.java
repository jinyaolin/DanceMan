package tw.imlab.danceman.animation;

import tw.imlab.danceman.Dancer;
import tw.imlab.danceman.tweener.Tweener;

public class MoveAnimation extends Animation {
    private float startPointX, startPointY;
    private float moveX, moveY;

    public MoveAnimation(Dancer dancer, Tweener tweener, int elapsedTime, float moveX, float moveY) {
        super(dancer, tweener, elapsedTime);
        this.moveX = moveX;
        this.moveY = moveY;
        startPointX = dancer.getPositionX();
        startPointY = dancer.getPositionY();
    }

    public void update () {
        float t = (System.nanoTime() - startTime);
        float st = tweener.getInterpolation(t, 0, 1, elapsedTime);
        if (st > 1) {
            dancer.setPosition(startPointX + moveX, startPointY + moveY);
            isFinish = true;
        }
        else {
            float currentPointX = startPointX + st * moveX;
            float currentPointY = startPointY + st * moveY;
            dancer.setPosition(currentPointX, currentPointY);
        }
    }
}
