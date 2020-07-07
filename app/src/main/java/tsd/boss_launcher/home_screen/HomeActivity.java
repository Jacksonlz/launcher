package tsd.boss_launcher.home_screen;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import tsd.boss_launcher.R;
import tsd.boss_launcher.utils.Constant;
import tsd.boss_launcher.utils.Utils;

public class HomeActivity extends AppCompatActivity {
    HomeButtonBroadcast homeButtonBroadcast = new HomeButtonBroadcast();
    FrameLayout root_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            Utils.setFullScreen(this);
            setContentView(R.layout.activity_home);
            root_container=findViewById(R.id.root_container);
            Utils.checkDefaultLauncher(this);
        }
     else{
             Utils.setFullScreen(this);
            setContentView(R.layout.activity_home);
            root_container=findViewById(R.id.root_container);
            Utils.checkDefaultLauncher(this);
        }
       /* Utils.setFullScreen(this);
        setContentView(R.layout.activity_home);
        root_container=findViewById(R.id.root_container);
        Utils.checkDefaultLauncher(this);*/
        HomeButtonBroadcast.registerBroadcast(this, homeButtonBroadcast);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWallpaper();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        ArrayList<ResolveInfo> appInfos = new ArrayList<>(getPackageManager().queryIntentActivities(mainIntent, 0));
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constant.ACTION_BROADCAST_APP_LIST_CHANGE, appInfos);
        getSupportFragmentManager().findFragmentById(R.id.home_fragment).setArguments(bundle);


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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(homeButtonBroadcast);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
