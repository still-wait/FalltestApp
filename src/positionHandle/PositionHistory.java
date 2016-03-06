package positionHandle;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import myview.Install;

import org.apache.http.conn.ConnectTimeoutException;

import com.example.start_one.R;

import tools.ServiceRulesException;
import tools.SystemBarTintManager;
import tools.UnderLineLinearLayout;
import tools.Utils;
import userlogin.BaseApplication;
import userlogin.LoginMainActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PositionHistory extends Activity implements OnClickListener {
	private static final int FLAG_HIS_SUCCESS = 1;
	private static final String MSG_REQUEST_ERROR = "请求位置信息出错。";
	public static final String MSG_SERVER_ERROR = "请求服务器错误。";
	public static final String MSG_SERVER_TIMEOUT = "请求服务器超时。";
	public static final String MSG_RESPONCE_TIMEOUT = "服务器响应超时。";
	private TextView txt_title;
	private static ProgressDialog dialog;
	private positionService positionService = new history_positionImp();
	private BaseApplication mApplication;
	private List<PositionBean> list = new ArrayList<PositionBean>();

	private UnderLineLinearLayout mUnderLineLinearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.position_history);
		mApplication = (BaseApplication) getApplication();
		BaseApplication.getInstance().addActivity(this);
		mUnderLineLinearLayout = (UnderLineLinearLayout) findViewById(R.id.underline_layout);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.TitleCA);// 通知栏颜色
		}
		initData();
		findViewById();
		setOnListener();
	}

	@SuppressLint("InlinedApi")
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	private void findViewById() {
		findViewById(R.id.img_back).setVisibility(View.VISIBLE);
		findViewById(R.id.txt_left).setVisibility(View.VISIBLE);
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("我的足迹");
	}

	private void setOnListener() {
		// TODO Auto-generated method stub
		findViewById(R.id.img_back).setOnClickListener(this);
		findViewById(R.id.txt_left).setOnClickListener(this);
	}

	private void initData() {
		/**
		 * loading...
		 */
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {

					list = positionService.sendMessage(mApplication
							.getLoginUserName());
                    Thread.sleep(1000);
					mHandler.sendEmptyMessage(FLAG_HIS_SUCCESS);

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
					data.putSerializable("ErrorMsg", MSG_REQUEST_ERROR);
					msg.setData(data);
					mHandler.sendMessage(msg);
				}
			}
		});
		thread.start();

	}

	private void addItems() {
		// TODO Auto-generated method stub
		for (int i = 0; i < list.size(); i++) {
			View v = LayoutInflater.from(this).inflate(
					R.layout.list_item_position, mUnderLineLinearLayout, false);
			((TextView) v.findViewById(R.id.tx_action)).setText(list.get(i)
					.getPosition());
			((TextView) v.findViewById(R.id.tx_action_time)).setText(list
					.get(i).getNowtime());
			((TextView) v.findViewById(R.id.tx_action_status)).setText("去过");
			mUnderLineLinearLayout.addView(v);
		}
	}

	private Handler mHandler = new Handler() {
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			if (dialog != null) {
				dialog.dismiss();
			}

			int flag = msg.what;
			switch (flag) {
			case 0:
				String errorMsg = (String) msg.getData().getSerializable(
						"ErrorMsg");
				showTip(errorMsg);
				break;
			case FLAG_HIS_SUCCESS:
				// showTip(MSG_SEND_SUCCESS);
				findViewById(R.id.phloadView).setVisibility(View.GONE);
				addItems();
				break;

			default:
				break;
			}
		}
	};

	private void showTip(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		// if (str == MSG_SEND_SUCCESS) {
		// Intent intent = new Intent(ResignActivity.this,
		// MainActivity.class);
		// startActivity(intent);
		// finish();
		// }
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(PositionHistory.this);
			break;
		case R.id.txt_left:
			Utils.finish(PositionHistory.this);
			break;
		}
	}
}
