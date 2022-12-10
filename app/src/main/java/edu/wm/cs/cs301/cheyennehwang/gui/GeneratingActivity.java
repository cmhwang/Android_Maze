package edu.wm.cs.cs301.cheyennehwang.gui;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.Vibrator;

import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.cheyennehwang.R;
import edu.wm.cs.cs301.cheyennehwang.generation.Factory;
import edu.wm.cs.cs301.cheyennehwang.generation.MazeFactory;
import edu.wm.cs.cs301.cheyennehwang.generation.MazeSettings;
import edu.wm.cs.cs301.cheyennehwang.generation.Maze;
import edu.wm.cs.cs301.cheyennehwang.generation.Order;

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

public class GeneratingActivity extends AppCompatActivity implements Runnable, Order {

    public Spinner driverSpinner;
    public Spinner botConfigSpinner;
    public SeekBar mazeProgress;

    // p6 thread elements
    private Thread loadingThread;
    public int loadProgress;
    private Handler handler = new Handler();

    public String botConfigSetting;
    public String driverSetting;

    public Boolean driverSet;

    public int skillLevel;
    enum Builder { DFS, Prim, Kruskal, Eller, Boruvka } ;
    public Builder builderAlgo;
    public boolean isPerfect;
    public boolean newMaze;
    public int seed;

    //replace with these for p7
    public Factory mazeFactory;
    public Handler mazeHandler;
    public Thread mazeThread;

    //music player
    public MediaPlayer music;
    //vibration creator
    public Vibrator vibrator;

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

        //starts music and plays song towards beginning of my neighbor totoro soundtrack
        music = MediaPlayer.create(GeneratingActivity.this,R.raw.mei_and_the_dust_bunnies);
        music.start();

        loadProgress = 0;

        //sets up vibration creator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //gathers prior values
        Intent transitionToGen = getIntent();
        //gets skill level
        skillLevel = transitionToGen.getIntExtra("diffLevel", 0);
        //sets generation algo
        if (transitionToGen.getStringExtra("generationAlgorithm").equalsIgnoreCase("Prim")){
            builderAlgo = Builder.Prim;
        } else if (transitionToGen.getStringExtra("generationAlgorithm").equalsIgnoreCase("Boruvka")){
            builderAlgo = Builder.Boruvka;
        } else {
            builderAlgo = Builder.DFS;
        }
        //set whether rooms
        if (transitionToGen.getBooleanExtra("roomsIn", true)){
            isPerfect = false;
        } else {
            isPerfect = true;
        }
        //sets whether visit or revisit
        if (transitionToGen.getBooleanExtra("explore", true)){
            newMaze = true;
        } else{
            newMaze = false;
        }
        //gets seed value
        seed = transitionToGen.getIntExtra("seedVal", 0);


        //processes and builds spinner to accept input for robot driver configuration
        driverSpinner = (Spinner) findViewById(R.id.driverInput);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.driver_opt, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driverSpinner.setAdapter(adapter);
        checkDriverInput(driverSpinner);

        //processes and builds spinner to accept input for robot sensor configuration
        botConfigSpinner = (Spinner) findViewById(R.id.robotConfigInput);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.config_opt, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        botConfigSpinner.setAdapter(adapter2);

        // sets up loading process
        mazeFactory = new MazeFactory();
        loadProgress = 0;
        mazeProgress = findViewById(R.id.buildProgress);
        mazeProgress.setProgress(loadProgress);
        checkSeekBarProgress(mazeProgress);

        // runs threads
        mazeHandler = new Handler();
        mazeThread = new Thread(this);
        mazeThread.start();

        // sets up thread that times the loading of the maze - for p6 just provides time frame to base progress updating on
//        new Thread(new Runnable() {
//            public void run() {
//                while (loadProgress < 100) {
//                    loadProgress += 1;
//                    handler.post(new Runnable() {
//                        public void run() {
//                            mazeProgress.setProgress(loadProgress);
//                        }
//                    });
//                    try {
//                        // Sleep for 200 milliseconds.
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();


    }

    /**
     * needed to implement order
     * gets skill level
     */
    @Override
    public int getSkillLevel(){
        return skillLevel;
    }

    /**
     * needed to implement order
     * gets builder/generation algo in order type
     * @return
     */
    @Override
    public Order.Builder getBuilder(){
        Intent transitionToGen = getIntent();
        if (transitionToGen.getStringExtra("generationAlgorithm").equalsIgnoreCase("Prim")){
            return Order.Builder.Prim;
        } else if (transitionToGen.getStringExtra("generationAlgorithm").equalsIgnoreCase("Boruvka")){
            return Order.Builder.Boruvka;
        } else {
            return Order.Builder.DFS;
        }
    }

    /**
     * needed to implement order
     * gets whether rooms included/perfect
     * @return
     */
    @Override
    public boolean isPerfect(){
        return isPerfect;
    }

    /**
     * needed to implement order
     * returns seed int
     * @return
     */
    @Override
    public int getSeed(){

        return seed;
    }

    /**
     * needed to implement order
     * delivers maze
     */
    @Override
    public void deliver(Maze myMaze){
        MazeSettings.getSettings().setMaze(myMaze);
    }

    /**
     * needed to implement order
     * updates progress
     */
    @Override
    public void updateProgress(int loadPercent){
        if (loadProgress < loadPercent && loadPercent <= 100){
            loadProgress = loadPercent;
            mazeProgress.setProgress(loadProgress);
        }
        //adds vibration effect
        if (loadPercent >= 100){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(500);
            }
        }
    }

    /**
     * handles what happens if user presses back button
     * returns to default title screen
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mazeFactory.cancel();
        mazeThread.interrupt();
        setResult(RESULT_CANCELED);
        Toast toast = Toast.makeText(GeneratingActivity.this, "Return to Title", Toast.LENGTH_SHORT);
        toast.show();
        Log.v("Back Button Pressed", "Returned to Title");
        mazeFactory = new MazeFactory() ;
        skillLevel = 0;
        builderAlgo = Builder.DFS;
        isPerfect = false;
        loadProgress = 0;
        seed = 0;
        mazeProgress.setProgress(loadProgress);
        music.stop();
        finish();

    }


    /**
     * Needed to clean up music
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //clean up
        mazeFactory = new MazeFactory();
        skillLevel = 0;
        builderAlgo = Builder.DFS;
        isPerfect = false;
        loadProgress = 0;
        seed = 0;
        mazeProgress.setProgress(0);
        music.stop();
    }

    /**
     * Helper method that handles responding to input for the driver setting
     * processes input selection from the driver spinner with action listeners
     * - sends log and toast message if input set
     * - if driver not set does nothing
     * if loading is finished: will also handle transition to play state
     * if loading unfinished: sends a message that once loading done will move
     * - checks this by checking loading progress var
     * @param spinner4Driver is a reference to the spinner for entering driver input
     */

    public void checkDriverInput(Spinner spinner4Driver){
        spinner4Driver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id){
                driverSetting = driverSpinner.getSelectedItem().toString();
                Log.v("Driver Set", driverSetting);

                if (driverSetting.equalsIgnoreCase("Manual")){

                    if (loadProgress >= 100){
                        // processes input from robot configuration spinner
                        botConfigSetting = botConfigSpinner.getSelectedItem().toString();
                        Log.v("Robot Configuration Set", botConfigSetting);

                        // does the actual transition to the next stage and passes along the needed input
                        Intent transitionToPlay = new Intent(GeneratingActivity.this, PlayManuallyActivity.class);
                        transitionToPlay.putExtra("driver", driverSetting);
                        transitionToPlay.putExtra("sensorConfig", botConfigSetting);

                        Toast toast5 = Toast.makeText(GeneratingActivity.this, "Start maze play with driver: " + driverSetting + ", robot configuration: " + botConfigSetting, Toast.LENGTH_SHORT);
                        toast5.show();
                        music.stop();
                        startActivity(transitionToPlay);
                    } else {
                        Log.v("Waiting on Generation", "Play Will Begin Soon");
                        Toast toastA = Toast.makeText(GeneratingActivity.this, "Driver Set, Maze play will start soon", Toast.LENGTH_SHORT);
                        toastA.show();
                    }

                } else if (driverSetting.equalsIgnoreCase("Wizard") || driverSetting.equalsIgnoreCase("WallFollower")){

                    if (loadProgress >= 100){
                        // processes input from robot configuration spinner
                        botConfigSetting = botConfigSpinner.getSelectedItem().toString();
                        Log.v("Robot Configuration Set", botConfigSetting);

                        // does the actual transition to the next stage and passes along the needed input
                        Intent transitionToPlay = new Intent(GeneratingActivity.this, PlayAnimationActivity.class);
                        transitionToPlay.putExtra("driver", driverSetting);
                        transitionToPlay.putExtra("sensorConfig", botConfigSetting);

                        Toast toast2 = Toast.makeText(GeneratingActivity.this, "Start maze play with driver: " + driverSetting + ", robot configuration: " + botConfigSetting, Toast.LENGTH_SHORT);
                        toast2.show();
                        music.stop();
                        startActivity(transitionToPlay);
                    } else {
                        //branch for if load not done
                        Log.v("Waiting on Generation", "Play Will Begin Soon");
                        Toast toastA = Toast.makeText(GeneratingActivity.this, "Driver Set, Maze play will start soon", Toast.LENGTH_SHORT);
                        toastA.show();
                    }

                } else {
                    Log.v("Driver Setting Null", "Please Select Driver");
                    Toast toast1 = Toast.makeText(GeneratingActivity.this, "Please Select a Driver", Toast.LENGTH_SHORT);
                    toast1.show();
                }

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.v("Driver Setting Null", "Please Select Driver");
                Toast toast6 = Toast.makeText(GeneratingActivity.this, "Please Select a Driver", Toast.LENGTH_SHORT);
                toast6.show();
            }
        });

    }
    /**
     * helper method that handles transition in case: load done and robot driver set, called as loading bar progress and reacts after loading bar finishes
     * if the driver not set sends log alert to set one
     * if driver set: gathers the robot configuration parameter and sends related log message
     * - sends log and toast message about driver and robot configuration setting
     * - handles transition into either play manual or play animation depending on driver setting
     * - should be null if nothing selected yet
     * @param loadProgressBar the loading seekbar
     */
    public void checkSeekBarProgress(SeekBar loadProgressBar){
        loadProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar loadProgressBar, int i, boolean b) {
                if (loadProgressBar.getProgress() >= 100){

                    driverSetting = driverSpinner.getSelectedItem().toString();

                    if (driverSetting.equalsIgnoreCase("Manual")){
                        Log.v("Driver Set", driverSetting);
                        // processes input from robot configuration spinner
                        botConfigSetting = botConfigSpinner.getSelectedItem().toString();
                        Log.v("Robot Configuration Set", botConfigSetting);

                        // does the actual transition to the next stage and passes along the needed input
                        Intent transitionToPlay = new Intent(GeneratingActivity.this, PlayManuallyActivity.class);
                        transitionToPlay.putExtra("driver", driverSetting);
                        transitionToPlay.putExtra("sensorConfig", botConfigSetting);

                        Toast toast5 = Toast.makeText(GeneratingActivity.this, "Start maze play with driver: " + driverSetting + ", robot configuration: " + botConfigSetting, Toast.LENGTH_SHORT);
                        toast5.show();
                        music.stop();
                        startActivity(transitionToPlay);

                    } else if (driverSetting.equalsIgnoreCase("Wizard") || driverSetting.equalsIgnoreCase("WallFollower")){
                        Log.v("Driver Set", driverSetting);
                        // processes input from robot configuration spinner
                        botConfigSetting = botConfigSpinner.getSelectedItem().toString();
                        Log.v("Robot Configuration Set", botConfigSetting);

                        // does the actual transition to the next stage and passes along the needed input
                        Intent transitionToPlay = new Intent(GeneratingActivity.this, PlayAnimationActivity.class);
                        transitionToPlay.putExtra("driver", driverSetting);
                        transitionToPlay.putExtra("sensorConfig", botConfigSetting);

                        Toast toast2 = Toast.makeText(GeneratingActivity.this, "Start maze play with driver: " + driverSetting + ", robot configuration: " + botConfigSetting, Toast.LENGTH_SHORT);
                        toast2.show();
                        music.stop();
                        startActivity(transitionToPlay);


                    } else {
                        Log.v("Driver Setting Null", "Please Select Driver");
                        Toast toast1 = Toast.makeText(GeneratingActivity.this, "Please Select a Driver", Toast.LENGTH_SHORT);
                        toast1.show();
                    }

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar loadProgressbar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar loadProgressBar) {

            }

        });
    }

    /**
    runs maze generation thread
     */
    @Override
    public void run(){
        mazeFactory.order(this);
        mazeFactory.waitTillDelivered();
        mazeHandler.post(new Runnable() {
            public void run() {
                Log.v("Maze Generation", "Done!");
                checkDriverInput(driverSpinner);
                assert (MazeSettings.getSettings().getMaze() != null) : "Maze Generation Failed!";
            }
        });
    }



}
