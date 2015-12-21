package tw.imlab.danceman.animation;

import tw.imlab.danceman.Dancer;
import tw.imlab.danceman.tweener.Tweener;

public class RotateAnimation extends Animation{
    private float startAngle;
    private float finishAngle;

    public RotateAnimation(Dancer dancer, Tweener tweener, int elapsedTime, float angle) {
        super(dancer, tweener, elapsedTime);
        startAngle = dancer.getAngle();
        finishAngle = angle;
    }

    public void update (){
        float t = (System.nanoTime() - startTime);
        float st = tweener.getInterpolation(t, 0, 1, elapsedTime);
        if (st > 1) {
            dancer.setAngle(finishAngle);
            isFinish = true;
        }
        else {
            float currentAngle = startAngle + st * (finishAngle - startAngle);
            dancer.setAngle(currentAngle);
        }
    }
}
