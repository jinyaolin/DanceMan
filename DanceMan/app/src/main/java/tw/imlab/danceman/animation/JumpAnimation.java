package tw.imlab.danceman.animation;

import android.util.Log;

import tw.imlab.danceman.Dancer;
import tw.imlab.danceman.tweener.Tweener;

public class JumpAnimation extends Animation{
    private float jumpHeight;
    private float startHeight;

    public JumpAnimation(Dancer dancer, Tweener tweener, int elapsedTime, float height) {
        super(dancer, tweener, elapsedTime);
        startHeight = dancer.getPositionX();
        jumpHeight = height;
    }

    public void update (){
        float t = (System.nanoTime() - startTime);
        float st = tweener.getInterpolation(t, 0, 1, elapsedTime);
        if (st > 1) {
            dancer.setPosition(startHeight, dancer.getPositionY());
            isFinish = true;
        }
        else {
            float currentHeight = startHeight + (float) Math.sin(st * Math.PI) * jumpHeight;
            dancer.setPosition(currentHeight, dancer.getPositionY());
        }
    }
}
