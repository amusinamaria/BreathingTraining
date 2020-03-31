package mary.breathingtraining.view.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import mary.timerforstrel.R;
import mary.breathingtraining.presenter.TimerPresenter;
import mary.breathingtraining.view.interfaces.ITimerActivity;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static mary.breathingtraining.model.TimerData.*;

public class TimerActivity extends AppCompatActivity implements ITimerActivity {

    TimerPresenter timerPresenter;

    ImageButton playButton;
    ImageButton pauseButton;

    MaterialProgressBar progressBar;
    TextView countdownText;
    TextView restText;
    TextView exerciseTitle;
    ImageView exerciseImage;

    int soundIds[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutWithScreenHeight());

        //чтобы телефон не засыпал
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        timerPresenter = new TimerPresenter(this, sharedPreferences);

        playButton = findViewById(R.id.fab_play);
        pauseButton = findViewById(R.id.fab_pause);

        progressBar = findViewById(R.id.progress_countdown);
        countdownText = findViewById(R.id.textView_countdown);
        restText = findViewById(R.id.textView_rest);
        exerciseTitle = findViewById(R.id.exerciseTitle);
        exerciseImage = findViewById(R.id.exerciseImage);
    }

    private int getLayoutWithScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float dpHeight = dm.heightPixels / dm.density;
        if (dpHeight <= 590) {
            return R.layout.activity_timer_short_screen;
        } else if(dpHeight > 700){
            return R.layout.activity_timer_long_screen;
        } else {
            return R.layout.activity_timer;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        timerPresenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerPresenter.onResume();
    }

    public void onClickPlayButton(View v) {
        timerPresenter.onClickedButtonPlay();
    }

    public void onClickPauseButton(View v) {
        timerPresenter.onClickedButtonPause();
    }

    public void updateButtons(String timerState) {

        switch (timerState) {
            case RUNNING: {
                playButton.setVisibility(View.GONE);
                playButton.setEnabled(false);
                pauseButton.setEnabled(true);
                break;
            }
            case PAUSED:
            case STOPPED: {
                playButton.setVisibility(View.VISIBLE);
                playButton.setEnabled(true);
                pauseButton.setEnabled(false);
                break;
            }
        }
    }

    public void updateTimerUI(int sniffsRemaining, String timerType) {
        countdownText.setText(String.valueOf(sniffsRemaining));
        progressBar.setProgress(sniffsRemaining, true);

        if (timerType.equals(WORK)) {
            countdownText.setVisibility(View.VISIBLE);
            restText.setVisibility(View.GONE);
            progressBar.setProgressTintList(ColorStateList.valueOf(getColor(R.color.myPink)));
        } else {
            restText.setVisibility(View.VISIBLE);
            countdownText.setVisibility(View.GONE);
            progressBar.setProgressTintList(ColorStateList.valueOf(getColor(R.color.myYellowGreen)));
        }
    }

    public void showExerciseInfo(String exercise) {
        int sId = getStringIdentifier(this, exercise);
        exerciseTitle.setText(getString(sId));
        int iId = getDrawableIdentifier(this, exercise);
        exerciseImage.setImageDrawable(getDrawable(iId));
    }

    private static int getStringIdentifier(Context context, String name) {
        return context.getResources().getIdentifier(name, "string", context.getPackageName());
    }

    private static int getDrawableIdentifier(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    public void setProgressBarMaxValue(int sniffsCount) {
        progressBar.setMax(sniffsCount);
    }

    public void loadSound(SoundPool soundPool) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundIds = new int[2];
        soundIds[0] = soundPool.load(this, R.raw.vdoh, 1);
    }

    public int[] getSoundIds() {
        return soundIds;
    }

    public void onClickSoundOffButton(View view) {
        updateSoundButton(true);
        timerPresenter.onClickedSoundOffButton();
    }

    public void onClickSoundOnButton(View view) {
        updateSoundButton(false);
        timerPresenter.onClickedSoundOnButton();
    }

    public void updateSoundButton(boolean isSoundOn) {
        findViewById(R.id.fab_soundOn).setVisibility(isSoundOn ? View.VISIBLE : View.GONE);
    }

    public void showFinishView() {
        findViewById(R.id.finishView).setVisibility(View.VISIBLE);
    }

    public void onClickFinish(View v) {
        //Завершить приложение
        this.finishAffinity();
    }

    @Override
    public void onBackPressed() {
        timerPresenter.resetTimerSession();
        super.onBackPressed();
    }
}

