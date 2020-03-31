package mary.breathingtraining.view.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import mary.timerforstrel.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickStart(View v) {
        Intent intent = new Intent(MainActivity.this, ProgramsActivity.class);
        startActivity(intent);
    }

    public void onClickTutorial(View v) {
        Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
        startActivity(intent);    }
}
