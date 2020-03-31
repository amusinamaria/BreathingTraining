package mary.breathingtraining.presenter;

import android.content.SharedPreferences;

import mary.breathingtraining.model.ProgramsData;
import mary.breathingtraining.model.SelectableProgram;
import mary.breathingtraining.view.interfaces.IProgramRowView;
import mary.breathingtraining.view.interfaces.IProgramsActivity;

import java.util.ArrayList;

public class ProgramsPresenter implements Presenter {

    private ProgramsData programsData;                //model
    private IProgramsActivity iProgramsActivity;      //view

    private ArrayList<SelectableProgram> selectablePrograms;
    private SelectableProgram selectedProgram = null;


    public ProgramsPresenter(IProgramsActivity iProgramsActivity, SharedPreferences sharedPreferences) {
        this.iProgramsActivity = iProgramsActivity;
        this.programsData = ProgramsData.getProgramsData(sharedPreferences);
        programsData.createDefaultProgramsIfNeeded();
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onResume() {
        getPrograms();
        showPrograms();
    }

    private void getPrograms() {
        selectablePrograms = programsData.convertProgramsToSelectablePrograms();
    }

    private void showPrograms() {
        iProgramsActivity.showPrograms(selectablePrograms);
    }

    public void onClickedCreateProgram() {
        iProgramsActivity.goToCreateProgram();
    }


    //-----------------For adapter and single choice selection------------------------------

    public void onBindProgramRowViewAtPosition(int position, IProgramRowView rowView) {
        SelectableProgram selectableProgram = selectablePrograms.get(position);
        rowView.setTitle(selectableProgram.getTitle());
        rowView.setSelected(selectableProgram.isSelected());
    }

    public int getProgramsRowsCount() {
        return selectablePrograms.size();
    }

    public void onItemClickedAtPosition(int position) {
        programsData.saveChosenProgramIdToSP(selectablePrograms.get(position).getId());
        iProgramsActivity.goToTimer();
    }

    public boolean isAnyProgramSelected() {
        return selectedProgram != null;
    }

    public boolean isProgramSelected(int position) {
        return isAnyProgramSelected() && selectedProgram.equals(selectablePrograms.get(position));
    }

    public void deselectProgram() {
        for (int i = 0; i < selectablePrograms.size(); i++) {
            if (selectablePrograms.get(i).isSelected()) {
                selectablePrograms.get(i).setSelected(false);
                iProgramsActivity.notifyAdapterItemChanged(i);
                selectedProgram = null;
                break;
            }
        }
    }

    public void setProgramSelected(int position) {
        selectedProgram = selectablePrograms.get(position);
        selectablePrograms.get(position).setSelected(true);
        iProgramsActivity.notifyAdapterItemChanged(position);
    }

    public void deleteSelectedProgram() {
        for (int i = 0; i < selectablePrograms.size(); i++) {
            if (selectablePrograms.get(i).isSelected()) {
                programsData.deleteProgramFromRealm(selectablePrograms.get(i));
                selectablePrograms.remove(i);
                iProgramsActivity.notifyAdapterItemRemoved(i);
                break;
            }
        }
        selectedProgram = null;
    }

    public boolean deselectProgramOnBackPressedIfNeeded() {
        if (isAnyProgramSelected()) {
            deselectProgram();
            return true;
        } else {
            return false;
        }
    }
}
