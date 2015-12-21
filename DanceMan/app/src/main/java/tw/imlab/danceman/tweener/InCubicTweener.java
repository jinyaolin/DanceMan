package tw.imlab.danceman.tweener;

public class InCubicTweener implements Tweener {
    public float getInterpolation(float t, float b, float c, float d) {
        return c*(t/=d)*t*t + b;
    }
}
