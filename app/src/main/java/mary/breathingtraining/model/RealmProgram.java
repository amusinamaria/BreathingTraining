package mary.breathingtraining.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmProgram extends RealmObject {

    @PrimaryKey
    private int id;
    private String title;
    private RealmList<String> exercises;
    private int sniffsCount;
    private long interval;

    public RealmProgram() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    RealmList<String> getExercises() {
        return exercises;
    }

    void setExercises(RealmList<String> exercises) {
        this.exercises = exercises;
    }

    int getSniffsCount() {
        return sniffsCount;
    }

    void setSniffsCount(int sniffsCount) {
        this.sniffsCount = sniffsCount;
    }

    long getInterval() {
        return interval;
    }

    void setInterval(long interval) {
        this.interval = interval;
    }
}
