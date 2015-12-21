package tw.imlab.danceman;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;

import java.util.Date;

public class UploadListener implements OSCListener {
    private DrawView drawView;

    public UploadListener(DrawView view) {
        drawView = view;
    }

    public void acceptMessage(Date time, OSCMessage message){
        drawView.saveFrame();
    }

    public String getAddress () {
        return "/save";
    }

}
