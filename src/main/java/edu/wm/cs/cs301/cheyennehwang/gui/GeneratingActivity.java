package edu.wm.cs.cs301.cheyennehwang.gui;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.cheyennehwang.R;

/**
 * Class: equivalent to StateGenerating
 * @author cheyennehwang
 *
 * Responsibilities: displays maze loading screen and sets driver + robot configuration
 * - keeps player engaged with progressbar, sets robot driver type, sets configuraton type
 * - also offers back navigation button option which will return to AMazeActivity/StateTitle screen and forward navigation to PlayAnimationActivity/StatePlayAnimation or PlayManuallyActivity/StatePlayManually
 *
 * Collaborators: AMazeActivity (StateTitle), PlayManuallyActivity (StatePlayManually), PlayAnimationActivity (StatePlayAnimation), UI, MazeFactory, Control
 *
 */

public class GeneratingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generationlayout);
    }
}
