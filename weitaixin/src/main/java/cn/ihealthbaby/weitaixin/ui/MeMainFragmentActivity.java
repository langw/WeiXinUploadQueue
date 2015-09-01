package cn.ihealthbaby.weitaixin.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ihealthbaby.weitaixin.R;
import cn.ihealthbaby.weitaixin.WeiTaiXinApplication;
import cn.ihealthbaby.weitaixin.base.BaseActivity;
import cn.ihealthbaby.weitaixin.ui.home.HomePageFragment;
import cn.ihealthbaby.weitaixin.ui.login.LoginActivity;
import cn.ihealthbaby.weitaixin.ui.mine.WoInfoFragment;
import cn.ihealthbaby.weitaixin.ui.monitor.MonitorFragment;
import cn.ihealthbaby.weitaixin.ui.record.RecordFragment;

/**
 * Created by Think on 2015/8/13.
 */
public class MeMainFragmentActivity extends BaseActivity {


    @Bind(R.id.iv_tab_01) ImageView iv_tab_01;
    @Bind(R.id.iv_tab_02) ImageView iv_tab_02;

    @Bind(R.id.iv_tab_03) ImageView iv_tab_03;
    @Bind(R.id.iv_tab_04) ImageView iv_tab_04;
    @Bind(R.id.container) FrameLayout container;

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_main_fragment);
        ButterKnife.bind(this);
        showTabFirst();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void showTabFirst() {
        iv_tab_01.setSelected(true);
        if (homePageFragment == null) {
            homePageFragment = new HomePageFragment();
        }
        fragmentManager = getFragmentManager();
        showFragment(R.id.container, homePageFragment);
    }


    public HomePageFragment homePageFragment;
    public MonitorFragment monitorFragment;
    public RecordFragment recordFragment;
    public WoInfoFragment woInfoFragment;

    @OnClick(R.id.iv_tab_01)
    public void iv_tab_01() {
        showTab(iv_tab_01);
        if (WeiTaiXinApplication.getInstance().isLogin) {
            homePageFragment=HomePageFragment.getInstance();
            showFragment(R.id.container, homePageFragment);
        }
    }

    @OnClick(R.id.iv_tab_02)
    public void iv_tab_02() {
        showTab(iv_tab_02);
        if (WeiTaiXinApplication.getInstance().isLogin) {
            if (monitorFragment == null) {
                monitorFragment = new MonitorFragment();
            }
            showFragment(R.id.container, monitorFragment);
        }
    }

    @OnClick(R.id.iv_tab_03)
    public void iv_tab_03() {
        showTab(iv_tab_03);
        if (WeiTaiXinApplication.getInstance().isLogin) {
            recordFragment=RecordFragment.getInstance();
            showFragment(R.id.container, recordFragment);
        }
    }


    @OnClick(R.id.iv_tab_04)
    public void iv_tab_04() {
        showTab(iv_tab_04);
        if (WeiTaiXinApplication.getInstance().isLogin) {
            woInfoFragment=WoInfoFragment.getInstance();
            showFragment(R.id.container, woInfoFragment);
        }
    }


    public void showTab(ImageView imageView) {
        if (!WeiTaiXinApplication.getInstance().isLogin) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            return;
        }

        iv_tab_01.setSelected(false);
        iv_tab_02.setSelected(false);
        iv_tab_03.setSelected(false);
        iv_tab_04.setSelected(false);
        imageView.setSelected(true);
    }


    private void showFragment(int container, Fragment fragment/*, int animIn, int animOut*/) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(animIn, animOut);
        fragmentTransaction.replace(container, fragment);
        fragmentTransaction.commit();
    }


}









