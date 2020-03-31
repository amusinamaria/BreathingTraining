package mary.breathingtraining.view.interfaces;

import mary.breathingtraining.model.SelectableProgram;

import java.util.ArrayList;

public interface IProgramsActivity {
    void showPrograms(ArrayList<SelectableProgram> programs);

    void goToTimer();

    void goToCreateProgram();

    void notifyAdapterItemChanged(int position);

    void notifyAdapterItemRemoved(int position);
}
