package tw.imlab.danceman;

import android.support.v7.app.AppCompatActivity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.illposed.osc.*;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView mGLView, drawView, animateView;
    private OSCPortIn receiver;
    private OSCListener changeDrawStateListener;
    private OSCListener setDeviceIDListener;
    private OSCListener changeAnimationStateListener;
    private Stitch stitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        stitch = new Stitch();

        try {
            receiver = new OSCPortIn(OSCPort.defaultSCOSCPort());
        } catch (Exception e) {
            e.printStackTrace();
        }

        addOSCListener();

        drawView = new DrawView(this, receiver, stitch);
        animateView = new AnimationView(this, receiver, stitch);
        changeDrawState();
    }

    public void setDeviceID (int index) {
        stitch.setDeviceID(index);
    }

    public void changeDrawState() {
        mGLView = drawView;
        setContentView(mGLView);
    }

    public void changeAnimationState() {
        mGLView = animateView;
        setContentView(mGLView);
    }

    private void addOSCListener () {
        changeDrawStateListener = new ChangeDrawStateListener(this);
        changeAnimationStateListener = new ChangeAnimationStateListener(this);
        setDeviceIDListener = new SetDeviceIDListener(this);
        receiver.addListener(changeDrawStateListener.getAddress(), changeDrawStateListener);
        receiver.addListener(changeAnimationStateListener.getAddress(), changeAnimationStateListener);
        receiver.addListener(setDeviceIDListener.getAddress(), setDeviceIDListener);
        receiver.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        try {
            receiver = new OSCPortIn(OSCPort.defaultSCOSCPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mGLView.onResume();
    }
}