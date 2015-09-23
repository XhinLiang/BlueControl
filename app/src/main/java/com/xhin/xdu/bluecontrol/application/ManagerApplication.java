package com.xhin.xdu.bluecontrol.application;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * ManagerApplication
 * 用于全局变量的储存和全局Activities的管理
 */
public class ManagerApplication extends Application {
    public final static String TAG = "ManagerApplication";

    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    private static ManagerApplication instance;

    //运用list来保存们每一个activity是关键
    private List<Activity> activities = new LinkedList<>();
    private List<Service> services = new LinkedList<>();


    //实例化一次
    public synchronized static ManagerApplication getInstance() {
        if (null == instance) {
            instance = new ManagerApplication();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public void addService(Service service) {
        services.add(service);
    }

    public void removeService(Service service) {
        services.remove(service);
    }


    public Service getService(int index) {
        return services.get(index);
    }

    public boolean isServiceEmpty() {
        return services.isEmpty();
    }

    public boolean isActivitiesEmpty() {
        return activities.isEmpty();
    }


    private void destroyComponent() {
        try {
            for (Service service : services) {
                if (service != null) {
                    service.stopForeground(true);
                    service.stopSelf();
                }
            }
            services.clear();
            for (Activity activity : activities) {
                if (activity != null) {
                    activity.finish();
                }
            }
            activities.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

}