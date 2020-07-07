package tsd.boss_launcher.app_list_data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import java.util.List;
import tsd.boss_launcher.utils.Constant;

public class AppAlterBroadcast extends BroadcastReceiver {
    AppListViewModel appListViewModel;

    public AppAlterBroadcast(AppListViewModel appListViewModel) {
        this.appListViewModel = appListViewModel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null) {
            return;
        }
        switch (intent.getAction()) {
            case Intent.ACTION_BOOT_COMPLETED:
            case Intent.ACTION_PACKAGE_ADDED:
            case Intent.ACTION_PACKAGE_REPLACED:
            case Intent.ACTION_PACKAGE_REMOVED:
            case Constant.ACTION_BROADCAST_APP_LIST_CHANGE:
                getAppInfoList(context);
                break;
        }
    }

    private void getAppInfoList(Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> appInfos = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        appListViewModel.setAppInfosLiveData(appInfos);
    }

    public static void registerBroadcast(Context context, BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addDataScheme("package");
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        context.registerReceiver(receiver, intentFilter);
    }
}
