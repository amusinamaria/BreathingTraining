package mary.breathingtraining.view.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import mary.breathingtraining.model.Program;
import mary.breathingtraining.presenter.CreateProgramPresenter;
import mary.breathingtraining.view.ExercisesAdapter;
import mary.breathingtraining.view.interfaces.ICreateProgramActivity;
import mary.timerforstrel.R;

public class CreateProgramActivity extends AppCompatActivity implements ICreateProgramActivity {

    CreateProgramPresenter createProgramPresenter;

    View chooseExercises;
    View chooseSniffsCount;
    View chooseInterval;
    View chooseTitle;

    TextInputEditText titleEditText;
    RecyclerView exercisesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_program);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        createProgramPresenter = new CreateProgramPresenter(this, sharedPreferences);

        titleEditText = findViewById(R.id.titleInput);
        titleEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onClickConfirmTitle(v);
                    return true;
                }
                return false;
            }
        });

        chooseExercises = findViewById(R.id.choose_exercises);
        chooseSniffsCount = findViewById(R.id.choose_sniffs_count);
        chooseInterval = findViewById(R.id.choose_interval);
        chooseTitle = findViewById(R.id.choose_title);

        setChooseViewVisible(chooseExercises);
        showExercises();

    }

    private void setChooseViewVisible(View visibleView) {
        chooseExercises.setVisibility(visibleView.equals(chooseExercises) ? View.VISIBLE : View.GONE);
        chooseSniffsCount.setVisibility(visibleView.equals(chooseSniffsCount) ? View.VISIBLE : View.GONE);
        chooseInterval.setVisibility(visibleView.equals(chooseInterval) ? View.VISIBLE : View.GONE);
        chooseTitle.setVisibility(visibleView.equals(chooseTitle) ? View.VISIBLE : View.GONE);
    }

    //---------Exercises------------------

    public void showExercises() {
        exercisesRecyclerView = findViewById(R.id.exercisesRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        exercisesRecyclerView.setLayoutManager(linearLayoutManager);
        exercisesRecyclerView.setAdapter(new ExercisesAdapter(createProgramPresenter));
    }

    public String getExerciseNameFromResources(String name) {
        int sId = getResources().getIdentifier(name, "string", getPackageName());
        return getString(sId);
    }

    public void onClickConfirmExercises(View v) {
        boolean result = createProgramPresenter.confirmExercisesIfNotNull();
        if(result) {
            setChooseViewVisible(chooseSniffsCount);
        }
    }

//---------SniffsCount------------------

    public void onClick8(View view) {
        createProgramPresenter.onClickedSniffsCount(Program.SNIFFS_COUNT_EIGHT);
        setChooseViewVisible(chooseInterval);
    }

    public void onClick16(View view) {
        createProgramPresenter.onClickedSniffsCount(Program.SNIFFS_COUNT_SIXTEEN);
        setChooseViewVisible(chooseInterval);
    }

    public void onClick32(View view) {
        createProgramPresenter.onClickedSniffsCount(Program.SNIFFS_COUNT_THIRTYTWO);
        setChooseViewVisible(chooseInterval);
    }


    //---------Interval------------------

    public void onClickMedlenniy(View view) {
        createProgramPresenter.onClickedInterval(Program.MAX_INTERVAL);
        setChooseViewVisible(chooseTitle);
    }

    public void onClickSredniy(View view) {
        createProgramPresenter.onClickedInterval(Program.MEDIUM_INTERVAL);
        setChooseViewVisible(chooseTitle);
    }

    public void onClickBistriy(View view) {
        createProgramPresenter.onClickedInterval(Program.MIN_INTERVAL);
        setChooseViewVisible(chooseTitle);
    }

    //---------Title------------------

    public void onClickConfirmTitle(View v) {
        String title = titleEditText.getText().toString();
        createProgramPresenter.onClickedConfirmName(title);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

}
