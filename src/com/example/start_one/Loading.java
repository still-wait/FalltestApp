package com.example.start_one;

import localService.LocationService;
import userlogin.BaseApplication;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.baidu.location.BDLocation;

public class Loading extends Activity {
	private static final int GOTO_MAIN_ACTIVITY = 0;
	private static final int INIT_USER = 1;

	private SharedPreferences pref;
	private SharedPreferences pref1;
	private SharedPreferences.Editor editor;
	private BaseApplication mApplication;
	private BDLocation location;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.loading_welcome);
		mApplication = (BaseApplication) getApplication();
		mHandler.sendEmptyMessage(INIT_USER);
		mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 3000);// 5秒跳转
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case INIT_USER:
				pref = getSharedPreferences("user", MODE_PRIVATE);
				String Username = pref.getString("username", "");
				String realname = pref.getString("realname", "");
				String cellphone1 = pref.getString("cellphone1", "");
				String cellphone2 = pref.getString("cellphone2", "");
				String cellphone3 = pref.getString("cellphone3", "");
				boolean isLogin = pref.getBoolean("islogin", false);
				if (isLogin) {
					mApplication.setislogin(true);
					mApplication.setLoginUserName(Username);
					mApplication.setrealname(realname);
					mApplication.setcellphone1(cellphone1);
					mApplication.setcellphone2(cellphone2);
					mApplication.setcellphone3(cellphone3);
				} else {
					mApplication.setislogin(false);
					mApplication.setLoginUserName(null);
					mApplication.setrealname(null);
					mApplication.setcellphone1(null);
					mApplication.setcellphone2(null);
					mApplication.setcellphone3(null);
				}
				/**
				 * 判断服务是否开启
				 */
				pref1 = getSharedPreferences("service", MODE_PRIVATE);
				boolean isopenDD = pref1.getBoolean("isopenDD", false);
				// Log.e("---loading--isopenDD---", "-----"+isopenDD);
				if (isopenDD) {
					mApplication.setIsopenDD(true);
				} else {
					mApplication.setIsopenDD(false);
				}
				/**
				 * 初始化紧急消息
				 */
//				String txt_message = pref1.getString("JJmessage", "");
//				Log.e("初始化信息111----", "--"+txt_message);
//				if (txt_message == "" & Username != null) {
//					editor = pref1.edit();
//					txt_message = realname + "在"// + location.getAddrStr()
//							+ "附近位置，发生跌倒或者一些事故，请尽快救助！！！";
//					Log.e("---初始化信息222----", "--"+txt_message);
//					editor.putString("JJmessage", txt_message);
//					editor.commit();
//				}
				 Intent startIntent = new Intent(Loading.this,LocationService.class);  
		            startService(startIntent); 
		            Log.e("---初始化服务----", "--"+"!!!!!!!!");
				
				break;
			case GOTO_MAIN_ACTIVITY:

				Intent intent = new Intent(Loading.this, MainActivity.class);
				startActivity(intent);
				finish();
				break;

			default:
				break;
			}
		};
	};

}
