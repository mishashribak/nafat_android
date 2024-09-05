package com.nafaz.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.innovatrics.android.dot.Dot;
import com.innovatrics.android.dot.dto.LivenessCheck2Arguments;
import com.innovatrics.android.dot.livenesscheck.liveness.DotPosition;
import com.innovatrics.android.dot.livenesscheck.model.SegmentConfiguration;
import com.innovatrics.android.dot.utils.LicenseUtils;
import com.nafaz.android.R;
import com.nafaz.android.api.callback.ApiCallback;
import com.nafaz.android.api.entity.CheckFaceRequest;
import com.nafaz.android.api.services.CheckFaceService;
import com.nafaz.android.app.AppConstants;
import com.nafaz.android.bus.BusProvider;
import com.nafaz.android.entity.AuthLogic;
import com.nafaz.android.entity.Identity;
import com.nafaz.android.event.AuthMethodDoneEvent;
import com.nafaz.android.ui.base.BaseActivity;
import com.nafaz.android.ui.view.button.LoadingMaterialButton;
import com.nafaz.android.util.NetworkUtils;
import com.nafaz.android.util.SnackBarUtils;
import com.nafaz.android.util.Utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class FaceFingerprintActivity extends BaseActivity {

    public static final String TAG = FaceFingerprintActivity.class.getSimpleName();
    private static final int REQUEST_FACE_CAPTURE = 0;

    @BindView(R.id.toolbar_common)
    Toolbar commonToolBar;

    @BindView(R.id.snackbarlocation)
    CoordinatorLayout snackBarLocation;

    @BindView(R.id.img_face)
    ImageView photoImageView;

    @BindView(R.id.btn_face_fingerprint_success)
    Button btnFFPSuccess;

    @BindView(R.id.btn_face_fingerprint_error)
    Button btnFFPError;

    @BindView(R.id.tv_face_fingerprint_error)
    TextView tvFFPError;

    @BindView(R.id.btn_face_fingerprint_capture_or_next)
    LoadingMaterialButton btnFFPCaptureOrNext;

    private String encodedImage;

    public static void open(@NotNull Context context) {
        context.startActivity(new Intent(context, FaceFingerprintActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_fingerprint);
        ButterKnife.bind(this);
        setSupportActionBar(commonToolBar);

        if (!Dot.getInstance().isInitialized())
            initDotLibrary();
        else
            launchFaceRecognition();

        commonToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        AuthLogic.currentStep++;
    }

    @Override
    public void onBackPressed() {
        if(AuthLogic.currentStep > 0)
            AuthLogic.currentStep--;
        finish();
    }

    private void initDotLibrary() {
        final byte[] license = LicenseUtils.loadRawLicense(this, R.raw.iengine);
        final Dot.Listener dotListener = new Dot.Listener() {

            @Override
            public void onInitSuccess() {
                runOnUiThread(() -> {
                    Timber.tag(TAG).d("DOT Android Kit is initialized.");
//                    setButtonsEnabled(true);
                    launchFaceRecognition();
                });
            }

            @Override
            public void onInitFail(final String message) {
                runOnUiThread(() -> {
                    Timber.tag(TAG).d("DOT Android Kit: %s", message);
//                    setButtonsEnabled(false);
                });
            }

            @Override
            public void onClosed() {
                runOnUiThread(() -> {
                    Timber.tag(TAG).d("DOT Android Kit is closed.");
//                    setButtonsEnabled(false);
                });
            }
        };

        Dot.getInstance().initAsync(license, dotListener);
    }

    @OnClick(R.id.btn_face_fingerprint_capture_or_next)
    public void onCaptureOrNextButtonClicked() {

        if (btnFFPCaptureOrNext.getText().equals(getMyContext().getString(R.string.activity_face_fingerprint_capture_btn)))
            launchFaceRecognition();

        else
            callCheckFaceAPI();
    }

    private void launchFaceRecognition() {
        final List<SegmentConfiguration> segmentList = createSegmentConfigurationList();

        final LivenessCheck2Arguments livenessCheck2Arguments = new LivenessCheck2Arguments.Builder()
                .segmentList(segmentList)
                .transitionType(LivenessCheck2Arguments.TransitionType.MOVING)
                .dotColorResId(R.color.colorPrimary)
                .build();

        final Intent intent = new Intent(this, FaceCaptureActivity.class);
        intent.putExtra(FaceCaptureActivity.ARGUMENTS, livenessCheck2Arguments);

        startActivityForResult(intent, REQUEST_FACE_CAPTURE);
    }

    private List<SegmentConfiguration> createSegmentConfigurationList() {
        final List<SegmentConfiguration> list = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            final DotPosition position = (DotPosition.getRandomPositionExclude(Arrays.asList(
                    i > 0 ? list.get(i - 1).getTargetPosition() : null,
                    i > 1 ? list.get(i - 2).getTargetPosition() : null)));
            list.add(new SegmentConfiguration(position.name(), 1000));
        }

        return list;
    }

    private void callCheckFaceAPI() {

        if (null == Identity.id)
            return;

        if (!NetworkUtils.isOnline(this)) {
            showInternetError(snackBarLocation);
            return;
        }

        btnFFPCaptureOrNext.showLoading();

        new CheckFaceService().start(new CheckFaceRequest(encodedImage, Identity.getPersonId()),
                new ApiCallback() {
                    @Override
                    public void onSuccess(Object object) {
                        btnFFPCaptureOrNext.hideLoading();

                        AuthLogic.add(AuthLogic.MethodType.FACE);

//                        if(AuthLogic.hasAllVerifiedMethods()){
//                            startActivity(new Intent(FaceFingerprintActivity.this, SuccessfulVerificationActivity.class).putExtra(
//                                    AppConstants.Extras.EXTRA_SUCCESS_MODE, AppConstants.Extras.EXTRA_SUCCESS_REGISTRATION
//                            ), false );
////                            SuccessfulVerificationActivity.open(CodeVerificationActivity.this, AppConstants.Extras.EXTRA_SUCCESS_REGISTRATION);
//                        }else{
                            startActivity(new Intent(FaceFingerprintActivity.this, SuccessfulVerificationActivity.class).putExtra(
                                    AppConstants.Extras.EXTRA_SUCCESS_MODE, AppConstants.Extras.EXTRA_SUCCESS_FACE
                            ), false );
//                            SuccessfulVerificationActivity.open(CodeVerificationActivity.this, AppConstants.Extras.EXTRA_SUCCESS_MOBILE);
//                        }

//                        AuthLogic.removeCompletedAuthFromSecondArray(AuthLogic.MethodType.FACE);
//                        BusProvider.getInstance().post(new AuthMethodDoneEvent(AuthLogic.MethodType.FACE));
//
//                        finish();
//
//                        if (AuthLogic.hasAtLeastTwoVerifiedMethods()) {
//                            RegisterNewUserActivity.open(FaceFingerprintActivity.this);
//                            return;
//                        }
//
//                        AuthLogic.MethodType methodType = AuthLogic.getNextStepFromSecondArray();
//
//                        switch (methodType) {
//                            case MOBILE:
//                                MobileVerificationActivity.open(FaceFingerprintActivity.this, AppConstants.Extras.EXTRA_MOBILE_VER_NORMAL);
//                                break;
//
//                            case FACE:
//                                // Face is done now
//                                break;
//
//                            case ID_NUM:
//                            case PROTECTION_QUES:
//                            case EMAIL:
//                                // In Later stages
//                                break;
//
//                            case ALL:
//                                AuthenticationMethodsActivity.open(FaceFingerprintActivity.this);
//                                break;
//                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        btnFFPCaptureOrNext.hideLoading();
                        showGeneralError(errorMessage);
                    }

                    @Override
                    public void onFail(String failMessage) {
                        btnFFPCaptureOrNext.hideLoading();
                        showGeneralError(failMessage);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_FACE_CAPTURE) {
            switch (resultCode) {
                case FaceCaptureActivity.RESULT_INTERRUPTED:
                case FaceCaptureActivity.RESULT_CAMERA_ACCESS_FAILED:
                case FaceCaptureActivity.RESULT_CAMERA_INIT_FAILED:
                case FaceCaptureActivity.RESULT_NO_CAMERA_PERMISSION:
                case FaceCaptureActivity.RESULT_FACE_CAPTURE_FAIL:
                case FaceCaptureActivity.RESULT_NO_MORE_SEGMENTS:
                case FaceCaptureActivity.RESULT_EYES_NOT_DETECTED:

                    handleCaptureOrNextButtonText(false);
                    handleSuccessOrErrorButtonVisibility(false);

                    break;

                case FaceCaptureActivity.RESULT_DONE:
                    Uri photoUri = data.getData();
                    byte[] template = data.getByteArrayExtra(FaceCaptureActivity.OUT_TEMPLATE);
                    float score = data.getFloatExtra(FaceCaptureActivity.OUT_SCORE, 0);
                    encodedImage = Base64.encodeToString(template, Base64.DEFAULT);
                    final boolean isSuccessfulPhoto = score >= FaceCaptureActivity.MIN_SCORE;

                    photoImageView.setImageURI(photoUri);

                    handleCaptureOrNextButtonText(isSuccessfulPhoto);
                    handleSuccessOrErrorButtonVisibility(isSuccessfulPhoto);

                    if (!isSuccessfulPhoto)
                        showGeneralError(null);

                    break;

                default:
                    SnackBarUtils.showSnackBar(this, "Unknown Face Capture result code: " + resultCode);
            }

        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleCaptureOrNextButtonText(boolean successfulPhoto) {
        btnFFPCaptureOrNext.hideLoading();
        btnFFPCaptureOrNext.setButtonText(successfulPhoto ?
                getMyContext().getString(R.string.btn_next) :
                getMyContext().getString(R.string.btn_capture));
    }

    private void handleSuccessOrErrorButtonVisibility(boolean successfulPhoto) {
        btnFFPSuccess.setVisibility(successfulPhoto ? View.VISIBLE : View.INVISIBLE);
        btnFFPError.setVisibility(successfulPhoto ? View.INVISIBLE : View.VISIBLE);
        tvFFPError.setVisibility(successfulPhoto ? View.GONE : View.VISIBLE);
    }
}
