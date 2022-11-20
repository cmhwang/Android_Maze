package edu.wm.cs.cs301.cheyennehwang.gui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
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

    Spinner driverSpinner;
    Spinner botConfigSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generationlayout);

        //processes and builds spinner to accept input for robot driver configuration
        Spinner driverSpinner = (Spinner) findViewById(R.id.driverInput);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.driver_opt, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driverSpinner.setAdapter(adapter);

        //processes and builds spinner to accept input for robot sensor configuration
        Spinner botConfigSpinner = (Spinner) findViewById(R.id.robotConfigInput);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.config_opt, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        botConfigSpinner.setAdapter(adapter);


    }
}
