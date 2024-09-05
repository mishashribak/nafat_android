package com.nafaz.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nafaz.android.R;
import com.nafaz.android.api.core.APIBulldozer;
import com.nafaz.android.api.entity.EnrollResponse;
import com.nafaz.android.api.interfaces.APIs;
import com.nafaz.android.entity.AuthLogic;
import com.nafaz.android.freeotp.Token;
import com.nafaz.android.manager.PrefManager;
import com.nafaz.android.ui.adapter.AccountHistoryAdapter;
import com.nafaz.android.ui.adapter.ActionListAdapter;
import com.nafaz.android.ui.base.BaseActivity;
import com.nafaz.android.ui.view.button.LoadingMaterialButton;
import com.nafaz.android.util.AnimationUtils;
import com.nafaz.android.util.ProgressDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nafaz.android.api.constants.APIConstants.ResponseCode.CODE_200;

public class ActionActivity extends BaseActivity {
    @BindView(R.id.listActions)
    RecyclerView rvActions;

    @BindView(R.id.btExit)
    TextView btnExit;

    @BindView(R.id.imgActions)
    ImageView imgActions;

    @BindView(R.id.toolbar_common)
    Toolbar commonToolBar;

    @OnClick(R.id.btExit)
    public void onExit() {
        finish();
        SplashActivity.open(this);
    }

    public static void open(@NotNull Context context) {
        context.startActivity(new Intent(context, ActionActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        ButterKnife.bind(this);

        setSupportActionBar(commonToolBar);

        LinearLayout linearLayout = findViewById(R.id.llAnimParent);
        linearLayout.setVisibility(View.INVISIBLE);
        AnimationUtils.animateSlideTopFromDown(linearLayout);

        rvActions.setLayoutManager(new LinearLayoutManager(this));
        rvActions.setAdapter(new ActionListAdapter(this, AuthLogic.availableActions, new ActionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String text, int position) {
                if(text.equals("REENROLL_TOTP")){
                    reEnrollTOTP();
                }else if(text.equals("ENROLL_TOTP")){
                    enrollTOTP();
                }
            }
        }));
    }

    private void enrollTOTP(){
        ProgressDialog.showProgress(this);
        final APIs apIs = APIBulldozer.createAPI(APIs.class);
        apIs.enrollTOTP()
                .enqueue(new Callback<EnrollResponse>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onResponse(@NotNull Call<EnrollResponse> call, @NotNull Response<EnrollResponse> response) {
                        ProgressDialog.hideprogressbar();
                        if (null == response.body())
                            return;
                        if (response.isSuccessful()) {
                            EnrollResponse enrollResponse= response.body();
                            if (enrollResponse.code == CODE_200){
                                PrefManager.getInstance(ActionActivity.this).setPreferences("totp", enrollResponse.tOtpUrl);
                                startActivity(new Intent(ActionActivity.this, SplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK), false );
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<EnrollResponse> call, @NotNull Throwable t) {
                        ProgressDialog.hideprogressbar();
                    }
                });
    }

    private void reEnrollTOTP(){
        ProgressDialog.showProgress(this);
        final APIs apIs = APIBulldozer.createAPI(APIs.class);
        apIs.reEnrollTOTP()
                .enqueue(new Callback<EnrollResponse>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onResponse(@NotNull Call<EnrollResponse> call, @NotNull Response<EnrollResponse> response) {
                        ProgressDialog.hideprogressbar();
                        if (null == response.body())
                            return;
                        if (response.isSuccessful()) {
                            EnrollResponse enrollResponse= response.body();
                            if (enrollResponse.code == CODE_200){
                                PrefManager.getInstance(ActionActivity.this).setPreferences("totp", enrollResponse.tOtpUrl);
                                startActivity(new Intent(ActionActivity.this, SplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK), false );
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<EnrollResponse> call, @NotNull Throwable t) {
                        ProgressDialog.hideprogressbar();
                    }
                });
    }
}
