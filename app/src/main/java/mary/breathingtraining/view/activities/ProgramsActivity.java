package mary.breathingtraining.view.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import mary.breathingtraining.view.interfaces.IProgramsActivity;
import mary.timerforstrel.R;
import mary.breathingtraining.model.SelectableProgram;
import mary.breathingtraining.presenter.ProgramsPresenter;
import mary.breathingtraining.view.ProgramsAdapter;

import java.util.ArrayList;

public class ProgramsActivity extends AppCompatActivity implements IProgramsActivity {

    ProgramsPresenter programsPresenter;
    RecyclerView programsRecyclerView;
    ProgramsAdapter programsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        programsPresenter = new ProgramsPresenter(this, sharedPreferences);
        programsAdapter = new ProgramsAdapter(programsPresenter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        programsPresenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        programsPresenter.onResume();
    }

    public void showPrograms(ArrayList<SelectableProgram> programs) {
        programsRecyclerView = findViewById(R.id.programsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        programsRecyclerView.setLayoutManager(linearLayoutManager);
        programsRecyclerView.setAdapter(programsAdapter);
    }

    public void goToTimer() {
        Intent intent = new Intent(ProgramsActivity.this, TimerActivity.class);
        startActivity(intent);
    }

    public void onClickCreateProgram(View view) {
        programsPresenter.onClickedCreateProgram();
    }

    public void goToCreateProgram() {
        Intent intent = new Intent(ProgramsActivity.this, CreateProgramActivity.class);
        startActivity(intent);
    }

    public void notifyAdapterItemChanged(int position) {
        programsAdapter.notifyItemChanged(position);
    }

    public void notifyAdapterItemRemoved(int position) {
        programsAdapter.notifyItemRemoved(position);
    }

    public void onBackPressed() {
        if (!programsPresenter.deselectProgramOnBackPressedIfNeeded()) {
            super.onBackPressed();
        }
    }
}
