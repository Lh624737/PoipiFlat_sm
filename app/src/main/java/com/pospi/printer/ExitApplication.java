package com.pospi.printer;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

public class ExitApplication extends Application
{
	private List <Activity>activityList = new LinkedList();
	 private static ExitApplication instance;
	 private ExitApplication()
	 {
	 }
	 public static ExitApplication getInstance()
	 {
	 if(null == instance)
	 {
	 instance = new ExitApplication();
	 }
	 return instance;
	 }
	 public void addActivity(Activity activity)
	 {
	 activityList.add(activity);
	 }
	 public void exit()
	 {
	   for(Activity activity:activityList)
	 {
	 activity.finish();
	 }
	 System.exit(0);
	 }
}

