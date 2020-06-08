package br.com.libertyseguros.mobile.libray;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.os.Build;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;
import android.util.Log;
import android.widget.Toast;
import br.com.libertyseguros.mobile.R;

public class FingerprintsAndroid {

    private KeyguardManager keyguardManager;

    private FingerprintManagerCompat fingerprintManager;

    private Activity context;

    private OnFingerprintsListener onFingerprintsListener;

    private CancellationSignal cancellationSignal;

    public FingerprintsAndroid(Activity context){
        keyguardManager = (KeyguardManager) context.getSystemService(context.KEYGUARD_SERVICE);
        fingerprintManager = FingerprintManagerCompat.from(context);
    }

    public boolean isHardwareOk(){
        if(!fingerprintManager.isHardwareDetected()) {
            return false;

        } else {
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void start(OnFingerprintsListener onFingerprintsListenerNow) {
         cancellationSignal = new CancellationSignal();

        onFingerprintsListener = onFingerprintsListenerNow;

        if (!fingerprintManager.hasEnrolledFingerprints()) {
            try {
                Toast.makeText(context, context.getString(R.string.message_fingertips_not_register), Toast.LENGTH_SHORT).show();
            } catch (Exception ex){
                Log.i(Config.TAG, ex.toString());
            }
        }else {

             fingerprintManager.authenticate(null, 0, cancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);

                    if(errorCode != 5){
                        onFingerprintsListener.onError();
                    }


                }
    
                @Override
                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                    super.onAuthenticationHelp(helpCode, helpString);

                    if(helpCode != 5){
                        onFingerprintsListener.onError();
                    }
                }
    
                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    cancellationSignal.cancel();

                    onFingerprintsListener.onSucess();
                }
    
                /*
                 * Called when authentication failed but the user can try again
                 * When called four times - on the next fail onAuthenticationError(FINGERPRINT_ERROR_LOCKOUT)
                 * will be called
                 */
                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();

                    onFingerprintsListener.onError();

                }
            }, null);
        }
    
    }

    public void cancel(){
        cancellationSignal.cancel();
    }

    public interface OnFingerprintsListener{
        public void onError();
        public void onSucess();
    }
}
