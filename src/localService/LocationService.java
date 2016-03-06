package localService;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.conn.ConnectTimeoutException;

import tools.ServiceRulesException;
import userlogin.BaseApplication;

import baiduMap.ConnectService;
import baiduMap.ConnectServiceImp;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;

public class LocationService extends Service {
	private static final int FLAG_SEND_SUCCESS = 1;
	private static final String MSG_SEND_ERROR = "位置上传出错。";
	private static final String MSG_SEND_SUCCESS = "位置上传成功。";
	public static final String MSG_SERVER_ERROR = "请求服务器错误。";
	public static final String MSG_SERVER_TIMEOUT = "请求服务器超时。";
	public static final String MSG_RESPONCE_TIMEOUT = "服务器响应超时。";
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private String mlocation;
	private double mLatitude;
	private double mLongtitude;
	private MyLocationListener mLocationListener;
	private LocationClient mLocationClient;
	private BaseApplication mApplication;
	private ConnectService connectService = new ConnectServiceImp();

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		pref = getSharedPreferences("service", MODE_PRIVATE);
		editor = pref.edit();
		mApplication = (BaseApplication) getApplication();
		mApplication.setpoision_flag(true);
		mLocationClient = new LocationClient(this);
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(60000);// 设置发起定位请求的间隔时间为5000ms
		mLocationClient.setLocOption(option);
		mLocationClient.start(); // 开始定位
		Log.e("--服务信息----", "@@@@@@@@@@@");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {

					while (mApplication.getpoision_flag() == true) {
						String realname = mApplication.getrealname();
						String mtxt = realname + "在" + mlocation
								+ "附近位置，发生跌倒或者一些事故，请尽快救助！！！";
						Log.e("--服务信息----", mtxt);
						editor.putString("JJmessage", mtxt);
                        editor.commit();
						// 获取系统时间
						String currentTime = DateFormat.format(
								"yyyy-MM-dd hh:mm:ss", new Date()).toString();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
						currentTime = sdf.format(new Date());

						connectService.sendMessage(
								mApplication.getLoginUserName(), currentTime,
								mlocation, mLatitude, mLongtitude);
						mHandler.sendEmptyMessage(FLAG_SEND_SUCCESS);
						Thread.sleep(10000);
					}
				} catch (ConnectTimeoutException e) {
					e.printStackTrace();
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putSerializable("ErrorMsg", MSG_SERVER_TIMEOUT);
					msg.setData(data);
					mHandler.sendMessage(msg);
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putSerializable("ErrorMsg", MSG_RESPONCE_TIMEOUT);
					msg.setData(data);
					mHandler.sendMessage(msg);
				} catch (ServiceRulesException e) {
					e.printStackTrace();
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putSerializable("ErrorMsg", e.getMessage());
					msg.setData(data);
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					Bundle data = new Bundle();
					data.putSerializable("ErrorMsg", MSG_SEND_ERROR);
					msg.setData(data);
					mHandler.sendMessage(msg);
				}
			}

		}).start();
		return super.onStartCommand(intent, flags, startId);
	}

	private Handler mHandler = new Handler() {
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			int flag = msg.what;
			switch (flag) {
			case 0:
				String errorMsg = (String) msg.getData().getSerializable(
						"ErrorMsg");
				break;
			case FLAG_SEND_SUCCESS:
				break;

			default:
				break;
			}
		}
	};

	private class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			mlocation = location.getAddrStr();
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();
		}
	}

}
