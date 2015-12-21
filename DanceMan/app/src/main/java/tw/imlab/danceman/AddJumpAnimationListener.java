package tw.imlab.danceman;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;

import java.util.Date;

import tw.imlab.danceman.animation.Animation;
import tw.imlab.danceman.animation.JumpAnimation;
import tw.imlab.danceman.tweener.NoneTweener;
import tw.imlab.danceman.tweener.Tweener;

public class AddJumpAnimationListener implements OSCListener {
    AnimationView animationView;

    public AddJumpAnimationListener (AnimationView view) {
        animationView = view;
    }

    public void acceptMessage (Date time, OSCMessage message) {
        Object arguments[] = message.getArguments();

        if (arguments.length != 3)
            return;

        int index;
        if (!(arguments[0] instanceof Integer))
            return;
        index = (int) arguments[0];
        if (index < 0 || index > Math.pow(2.0, 12) - 1)
            return;

        int elapsedTime;
        if (!(arguments[1] instanceof Integer))
            return;
        elapsedTime = (int)arguments[1];

        int height;
        if (!(arguments[2] instanceof Integer))
            return;
        height = (int) arguments[2];

        Tweener tweener = new NoneTweener();
        String indexString = String.format("%12s", Integer.toBinaryString(index)).replace(' ', '0');

        for (int i = 0; i < indexString.length(); ++i) {
            if (indexString.charAt(indexString.length() - 1 - i) == '1') {
                Animation animation = new JumpAnimation(animationView.getDancer(i), tweener, elapsedTime * 1000000, height);
                animationView.addAnimation(animation);
            }
        }
    }

    public String getAddress () {
        return "/jump";
    }
}
