package edu.wm.cs.cs301.cheyennehwang.gui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

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

    public Spinner driverSpinner;
    public Spinner botConfigSpinner;

    private Thread loadingThread;
    public int loadProgress;
    private Handler handler = new Handler();

    public String botConfigSetting;
    public String driverSetting;

    /**
     * Sets up any ui features that need additional specifications
     * - specfically here sets up sinner for accepting driver input and robot configuration
     * - also begins and runs the loading thread that updates the maze generation progress bar
     * - in p6 the progress bar just updated based on time
     * Sets up listeners for the maze progress bar and driver inout spinner when user ready to move to next state
     * @param savedInstanceState is what is passed around
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generationlayout);

        //processes and builds spinner to accept input for robot driver configuration
        driverSpinner = (Spinner) findViewById(R.id.driverInput);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.driver_opt, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driverSpinner.setAdapter(adapter);
        respondToDriverSetting(driverSpinner);

        //processes and builds spinner to accept input for robot sensor configuration
        botConfigSpinner = (Spinner) findViewById(R.id.robotConfigInput);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.config_opt, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        botConfigSpinner.setAdapter(adapter2);

        // sets up thread that times the loading of the maze - for p6 just provides time frame to base progress updating on
        loadProgress = 0;
        SeekBar mazeProgress = findViewById(R.id.buildProgress);
        new Thread(new Runnable() {
            public void run() {
                while (loadProgress < 100) {
                    loadProgress += 5;
                    handler.post(new Runnable() {
                        public void run() {
                            mazeProgress.setProgress(loadProgress);
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //checkAfterProgressLoad(driverSetting);
            }
        }).start();

        // checks to see if there is a driver input, if no driver input send toast message for one, if there is then next screen
        checkAfterProgressLoad(driverSetting);
    }

    /**
     * handles what happens if user presses back button
     * returns to default title screen
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        Toast toast = Toast.makeText(GeneratingActivity.this, "Return to Title", Toast.LENGTH_SHORT);
        toast.show();
        Log.v("Back Button Pressed", "Returned to Title");
        finish();

    }

    /**
     * helper method that handles transition in case: load done and robot driver set, called after loading bar finishes
     * if the driver not set sends log alert to set one
     * if driver set: gathers the robot configuration parameter and sends related log message
     * - sends log and toast message about driver and robot configuration setting
     * - handles transition into either play manual or play animation depending on driver setting
     * @param driverSetting is the param that would hold the var for the driver setting
     * - should be null if nothing selected yet
     */
    public void checkAfterProgressLoad(String driverSetting){
        if (driverSetting == null){
            Log.v("Driver Setting Null", "Please Select Driver");
            Toast toast1 = Toast.makeText(GeneratingActivity.this, "Please Select a Driver", Toast.LENGTH_SHORT);
            toast1.show();
        } else {
            // processes input from robot configuration spinner
            botConfigSetting = botConfigSpinner.getSelectedItem().toString();
            Log.v("Robot Configuration Set", botConfigSetting);

            // does the actual transition to the next stage and passes along the needed input
            Intent transitionToPlay;
            if (driverSetting.equalsIgnoreCase("manual")){
                transitionToPlay = new Intent(GeneratingActivity.this, PlayManuallyActivity.class);
                transitionToPlay.putExtra("driver", driverSetting);
                transitionToPlay.putExtra("sensorConfig", botConfigSetting);
            } else {
                transitionToPlay = new Intent(GeneratingActivity.this, PlayAnimationActivity.class);
                transitionToPlay.putExtra("driver", driverSetting);
                transitionToPlay.putExtra("sensorConfig", botConfigSetting);
            }

            Toast toast2 = Toast.makeText(GeneratingActivity.this, "Start maze play with driver: " + driverSetting + ", robot configuration: " + botConfigSetting, Toast.LENGTH_SHORT);
            toast2.show();
            startActivity(transitionToPlay);
        }
    }

    /**
     * Helper method that handles responding to input for the driver setting
     * processes input selection from the driver spinner with action listeners
     * - sends log and toast message when set
     * if loading is finished: will also handle transition to play state
     * if loading unfinished: sends a message that once loading done will move
     * - checks this by checking loading progress var
     * @param spinHolder is a reference to the spinner for entering driver input
     */
    public void respondToDriverSetting(Spinner spinHolder){

    }


}
