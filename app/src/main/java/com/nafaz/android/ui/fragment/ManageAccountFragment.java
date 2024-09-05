package com.nafaz.android.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nafaz.android.R;
import com.nafaz.android.api.callback.ApiCallback;
import com.nafaz.android.api.entity.DisableAuthMethodRequest;
import com.nafaz.android.api.services.DisableAuthenticationMethodService;
import com.nafaz.android.api.services.GetAuthenticationMethodsService;
import com.nafaz.android.entity.AuthenticationMethod;
import com.nafaz.android.entity.Identity;
import com.nafaz.android.entity.TagId;
import com.nafaz.android.entity.listener.OnItemClickListener;
import com.nafaz.android.ui.adapter.ManageAccountAdapter;
import com.nafaz.android.ui.dialog.VerificationInfoDialog;
import com.nafaz.android.util.NetworkUtils;
import com.nafaz.android.util.SnackBarUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ManageAccountFragment extends Fragment implements OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ManageAccountAdapter adapter;

    @BindView(R.id.snackbarlocation)
    CoordinatorLayout snackBarLocation;

    @BindView(R.id.list)
    RecyclerView recyclerView;

    @BindView(R.id.pb_mng_acc)
    ProgressBar progressBar;

    private List<AuthenticationMethod> authenticationMethodList = new ArrayList<>();

    // TODO: Rename and change types and number of parameters
    public static ManageAccountFragment newInstance(String param1, String param2) {
        ManageAccountFragment fragment = new ManageAccountFragment();
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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_account, container, false);

        ButterKnife.bind(this, view);
        // Set the adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new ManageAccountAdapter(authenticationMethodList);
        adapter.setOnItemDeleteListener(this);
        adapter.setOnItemEditListener(this);
        adapter.setOnItemVerifiedListener(this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        callAuthMethodsAPI();
    }

    private void callAuthMethodsAPI() {

        if (!NetworkUtils.isOnline(getContext())) {
            SnackBarUtils.showNoInternetSnackBar(snackBarLocation);
            return;
        }

        if (null == Identity.id)
            return;

        showProgress(true);

        new GetAuthenticationMethodsService().start(Identity.getPersonId(),
                new ApiCallback<List<AuthenticationMethod>>() {
                    @Override
                    public void onSuccess(List<AuthenticationMethod> methodList) {
                        showProgress(false);
                        authenticationMethodList = methodList;
                        adapter.addAll(authenticationMethodList);
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
        recyclerView.setVisibility(!show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(View v) {

        switch (v.getId()) {

            case R.id.img_manage_acc_delete:
                int position = (int) v.getTag(TagId.POSITION);
                VerificationInfoDialog.show(getContext(), VerificationInfoDialog.DialogType.PROMPT, new VerificationInfoDialog.OnVerificationInfoListener() {
                    @Override
                    public void onNext() {
                        disableAuthMethod(position);
                    }

                    @Override
                    public void onBack() {

                    }
                });
                break;

            case R.id.img_manage_acc_edit:
                // TODO: 2019-06-26  handle edit clicks
                break;

            case R.id.img_manage_acc_verified:
                // TODO: 2019-06-26  handle verified clicks
                break;
        }
    }

    private void disableAuthMethod(int position) {

        if (null == Identity.id)
            return;

        if (!NetworkUtils.isOnline(getContext())) {
            SnackBarUtils.showNoInternetSnackBar(snackBarLocation);
            return;
        }

        new DisableAuthenticationMethodService().start(
                new DisableAuthMethodRequest(authenticationMethodList.get(position).code,
                        Identity.getPersonId()),
                new ApiCallback<List<AuthenticationMethod>>() {
                    @Override
                    public void onSuccess(List<AuthenticationMethod> methodList) {
                        authenticationMethodList = methodList;
                        adapter.addAll(authenticationMethodList);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        SnackBarUtils.showSnackBar(getContext(), R.string.error_general);
                    }

                    @Override
                    public void onFail(String failMessage) {
                        SnackBarUtils.showSnackBar(getContext(), R.string.error_general);
                    }
                }
        );
    }
}
