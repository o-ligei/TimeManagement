package com.example.wowtime.ui.pomodoro;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.wowtime.dto.WhiteListItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gray_dog3 on 16/3/3.
 * 扫描本地安装的应用,工具类
 */
public class ApkTool {
    static  String TAG = "ApkTool";
    public static List<WhiteListItem> mLocalInstallApps = null;

    public static List<WhiteListItem> scanLocalInstallAppList(PackageManager packageManager, List<String> alreadySelectedPaackage) {
        List<WhiteListItem> whiteListItems = new ArrayList<WhiteListItem>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);

                //过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0)
                    continue;

                WhiteListItem whiteListItem = new WhiteListItem();
                whiteListItem.setPackageName(packageInfo.packageName);
                whiteListItem.setAppName(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null)
                    continue;
                whiteListItem.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                if(alreadySelectedPaackage.indexOf(packageInfo.packageName)!=-1) {
                    System.out.println("apktoolAlreadySelect:"+packageInfo.packageName);
                    whiteListItem.setSelected(true);
                }
                else {
                    System.out.println("apktoolHaven'tSelect:"+packageInfo.packageName);
                    whiteListItem.setSelected(false);
                }
                whiteListItems.add(whiteListItem);
            }
        }catch (Exception e){
            Log.e(TAG,"===============获取应用包信息失败");
            return null;
        }
        return whiteListItems;
    }

    public static List<WhiteListItem> getSelectedPackageList(PackageManager packageManager, List<String> selectedPackage) {
        List<WhiteListItem> whiteListItems = new ArrayList<WhiteListItem>();
        List<PackageInfo> packageInfos;
        try {
            packageInfos = packageManager.getInstalledPackages(0);
        } catch (Exception e) {
            Log.e(TAG, "===============获取应用包信息失败");
            return null;
        }
        for (String selected:selectedPackage) {
            for (PackageInfo packageInfo : packageInfos) {
                if (packageInfo.packageName.equals(selected)) {
                    WhiteListItem whiteListItem = new WhiteListItem();
                    whiteListItem.setPackageName(packageInfo.packageName);
                    whiteListItem.setAppName(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
                    if (packageInfo.applicationInfo.loadIcon(packageManager) == null)
                        continue;
                    whiteListItem.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                    whiteListItem.setSelected(true);
                    whiteListItems.add(whiteListItem);
                    break;
                }
            }
        }

        return whiteListItems;
    }

    public static List<PackageInfo> scanLocalInstallAppListByPackage(PackageManager packageManager) {
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

//    public static List<PackageInfo> getAllApps(Context context) {
//        List<PackageInfo> apps = new ArrayList<PackageInfo>();
//        PackageManager pManager = context.getPackageManager();
//        //获取手机内所有应用
//        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
//        for (int i = 0; i < paklist.size(); i++) {
//            PackageInfo pak = (PackageInfo) paklist.get(i);
//            //判断是否为非系统预装的应用程序
//            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
//                // customs applications
//                apps.add(pak);
//            }
//        }
//        return apps;
//    }
