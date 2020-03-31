package mary.breathingtraining.view;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import mary.timerforstrel.R;
import mary.breathingtraining.presenter.ProgramsPresenter;

public class ProgramsAdapter extends RecyclerView.Adapter<ProgramViewHolder> {

    private final ProgramsPresenter presenter;

    public ProgramsAdapter(ProgramsPresenter programsPresenter) {
        this.presenter = programsPresenter;
    }

    @NonNull
    @Override
    public ProgramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProgramViewHolder(presenter, LayoutInflater.from(parent.getContext())
                .inflate(R.layout.program, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramViewHolder holder, int position) {
        presenter.onBindProgramRowViewAtPosition(position, holder);
    }

    @Override
    public int getItemCount() {
        return presenter.getProgramsRowsCount();
    }
}