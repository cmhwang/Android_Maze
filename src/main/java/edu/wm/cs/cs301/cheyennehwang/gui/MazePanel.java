package edu.wm.cs.cs301.cheyennehwang.gui;

import android.util.AttributeSet;
import android.view.View;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import edu.wm.cs.cs301.cheyennehwang.R;

public class MazePanel extends View {
    private Bitmap mazeBitmap;
    private Canvas mazeCanvas;
    private Paint mazePaint;
    public boolean isManual;


    public MazePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mazeCanvas = new Canvas();
        mazePaint = new Paint();

        mazePaint.setColor(Color.BLACK);
        mazeCanvas.drawRect(0, 200, 388, 298, mazePaint);

        mazePaint.setColor(Color.LTGRAY);
        mazeCanvas.drawRect(0, 0, 300, 200, mazePaint);

    }

}
