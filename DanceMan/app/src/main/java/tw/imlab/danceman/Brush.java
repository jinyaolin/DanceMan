package tw.imlab.danceman;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;


public class Brush {
    private Bitmap brushTexture;
    private int strokeSize;
    private boolean type; //true=brush false=eraser
    private int brushColor;

    public Brush() {
        // default stroke Size
        strokeSize = 40;
        brushColor = Color.WHITE;
        brushTexture = Bitmap.createBitmap(strokeSize, strokeSize, Bitmap.Config.ARGB_8888);
        setBrushTexture();
        //brushTexture.eraseColor(Color.WHITE);
        brushTexture = SetCircle(brushTexture);
        type = true;
    }

    public Bitmap getStroke() {
        return brushTexture;
    }

    private void setBrushTexture(){
        brushTexture.eraseColor(Color.TRANSPARENT);
        for (int i=0; i<strokeSize; ++i)
            for(int j=0; j<strokeSize; ++j){
                if (Math.random()*((double)strokeSize-1)/2 >
                        Math.sqrt(((double) i - ((double) strokeSize-1)/2) * ((double) i - ((double)strokeSize-1)/2) +
                                ((double)j-((double)strokeSize-1)/2)*((double)j-((double)strokeSize-1)/2)))
                    brushTexture.setPixel(i,j,brushColor);
            }
    }


    public void setColor (int color) {
        brushColor = color;
        if (color == Color.BLACK) {
            setStrokeSize(60);
            brushTexture.eraseColor(Color.BLACK);
        } else {
            setStrokeSize(40);
            setBrushTexture();
        }
        brushTexture = SetCircle(brushTexture);
    }


    public void setStrokeSize(int size) {
        strokeSize = size;
        brushTexture = Bitmap.createScaledBitmap(brushTexture, strokeSize, strokeSize, true);
    }

    public Bitmap SetCircle(Bitmap bitmapImg){
        Bitmap output = Bitmap.createBitmap(bitmapImg.getWidth(),
                bitmapImg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapImg.getWidth(),
                bitmapImg.getHeight());

        paint.setAntiAlias(false);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmapImg.getWidth() / 2,
                bitmapImg.getHeight() / 2, bitmapImg.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmapImg, rect, rect, paint);
        return output;
    }

    public int getStrokeSize() {
        return strokeSize;
    }
}