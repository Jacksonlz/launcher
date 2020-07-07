package tsd.boss_launcher.app_list_screen;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import tsd.boss_launcher.R;
import tsd.boss_launcher.app_list_data.AppAlterBroadcast;
import tsd.boss_launcher.app_list_data.AppListViewModel;
import tsd.boss_launcher.utils.Constant;
import tsd.boss_launcher.utils.Utils;

public class AppListActivity extends AppCompatActivity {
    AppAlterBroadcast appAlterBroadcast;
    FrameLayout root_container;
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Utils.setFullScreen(this);
        setContentView(R.layout.activity_app_list);
       // root_container=findViewById(R.id.root_container);
        AppListViewModel appListViewModel = ViewModelProviders.of(this).get(AppListViewModel.class);
        appAlterBroadcast = new AppAlterBroadcast(appListViewModel);
        // List<ResolveInfo> appInfos = getIntent().getParcelableArrayListExtra(Constant.ACTION_BROADCAST_APP_LIST_CHANGE);
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        ArrayList<ResolveInfo> appInfos = new ArrayList<>(getPackageManager().queryIntentActivities(mainIntent, 0));
        if (appInfos != null) {

            appListViewModel.setAppInfosLiveData(appInfos);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppAlterBroadcast.registerBroadcast(this, appAlterBroadcast);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(appAlterBroadcast);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWallpaper();
    }

    public boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    void updateWallpaper() {
        try {

            //WallpaperManager����??��?����??
            WallpaperManager mWallpaperManager = WallpaperManager.getInstance(this);
            Drawable mBackground = mWallpaperManager.getDrawable();

            root_container.setBackground(mBackground);


        } catch (RuntimeException e) {

        }

    }
}
