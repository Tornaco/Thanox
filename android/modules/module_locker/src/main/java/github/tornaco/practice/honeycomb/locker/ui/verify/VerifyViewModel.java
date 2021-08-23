package github.tornaco.practice.honeycomb.locker.ui.verify;

import static github.tornaco.android.thanos.core.app.activity.VerifyResult.REASON_USER_CANCEL;
import static github.tornaco.android.thanos.core.app.activity.VerifyResult.REASON_USER_FP_INCORRECT;
import static github.tornaco.android.thanos.core.app.activity.VerifyResult.REASON_USER_KEY_NOT_SET;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.os.CancellationSignal;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;

import com.elvishew.xlog.XLog;

import java.util.Objects;

import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.app.activity.ActivityStackSupervisor;
import github.tornaco.android.thanos.core.app.activity.VerifyResult;
import github.tornaco.practice.honeycomb.locker.util.fingerprint.FingerprintManagerCompat;

public class VerifyViewModel extends AndroidViewModel {
    private static final long PROGRESS_MAX = ActivityStackSupervisor.LOCKER_VERIFY_TIMEOUT_MILLS;
    private int requestCode;
    public String pkg;

    public ObservableBoolean verified = new ObservableBoolean(false);
    public ObservableInt progress = new ObservableInt((int) PROGRESS_MAX);
    public ObservableInt progressMax = new ObservableInt((int) PROGRESS_MAX);
    public ObservableInt failCount = new ObservableInt(0);
    private CancellationSignal cancellationSignal;

    private ActivityStackSupervisor lockerManager;

    public VerifyViewModel(@NonNull Application application) {
        super(application);
    }

    public void start() {
        if (!isCurrentLockMethodKeySet()) {
            pass(REASON_USER_KEY_NOT_SET);
            return;
        }
        checkTimeout();
    }

    public void pause() {
        cancelFingerPrint();
    }

    public void resume() {
        setupFingerPrint();
    }

    public boolean isFingerPrintSupportAndEnabled() {
        if (!lazyGetLockerManager().isFingerPrintEnabled()) return false;
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.USE_FINGERPRINT)
                != PackageManager.PERMISSION_GRANTED) {
            XLog.w("FP Permission is missing...");
            return false;
        }
        if (!FingerprintManagerCompat.from(getApplication()).isHardwareDetected()) {
            XLog.w("FP HW is missing...");
            return false;
        }
        return true;
    }

    public void verify(String input) {
        if (!isInputCorrect(input)) {
            failOnce();
        } else {
            pass(VerifyResult.REASON_USER_INPUT_CORRECT);
        }
    }

    private void pass(int reason) {
        Objects.requireNonNull(lazyGetLockerManager()).setVerifyResult(requestCode, VerifyResult.ALLOW, reason);
        verified.set(true);
    }

    private void failOnce() {
        failCount.set(failCount.get() + 1);
    }

    public void failFinally(int reason) {
        Objects.requireNonNull(lazyGetLockerManager()).setVerifyResult(requestCode, VerifyResult.IGNORE, reason);
        verified.set(true);
    }

    public void cancel() {
        XLog.i("VerifyViewModel Cancel.");
        if (!verified.get()) {
            Objects.requireNonNull(lazyGetLockerManager()).setVerifyResult(requestCode,
                    VerifyResult.IGNORE, REASON_USER_CANCEL);
            verified.set(true);
        }

        cancelFingerPrint();
    }

    private int getLockMethod() {
        return Objects.requireNonNull(lazyGetLockerManager()).getLockerMethod();
    }

    private boolean isInputCorrect(String input) {
        XLog.i("Check input, method is: %s", getLockMethod());
        return Objects.requireNonNull(lazyGetLockerManager()).isLockerKeyValid(getLockMethod(), input);
    }

    private boolean isCurrentLockMethodKeySet() {
        return Objects.requireNonNull(lazyGetLockerManager()).isLockerKeySet(getLockMethod());
    }

    private void setupFingerPrint() {
        if (!lazyGetLockerManager().isFingerPrintEnabled()) {
            return;
        }
        cancelFingerPrint();
        cancellationSignal = authenticateFingerPrint(
                new FingerprintManagerCompat.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(
                            FingerprintManagerCompat.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        XLog.d("onAuthenticationSucceeded:" + result);
                        pass(REASON_USER_FP_INCORRECT);
                    }

                    @Override
                    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                        super.onAuthenticationHelp(helpMsgId, helpString);
                        XLog.i("onAuthenticationHelp:" + helpString);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        XLog.d("onAuthenticationFailed");
                    }

                    @Override
                    public void onAuthenticationError(int errMsgId, CharSequence errString) {
                        super.onAuthenticationError(errMsgId, errString);
                        XLog.d("onAuthenticationError:" + errString);
                    }
                });
    }

    private void cancelFingerPrint() {
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
        }
    }

    private CancellationSignal authenticateFingerPrint(FingerprintManagerCompat.AuthenticationCallback callback) {
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.USE_FINGERPRINT)
                != PackageManager.PERMISSION_GRANTED) {
            XLog.w("FP Permission is missing...");
            return null;
        }
        if (!FingerprintManagerCompat.from(getApplication()).isHardwareDetected()) {
            XLog.w("FP HW is missing...");
            return null;
        }
        CancellationSignal cancellationSignal = new CancellationSignal();
        FingerprintManagerCompat.from(getApplication())
                .authenticate(null, 0, cancellationSignal, callback, null);
        XLog.i("FP authenticate...");
        return cancellationSignal;
    }

    private void checkTimeout() {
        // 60FPS
        // 1000 / 60 ~= 16.7ms
        CountDownTimer countDownTimer = new CountDownTimer(
                ActivityStackSupervisor.LOCKER_VERIFY_TIMEOUT_MILLS, 17) {
            @Override
            public void onTick(long l) {
                progress.set((int) l);
            }

            @Override
            public void onFinish() {
                onTimeout();
            }
        };
        countDownTimer.start();
        verified.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                countDownTimer.cancel();
            }
        });
    }

    private void onTimeout() {
        cancel();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    private ActivityStackSupervisor lazyGetLockerManager() {
        if (lockerManager != null) return lockerManager;
        lockerManager = ThanosManager.from(getApplication().getApplicationContext())
                .getActivityStackSupervisor();
        return lockerManager;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }
}
