package edu.wm.cs.cs301.cheyennehwang.gui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.cheyennehwang.R;

/**
 * Class: equivalent to State Losing
 * @author cheyennehwang
 *
 * Responsibilities; displays finish page in losing scenario
 * - navigate back to AAMzeActivity/State Title based on input
 * - diaplay path length taken, display shortest path length possible, display robot energy used (if applicable)
 *
 * Collaborators: PlayAnimationActivity (State PlayAnimation), PlayManuallyActivity (State PlayManually), AMazeActivity (State Title), UI, MazeFactory, Control
 *
 */

public class LosingActivity extends AppCompatActivity {

    /**
     * Sets up any ui features that need additional specifications
     * - specifically here sets up text fields for lose reson, shortest path, path taken, energy used, and replay button
     * Sets up listener for when user ready to move to next state title again if replay button hit button
     * @param savedInstanceState is what is passed around
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loselayout);

        // updates text field for reason you lost
        TextView xpLossField = (TextView)  findViewById(R.id.xpLossInput);
        // holder var until p7 implementation
        String temp0 = "IDK - P6";
        xpLossField.setText(temp0);

        // updates text field for your path taken
        TextView yourPathField = (TextView)  findViewById(R.id.yourLosePathInput);
        // holder var until p7 implementation
        String temp = "50";
        yourPathField.setText(temp);

        // updates text field for shortest path taken -
        TextView shortestPathField = (TextView)  findViewById(R.id.loseShortestPathInput);
        // holder var until p7 implementation
        String temp1 = "100";
        shortestPathField.setText(temp1);

        // updates text field for robot energy used
        TextView energyUsedField = (TextView)  findViewById(R.id.finalEnergyInput);
        // holder var until p7 implementation
        String temp2 = "3500";
        energyUsedField.setText(temp2);

        returnToTitle((Button) findViewById(R.id.losePlayAgain));

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
                Intent transitionToStart = new Intent(LosingActivity.this, AMazeActivity.class);
                Toast toast = Toast.makeText(LosingActivity.this, "Return to Title Screen", Toast.LENGTH_SHORT);
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
        Toast toast = Toast.makeText(LosingActivity.this, "Return to Title", Toast.LENGTH_SHORT);
        toast.show();
        Log.v("Back Button Pressed", "Returned to Title");
        finish();

    }
}

