package edu.wm.cs.cs301.cheyennehwang.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import edu.wm.cs.cs301.cheyennehwang.R;
import android.widget.Toast;

/**
 * Class: equivalent to state title
 * @author cheyennehwang
 *
 * Responsibilities: Display welcome an gather input parameters
 * - set maze complexity, room input, maze generation algorithm, replay option
 *
 * Collaborators: Generating Activity (State Generating), Control, UI, MazeFactory
 */

public class AMazeActivity extends AppCompatActivity {



    public int mazeLevel;
    public String genAlgo;
    public boolean incRooms;
    Spinner genAlgoSpinner;
    SeekBar sizeSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        Log.v("Maze App Launched", "Success");


        //sets up and handles spinner that processes input for maze generation algo
        Spinner genAlgoSpinner = (Spinner) findViewById(R.id.genAlgoInput);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.algo_opt, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genAlgoSpinner.setAdapter(adapter);


        //aggregates all the maze generation input and performs process to move to next screen, based on whether revisit or explore clicked
        goNext((Button) findViewById(R.id.exploreButton), true);
        goNext((Button) findViewById(R.id.revisitButton), false);



    }

    /**
     * gathers all the input data for maze generation
     * sends log message about the data gathered
     * also performs the navigation into GeneratingActivity/state generating
     * takes either the revisit or explore buttons as input
     * @param nextButton is either the revisit or explore buttons, needed to set up action listener
     * @param newMaze boolean to represent whether a new maze created, irrelevant for p6
     */

    public void goNext(Button nextButton, boolean newMaze) {
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //sets up seekbar for gathering maze level input and processes the input
                sizeSeekBar = (SeekBar) findViewById(R.id.levelSeekBar);
                mazeLevel = sizeSeekBar.getProgress();
                Log.v("Maze Level Set", String.valueOf(mazeLevel));

                // processes input from room switch for whether to include rooms
                SwitchCompat roomSwitch = findViewById(R.id.roomSwitch);
                incRooms = roomSwitch.isChecked();
                Log.v("Room Inclusion Set", String.valueOf(incRooms));

                // processes input for maze generation algorithm
                Spinner genAlgoSpinner = (Spinner) findViewById(R.id.genAlgoInput);
                genAlgo = genAlgoSpinner.getSelectedItem().toString();
                Log.v("Maze Generation Set", genAlgo);

                // processes input for whether we are creating new maze or using old one - not fully acted on in P6
                if (newMaze){
                    // branch for explore being hit, new maze
                    Log.v("Revisit or Explore", "Explore New");
                } else {
                    // branch for revisit being hit, for now do the same input
                    // in the future: look through old log of seeds for an old seed
                    Log.v("Revisit or Explore", "Revisit Old");
                }

                // does the actual transition to the next stage and passes along the needed input
                Intent transitionToGen = new Intent(AMazeActivity.this, GeneratingActivity.class);
                transitionToGen.putExtra("diffLevel", mazeLevel);
                transitionToGen.putExtra("generationAlgorithm", genAlgo);
                transitionToGen.putExtra("roomsIn", incRooms);

                Toast toast = Toast.makeText(AMazeActivity.this, "Begin Maze Generation", Toast.LENGTH_SHORT);
                toast.show();
                startActivity(transitionToGen);
            }
        });
    }



}

