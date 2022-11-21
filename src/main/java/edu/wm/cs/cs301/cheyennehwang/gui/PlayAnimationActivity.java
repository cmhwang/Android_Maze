package edu.wm.cs.cs301.cheyennehwang.gui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import edu.wm.cs.cs301.cheyennehwang.R;

public class PlayAnimationActivity extends AppCompatActivity {

    public int playSpeed;
    public SeekBar playSpeedBar;

    public int botEnergyUsed;
    public ProgressBar botRemainEnergy;

    public boolean showMap;

    public Intent transitionToEnd;

    public int playPauseCounter = 0;


    /**
     * Sets up any ui features that need additional specifications
     * - specfically here sets up switches for view mode, play speed, play/pause button, skip to end buttons
     * Sets up listener for when user ready to move to next state based on skip buttons
     * @param savedInstanceState is what is passed around
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playanimationlayout);

        beginPlayPause((Button) findViewById(R.id.playButton), playPauseCounter);


        //handles shortcuts for when you want to move to winning/losing screen
        skipEnd((Button) findViewById(R.id.skip2win), true);
        skipEnd((Button) findViewById(R.id.skip2lose), false);

    }

    /**
     * handles what happens if user presses back button
     * returns to default title screen
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        Toast toast = Toast.makeText(PlayAnimationActivity.this, "Return to Title", Toast.LENGTH_SHORT);
        toast.show();
        Log.v("Back Button Pressed", "Returned to Title");
        finish();

    }

    /**
     * handles action listener for play/pause button
     * changes text based on clicked
     * sends log and toast messages based on what's pressed
     */
    public void beginPlayPause(Button animPlayButton, int counter){
        animPlayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                //sets up the text to switch on the play button
                if (counter % 2 == 1){
                    animPlayButton.setText("PAUSE");
                    Log.v("Animation Control hit", "Play");

                    Toast toastPlay = Toast.makeText(PlayAnimationActivity.this, "Play Animation", Toast.LENGTH_SHORT);
                    toastPlay.show();
                } else {
                    animPlayButton.setText("PLAY");
                    Log.v("Animation Control hit", "Paused");

                    Toast toastPause = Toast.makeText(PlayAnimationActivity.this, "Pause Animation", Toast.LENGTH_SHORT);
                    toastPause.show();
                }
                playPauseCounter = playPauseCounter + 1;
            }

        }
        );
    }

    /**
     * gathers all the input data for maze animation watching
     * sends log message and toast message about the data gathered
     * also performs the navigation into WinningActivity/state winning or LosingActivity/stateLosting
     * takes either the win or lose buttons as input, differentiates vased on winPath var
     * @param nextButton is either the revisit or explore buttons, needed to set up action listener
     * @param winPath boolean to represent whether a winning button chosen
     */
    public void skipEnd(Button nextButton, Boolean winPath){
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //sets up seekbar for gathering robot energy used input and processes the input so it can be passed
                botRemainEnergy = (ProgressBar) findViewById(R.id.energyBar);
                botEnergyUsed = botRemainEnergy.getProgress();
                Log.v("Robot Energy Used", String.valueOf(botEnergyUsed));
                Toast toastEnergy = Toast.makeText(PlayAnimationActivity.this, "Robot Energy Used: " + String.valueOf(botEnergyUsed), Toast.LENGTH_SHORT);
                toastEnergy.show();

                //sets up seekbar for gathering play speed input and processes the input
                playSpeedBar = (SeekBar) findViewById(R.id.speedBar);
                playSpeed = playSpeedBar.getProgress();
                Log.v("Speed Level Setting", String.valueOf(playSpeed));
                Toast toastSpeed = Toast.makeText(PlayAnimationActivity.this, "Animation Speed: " + String.valueOf(playSpeed), Toast.LENGTH_SHORT);
                toastSpeed.show();

                // processes input from show map switch for whether to show map with maze + sln in view
                SwitchCompat showMapViewSwitch = findViewById(R.id.mapSwitch);
                showMap = showMapViewSwitch.isChecked();
                Log.v("Map View Set", String.valueOf(showMap));
                Toast toastMapView = Toast.makeText(PlayAnimationActivity.this, "Map View Set: " + String.valueOf(showMap), Toast.LENGTH_SHORT);
                toastMapView.show();


                // processes input for whether we go to state winning or losing
                if (winPath){
                    // branch for winning choice
                    Log.v("Switch To End Screen", "Win Version");
                    transitionToEnd = new Intent(PlayAnimationActivity.this, WinningActivity.class);
                    transitionToEnd.putExtra("energyUsage", botEnergyUsed);
                    transitionToEnd.putExtra("pathTakenLength", 100);
                    transitionToEnd.putExtra("shortestLength", 100);
                } else {
                    // branch for losing choice
                    Log.v("Switch To End Screen", "Win Version");
                    transitionToEnd = new Intent(PlayAnimationActivity.this, LosingActivity.class);
                    transitionToEnd.putExtra("energyUsage", botEnergyUsed);
                    transitionToEnd.putExtra("pathTakenLength", 100);
                    transitionToEnd.putExtra("shortestLength", 100);
                }

                // does the actual transition to the next stage and passes along the needed input
                Toast toast = Toast.makeText(PlayAnimationActivity.this, "Switch to Ending Screen: Won? " + String.valueOf(winPath), Toast.LENGTH_SHORT);
                toast.show();
                startActivity(transitionToEnd);
            }
        });
    }


}
