package com.example.wowtime.ui.pomodoro;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gray_dog3 on 16/3/3.
 * 扫描本地安装的应用,工具类
 */
public class ApkTool {
    static  String TAG = "ApkTool";
    public static List<MyAppInfo> mLocalInstallApps = null;

    public static List<MyAppInfo> scanLocalInstallAppList(PackageManager packageManager) {
        List<MyAppInfo> myAppInfos = new ArrayList<MyAppInfo>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //过滤掉系统app
                if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                    continue;
                }
                MyAppInfo myAppInfo = new MyAppInfo();
                myAppInfo.setAppName(packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
                if (packageInfo.applicationInfo.loadIcon(packageManager) == null) {
                    continue;
                }
                myAppInfo.setImage(packageInfo.applicationInfo.loadIcon(packageManager));
                myAppInfos.add(myAppInfo);
            }
        }catch (Exception e){
            Log.e(TAG,"===============获取应用包信息失败");
            return null;
        }
        return myAppInfos;
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
