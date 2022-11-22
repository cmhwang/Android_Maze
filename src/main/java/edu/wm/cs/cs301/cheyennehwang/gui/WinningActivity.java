package edu.wm.cs.cs301.cheyennehwang.gui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import edu.wm.cs.cs301.cheyennehwang.R;

/**
 * Class: equivalent to State Winning
 * @author cheyennehwang
 *
 * Responsibilities: displays finish page for winning scenario
 * - naviagate back to AMazeActivity (State Title) as an optoin
 * - displays path length taken displays shortest path length possible, displays robot energy consumption (if applicable)
 *
 * Collaborators: AMazeActivity (State Title), PlayAnimationActivity (State PlayAnimation), PlayManuallyActivity (State PlayManually), UI, MazeFactory, Control
 *
 */

public class WinningActivity extends AppCompatActivity {


    /**
     * Sets up any ui features that need additional specifications
     * - specfically here sets uptext fields for shortest path, path taken, energy used, and replay button
     * Sets up listener for when user ready to move to next state title again if replay button hit button
     * @param savedInstanceState is what is passed around
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winlayout);

        // updates text field for your path taken
        Intent transitionToEnd = getIntent();
        String pathVar = transitionToEnd.getStringExtra("pathTakenLength");
        TextView yourPathField = (TextView)  findViewById(R.id.yourPathInput);
        yourPathField.setText(pathVar);

        // updates text field for shortest path taken
        TextView shortestPathField = (TextView)  findViewById(R.id.shortestPathInput);
        String shortestVar = transitionToEnd.getStringExtra("shortestLength");
        shortestPathField.setText(shortestVar);

        // updates text field for robot energy used
        TextView energyUsedField = (TextView)  findViewById(R.id.finalEnergyInput);
        String energyUsed = transitionToEnd.getStringExtra("energyUsage");
        energyUsedField.setText(energyUsed);



        returnToTitle((Button) findViewById(R.id.winPlayAgain));

    }

    /**
     * sets up action listener for replay button
     * performs return to StateTitle/AMAzeActivity when hit
     * @param nextButton is reference to the replay button
     */
    public void returnToTitle(Button nextButton) {
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.v("Replay Button Pressed", "Returned to Title");
                Intent transitionToStart = new Intent(WinningActivity.this, AMazeActivity.class);
                Toast toast = Toast.makeText(WinningActivity.this, "Return to Title Screen", Toast.LENGTH_SHORT);
                toast.show();
                startActivity(transitionToStart);
            }
        });
    }


    /**
     * handles what happens if user presses back button
     * returns to default title screen
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        Toast toast = Toast.makeText(WinningActivity.this, "Return to Title", Toast.LENGTH_SHORT);
        toast.show();
        Log.v("Back Button Pressed", "Returned to Title");
        finish();

    }
}
