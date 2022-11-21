package edu.wm.cs.cs301.cheyennehwang.gui;

import android.util.AttributeSet;
import android.view.View;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import edu.wm.cs.cs301.cheyennehwang.R;

/**
 * Class: maze panel view drawer which extends view class
 * @author cheyennehwang
 *
 * Responsibilities: creates the screen that depicts the maze play experience based on user input
 *
 * Collaborators: PlayAnimationActivity (State PlayAnimation), PlayManuallyActivity (State PlayManually), UI, MazeFactory, Control
 *
 */

public class MazePanel extends View {
    private Bitmap mazeBitmap;
    private Canvas mazeCanvas;
    private Paint mazePaint;
    public boolean isManual;


    public MazePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mazeCanvas = new Canvas();
        mazePaint = new Paint();


    }

    protected void onDraw(Canvas mazeCanvas) {
        super.onDraw(mazeCanvas);

        mazePaint.setColor(Color.BLACK);
        mazeCanvas.drawRect(0, 500, 1000, 1000, mazePaint);

        mazePaint.setColor(Color.LTGRAY);
        mazeCanvas.drawRect(0, 0, 1000, 500, mazePaint);
    }

}
