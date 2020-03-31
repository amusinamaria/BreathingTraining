package mary.breathingtraining.view.interfaces;

import android.media.SoundPool;

public interface ITimerActivity {
    void updateButtons(String timerState);

    void updateTimerUI(int sniffsRemaining, String timerType);

    void showExerciseInfo(String exercise);

    void setProgressBarMaxValue(int sniffsCount);

    void loadSound(SoundPool soundPool);

    int[] getSoundIds();

    void updateSoundButton(boolean isSoundOn);

    void showFinishView();
}
