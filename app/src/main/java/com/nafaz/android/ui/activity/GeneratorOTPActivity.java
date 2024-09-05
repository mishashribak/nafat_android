package com.nafaz.android.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nafaz.android.R;
import com.nafaz.android.api.core.APIBulldozer;
import com.nafaz.android.api.entity.CheckIdRequest;
import com.nafaz.android.api.entity.CheckIdResponse;
import com.nafaz.android.api.entity.EnrollRequest;
import com.nafaz.android.api.entity.EnrollResponse;
import com.nafaz.android.api.helper.ErrorParser;
import com.nafaz.android.api.interfaces.APIs;
import com.nafaz.android.app.App;
import com.nafaz.android.entity.AuthLogic;
import com.nafaz.android.freeotp.Token;
import com.nafaz.android.freeotp.TokenCode;
import com.nafaz.android.manager.PrefManager;
import com.nafaz.android.ui.base.BaseActivity;
import com.nafaz.android.util.AnimationUtils;
import com.nafaz.android.util.NetworkUtils;
import com.nafaz.android.util.ProgressDialog;

import org.jetbrains.annotations.NotNull;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.nafaz.android.api.constants.APIConstants.ResponseCode.CODE_200;
import static com.nafaz.android.manager.PrefManager.Keys.KEY_LANGUAGE;

public class GeneratorOTPActivity extends BaseActivity {

    @BindView(R.id.btCopy)
    MaterialButton btCopy;

    @BindView(R.id.toolbar_common)
    Toolbar commonToolbar;

    @BindView(R.id.otpProgress)
    ProgressBar otpProgress;

    @BindView(R.id.txtOTP)
    TextInputEditText txtOTP;

    @BindView(R.id.txtDescOtp)
    TextView txtDescOtp;

    @BindView(R.id.txtProgress)
    TextView txtProgress;

    private String code="";
    private int period;
    private String strTokenUrl="";
    private CountDownTimer countDownTimer;
    private Token token;
    private TokenCode tokenCode;
    private int remainTime = 0;
    private int currentValue = 0;

    public static void open(@NotNull Context context) {
        context.startActivity(new Intent(context, GeneratorOTPActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator_otp);
        ButterKnife.bind(this);
        setSupportActionBar(commonToolbar);

        LinearLayout linearLayout = findViewById(R.id.llAnimParent);
        linearLayout.setVisibility(View.INVISIBLE);
        AnimationUtils.animateSlideTopFromDown(linearLayout);

        strTokenUrl = PrefManager.getInstance(GeneratorOTPActivity.this).getPreferences("totp", "");
//        strTokenUrl = "otpauth://totp/KSA%20IAM%20SSO%20null:2309470215?secret=2ZWLMX7EJEQHCMIN&issuer=KSA+IAM+SSO+null&algorithm=SHA256&digits=6&period=30";
        if(!strTokenUrl.isEmpty()){
            try {
                token =  new Token(strTokenUrl);
                tokenCode = token.generateCodes();

                if(!PrefManager.getInstance(GeneratorOTPActivity.this).getPreferences("code", "").isEmpty()){
                    txtOTP.setText(PrefManager.getInstance(GeneratorOTPActivity.this).getPreferences("code", ""));
                }

                Uri uri= Uri.parse(strTokenUrl);
                period = Integer.parseInt(uri.getQueryParameter("period"));
                Log.e("GeneratorOTPActivity", "period: "+period);

                if(period < 0)
                    return;
                otpProgress.setMax(period);
                remainTime = tokenCode.getRemainingSeconds();
                Log.e("GeneratorOTPActivity", "remainTime: "+remainTime);

                if(remainTime != 0){
                    txtDescOtp.setText(getString(R.string.new_password_60, remainTime+""));
                    currentValue = period - remainTime;
                    otpProgress.setProgress(currentValue);
                    Log.e("GeneratorOTPActivity", "currentValue: "+currentValue);
                    generateToken();
                }

            } catch (Token.TokenUriInvalidException e) {
                e.printStackTrace();
            }
        }
    }


    @OnClick(R.id.btCopy)
    public void onCopy() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("copied text", code);
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
    }

    private void generateToken(){
        countDownTimer = new CountDownTimer(remainTime * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished/1000 > 0)
                    txtDescOtp.setText(getString(R.string.new_password_60, millisUntilFinished/1000+""));
                txtProgress.setText(String.valueOf(millisUntilFinished/1000));
                otpProgress.setProgress(period  - (int)millisUntilFinished/1000);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onFinish() {
                try {
                    code = token.generateCodes().getCurrentCode();
                    PrefManager.getInstance(GeneratorOTPActivity.this).setPreferences("code",code);
                    txtOTP.setText(code);
                    if(countDownTimer != null){
                        countDownTimer.cancel();
                    }
                    currentValue = 0;
                    remainTime =  period;
                    otpProgress.setProgress(0);
                    generateToken();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        countDownTimer.start();
    }
}
