package mary.breathingtraining.model;

import android.content.SharedPreferences;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import java.util.ArrayList;

import static mary.breathingtraining.model.Program.*;

public class ProgramsData {

    private static ProgramsData programsData;
    private SharedPreferences sharedPreferences;

    private static final String HAS_VISITED_KEY = "hasVisited";
    private static final String CHOSEN_PROGRAM_ID_KEY = "chosenProgram";

    private Realm realm;


    public static ProgramsData getProgramsData(SharedPreferences sharedPreferences) {
        if (programsData == null) {
            programsData = new ProgramsData(sharedPreferences);
        }
        return programsData;
    }

    private ProgramsData(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    private Realm openRealm() {
        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }

    private void saveProgram(String title, ArrayList<String> exercises, int sniffsCount, long interval) {
        RealmProgram realmProgram = new RealmProgram();
        realmProgram.setId(createId());
        if (title.isEmpty()) {
            title = "Программа " + String.valueOf(realmProgram.getId() + 1);
        }
        realmProgram.setTitle(title);
        RealmList<String> realmExercises = new RealmList<>();
        realmExercises.addAll(exercises);

        realmProgram.setExercises(realmExercises);
        realmProgram.setSniffsCount(sniffsCount);
        realmProgram.setInterval(interval);

        Realm realm = openRealm();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmProgram);
        realm.commitTransaction();
    }

    public void saveProgram(Program program) {
        saveProgram(program.getTitle(), program.getExercises(), program.getSniffsCount(), program.getInterval());
    }

    private int createId() {
        RealmResults<RealmProgram> realmPrograms = getRealmPrograms();
        int nextId;
        if (realmPrograms.size() > 0) {
            Number currentIdNum = realmPrograms.max("id");
            if (currentIdNum != null) {
                nextId = currentIdNum.intValue() + 1;
            } else {
                nextId = 0;
            }
        } else {
            nextId = 0;
        }
        return nextId;
    }

    private RealmResults<RealmProgram> getRealmPrograms() {
        Realm realm = openRealm();
        RealmResults<RealmProgram> result = realm.where(RealmProgram.class).findAll();
        result = result.sort("id");
        return result;
    }

    private ArrayList<Program> getPrograms() {
        RealmResults<RealmProgram> realmPrograms = getRealmPrograms();
        ArrayList<Program> programs = new ArrayList<>();
        for (RealmProgram realmProgram : realmPrograms) {
            Program program = createProgramFromRealmProgram(realmProgram);
            programs.add(program);
        }
        return programs;
    }

    private Program createProgramFromRealmProgram(RealmProgram realmProgram) {
        Program program = new Program();
        program.setId(realmProgram.getId());
        ArrayList<String> exercises = new ArrayList<>(realmProgram.getExercises());
        program.setExercises(exercises);
        program.setTitle(realmProgram.getTitle());
        program.setInterval(realmProgram.getInterval());
        program.setSniffsCount(realmProgram.getSniffsCount());
        return program;
    }

    public void createDefaultProgramsIfNeeded() {
        if (isThisVisitFirst()) {
            ArrayList<String> lightExercises = new ArrayList<>();
            lightExercises.add(PROSTOVDOHI);
            saveProgram("Легкая", lightExercises, SNIFFS_COUNT_EIGHT, MEDIUM_INTERVAL);
            ArrayList<String> mediumExercises = new ArrayList<>();
            mediumExercises.add(LADOSHKI);
            mediumExercises.add(USHKI);
            saveProgram("Средняя", mediumExercises, SNIFFS_COUNT_SIXTEEN, MEDIUM_INTERVAL);
            ArrayList<String> hardExercises = new ArrayList<>();
            hardExercises.add(POGONCHIKI);
            hardExercises.add(KOSHKA);
            hardExercises.add(SHAGI);
            saveProgram("Сложная", hardExercises, SNIFFS_COUNT_THIRTYTWO, MEDIUM_INTERVAL);
        }
    }

    private boolean isThisVisitFirst() {
        // проверяем, первый ли раз открывается программа
        boolean hasVisited = sharedPreferences.getBoolean(HAS_VISITED_KEY, false);
        if (!hasVisited) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(HAS_VISITED_KEY, true);
            editor.apply();
        }
        return !hasVisited;
    }

    public void saveChosenProgramIdToSP(int programId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CHOSEN_PROGRAM_ID_KEY, programId);
        editor.apply();
    }

    private int getChosenProgramIdFromSP() {
        return sharedPreferences.getInt(CHOSEN_PROGRAM_ID_KEY, 0);
    }

    Program getChosenProgram() {
        int programId = getChosenProgramIdFromSP();
        Program program = new Program();
        RealmResults<RealmProgram> realmPrograms = getRealmPrograms();
        for (RealmProgram realmProgram : realmPrograms) {
            if (realmProgram.getId() == programId) {
                program = createProgramFromRealmProgram(realmProgram);
                break;
            }
        }
        return program;
    }

    private SelectableProgram createSelectableProgramFromProgram(Program program) {
        return new SelectableProgram(program, false);
    }

    public ArrayList<SelectableProgram> convertProgramsToSelectablePrograms() {
        ArrayList<Program> programs = getPrograms();
        ArrayList<SelectableProgram> selectablePrograms = new ArrayList<>();
        for (Program program : programs) {
            selectablePrograms.add(createSelectableProgramFromProgram(program));
        }
        return selectablePrograms;
    }

    public void deleteProgramFromRealm(SelectableProgram program) {
        RealmProgram result = realm.where(RealmProgram.class).equalTo("id", program.getId()).findFirst();
        if (result != null) {
            realm.beginTransaction();
            result.deleteFromRealm();
            realm.commitTransaction();
        }
    }
}
