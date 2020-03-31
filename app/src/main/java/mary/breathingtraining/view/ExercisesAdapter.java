package mary.breathingtraining.view;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import mary.breathingtraining.presenter.CreateProgramPresenter;
import mary.timerforstrel.R;

public class ExercisesAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {

    private final CreateProgramPresenter presenter;

    public ExercisesAdapter(CreateProgramPresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExerciseViewHolder(presenter, LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        presenter.onBindExerciseRowViewAtPosition(position, holder);

    }

    @Override
    public int getItemCount() {
        return presenter.getExercisesRowsCount();
    }
}