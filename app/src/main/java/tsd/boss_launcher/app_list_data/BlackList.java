package tsd.boss_launcher.app_list_data;

import android.content.ComponentName;

import java.util.Arrays;
import java.util.List;

/**
 * a list contain some system app which should not display.
 *
 * @author hellopenggao@gmail.com
 */
public interface BlackList {
    List<ComponentName> blackList = Arrays.asList(
            new ComponentName("com.hisilicon.launcher", "com.hisilicon.launcher.MainActivity"),
            new ComponentName("tsd.bosshub_launcher", "tsd.bosshub_launcher.home_screen.HomeActivity"),
            new ComponentName("tsd.boss_launcher", "tsd.boss_launcher.home_screen.HomeActivity")
    );
}
