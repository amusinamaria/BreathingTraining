package mary.breathingtraining.model;

public class Exercise {

    private String text;
    private boolean isSelected = false;

    public Exercise(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }
}
