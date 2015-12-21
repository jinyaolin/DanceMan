package tw.imlab.danceman;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;

import java.util.Date;

import tw.imlab.danceman.animation.Animation;
import tw.imlab.danceman.animation.RotateAnimation;
import tw.imlab.danceman.tweener.InQuadTweener;
import tw.imlab.danceman.tweener.Tweener;

public class AddTwistAnimationListener implements OSCListener {
    AnimationView animationView;

    public AddTwistAnimationListener (AnimationView view) {
        animationView = view;
    }

    public void acceptMessage (Date time, OSCMessage message) {
        Object arguments[] = message.getArguments();
        int index = (int)arguments[0];
        int elapsedTime = (int)arguments[1];
        int angle = (int) arguments[2];

        Tweener tweener = new InQuadTweener();
        String indexString = String.format("%12s", Integer.toBinaryString(index)).replace(' ', '0');

        for (int i = 0; i < indexString.length(); ++i) {
            if (indexString.charAt(indexString.length() - 1 - i) == '1') {
                Animation animation = new RotateAnimation(animationView.getDancer(i), tweener, elapsedTime * 1000000, angle);
                animationView.addAnimation(animation);
            }
        }
    }

    public String getAddress () {
        return "/twist";
    }
}
