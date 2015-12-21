package tw.imlab.danceman.tweener;

public class OutCubicTweener implements Tweener {
    public float getInterpolation(float t, float b, float c, float d) {
        return c*((t=t/d-1)*t*t + 1) + b;
    }
}
