package mary.breathingtraining.view;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import mary.timerforstrel.R;
import mary.breathingtraining.presenter.ProgramsPresenter;
import mary.breathingtraining.view.interfaces.IProgramRowView;

public class ProgramViewHolder extends RecyclerView.ViewHolder implements IProgramRowView {

    private TextView title;
    private ImageView trash;

    ProgramViewHolder(final ProgramsPresenter programsPresenter, View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.programTitle);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (programsPresenter.isAnyProgramSelected()) {
                    programsPresenter.deselectProgram();
                } else {
                    programsPresenter.onItemClickedAtPosition(getAdapterPosition());
                }
            }
        });
        title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (programsPresenter.isProgramSelected(getAdapterPosition())) {
                    return false;
                } else {
                    programsPresenter.deselectProgram();
                    programsPresenter.setProgramSelected(getAdapterPosition());
                    return true;
                }
            }
        });

        trash = itemView.findViewById(R.id.trashIcon);
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                programsPresenter.deleteSelectedProgram();
            }
        });
    }

    @Override
    public void setTitle(String text) {
        title.setText(text);
    }

    @Override
    public void setSelected(boolean isSelected) {
        trash.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        title.setBackgroundColor(isSelected ? Color.parseColor("#4A506C") : Color.parseColor("#37394f"));
    }
}



