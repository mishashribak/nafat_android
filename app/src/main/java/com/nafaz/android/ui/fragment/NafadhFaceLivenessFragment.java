package com.nafaz.android.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.innovatrics.android.dot.dto.Photo;
import com.innovatrics.android.dot.face.DetectedFace;
import com.innovatrics.android.dot.facecapture.steps.CaptureState;
import com.innovatrics.android.dot.fragment.LivenessCheck2Fragment;
import com.innovatrics.android.dot.livenesscheck.controller.FaceLivenessState;
import com.innovatrics.android.dot.livenesscheck.liveness.SegmentPhoto;
import com.nafaz.android.ui.activity.FaceCaptureActivity;
import com.nafaz.android.util.Utils;

import java.io.File;
import java.util.List;

import timber.log.Timber;


public class NafadhFaceLivenessFragment extends LivenessCheck2Fragment {

    private static final String TAG = NafadhFaceLivenessFragment.class.getSimpleName();
    private DetectedFace detectedFace;

    @Override
    protected void onCameraInitFail() {
        if (null == getActivity())
            return;

        getActivity().setResult(FaceCaptureActivity.RESULT_CAMERA_INIT_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onCameraAccessFailed() {
        if (null == getActivity())
            return;

        getActivity().setResult(FaceCaptureActivity.RESULT_CAMERA_ACCESS_FAILED);
        getActivity().finish();
    }

    @Override
    protected void onNoCameraPermission() {
        if (null == getActivity())
            return;

        getActivity().setResult(FaceCaptureActivity.RESULT_NO_CAMERA_PERMISSION);
        getActivity().finish();
    }

    @Override
    protected void onCaptureStateChange(final CaptureState captureStep, Photo photo) {
        Timber.tag(TAG).i("CaptureState: %s", captureStep);
    }

    @Override
    protected void onCaptureSuccess(final DetectedFace detectedFace) {
        this.detectedFace = detectedFace;
    }

    @Override
    protected void onFaceCaptureFail() {
        if (null == getActivity())
            return;

        getActivity().setResult(FaceCaptureActivity.RESULT_FACE_CAPTURE_FAIL);
        getActivity().finish();
    }

    @Override
    protected void onLivenessStateChange(final FaceLivenessState faceLivenessState) {
        if (faceLivenessState == FaceLivenessState.LOST) {
            startLivenessCheck();
        }
    }

    @Override
    protected void onLivenessCheckDone(final float score, final List<SegmentPhoto> keyFrameList) {
        if (null == getContext() || null == getActivity())
            return;

        final Bitmap bitmap = detectedFace.createCroppedImage();
        final File file = Utils.getTempFile(getContext());
        final Uri uri = Uri.fromFile(file);
        com.innovatrics.android.dot.utils.Utils.saveBitmapAsJpeg(bitmap, uri);
        final byte[] template = detectedFace.createTemplate().getTemplate();

        final Intent intent = new Intent();
        intent.setData(uri);
        intent.putExtra(FaceCaptureActivity.OUT_SCORE, score);
        intent.putExtra(FaceCaptureActivity.OUT_TEMPLATE, template);

        getActivity().setResult(FaceCaptureActivity.RESULT_DONE, intent);
        getActivity().finish();
    }

    @Override
    protected void onLivenessCheckFailNoMoreSegments() {
        if (null == getActivity())
            return;

        getActivity().setResult(FaceCaptureActivity.RESULT_NO_MORE_SEGMENTS);
        getActivity().finish();
    }

    @Override
    protected void onLivenessCheckFailEyesNotDetected() {
        if (null == getActivity())
            return;

        getActivity().setResult(FaceCaptureActivity.RESULT_EYES_NOT_DETECTED);
        getActivity().finish();
    }
}
