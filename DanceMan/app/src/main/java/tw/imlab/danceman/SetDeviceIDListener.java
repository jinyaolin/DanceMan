package tw.imlab.danceman;

import android.os.Handler;
import android.os.Looper;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;

import java.util.Date;

public class SetDeviceIDListener implements OSCListener {
    private MainActivity mainActivity;
    private int index;

    public SetDeviceIDListener(MainActivity activity) {
        mainActivity = activity;
    }

    public void acceptMessage(Date time, OSCMessage message){
        Object arguments[] = message.getArguments();
        index = (int)arguments[0];

        Handler refresh = new Handler(Looper.getMainLooper());
        refresh.post(new Runnable() {
            public void run() {
                mainActivity.setDeviceID(index);
            }
        });
    }


    public String getAddress () {
        return "/device";
    }
}
