package tw.imlab.danceman.animation;

import tw.imlab.danceman.Dancer;
import tw.imlab.danceman.tweener.Tweener;

public abstract class Animation {
    protected Dancer dancer;
    protected Tweener tweener;
    protected int elapsedTime;
    protected long startTime;
    protected boolean isFinish;

    public Animation(Dancer dancer, Tweener tweener, int elapsedTime) {
        this.dancer = dancer;
        this.tweener = tweener;
        this.elapsedTime = elapsedTime;
        startTime = System.nanoTime();
        isFinish = false;
    }

    public abstract void update ();

    public boolean isFinished () {
        return isFinish;
    }
}
