package tw.imlab.danceman.tweener;

public class NoneTweener implements Tweener{
    public float getInterpolation(float t, float b, float c, float d) {
        return c*t/d + b;
    }
}
