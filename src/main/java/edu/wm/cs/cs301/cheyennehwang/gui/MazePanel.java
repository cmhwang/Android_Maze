package edu.wm.cs.cs301.cheyennehwang.gui;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.RequiresApi;

import edu.wm.cs.cs301.cheyennehwang.R;

/**
 * Class: maze panel view drawer which extends view class
 * @author cheyennehwang
 *
 * Responsibilities: creates the screen that depicts the maze play experience based on user input
 *
 * Collaborators: Map, FirstPersonView, CompassRose, PlayAnimationActivity (State PlayAnimation), PlayManuallyActivity (State PlayManually), UI, MazeFactory, Control
 *
 */

public class MazePanel extends View implements P7PanelF22{
    private Bitmap mazeBitmap;
    private Canvas mazeCanvas;
    private Paint mazePaint;
    public static BitmapShader mazeShader;
    public Typeface mazeFont;

    public boolean canDraw;

    //for flexible view dimesnions
    public int width;
    public int height;

    /**
     * Basic constructor that sets up the maze's canvas and paints
     */
    public MazePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mazeBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        mazeCanvas = new Canvas(mazeBitmap);
        mazePaint = new Paint();

        Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.drawable.forest_pic);
        mazeShader = new BitmapShader(image, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
        canDraw = true;



    }

    /**
     * helper method to draw the backgrounds according to specification
     * @param canvas is the master canvas
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.v("MazePanel", "Drawing maze panel");
//        myTestImage(mazeCanvas);
        commit(canvas);


    }

    /**
     * Commits all accumulated drawings to the UI.
     * Substitute for MazePanel.update method.
     *
     * updates based on what's been added to the canvas
     */
    @Override
    public void commit(){
        // only create new objects in the constructor, redraw the rest
        // in commit invalidate method which will send it to on draw
//        mazeCanvas.drawBitmap(mazeBitmap, 0, 0, mazePaint);
//        mazeBitmap = Bitmap.createBitmap(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Bitmap.Config.ARGB_8888);
//        mazeCanvas = new Canvas(mazeBitmap);
        invalidate();


    }

    /**
     * Commits all accumulated drawings to the UI.
     * Substitute for MazePanel.update method.
     *
     * updates based on what's been added to the canvas
     */
    public void commit(Canvas c){

        c.drawBitmap(mazeBitmap, 0, 0, mazePaint);
        mazeBitmap = Bitmap.createBitmap(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Bitmap.Config.ARGB_8888);
        mazeCanvas = new Canvas(mazeBitmap);

    }

    /**
     * Tells if instance is able to draw.
     * This ability depends on the context, for instance, in a testing environment, drawing may be not possible and not desired.
     * Substitute for code that checks if graphics object for drawing is not null.
     *
     *  @return true if drawing is possible, false if not.
     */
    @Override
    public boolean isOperational(){
        if ( mazeCanvas == null ){
            Log.e("MazePanel", "Can't draw on canvas");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Sets the color for future drawing requests
     * The color setting will remain in effect till this method is called again and with a different color.
     * Substitute for Graphics.setColor method.
     *
     * How its done: sets the paint variable for the mazepanel object to the color requested
     * @param argb gives the alpha, red, green, and blue encoded value of the color
     */
    @Override
    public void setColor(int argb){
        mazePaint.setColor(argb);
    }

    /**
     * Returns the ARGB value for the current color setting.
     *
     * how i did it: checks the paint object for the class which contains current color being used to draw, returns its int val
     * @return integer ARGB value
     */
    @Override
    public int getColor(){

        int colorVal = mazePaint.getColor();
        return colorVal;
    }


    /**
     * Draws two solid rectangles to provide a background.
     * Note that this also erases any previous drawings.
     * The color setting adjusts to the distance to the exit to provide an additional clue for the user.
     * Colors transition from black to gold and from grey to green.
     * Substitute for FirstPersonView.drawBackground method.
     *
     * execution: creates a new canvas for the panel's canvas var to erase previous drawings
     * draws two rectangles as background as background
     * uses percent to determine what color to draw the background rectangles
     *
     * @param percentToExit gives the distance to exit
     */
    @SuppressLint("NewApi")
    @Override
    public void addBackground(float percentToExit){

        float halfVar = new Float(49.9f);

        if (percentToExit < halfVar){
            int skyColor = Color.argb(255, 197, 214, 246);
            mazePaint.setColor(skyColor);
            addFilledRectangle(0, 0, width, height/2);
//            mazePaint.setStyle(Paint.Style.FILL);
//            mazeCanvas.drawRect(0, 500, 1000, 1000, mazePaint);

            int grassColor = Color.argb(255, 197, 246, 203);
            mazePaint.setColor(grassColor);
            addFilledRectangle(0,height/2, width, height/2);
//            mazePaint.setStyle(Paint.Style.FILL);
//            mazeCanvas.drawRect(0, 0, 1000, 500, mazePaint);
        } else {
            int eveningColor = Color.argb(255, 246, 239, 197);
            mazePaint.setColor(eveningColor);
            addFilledRectangle(0, 0, width, height/2);
//            mazePaint.setStyle(Paint.Style.FILL);
//            mazeCanvas.drawRect(0, 500, 1000, 1000, mazePaint);

            int grassColor = Color.argb(255, 197, 246, 203);
            mazePaint.setColor(grassColor);
            addFilledRectangle(0,height/2, width, height/2);
//            mazePaint.setStyle(Paint.Style.FILL);
//            mazeCanvas.drawRect(0, 0, 1000, 500, mazePaint);
        }


    }

    /**
     * Adds a filled rectangle.
     * The rectangle is specified with the {@code (x,y)} coordinates
     * of the upper left corner and then its width for the
     * x-axis and the height for the y-axis.
     * Substitute for Graphics.fillRect() method
     * @param x is the x-coordinate of the top left corner
     * @param y is the y-coordinate of the top left corner
     * @param width is the width of the rectangle
     * @param height is the height of the rectangle
     */
    @Override
    public void addFilledRectangle(int x, int y, int width, int height){
        mazePaint.setStyle(Paint.Style.FILL);
        mazeCanvas.drawRect((float) x, (float) y, (float) (x + width), (float) (y + height), mazePaint);
    }

    /**
     * Adds a filled polygon.
     * The polygon is specified with {@code (x,y)} coordinates
     * for the n points it consists of. All x-coordinates
     * are given in a single array, all y-coordinates are
     * given in a separate array. Both arrays must have
     * same length n. The order of points in the arrays
     * matter as lines will be drawn from one point to the next
     * as given by the order in the array.
     * Substitute for Graphics.fillPolygon() method
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints){
        mazePaint.setStyle(Paint.Style.FILL);


        Path polyPath = new Path();
        if (nPoints > 0){
            polyPath.moveTo(xPoints[0], yPoints[0]);
            for (int i = 1; i < nPoints; i++){
                polyPath.lineTo(xPoints[i], yPoints[i]);
            }
            polyPath.close();
        }
        mazeCanvas.drawPath(polyPath, mazePaint);

    }

    /**
     * Adds an unfilled polygon.
     * The polygon is specified with {@code (x,y)} coordinates
     * for the n points it consists of. All x-coordinates
     * are given in a single array, all y-coordinates are
     * given in a separate array. Both arrays must have
     * same length n. The order of points in the arrays
     * matter as lines will be drawn from one point to the next
     * as given by the order in the array.
     * Substitute for Graphics.drawPolygon method
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints){
        mazePaint.setStyle(Paint.Style.STROKE);

        Path polyPath = new Path();
        if (nPoints > 0){
            polyPath.moveTo(xPoints[0], yPoints[0]);
            for (int i = 1; i < nPoints; i++){
                polyPath.lineTo(xPoints[i], yPoints[i]);
            }
            polyPath.close();
        }
        mazeCanvas.drawPath(polyPath, mazePaint);
    }

    /**
     * Adds a line.
     * A line is described by {@code (x,y)} coordinates for its
     * starting point and its end point.
     * Substitute for Graphics.drawLine method
     * @param startX is the x-coordinate of the starting point
     * @param startY is the y-coordinate of the starting point
     * @param endX is the x-coordinate of the end point
     * @param endY is the y-coordinate of the end point
     */
    @Override
    public void addLine(int startX, int startY, int endX, int endY){
        mazePaint.setStyle(Paint.Style.FILL);

        mazeCanvas.drawLine((float) startX, (float) startY, (float) endX, (float) endY, mazePaint);
    }

    /**
     * Adds a filled oval.
     * The oval is specified with the {@code (x,y)} coordinates
     * of the upper left corner and then its width for the
     * x-axis and the height for the y-axis. An oval is
     * described like a rectangle.
     * Substitute for Graphics.fillOval method
     * @param x is the x-coordinate of the top left corner
     * @param y is the y-coordinate of the top left corner
     * @param width is the width of the oval
     * @param height is the height of the oval
     */
    @SuppressLint("NewApi")
    @Override
    public void addFilledOval(int x, int y, int width, int height){
        mazePaint.setStyle(Paint.Style.FILL);


        mazeCanvas.drawOval((float) x, (float) y, (float) (x + width), (float) (y + height), mazePaint);
    }

    /**
     * Adds the outline of a circular or elliptical arc covering the specified rectangle.
     * The resulting arc begins at startAngle and extends for arcAngle degrees,
     * using the current color. Angles are interpreted such that 0 degrees
     * is at the 3 o'clock position. A positive value indicates a counter-clockwise
     * rotation while a negative value indicates a clockwise rotation.
     * The center of the arc is the center of the rectangle whose origin is
     * (x, y) and whose size is specified by the width and height arguments.
     * The resulting arc covers an area width + 1 pixels wide
     * by height + 1 pixels tall.
     * The angles are specified relative to the non-square extents of
     * the bounding rectangle such that 45 degrees always falls on the
     * line from the center of the ellipse to the upper right corner of
     * the bounding rectangle. As a result, if the bounding rectangle is
     * noticeably longer in one axis than the other, the angles to the start
     * and end of the arc segment will be skewed farther along the longer
     * axis of the bounds.
     * Substitute for Graphics.drawArc method
     * @param x the x coordinate of the upper-left corner of the arc to be drawn.
     * @param y the y coordinate of the upper-left corner of the arc to be drawn.
     * @param width the width of the arc to be drawn.
     * @param height the height of the arc to be drawn.
     * @param startAngle the beginning angle.
     * @param arcAngle the angular extent of the arc, relative to the start angle.
     */
    @SuppressLint("NewApi")
    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle){
        mazePaint.setStyle(Paint.Style.STROKE);

        if (arcAngle > 0){
            mazeCanvas.drawArc((float) x, (float) y, (float) (x + width), (float) (y + height), (float) startAngle, (float) arcAngle, false, mazePaint);
        } else {
            int newStart = startAngle + arcAngle;
            int newSweep = 0 - arcAngle;

            if (newStart < 0){
                newStart = newStart + 360;
            }

            mazeCanvas.drawArc((float) x, (float) y, (float) (x + width), (float) (y + height), (float) newStart, (float) newSweep, false, mazePaint);
        }
    }


    /**
     * Adds a string at the given position.
     * Substitute for CompassRose.drawMarker method
     * @param x the x coordinate
     * @param y the y coordinate
     * @param str the string
     */
    @Override
    public void addMarker(float x, float y, String str){
        mazePaint.setStyle(Paint.Style.STROKE);

        mazeCanvas.drawText(str, x, y, mazePaint);
    }

    /**
     * An enumerated type to match 1-1 the awt.RenderingHints used
     * in CompassRose and MazePanel.
     */
    enum P7RenderingHints { KEY_RENDERING, VALUE_RENDER_QUALITY, KEY_ANTIALIASING, VALUE_ANTIALIAS_ON, KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR } ;

    /**
     * Sets the value of a single preference for the rendering algorithms.
     * It internally maps given parameter values into corresponding java.awt.RenderingHints
     * and assigns that to the internal graphics object.
     * Hint categories include controls for rendering quality
     * and overall time/quality trade-off in the rendering process.
     *
     * Refer to the awt RenderingHints class for definitions of some common keys and values.
     *
     * Note for Android: start with an empty default implementation.
     * Postpone any implementation efforts till the Android default rendering
     * results in unsatisfactory image quality.
     *
     * @param hintKey the key of the hint to be set.
     * @param hintValue the value indicating preferences for the specified hint category.
     */
    @Override
    public void setRenderingHint(P7PanelF22.P7RenderingHints hintKey, P7PanelF22.P7RenderingHints hintValue){
        //TODO fill
    }

    /**
     * helper method to test that drawing methods and panel updating actually works
     */
    private void myTestImage(Canvas c){
        setColor(Color.BLUE);
        addFilledOval(0, 0 , 500, 500);

    }

    /**
     * helper method meant to replace old add wall involving graphics
     * meant to translate over directly
     * also allows for shader to be set without interfering with drawing of non-wall polygons
     * @param xPoints
     * @param yPoints
     * @param nPoints
     */
    public void addWall(int[] xPoints, int[] yPoints, int nPoints) {
        Matrix shaderMatrix = new Matrix();
        float[] points = new float[nPoints * 2];
        shaderMatrix.mapPoints(points);
        mazeShader.setLocalMatrix(shaderMatrix);
        mazePaint.setShader(mazeShader);

        addFilledPolygon(xPoints, yPoints, nPoints);

        //resets shader back to old
        mazePaint.setShader(null);
    }

    /**
     * helper method to make the dimensions actaully flexible
     */
    public void setFlexibleDimensions(int widthInput, int heightInput){
        width = widthInput;
        height = heightInput;

    }

    /**
     * helper method to set fonts
     * @param font name
     */
    public void setFont(String font) {
        mazeFont = Typeface.create(font, Typeface.BOLD);
        mazePaint.setTypeface(mazeFont);
    }


}
