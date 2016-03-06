package baiduMap;

import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.conn.ConnectTimeoutException;

import tools.ServiceRulesException;
import userlogin.BaseApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import baiduMap.MyOrientationListener.OnOrientationListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.start_one.R;

public class MapMainActivity extends Activity {
	private static final int FLAG_SEND_SUCCESS = 1;
	private static final String MSG_SEND_ERROR = "位置上传出错。";
	private static final String MSG_SEND_SUCCESS = "位置上传成功。";
	public static final String MSG_SERVER_ERROR = "请求服务器错误。";
	public static final String MSG_SERVER_TIMEOUT = "请求服务器超时。";
	public static final String MSG_RESPONCE_TIMEOUT = "服务器响应超时。";

	private ConnectService connectService = new ConnectServiceImp();
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private TextView Location;
	private Context context;

	// 定位相关
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private boolean isFirstIn = true;
	private double mLatitude;
	private double mLongtitude;
	private BDLocation location;
	// 自定义定位图标
	private BitmapDescriptor mIconLocation;
	private MyOrientationListener myOrientationListener;
	private float mCurrentX;
	private LocationMode mLocationMode;

	private BaseApplication mApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_NEEDS_MENU_KEY,
		// WindowManager.LayoutParams.FLAG_NEEDS_MENU_KEY);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.mapview);
		mApplication = (BaseApplication) getApplication();
		mApplication.setpoision_flag(true);
		Location = (TextView) findViewById(R.id.location);
		this.context = this;

		initView();
		// 初始化定位
		initLocation();

//		Thread thread = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
//
//					while (mApplication.getpoision_flag() == true) {
//						String mpoision = Location.getText().toString();
//						String currentTime = DateFormat.format(
//								"yyyy-MM-dd hh:mm:ss", new Date()).toString();
//						SimpleDateFormat sdf = new SimpleDateFormat(
//								"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//						currentTime = sdf.format(new Date());
//						connectService.sendMessage(
//								mApplication.getLoginUserName(), currentTime,
//								mpoision,mLatitude,mLongtitude);
//						handler.sendEmptyMessage(FLAG_SEND_SUCCESS);
//						Thread.sleep(5000);
//					}
//				} catch (ConnectTimeoutException e) {
//					e.printStackTrace();
//					Message msg = new Message();
//					Bundle data = new Bundle();
//					data.putSerializable("ErrorMsg", MSG_SERVER_TIMEOUT);
//					msg.setData(data);
//					handler.sendMessage(msg);
//				} catch (SocketTimeoutException e) {
//					e.printStackTrace();
//					Message msg = new Message();
//					Bundle data = new Bundle();
//					data.putSerializable("ErrorMsg", MSG_RESPONCE_TIMEOUT);
//					msg.setData(data);
//					handler.sendMessage(msg);
//				} catch (ServiceRulesException e) {
//					e.printStackTrace();
//					Message msg = new Message();
//					Bundle data = new Bundle();
//					data.putSerializable("ErrorMsg", e.getMessage());
//					msg.setData(data);
//					handler.sendMessage(msg);
//				} catch (Exception e) {
//					e.printStackTrace();
//					Message msg = new Message();
//					Bundle data = new Bundle();
//					data.putSerializable("ErrorMsg", MSG_SEND_ERROR);
//					msg.setData(data);
//					handler.sendMessage(msg);
//				}
//			}
//		});
//		thread.start();
	}

//	private static class IHandler extends Handler {
//
//		private final WeakReference<Activity> mActivity;
//
//		public IHandler(MapMainActivity activity) {
//			mActivity = new WeakReference<Activity>(activity);
//		}
//
//		@Override
//		public void handleMessage(Message msg) {
//
//			// if (dialog != null) {
//			// dialog.dismiss();
//			// }
//
//			int flag = msg.what;
//			switch (flag) {
//			case 0:
//				String errorMsg = (String) msg.getData().getSerializable(
//						"ErrorMsg");
//				((MapMainActivity) mActivity.get()).showTip(errorMsg);
//				break;
//			case FLAG_SEND_SUCCESS:
//				((MapMainActivity) mActivity.get()).showTip(MSG_SEND_SUCCESS);
//				break;
//
//			default:
//				break;
//			}
//		}
//	}
//
//	private void showTip(String str) {
//		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
//		if (str == MSG_SEND_SUCCESS) {
//			// Intent intent = new Intent(ResignActivity.this,
//			// MainActivity.class);
//			// startActivity(intent);
//			// finish();
//		}
//	}
//
//	private IHandler handler = new IHandler(this);

	private void initLocation() {

		mLocationMode = LocationMode.NORMAL;
		mLocationClient = new LocationClient(this);
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);

		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);
		// 初始化图标
		mIconLocation = BitmapDescriptorFactory
				.fromResource(R.drawable.navi_map_gps_locked);
		myOrientationListener = new MyOrientationListener(context);

		myOrientationListener
				.setOnOrientationListener(new OnOrientationListener() {
					@Override
					public void onOrientationChanged(float x) {
						mCurrentX = x;
					}
				});

		mLocationClient.start(); // 开始定位
	}

	private void initView() {
		mMapView = (MapView) findViewById(R.id.id_bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 开启定位
		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocationClient.isStarted())
			mLocationClient.start();
		// 开启方向传感器
		myOrientationListener.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();

		// 停止定位
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		// 停止方向传感器
		myOrientationListener.stop();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.id_map_common:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;

		case R.id.id_map_site:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;

		case R.id.id_map_traffic:
			if (mBaiduMap.isTrafficEnabled()) {
				mBaiduMap.setTrafficEnabled(false);
				item.setTitle("实时交通(off)");
			} else {
				mBaiduMap.setTrafficEnabled(true);
				item.setTitle("实时交通(on)");
			}
			break;
		case R.id.id_map_location:
			centerToMyLocation();
			break;
		case R.id.id_map_mode_common:
			mLocationMode = LocationMode.NORMAL;
			break;
		case R.id.id_map_mode_following:
			mLocationMode = LocationMode.FOLLOWING;
			break;
		case R.id.id_map_mode_compass:
			mLocationMode = LocationMode.COMPASS;
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * 定位到我的位置
	 */
	private void centerToMyLocation() {
		LatLng latLng = new LatLng(mLatitude, mLongtitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}

	private class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {

			MyLocationData data = new MyLocationData.Builder()//
					.direction(mCurrentX)//
					.accuracy(location.getRadius())//
					.latitude(location.getLatitude())//
					.longitude(location.getLongitude())//
					.build();
			mBaiduMap.setMyLocationData(data);
			// 设置自定义图标
			MyLocationConfiguration config = new MyLocationConfiguration(
					mLocationMode, true, mIconLocation);
			mBaiduMap.setMyLocationConfigeration(config);

			// 更新经纬度
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();

			Location.setText("我的位置:" + location.getAddrStr());
			if (isFirstIn) {
				LatLng latLng = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				isFirstIn = false;
				mLocationMode = LocationMode.FOLLOWING;
				Toast.makeText(context, location.getAddrStr(),
						Toast.LENGTH_SHORT).show();
			}

		}
	}

}
