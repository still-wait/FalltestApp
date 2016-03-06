package fragment;

import userlogin.BaseApplication;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import baiduMap.MyOrientationListener;
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

public class BaidumapFragment extends Fragment {
//	private static final int FLAG_SEND_SUCCESS = 1;
//	private static final String MSG_SEND_ERROR = "λ���ϴ�����";
//	private static final String MSG_SEND_SUCCESS = "λ���ϴ��ɹ���";
	public static final String MSG_SERVER_ERROR = "�������������";
	public static final String MSG_SERVER_TIMEOUT = "�����������ʱ��";
	public static final String MSG_RESPONCE_TIMEOUT = "��������Ӧ��ʱ��";

//	private ConnectService connectService = new ConnectServiceImp();
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private TextView Location;
	private Context context;

	// ��λ���
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private boolean isFirstIn = true;
	private double mLatitude;
	private double mLongtitude;
	private BDLocation location;
	// �Զ��嶨λͼ��
	private BitmapDescriptor mIconLocation;
	private MyOrientationListener myOrientationListener;
	private float mCurrentX;
	private LocationMode mLocationMode;

	private Activity ctx;
	private View layout;
	private BaseApplication mApplication;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mApplication = (BaseApplication) getActivity().getApplication();
		if (layout == null) {
			ctx = this.getActivity();
			SDKInitializer.initialize(getActivity().getApplicationContext());
			layout = ctx.getLayoutInflater().inflate(R.layout.mapview, container, false);
			setHasOptionsMenu(true);
		} else {
			ViewGroup parent = (ViewGroup) layout.getParent();
			if (parent != null) {
				parent.removeView(layout);
			}
		}
		return layout;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mApplication = (BaseApplication) getActivity().getApplication();
		mApplication.setpoision_flag(true);
		Location = (TextView) getActivity().findViewById(R.id.location);
		this.context = getActivity();

		initView();
		// ��ʼ����λ
		initLocation();
	}
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
//						mHandler.sendEmptyMessage(FLAG_SEND_SUCCESS);
//						Thread.sleep(5000);
//					}
//				} catch (ConnectTimeoutException e) {
//					e.printStackTrace();
//					Message msg = new Message();
//					Bundle data = new Bundle();
//					data.putSerializable("ErrorMsg", MSG_SERVER_TIMEOUT);
//					msg.setData(data);
//					mHandler.sendMessage(msg);
//				} catch (SocketTimeoutException e) {
//					e.printStackTrace();
//					Message msg = new Message();
//					Bundle data = new Bundle();
//					data.putSerializable("ErrorMsg", MSG_RESPONCE_TIMEOUT);
//					msg.setData(data);
//					mHandler.sendMessage(msg);
//				} catch (ServiceRulesException e) {
//					e.printStackTrace();
//					Message msg = new Message();
//					Bundle data = new Bundle();
//					data.putSerializable("ErrorMsg", e.getMessage());
//					msg.setData(data);
//					mHandler.sendMessage(msg);
//				} catch (Exception e) {
//					e.printStackTrace();
//					Message msg = new Message();
//					Bundle data = new Bundle();
//					data.putSerializable("ErrorMsg", MSG_SEND_ERROR);
//					msg.setData(data);
//					mHandler.sendMessage(msg);
//				}
//			}
//		});
//		thread.start();
//	}
//
//	private Handler mHandler = new Handler() {
//		@SuppressLint("HandlerLeak")
//		@Override
//		public void handleMessage(Message msg) {
//			int flag = msg.what;
//			switch (flag) {
//			case 0:
//				String errorMsg = (String) msg.getData().getSerializable(
//						"ErrorMsg");
//				showTip(errorMsg);
//				break;
//			case FLAG_SEND_SUCCESS:
//				showTip(MSG_SEND_SUCCESS);
//				break;
//
//			default:
//				break;
//			}
//		}
//	};
//
//	private void showTip(String str) {
//		Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();
//		if (str == MSG_SEND_SUCCESS) {
//			// Intent intent = new Intent(ResignActivity.this,
//			// MainActivity.class);
//			// startActivity(intent);
//			// finish();
//		}
//	}

	private void initLocation() {

		mLocationMode = LocationMode.NORMAL;
		mLocationClient = new LocationClient(getActivity());
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);

		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);
		// ��ʼ��ͼ��
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

		mLocationClient.start(); // ��ʼ��λ
	}

	private void initView() {
		mMapView = (MapView) getActivity().findViewById(R.id.id_bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
	}

	@Override
	public void onResume() {
		super.onResume();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
		// ������λ
		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocationClient.isStarted())
			mLocationClient.start();
		// �������򴫸���
		myOrientationListener.start();
	}

	@Override
	public void onPause() {
		super.onPause();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();

		// ֹͣ��λ
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		// ֹͣ���򴫸���
		myOrientationListener.stop();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onDestroy();
	}

//	public boolean onCreateOptionsMenu(Menu menu) {
//		getActivity().getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
	
	@Override  
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)  
    {  
        inflater.inflate(R.menu.baidumap_menu, menu);  
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
				item.setTitle("ʵʱ��ͨ(off)");
			} else {
				mBaiduMap.setTrafficEnabled(true);
				item.setTitle("ʵʱ��ͨ(on)");
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

//		return super.onOptionsItemSelected(item);
		return true;
	}

	/**
	 * ��λ���ҵ�λ��
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
			// �����Զ���ͼ��
			MyLocationConfiguration config = new MyLocationConfiguration(
					mLocationMode, true, mIconLocation);
			mBaiduMap.setMyLocationConfigeration(config);

			// ���¾�γ��
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();

			Location.setText("�ҵ�λ��:" + location.getAddrStr());
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
