package com.nafaz.android.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.nafaz.android.R;
import com.nafaz.android.api.callback.ApiCallback;
import com.nafaz.android.api.services.GetUserProfileService;
import com.nafaz.android.entity.Identity;
import com.nafaz.android.entity.UserProfile;
import com.nafaz.android.util.NetworkUtils;
import com.nafaz.android.util.SnackBarUtils;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.img_profile_photo)
    ImageView imgProfilePhoto;

    @BindView(R.id.btn_profile_edit)
    MaterialButton btnEdit;

    @BindView(R.id.snackbarlocation)
    CoordinatorLayout snackBarLocation;

    @BindView(R.id.cl_profile_info)
    ConstraintLayout clProfileInfoContainer;

    @BindView(R.id.tv_profile_name)
    TextView tvName;

    @BindView(R.id.tv_profile_age)
    TextView tvAge;

    @BindView(R.id.tv_marital_status_name)
    TextView tvMaritalStatus;

    @BindView(R.id.pb_profile)
    ProgressBar progressBar;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getUserProfileAPI();
    }

    private void getUserProfileAPI() {

        if (null == Identity.id)
            return;

        if (!NetworkUtils.isOnline(getContext())) {
            SnackBarUtils.showNoInternetSnackBar(snackBarLocation);
            return;
        }

        showProgress(true);

        new GetUserProfileService().start(Identity.getPersonId(),
                new ApiCallback<UserProfile>() {
                    @Override
                    public void onSuccess(UserProfile userProfile) {
                        showProgress(false);
                        UserProfile.set(userProfile);
                        fillData(userProfile);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        showProgress(false);
                        SnackBarUtils.showSnackBar(getContext(), R.string.error_general);
                    }

                    @Override
                    public void onFail(String failMessage) {
                        showProgress(false);
                        SnackBarUtils.showSnackBar(getContext(), R.string.error_general);
                    }
                });
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        clProfileInfoContainer.setVisibility(!show ? View.VISIBLE : View.GONE);
    }

    private void fillData(@NotNull UserProfile userProfile) {
        tvName.setText(userProfile.getName());
        // TODO: 2019-07-21 fill other profile based on business requirements
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
