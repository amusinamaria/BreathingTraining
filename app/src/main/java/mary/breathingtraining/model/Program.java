package mary.breathingtraining.model;

import java.util.ArrayList;

public class Program {

    public static final long MIN_INTERVAL = 800;
    public static final long MEDIUM_INTERVAL = 1000;
    public static final long MAX_INTERVAL = 1300;

//    public static final long MIN_INTERVAL = 600;
//    public static final long MEDIUM_INTERVAL = 800;
//    public static final long MAX_INTERVAL = 1000;

    public static final int SNIFFS_COUNT_EIGHT = 8;
    public static final int SNIFFS_COUNT_SIXTEEN = 16;
    public static final int SNIFFS_COUNT_THIRTYTWO = 32;

    public static final String PROSTOVDOHI = "prostovdohi";
    public static final String LADOSHKI = "ladoshki";
    public static final String POGONCHIKI = "pogonchiki";
    public static final String NASOS = "nasos";
    public static final String KOSHKA = "koshka";
    public static final String OBNIMIPLECHI = "obnimi";
    public static final String BOLSHOYMAYATNIK = "mayatnik";
    public static final String POVOROTIGOLOVI = "povorotigolovi";
    public static final String USHKI = "ushki";
    public static final String MAYATNIK_GOLOVOY = "mayatnikgolovoy";
    public static final String PEREKATY = "perekaty";
    public static final String SHAGI = "shagi";

    private int id;
    private String title;
    private ArrayList<String> exercises;
    private int sniffsCount;
    private long interval;

    public Program() {
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

    ArrayList<String> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<String> exercises) {
        this.exercises = exercises;
    }

    int getSniffsCount() {
        return sniffsCount;
    }

    public void setSniffsCount(int sniffsCount) {
        this.sniffsCount = sniffsCount;
    }

    long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
