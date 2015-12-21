package tw.imlab.danceman.tweener;

public class InOutCubicTweener implements Tweener {
    public float getInterpolation(float t, float b, float c, float d) {
        if((t/=d/2) < 1)
            return c/2*t*t*t + b;
        return c/2*((t-=2)*t*t + 2) + b;
    }
}
