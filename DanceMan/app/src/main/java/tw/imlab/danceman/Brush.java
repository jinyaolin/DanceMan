package tw.imlab.danceman;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class Brush {
    private Bitmap brushTexture;
    private int strokeSize;

    public Brush() {
        // default stroke Size
        strokeSize = 100;
        brushTexture = Bitmap.createBitmap(strokeSize, strokeSize, Bitmap.Config.ARGB_8888);
        brushTexture.eraseColor(Color.BLUE);

        Log.d("BRUSH", Integer.toString(brushTexture.getPixel(20, 20)));
    }

    public Bitmap getStroke() {
        return brushTexture;
    }

    public void setStrokeSize(int size) {
        strokeSize = size;
        brushTexture = Bitmap.createScaledBitmap(brushTexture, strokeSize, strokeSize, true);
    }

    public int getStrokeSize() {
        return strokeSize;
    }
}