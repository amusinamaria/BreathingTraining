package mary.breathingtraining.model;

import android.content.SharedPreferences;

import java.util.ArrayList;


public class TimerData {

    private static final String TIMER_STATE_KEY = "timerState";
    private static final String SNIFFS_REMAINING_KEY = "sniffsRemaining";
    private static final String WORK_MODULE_KEY = "workModule";
    private static final String SOUND_IS_ON_KEY = "soundIsOn";

    public static final String RUNNING = "running";
    public static final String STOPPED = "stopped";
    public static final String PAUSED = "paused";

    public static final String WORK = "work";
    public static final String REST = "rest";

    public static final int REST_SNIFFS_COUNT = 7; //8 вдохов отдыхаем

    private SharedPreferences sharedPreferences;
    private ArrayList<String> exercises;
    private Program program;

    public TimerData(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        setWorkModuleToSP(0);
        ProgramsData programsData = ProgramsData.getProgramsData(sharedPreferences);
        program = programsData.getChosenProgram();
        exercises = program.getExercises();
    }

    public int getSniffsCount() {
        return program.getSniffsCount();
    }

    public long getCountdownInterval() {
        return program.getInterval();
    }

    public String getCurrentExercise() {
        int currentWorkModule = getWorkModuleFromSP();
        return exercises.get(currentWorkModule / 3);
    }

    public boolean isCurrentModuleLast() {
        int currentModule = sharedPreferences.getInt(WORK_MODULE_KEY, 0);
        int lastModule = exercises.size() * 3 - 1;
        return currentModule >= lastModule;
    }

//-----------------------------------------------SharedPreferences--------------------------------------------

    public String getTimerStateFromSP() {
        return sharedPreferences.getString(TIMER_STATE_KEY, STOPPED);
    }

    public void setTimerStateToSP(String timerState) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TIMER_STATE_KEY, timerState);
        editor.apply();
    }

    public int getSniffsRemainingFromSP() {
        return sharedPreferences.getInt(SNIFFS_REMAINING_KEY, 0);
    }

    public void setSniffsRemainingToSP(int sniffsRemaining) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SNIFFS_REMAINING_KEY, sniffsRemaining);
        editor.apply();
    }

    private int getWorkModuleFromSP() {
        //блоки вдохов между отдыхами. В каждом упр их 3. сквозная нумерация с начала
        return sharedPreferences.getInt(WORK_MODULE_KEY, 0);
    }

    public void setWorkModuleToSP(int workModule) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WORK_MODULE_KEY, workModule);
        editor.apply();
    }

    public void updateWorkModuleInSP() {
        int prevWorkModule = getWorkModuleFromSP();
        setWorkModuleToSP(prevWorkModule + 1);
    }

    public boolean getSoundStateFromSP() {
        return sharedPreferences.getBoolean(SOUND_IS_ON_KEY, true);
    }

    public void saveSoundStateToSP(boolean isSoundOn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SOUND_IS_ON_KEY, isSoundOn);
        editor.apply();
    }
}
