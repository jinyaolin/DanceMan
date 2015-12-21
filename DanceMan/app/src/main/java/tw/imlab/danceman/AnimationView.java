package tw.imlab.danceman;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCPortIn;

import tw.imlab.danceman.animation.Animation;

public class AnimationView extends GLSurfaceView {
    private final AnimationViewRenderer mRenderer;
    private OSCPortIn receiver;
    private OSCListener addRotateAnimationListener;
    private OSCListener addMoveAnimationListener;
    private OSCListener addJumpAnimationListener;
    private OSCListener addSwingAnimationListener;
    private OSCListener addTranslateAnimationListener;
    private OSCListener addTwistAnimationListener;

    public AnimationView(Context context, OSCPortIn receiver, Stitch stitch) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView

        mRenderer = new AnimationViewRenderer(stitch);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        this.receiver = receiver;

        addRotateAnimationListener = new AddRotateAnimationListener(this);
        receiver.addListener(addRotateAnimationListener.getAddress(), addRotateAnimationListener);
        addMoveAnimationListener = new AddMoveAnimationListener(this);
        receiver.addListener(addMoveAnimationListener.getAddress(), addMoveAnimationListener);
        addJumpAnimationListener = new AddJumpAnimationListener(this);
        receiver.addListener(addJumpAnimationListener.getAddress(), addJumpAnimationListener);
        addSwingAnimationListener = new AddSwingAnimationListener(this);
        receiver.addListener(addSwingAnimationListener.getAddress(), addSwingAnimationListener);
        addTranslateAnimationListener = new AddTranslateAnimationListener(this);
        receiver.addListener(addTranslateAnimationListener.getAddress(), addTranslateAnimationListener);
        addTwistAnimationListener = new AddTwistAnimationListener(this);
        receiver.addListener(addTwistAnimationListener.getAddress(), addTwistAnimationListener);


    }

    public Dancer getDancer(int i) {
        return mRenderer.getDancer(i);
    }

    public void addAnimation(Animation animation) {
        mRenderer.addAnimation(animation);
    }
}