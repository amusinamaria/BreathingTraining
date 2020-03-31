package mary.breathingtraining.model;

public class SelectableProgram extends Program {

    private boolean isSelected;


    SelectableProgram(Program program, boolean isSelected) {
        super();
        this.isSelected = isSelected;
        this.setId(program.getId());
        this.setTitle(program.getTitle());
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

