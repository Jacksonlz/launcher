package tsd.boss_launcher.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * @author hellopenggao@gmail.com
 */
public class Utils {
    public static void setFullScreen(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        Window window = activity.getWindow();
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
//        window.setAttributes(params);
    }

    public static void checkDefaultLauncher(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = activity.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo == null) {
            return;
        }
        if (resolveInfo.activityInfo.packageName.equals(activity.getPackageName())) {
            return;
        }
        Intent paramIntent = new Intent("android.intent.action.MAIN");
        paramIntent.setComponent(new ComponentName("android", "com.android.internal.app.ResolverActivity"));
        paramIntent.addCategory("android.intent.category.DEFAULT");
        paramIntent.addCategory("android.intent.category.HOME");
        activity.startActivity(paramIntent);
    }

    public static void showAllPackage(Activity activity) {


        try {
            Intent intent=new Intent();
            intent.setPackage("com.hisilicon.tvui");
            activity.startActivity(intent);
        } catch (Exception e) {
            Intent intent=new Intent();
            intent.setPackage("com.hisilicon.tvsetting");
            Toast.makeText(activity,"2222",Toast.LENGTH_LONG).show();
            activity.startActivity(intent);
        }

//        List<PackageInfo> appInfos = activity.getPackageManager().getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);
//        ScrollView scrollView = new ScrollView(activity);
//        LinearLayout linearLayout = new LinearLayout(activity);
//        scrollView.addView(linearLayout);
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        for (PackageInfo p : appInfos) {
//            TextView textView = new TextView(activity);
//            String text =  "/" + p.applicationInfo.loadLabel(activity.getPackageManager());
//            textView.setText(p.packageName + "/" + text.toString());
//            linearLayout.addView(textView);
//        }
//        activity.setContentView(scrollView);
    }
}
