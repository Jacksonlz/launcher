package tsd.boss_launcher.app_list_data;

import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

/**
 * @author hellopenggao@gmail.com
 */
public class AppListViewModel extends ViewModel {
    MutableLiveData<List<ResolveInfo>> appInfosLiveData = new MediatorLiveData<>();

    public MutableLiveData<List<ResolveInfo>> getAppInfosLiveData() {
        return appInfosLiveData;
    }

    public void setAppInfosLiveData(List<ResolveInfo> appInfos) {
    	
        for (ComponentName componentName : BlackList.blackList) {
        	
            for (ResolveInfo resolveInfo : appInfos) {
                if (resolveInfo.activityInfo.packageName.equals(componentName.getPackageName()) && resolveInfo.activityInfo.name.equals(componentName.getClassName())) {
                    appInfos.remove(resolveInfo);
                    break;
                }
            }
        }
        appInfosLiveData.postValue(appInfos);
        
        
    }
}
