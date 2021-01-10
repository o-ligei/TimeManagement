package com.example.wowtime.ui.pomodoro;

import android.app.ActivityManager;
import android.app.Application;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import androidx.annotation.RequiresApi;
import com.example.wowtime.dto.WhiteListItem;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


public class ApkTool {

    static String TAG = "ApkTool";
    public static List<WhiteListItem> mLocalInstallApps = null;

    public static List<WhiteListItem> scanLocalInstallAppList(PackageManager packageManager,
            List<String> alreadySelectedPaackage) {
        List<WhiteListItem> whiteListItems = new ArrayList<WhiteListItem>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);

                //过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    continue;
                }

                WhiteListItem whiteListItem = new WhiteListItem();
                whiteListItem.setPackageName(packageInfo.packageName);
                whiteListItem.setAppName(
                        packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) { continue; }
                whiteListItem.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                if (alreadySelectedPaackage.indexOf(packageInfo.packageName) != -1) {
                    whiteListItem.setSelected(true);
                } else { whiteListItem.setSelected(false); }
                whiteListItem.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                whiteListItems.add(whiteListItem);
            }
        } catch (Exception e) {
            Log.e(TAG, "===============获取应用包信息失败");
            return null;
        }
        return whiteListItems;
    }

    public static List<WhiteListItem> getSelectedPackageList(PackageManager packageManager,
            List<String> selectedPackage) {
        List<WhiteListItem> whiteListItems = new ArrayList<WhiteListItem>();
        List<PackageInfo> packageInfos;
        try {
            packageInfos = packageManager.getInstalledPackages(0);
        } catch (Exception e) {
            Log.e(TAG, "===============获取应用包信息失败");
            return null;
        }
        for (String selected : selectedPackage) {
            for (PackageInfo packageInfo : packageInfos) {
                if (packageInfo.packageName.equals(selected)) {
                    WhiteListItem whiteListItem = new WhiteListItem();
                    whiteListItem.setPackageName(packageInfo.packageName);
                    whiteListItem.setAppName(
                            packageManager.getApplicationLabel(packageInfo.applicationInfo)
                                          .toString());
                    if (packageInfo.applicationInfo.loadIcon(packageManager) == null) { continue; }
                    whiteListItem.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                    whiteListItem.setSelected(true);
                    whiteListItems.add(whiteListItem);
                    break;
                }
            }
        }

        return whiteListItems;
    }

    public static List<PackageInfo> scanLocalInstallAppListByPackage(
            PackageManager packageManager) {
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            PackageInfo packageInfo = packageInfos.get(i);

            //过滤掉系统app
            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                packageInfos.remove(packageInfo);
            }
        }
        return packageInfos;
    }

    public static void resizePikcer(FrameLayout tp) {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList) {
            resizeNumberPicker(np);
        }
    }

    public static List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    public static void resizeNumberPicker(NumberPicker np) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, 400);
        params.setMargins(10, 0, 10, 0);
        np.setLayoutParams(params);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static String getTaskPackname(Application self) {
        String currentApp = "CurrentNULL";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) self
                    .getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm
                    .queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager) self.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }
        return currentApp;
    }
}

//some reference for feaaaaaa
//
//    PackageManager pManager = WhiteListActivity.this.getPackageManager();
//    List<PackageInfo> appList  =getAllApps(WhiteListActivity.this);
//
//        for(int i=0;i<appList.size();i++) {
//        PackageInfo pinfo = appList.get(i);
//        ShareItemInfo shareItem = new ShareItemInfo();
//        //set Icon
//        shareItem.setIcon(pManager.getApplicationIcon(pinfo.applicationInfo));
//        //set Application Name
//        shareItem.setLabel(pManager.getApplicationLabel(pinfo.applicationInfo).toString());
//        //set Package Name
//        shareItem.setPackageName(pinfo.applicationInfo.packageName);
//
//        System.out.println("!!!"+pManager.getApplicationLabel(pinfo.applicationInfo).toString());
//        }
