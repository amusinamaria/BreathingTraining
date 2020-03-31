package mary.breathingtraining.presenter;

import android.content.SharedPreferences;

import mary.breathingtraining.view.interfaces.ICreateProgramActivity;
import mary.breathingtraining.model.Exercise;
import mary.breathingtraining.model.Program;
import mary.breathingtraining.model.ProgramsData;
import mary.breathingtraining.view.interfaces.IExerciseRowView;

import java.util.ArrayList;

import static mary.breathingtraining.model.Program.*;

public class CreateProgramPresenter implements Presenter {

    private ProgramsData programsData;                          //model
    private ICreateProgramActivity iCreateProgramActivity;      //view

    private Program newProgram;
    private ArrayList<Exercise> exercises;


    public CreateProgramPresenter(ICreateProgramActivity iCreateProgramActivity, SharedPreferences sharedPreferences) {
        this.iCreateProgramActivity = iCreateProgramActivity;
        this.programsData = ProgramsData.getProgramsData(sharedPreferences);
        newProgram = new Program();
        String[] array = {PROSTOVDOHI, LADOSHKI, POGONCHIKI, NASOS, KOSHKA, OBNIMIPLECHI, BOLSHOYMAYATNIK, POVOROTIGOLOVI, USHKI, MAYATNIK_GOLOVOY, PEREKATY, SHAGI};
        exercises = new ArrayList<>();
        for (String exerciseTitle : array) {
            Exercise ex = new Exercise(exerciseTitle);
            exercises.add(ex);
        }
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
    }

    public boolean confirmExercisesIfNotNull() {
        ArrayList<String> selectedExercises = new ArrayList<>();
        for (Exercise e : exercises) {
            if (e.isSelected()) {
                selectedExercises.add(e.getText());
            }
        }
        if (selectedExercises.size() > 0) {
            newProgram.setExercises(selectedExercises);
            return true;
        } else {
            return false;
        }
    }

    public void onClickedSniffsCount(int sniffsCount) {
        newProgram.setSniffsCount(sniffsCount);
    }

    public void onClickedInterval(long interval) {
        newProgram.setInterval(interval);
    }

    public void onClickedConfirmName(String name) {
        newProgram.setTitle(name);
        programsData.saveProgram(newProgram);
        iCreateProgramActivity.onBackPressed();
    }

    //-------------------For_Adapter---------------------

    public void onBindExerciseRowViewAtPosition(int position, IExerciseRowView rowView) {
        rowView.setTitle(iCreateProgramActivity.getExerciseNameFromResources(exercises.get(position).getText()));
    }

    public int getExercisesRowsCount() {
        return exercises.size();
    }

    public void onItemClickedAtPosition(int position) {
        exercises.get(position).setSelected(!exercises.get(position).isSelected());
    }

    public boolean isExerciseSelected(int position) {
        return exercises.get(position).isSelected();
    }

}
