package edu.wm.cs.cs301.cheyennehwang.gui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import edu.wm.cs.cs301.cheyennehwang.R;

public class PlayManuallyActivity extends AppCompatActivity {

    public int stepsTaken = 0;

    public ProgressBar botRemainEnergy;
    public int botEnergyUsed;

    public SeekBar scaleSeekBar;
    public int scaleVar;

    public SwitchCompat mazeViewSwitch;
    public boolean showMaze;

    public SwitchCompat wallsViewSwitch;
    public boolean showWalls;

    public SwitchCompat solutionViewSwitch;
    public boolean showSolution;

    public Intent transitionToEnd;



    /**
     * Sets up any ui features that need additional specifications
     * - specfically here sets up switches for map view mode, scale view size, walls view mode, solution view mode, controls, shortcut to end button
     * Sets up listener for when user ready to move to next state based on shortcut button
     * @param savedInstanceState is what is passed around
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playmanuallayout);

        //sets up ui implementation for maze view switches
        mazeViewSwitch = (SwitchCompat) findViewById(R.id.mazewidth);
        wallsViewSwitch = (SwitchCompat) findViewById(R.id.wallSwitch);
        solutionViewSwitch = (SwitchCompat) findViewById(R.id.slnSwitch);

        //sets up listeners for control navigation buttons
        controlHandler((Button) findViewById(R.id.forwardButton), 0);
        controlHandler((Button) findViewById(R.id.leftButton), 1);
        controlHandler((Button) findViewById(R.id.rightbutton), 2);
        controlHandler((Button) findViewById(R.id.jumpButton), 3);

        //sets up listener for shortcut button
        skipEnd((Button) findViewById(R.id.shortcutButton));

    }
    /**
     * handles action listener for navigation control button
     * increments path length taken var if its jump or forward button that's pressed
     * knows which one pressed based on dir var: 0 = f, 1 = l, 2 = r, 3 = jump
     */
    public void controlHandler(Button controlButton, int dir){
        controlButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                //increments path length and sends log based on type of button
                if (dir == 0){
                    stepsTaken++;
                    Log.v("Navigation Control Hit", "Forward");
                    Toast toastF = Toast.makeText(PlayManuallyActivity.this, "Forward Control Hit", Toast.LENGTH_SHORT);
                    toastF.show();
                } else if (dir == 1){
                    Log.v("Navigation Control Hit", "Left");
                    Toast toastL = Toast.makeText(PlayManuallyActivity.this, "Left Control Hit", Toast.LENGTH_SHORT);
                    toastL.show();
                } else if (dir == 2){
                    Log.v("Navigation Control Hit", "Right");
                    Toast toastR = Toast.makeText(PlayManuallyActivity.this, "Right Control Hit", Toast.LENGTH_SHORT);
                    toastR.show();
                } else {
                    stepsTaken++;
                    Log.v("Navigation Control Hit", "Jump");
                    Toast toastJ = Toast.makeText(PlayManuallyActivity.this, "Jump Control Hit", Toast.LENGTH_SHORT);
                    toastJ.show();
                }
            }

        }
        );
    }

    /**
     * handles what happens if user presses back button
     * returns to default title screen
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        Toast toast = Toast.makeText(PlayManuallyActivity.this, "Return to Title", Toast.LENGTH_SHORT);
        toast.show();
        Log.v("Back Button Pressed", "Returned to Title");
        finish();

    }

    /**
     * gathers all the input data for maze playing view
     * sends log message and toast message about the data gathered
     * also performs the navigation into StateWinning/WinningActivity
     * @param nextButton is the shortcut button hit
     */
    public void skipEnd(Button nextButton){
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //sets up seekbar for gathering robot energy used input and processes the input so it can be passed
                botRemainEnergy = (ProgressBar) findViewById(R.id.energyBar);
                botEnergyUsed = botRemainEnergy.getProgress();
                Log.v("Robot Energy Used", String.valueOf(botEnergyUsed));
                Toast toastEnergy = Toast.makeText(PlayManuallyActivity.this, "Robot Energy Used: " + String.valueOf(botEnergyUsed), Toast.LENGTH_SHORT);
                toastEnergy.show();

                //sets up seekbar for gathering maze scale input and processes the input
                scaleSeekBar = (SeekBar) findViewById(R.id.sizeBar);
                scaleVar = scaleSeekBar.getProgress();
                Log.v("Maze Scale Setting", String.valueOf(scaleVar));
                Toast toastScale = Toast.makeText(PlayManuallyActivity.this, "Maze Scale: " + String.valueOf(scaleVar), Toast.LENGTH_SHORT);
                toastScale.show();

                // processes input from show maze switch for whether to show maze in view
                mazeViewSwitch = findViewById(R.id.mazewidth);
                showMaze = mazeViewSwitch.isChecked();
                Log.v("Maze View Set", String.valueOf(showMaze));
                Toast toastMazeView = Toast.makeText(PlayManuallyActivity.this, "Maze View Set: " + String.valueOf(showMaze), Toast.LENGTH_SHORT);
                toastMazeView.show();

                // processes input from show walls switch for whether to show walls in view
                wallsViewSwitch = findViewById(R.id.wallSwitch);
                showWalls = wallsViewSwitch.isChecked();
                Log.v("Walls View Set", String.valueOf(showWalls));
                Toast toastWallsView = Toast.makeText(PlayManuallyActivity.this, "Walls View Set: " + String.valueOf(showWalls), Toast.LENGTH_SHORT);
                toastWallsView.show();

                // processes input from show solution switch for whether to show solution in view
                solutionViewSwitch = findViewById(R.id.slnSwitch);
                showSolution = solutionViewSwitch.isChecked();
                Log.v("Solution View Set", String.valueOf(showSolution));
                Toast toastSlnView = Toast.makeText(PlayManuallyActivity.this, "Solution View Set: " + String.valueOf(showSolution), Toast.LENGTH_SHORT);
                toastSlnView.show();

                // does the actual transition to the next stage and passes along the needed input
                Log.v("Switch To End Screen", "Win Screen");
                transitionToEnd = new Intent(PlayManuallyActivity.this, WinningActivity.class);
                transitionToEnd.putExtra("energyUsage", botEnergyUsed);
                transitionToEnd.putExtra("pathTakenLength", stepsTaken);
                transitionToEnd.putExtra("shortestLength", 100);
                Toast toast = Toast.makeText(PlayManuallyActivity.this, "Switch to Ending Screen, Path Length Taken: " + String.valueOf(stepsTaken), Toast.LENGTH_SHORT);
                toast.show();
                startActivity(transitionToEnd);
            }
        });
    }
}
