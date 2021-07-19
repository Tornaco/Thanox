package github.tornaco.practice.honeycomb.locker.util.fingerprint;

import android.content.Context;

import androidx.core.os.CancellationSignal;

import com.fingerprints.service.FingerprintManager;

/**
 * Created by guohao4 on 2017/10/24.
 * Email: Tornaco@163.com
 */
// http://open-wiki.flyme.cn/index.php?title=%E6%8C%87%E7%BA%B9%E8%AF%86%E5%88%ABAPI
class FingerprintManagerCompatApiFlyme {

    private static FingerprintManager getFingerprintManagerOrNull(Context context) {
        try {
            return FingerprintManager.open();
        } catch (Throwable e) {
            return null;
        }
    }

    static boolean hasEnrolledFingerprints(Context context) {
        final FingerprintManager fp = getFingerprintManagerOrNull(context);
        try {
            return (fp != null) && fp.isSurpport() && fp.getIds() != null && fp.getIds().length > 0;
        } finally {
            if (fp != null) {
                fp.release();
            }
        }
    }

    static boolean isHardwareDetected(Context context) {
        final FingerprintManager fp = getFingerprintManagerOrNull(context);
        try {
            return (fp != null) && fp.isSurpport();
        } finally {
            if (fp != null) {
                fp.release();
            }
        }
    }

    static void authenticate(Context context, CancellationSignal cancel,
                             final FingerprintManagerCompat.AuthenticationCallback callback) {
        if (!hasEnrolledFingerprints(context)) {
            return;
        }
        final FingerprintManager fp = getFingerprintManagerOrNull(context);
        try {
            cancel.setOnCancelListener(new CancellationSignal.OnCancelListener() {
                @Override
                public void onCancel() {
                    try {
                        fp.abort();
                        fp.release();
                    } catch (Exception ignored) {
                    }
                }
            });
            fp.startIdentify(new FingerprintManager.IdentifyCallback() {
                @Override
                public void onIdentified(int id, boolean updated) {
                    callback.onAuthenticationSucceeded(null);
                    fp.release();
                }

                @Override
                public void onNoMatch() {
                    callback.onAuthenticationFailed();
                }
            }, fp.getIds());
        } finally {
            try {
                fp.release();
            } catch (Exception ignored) {
            }
        }
    }
}
