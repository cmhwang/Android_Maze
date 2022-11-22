package edu.wm.cs.cs301.cheyennehwang.gui;


import android.content.Intent;
import android.graphics.Path;
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

public class MazePanel1 extends View {
    private Bitmap mazeBitmap;
    private Canvas mazeCanvas;
    private Paint mazePaint;
    public boolean isManual;

    /**
     * Basic constructor that sets up the maze's canvas and paints
     */
    public MazePanel1(Context context, AttributeSet attrs) {
        super(context, attrs);
        mazeCanvas = new Canvas();
        mazePaint = new Paint();

    }

    /**
     * helper method to draw the backgrounds according to specification
     * @param canvas is the master canvas
     */
    protected void onDraw(Canvas canvas) {
        mazeCanvas = canvas;
        super.onDraw(mazeCanvas);

        mazePaint.setColor(Color.BLACK);
        mazeCanvas.drawRect(0, 500, 1000, 1000, mazePaint);

        mazePaint.setColor(Color.LTGRAY);
        mazeCanvas.drawRect(0, 0, 1000, 500, mazePaint);

        mazePaint.setColor(Color.GREEN);
        int[] greenXPoints = new int[]{50, 250, 350, 150};
        int[] greenYPoints= new int[]{500, 100, 300, 700};
        Path greenPath = new Path();
        greenPath.moveTo(greenXPoints[0], greenYPoints[0]);
        for (int i = 1; i < 4; i++) {
            greenPath.lineTo(greenXPoints[i], greenYPoints[i]);
        }
        greenPath.close();
        mazeCanvas.drawPath(greenPath, mazePaint);

        mazePaint.setColor(Color.YELLOW);
        int[] yellowXPoints = new int[]{550, 750, 650, 450};
        int[] yellowYPoints= new int[]{100, 500, 700, 300};
        Path yellowPath = new Path();
        yellowPath.moveTo(yellowXPoints[0], yellowYPoints[0]);
        for (int i = 1; i < 4; i++) {
            yellowPath.lineTo(yellowXPoints[i], yellowYPoints[i]);
        }
        yellowPath.close();
        mazeCanvas.drawPath(yellowPath, mazePaint);

    }



}
