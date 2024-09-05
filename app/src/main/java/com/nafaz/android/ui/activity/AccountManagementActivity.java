package com.nafaz.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nafaz.android.R;
import com.nafaz.android.entity.AccountHistory;
import com.nafaz.android.ui.base.BaseActivity;
import com.nafaz.android.ui.fragment.AccountHistoryFragment;
import com.nafaz.android.ui.fragment.ManageAccountFragment;
import com.nafaz.android.ui.fragment.ProfileFragment;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountManagementActivity extends BaseActivity implements
        AccountHistoryFragment.OnListFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener {

    public static void open(@NotNull Context context) {
        context.startActivity(new Intent(context, AccountManagementActivity.class));
    }

    @BindView(R.id.toolbar_common)
    Toolbar commonToolBar;

    @BindView(R.id.nav_view)
    BottomNavigationView navView;

    private FragmentManager fragmentManager;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        ButterKnife.bind(this);
        setSupportActionBar(commonToolBar);
        fragmentManager = getSupportFragmentManager();
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_acc_history);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_acc_history:
                fragment = AccountHistoryFragment.newInstance();
                setToolBarTitle(commonToolBar, R.string.title_acc_history);
                break;
            case R.id.navigation_profile:
                fragment = ProfileFragment.newInstance("", "");
                setToolBarTitle(commonToolBar, R.string.title_profile);
                break;
            case R.id.navigation_manage_acc:
                fragment = ManageAccountFragment.newInstance("", "");
                setToolBarTitle(commonToolBar, R.string.title_manage_acc);
                break;
        }

        fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();

        return true;
    };

    @Override
    public void onListFragmentInteraction(AccountHistory item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
