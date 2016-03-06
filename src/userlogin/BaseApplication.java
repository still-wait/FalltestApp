package userlogin;

import java.util.ArrayList;

import fragment.PatternFragment;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;

public class BaseApplication extends Application {
	private ArrayList<Activity> mList = new ArrayList<Activity>();
	private static BaseApplication instance;
	private String login_username = "";
	private boolean poision_flag = false;
	private boolean islogin = false;
	private boolean isopenDD = false;
	private String realname = "";
	private String cellphone1 = "";
	private String cellphone2 = "";
	private String cellphone3 = "";
	private int id = 0;
//	private NotificationManager mNotificationManager;

	// private BaseApplication() {
	// }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLoginUserName() {
		return login_username;
	}

	public void setLoginUserName(String login_username) {
		this.login_username = login_username;
	}

	public String getrealname() {
		return realname;
	}

	public void setrealname(String realname) {
		this.realname = realname;
	}

	public String getcellphone1() {
		return cellphone1;
	}

	public void setcellphone1(String cellphone1) {
		this.cellphone1 = cellphone1;
	}

	public String getcellphone2() {
		return cellphone2;
	}

	public void setcellphone2(String cellphone2) {
		this.cellphone2 = cellphone2;
	}

	public String getcellphone3() {
		return cellphone3;
	}

	public void setcellphone3(String cellphone3) {
		this.cellphone3 = cellphone3;
	}

	public boolean getpoision_flag() {
		return poision_flag;
	}

	public void setpoision_flag(boolean poision_flag) {
		this.poision_flag = poision_flag;
	}

	public boolean getislogin() {
		return islogin;
	}

	public void setislogin(boolean islogin) {
		this.islogin = islogin;
	}

	public boolean getisIsopenDD() {
		return isopenDD;
	}

	public void setIsopenDD(boolean isopenDD) {
		this.isopenDD = isopenDD;
	}

	public synchronized static BaseApplication getInstance() {
		if (null == instance) {
			instance = new BaseApplication();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
			PatternFragment.mNotificationManager.cancelAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

}
