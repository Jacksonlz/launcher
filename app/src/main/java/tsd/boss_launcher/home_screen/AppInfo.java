package tsd.boss_launcher.home_screen;

import android.content.ComponentName;

/**
 * @author hellopenggao@gmail.com
 */
public interface AppInfo {
    ComponentName systemClockComponent = new ComponentName("com.android.deskclock", "com.android.deskclock.DeskClock");
    ComponentName systemClockComponent2 = new ComponentName("com.google.android.deskclock", "com.android.deskclock.DeskClock");
    ComponentName whiteBoardComponent = new ComponentName("com.newskyer.draw", "com.newskyer.draw.MainActivity");
    ComponentName signalComponent = new ComponentName("com.hisilicon.tvsetting", "com.hisilicon.tvsetting.SourceListActivity");
    //ComponentName screenCastComponent = new ComponentName("com.bozee.andisplay.configer", "com.bozee.andisplay.configer.StartupActivity");
    ComponentName screenCastComponent = new ComponentName("com.ecloud.eshare.server", "com.ecloud.eshare.server.CifsClientActivity");
    ComponentName recordScreenComponent = new ComponentName("com.kjd.screenrecordlibrary", "com.kjd.screenrecordlibrary.MainActivity");


}
