package mary.breathingtraining.presenter;

import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;

import mary.breathingtraining.model.MyCountDownTimer;
import mary.breathingtraining.model.TimerData;
import mary.breathingtraining.view.interfaces.ITimerActivity;

public class TimerPresenter implements Presenter {

    private TimerData timerData;                //model
    private ITimerActivity iTimerActivity;      //view

    private MyCountDownTimer timer;
    private String timerState = TimerData.STOPPED;
    private String timerType = TimerData.WORK;

    private int sniffsCount = 0;
    private int sniffsRemaining = 0;
    private long countdownInterval = 0;
//    private int lastSniffsRemaining = 0;

    private SoundPool soundPool;
    private boolean isSoundOn = true;


    public TimerPresenter(ITimerActivity iTimerActivity, SharedPreferences sharedPreferences) {
        this.iTimerActivity = iTimerActivity;
        this.timerData = new TimerData(sharedPreferences);
    }

    @Override
    public void onPause() {
        if (timerState.equals(TimerData.RUNNING)) {
            timer.cancel();
        }
        soundPool.release();
        timerData.setSniffsRemainingToSP(sniffsRemaining);
        timerData.setTimerStateToSP(timerState);
    }

    @Override
    public void onResume() {
        initSoundPool();
        initTimer();
    }

    public void onClickedButtonPlay() {
        startTimer();
        timerState = TimerData.RUNNING;
        iTimerActivity.updateButtons(TimerData.RUNNING);
    }

    public void onClickedButtonPause() {
        timer.cancel();
        timerState = TimerData.PAUSED;
        iTimerActivity.updateButtons(TimerData.PAUSED);
    }

    private void initTimer() {
        timerState = timerData.getTimerStateFromSP();
        setNewTimerLength();
        String exercise = timerData.getCurrentExercise();
        iTimerActivity.showExerciseInfo(exercise);

        if (timerState.equals(TimerData.RUNNING) || timerState.equals(TimerData.PAUSED)) {
            sniffsRemaining = timerData.getSniffsRemainingFromSP();
        } else {
            sniffsRemaining = sniffsCount;
        }

        //resume where we left off
        if (timerState.equals(TimerData.RUNNING)) {
            startTimer();
        }
        isSoundOn = timerData.getSoundStateFromSP();
        iTimerActivity.updateSoundButton(isSoundOn);
        iTimerActivity.updateButtons(timerState);
        iTimerActivity.updateTimerUI(sniffsRemaining, timerType);
    }

    private void startTimer() {
        countdownInterval = timerData.getCountdownInterval();
        String exercise = timerData.getCurrentExercise();
        iTimerActivity.showExerciseInfo(exercise);

        timer = new MyCountDownTimer(sniffsRemaining * countdownInterval + countdownInterval, countdownInterval ) {   //+ countdownInterval
            int[] soundIds = iTimerActivity.getSoundIds();

            public void onTick(long millisUntilFinished) {
                sniffsRemaining = (int) (millisUntilFinished / countdownInterval);
                //&& sniffsRemaining != sniffsCount нужно, чтобы не было первого звука снифа "на месте", до начала убывания прогрессбара
                if (timerType.equals(TimerData.WORK) && isSoundOn && sniffsRemaining != sniffsCount) {   //&& lastSniffsRemaining != sniffsRemaining   //? нужны для того, чтобы звук был не на каждый тик, а только когда меняется цифра в UI таймера
                    soundPool.play(soundIds[0], 1, 1, 1, 0, 1.0f);
//                    lastSniffsRemaining = sniffsRemaining;
                }
                iTimerActivity.updateTimerUI(sniffsRemaining, timerType);
            }

            public void onFinish() {
                onTimerFinished();
            }
        };
        timer.start();
    }


//    private void startTimer() {
//        countdownInterval = timerData.getCountdownInterval();
//        String exercise = timerData.getCurrentExercise();
//        iTimerActivity.showExerciseInfo(exercise);
//
//        //ЭТО РАБОТАЕТ, ХОТЯ НЕ ДОЛЖНО. НЕ ПЫТАТЬСЯ УЛУЧШИТЬ!!!
//        //http://qaru.site/questions/142971/android-countdowntimer-skips-last-ontick
//        // + 500, чтобы таймер не пропускал последний onTick
//        timer = new CountDownTimer((sniffsRemaining * countdownInterval + countdownInterval) + 500, countdownInterval) {
//            int[] soundIds = iTimerActivity.getSoundIds();
//
//            public void onTick(long millisUntilFinished) {
//                // Если это последний тик - показать ноль
//                if (millisUntilFinished < countdownInterval + 500) {
//                    if (timerType.equals(WORK) && isSoundOn && lastSniffsRemaining != sniffsRemaining) {
//                        soundPool.play(soundIds[0], 1, 1, 1, 0, 1.0f);
//                        sniffsRemaining = 0;
//                        lastSniffsRemaining = sniffsRemaining;
//                    }
//                    iTimerActivity.updateTimerUI(0, timerType);
//
//                } else {
//                    //-1 компенсирует прибавленные 500
//                    sniffsRemaining = (int) (millisUntilFinished / countdownInterval) -1;
//                    //Эти условия нужны для того, чтобы звук был не на каждый тик, а только когда меняется цифра в UI таймера
//                    if (timerType.equals(WORK) && isSoundOn && lastSniffsRemaining != sniffsRemaining && sniffsRemaining != sniffsCount) {
//                        soundPool.play(soundIds[0], 1, 1, 1, 0, 1.0f);
//                        lastSniffsRemaining = sniffsRemaining;
//                    }
//                    iTimerActivity.updateTimerUI(sniffsRemaining, timerType);
//                }
//            }
//
//            public void onFinish() {
//                onTimerFinished();
//            }
//        };
//        timer.start();
//    }

    //Этот таймер пропускал последний onTick:
//    private void startTimer() {
//        countdownInterval = timerData.getCountdownInterval();
//        String exercise = timerData.getCurrentExercise();
//        iTimerActivity.showExerciseInfo(exercise);
//
//        //-400 чтобы исправить баг CountDownTimer https://stackoverflow.com/questions/8857590/android-countdowntimer-skips-last-ontick/12283400#12283400
//        timer = new CountDownTimer(sniffsRemaining * countdownInterval + countdownInterval, countdownInterval ) {  //-400
//            int[] soundIds = iTimerActivity.getSoundIds();
//
//            public void onTick(long millisUntilFinished) {
//                sniffsRemaining = (int) (millisUntilFinished / countdownInterval);
//                //Эти условия нужны для того, чтобы звук был не на каждый тик, а только когда меняется цифра в UI таймера
//                if (timerType.equals(WORK) && isSoundOn && lastSniffsRemaining != sniffsRemaining && sniffsRemaining != sniffsCount) {
//                    soundPool.play(soundIds[0], 1, 1, 1, 0, 1.0f);
//                    lastSniffsRemaining = sniffsRemaining;
//                }
//                iTimerActivity.updateTimerUI(sniffsRemaining, timerType);
//            }
//
//            public void onFinish() {
//                onTimerFinished();
//            }
//        };
//        timer.start();
//    }


    private void onTimerFinished() {
        timerState = TimerData.STOPPED;

        //если только что завершенный таймер рабочий
        if (timerType.equals(TimerData.WORK)) {
            //если завершен последний модуль
            if (timerData.isCurrentModuleLast()) {
                //обнулить все, показать вьюху завершения, выйти из метода
                resetTimerSession();
                iTimerActivity.showFinishView();
                return;

            } else {
                //обновить модуль упражнений
                timerData.updateWorkModuleInSP();
            }
        }

        timerType = (timerType.equals(TimerData.WORK) ? TimerData.REST : TimerData.WORK);
        setNewTimerLength();

        timerData.setSniffsRemainingToSP(sniffsCount);
        sniffsRemaining = sniffsCount;

        iTimerActivity.updateTimerUI(sniffsRemaining, timerType);
        onClickedButtonPlay();
    }

    private void setNewTimerLength() {
        if (timerType.equals(TimerData.WORK)) {
            sniffsCount = timerData.getSniffsCount();
        } else {
            sniffsCount = TimerData.REST_SNIFFS_COUNT;
        }
        iTimerActivity.setProgressBarMaxValue(sniffsCount);
    }

    private void initSoundPool() {
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(attrs)
                .build();
        iTimerActivity.loadSound(soundPool);
    }

    public void onClickedSoundOffButton() {
        isSoundOn = true;
        timerData.saveSoundStateToSP(true);
    }

    public void onClickedSoundOnButton() {
        isSoundOn = false;
        timerData.saveSoundStateToSP(false);
    }

    public void resetTimerSession() {
        if (timer != null) {
            timer.cancel();
        }
        timerState = TimerData.STOPPED;
        sniffsRemaining = 0;
        timerData.setWorkModuleToSP(0);
    }
}
