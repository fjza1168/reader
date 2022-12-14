package com.ldp.reader.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.blankj.utilcode.util.BarUtils;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ldp.reader.R;
import com.ldp.reader.RxBus;
import com.ldp.reader.event.BookSyncEvent;
import com.ldp.reader.ui.base.BaseActivity;
import com.ldp.reader.ui.dialog.SexChooseDialog;
import com.ldp.reader.ui.fragment.BookShelfFragment;
import com.ldp.reader.ui.fragment.CommunityFragment;
import com.ldp.reader.ui.fragment.FindFragment;
import com.ldp.reader.utils.Constant;
import com.ldp.reader.utils.PermissionsChecker;
import com.ldp.reader.utils.SharedPreUtils;
import com.ldp.reader.utils.SystemBarUtils;
import com.ldp.reader.utils.ToastUtils;
import com.ldp.reader.widget.tab.ScrollTab;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author ldp
 */
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    /*************Constant**********/
    private static final int WAIT_INTERVAL = 2000;
    private static final int PERMISSIONS_REQUEST_STORAGE = 1;

    static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /***************Object*********************/
    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    @BindView(R.id.st_main)
    ScrollTab stMain;
    /**************View***************/
    @BindView(R.id.tab_tl_indicator)
    protected TabLayout mTlIndicator;
    @BindView(R.id.tab_vp)
    protected ViewPager mVp;
    private List<String> mTitleList;
    private PermissionsChecker mPermissionsChecker;
    /*****************Params*********************/
    private boolean isPrepareFinish = false;


    @Override
    protected int getContentId() {
        return R.layout.activity_base_tab;
    }

    /**************init method***********************/
    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        toolbar.setLogo(R.mipmap.logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");
        setUpTabLayout();
        BarUtils.transparentStatusBar(this);
    }



    protected List<Fragment> createTabFragments() {
        initFragment();
        return mFragmentList;
    }

    private void initFragment() {
        Fragment bookShelfFragment = new BookShelfFragment();
        Fragment communityFragment = new CommunityFragment();
        Fragment discoveryFragment = new FindFragment();
        mFragmentList.add(bookShelfFragment);
        mFragmentList.add(communityFragment);
        mFragmentList.add(discoveryFragment);
    }

    private void setUpTabLayout() {
        createTabFragments();
        mTitleList = createTabTitles();


        TabFragmentPageAdapter adapter = new TabFragmentPageAdapter(getSupportFragmentManager());
        mVp.setAdapter(adapter);
        mVp.setOffscreenPageLimit(3);

        stMain.setTitles(mTitleList);
        stMain.setViewPager(mVp);
        stMain.setOnTabListener((index, v) -> mVp.setCurrentItem(index, true));
    }



    protected List<String> createTabTitles() {
        String[] titles = getResources().getStringArray(R.array.nb_fragment_title);
        return Arrays.asList(titles);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String token = SharedPreUtils.getInstance().getString("token");
        if (TextUtils.isEmpty(token)) {
            menu.getItem(0).setTitle("?????????");
        } else {
            String userName = SharedPreUtils.getInstance().getString("userName");
            menu.getItem(0).setTitle(userName);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Class<?> activityCls = null;
        switch (id) {
            case R.id.action_search:
                activityCls = SearchActivity.class;
                break;
            case R.id.action_login:
                activityCls = LoginActivity.class;
                break;
            case R.id.action_my_message:
                break;
            case R.id.action_download:
                activityCls = DownloadActivity.class;
                break;
            case R.id.action_sync_bookshelf:
                RxBus.getInstance().post(new BookSyncEvent());
                break;
            case R.id.action_scan_local_book:
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                    if (mPermissionsChecker == null) {
                        mPermissionsChecker = new PermissionsChecker(this);
                    }

                    //?????????????????????SD????????????
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                        //????????????
                        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST_STORAGE);
                        return super.onOptionsItemSelected(item);
                    }
                }

                activityCls = FileSystemActivity.class;
                break;
            case R.id.action_wifi_book:
                break;
            case R.id.action_feedback:
                break;
            case R.id.action_night_mode:
                break;
            case R.id.action_settings:
                activityCls = MoreSettingActivity.class;

                break;
            default:
                break;
        }
        if (activityCls != null) {
            Intent intent = new Intent(this, activityCls);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (menu != null && menu instanceof MenuBuilder) {
            try {
                Method method = menu.getClass().
                        getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                method.setAccessible(true);
                method.invoke(menu, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onPreparePanel(featureId, view, menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_STORAGE: {
                // ???????????????????????????????????????0
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //????????? FileSystemActivity
                    Intent intent = new Intent(this, FileSystemActivity.class);
                    startActivity(intent);

                } else {
                    ToastUtils.show("??????????????????????????????");
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!isPrepareFinish) {
            mVp.postDelayed(
                    () -> isPrepareFinish = false, WAIT_INTERVAL
            );
            isPrepareFinish = true;
            Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /******************inner class*****************/
    class TabFragmentPageAdapter extends FragmentPagerAdapter {

        public TabFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }
}
