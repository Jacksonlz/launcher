package tsd.boss_launcher;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author hellopenggao@gmail.com
 */
public class BosshubApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "65f29953ab", true);
    }
}
