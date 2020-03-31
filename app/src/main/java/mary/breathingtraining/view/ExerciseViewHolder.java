package mary.breathingtraining.view;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import mary.breathingtraining.presenter.CreateProgramPresenter;
import mary.timerforstrel.R;
import mary.breathingtraining.view.interfaces.IExerciseRowView;

public class ExerciseViewHolder extends RecyclerView.ViewHolder implements IExerciseRowView {

    private TextView title;

    ExerciseViewHolder(final CreateProgramPresenter presenter, View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.exerciseTitle);
        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onItemClickedAtPosition(getAdapterPosition());
                v.setBackgroundColor(presenter.isExerciseSelected(getAdapterPosition()) ? Color.parseColor("#4a506c") : Color.parseColor("#37394f"));
            }
        });
    }

    @Override
    public void setTitle(String text) {
        title.setText(text);
    }
}