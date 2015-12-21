package tw.imlab.danceman.animation;

import tw.imlab.danceman.Dancer;
import tw.imlab.danceman.tweener.Tweener;

public class TwistAnimation extends Animation{
    private float TwistAngle;
    private float startAngle;

    public TwistAnimation(Dancer dancer, Tweener tweener, int elapsedTime, float angle) {
        super(dancer, tweener, elapsedTime);
        startAngle = dancer.getAngle();
        TwistAngle = angle;
    }

    public void update (){
        float t = (System.currentTimeMillis() - startTime);
    }
}
