package edu.wm.cs.cs301.cheyennehwang.gui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import edu.wm.cs.cs301.cheyennehwang.R;
import android.widget.Toast;


public class AMazeActivity extends AppCompatActivity{
    /**
     * Class: equivalent to state title
     *
     * Responsibilities: Display welcome an gather input parameters
     * set maze complexity, room input, maze generation algorithm, replay option
     *
     * Collaborators: Generating Activity (State Generating), Control, UI, MazeFactory
     */

    SeekBar levelSeekBar;
    TextView titleText;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        levelSeekBar=(SeekBar)findViewById(R.id.levelSeekBar);
        titleText = (TextView) findViewById(R.id.textView1);
    }

}

