package tw.imlab.danceman;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;

import java.util.Date;

public class ChangeAnimationStateListener implements OSCListener {
    private MainActivity mainActivity;

    public ChangeAnimationStateListener(MainActivity activity) {
        mainActivity = activity;
    }

    public void acceptMessage(Date time, OSCMessage message) {
        Handler refresh = new Handler(Looper.getMainLooper());
        refresh.post(new Runnable() {
            public void run() {
                mainActivity.changeAnimationState();
            }
        });
    }

    public String getAddress () {
        return "/animate";
    }
}