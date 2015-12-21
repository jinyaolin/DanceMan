package tw.imlab.danceman.animation;

import tw.imlab.danceman.Dancer;
import tw.imlab.danceman.tweener.Tweener;

public class SwingAnimation extends Animation{
    private float swingAngle;
    private float startAngle;

    public SwingAnimation(Dancer dancer, Tweener tweener, int elapsedTime, float angle) {
        super(dancer, tweener, elapsedTime);
        startAngle = dancer.getAngle();
        swingAngle = angle;
    }

    public void update (){
        float t = (System.nanoTime() - startTime);
        float st = tweener.getInterpolation(t, 0, 1, elapsedTime);
        if (st > 1) {
            dancer.setAngle(startAngle);
            isFinish = true;
        }
        else {
            float currentAngle = (float) Math.sin(st * 2 * Math.PI) * (startAngle - swingAngle);
            dancer.setAngle(currentAngle);
        }
    }

}
