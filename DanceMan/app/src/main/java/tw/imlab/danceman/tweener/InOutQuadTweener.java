package tw.imlab.danceman.tweener;

public class InOutQuadTweener implements Tweener {
    public float getInterpolation(float t, float b, float c, float d) {
        if((t/=d/2) < 1)
            return c/2*t*t + b;
        return -c/2 *((--t)*(t-2) - 1) + b;
    }
}
