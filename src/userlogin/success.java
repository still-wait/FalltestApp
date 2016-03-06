package userlogin;

import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.start_one.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class success extends ActionBarActivity {

	private UserService userService = new UserServiceImpl(); // 关键
	private static ProgressDialog dialog;

	private static final int FLAG_SELECT_SUCCESS = 1;
	private static final String MSG_SELECT_ERROR = "获取出错。";
	private static final String MSG_SELECT_SUCCESS = "获取成功。";
	public static final String MSG_SERVER_ERROR = "请求服务器错误。";
	public static final String MSG_SERVER_TIMEOUT = "请求服务器超时。";
	public static final String MSG_RESPONCE_TIMEOUT = "服务器响应超时。";
	private static String loginName;
	private static String realname;
	private static String cellphone1;
	private static String cellphone2;
	private static String cellphone3;
	private static TextView username;
	private static TextView Realname;
	private static TextView Cellphone1;
	private static TextView Cellphone2;
	private static TextView Cellphone3;
	static JSONObject obj=new JSONObject();

	private SharedPreferences pref; 
	private SharedPreferences.Editor editor;
	 private BaseApplication mApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.success);
		
		mApplication = (BaseApplication) getApplication();
		
		username = (TextView) findViewById(R.id.account2);
		Realname = (TextView) findViewById(R.id.name2);
		Cellphone1 = (TextView) findViewById(R.id.phone12);
		Cellphone2 = (TextView) findViewById(R.id.phone22);
		Cellphone3 = (TextView) findViewById(R.id.phone32);
		Button button1 = (Button) findViewById(R.id.request);
		Button button2 = (Button) findViewById(R.id.revise);
		button1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (dialog == null) {
					dialog = new ProgressDialog(success.this);
				}
				dialog.setTitle("请等待");
				dialog.setMessage("查询中...");
				dialog.setCancelable(false);
				dialog.show();
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
//							loginName=pref.getString("username", "");
//							loginName="tom";
							loginName=mApplication.getLoginUserName();
							obj=userService.selectUser(loginName, realname,
									cellphone1, cellphone2, cellphone3);
							
							handler.sendEmptyMessage(FLAG_SELECT_SUCCESS);
							

						} catch (ConnectTimeoutException e) {
							e.printStackTrace();
							Message msg = new Message();
							Bundle data = new Bundle();
							data.putSerializable("ErrorMsg", MSG_SERVER_TIMEOUT);
							msg.setData(data);
							handler.sendMessage(msg);
						} catch (SocketTimeoutException e) {
							e.printStackTrace();
							Message msg = new Message();
							Bundle data = new Bundle();
							data.putSerializable("ErrorMsg",
									MSG_RESPONCE_TIMEOUT);
							msg.setData(data);
							handler.sendMessage(msg);
						} catch (ServiceRulesException e) {
							e.printStackTrace();
							Message msg = new Message();
							Bundle data = new Bundle();
							data.putSerializable("ErrorMsg", e.getMessage());
							msg.setData(data);
							handler.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
							Message msg = new Message();
							Bundle data = new Bundle();
							data.putSerializable("ErrorMsg", MSG_SELECT_ERROR);
							msg.setData(data);
							handler.sendMessage(msg);
						}
					}
				});
				thread.start();

			}
		});
		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			}
		});
	}

	private static class IHandler extends Handler {

		private final WeakReference<ActionBarActivity> mActivity;

		public IHandler(success success) {
			mActivity = new WeakReference<ActionBarActivity>(success);
		}

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
				((success) mActivity.get()).showTip(errorMsg);
				break;
			case 1:
				try {
					loginName = obj.getString("username");
					realname = obj.getString("realname");
					cellphone1 = obj.getString("cellphone1");
					cellphone2 = obj.getString("cellphone2");
					cellphone3 = obj.getString("cellphone3");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				username.setText(loginName);
				Realname.setText(realname);
				Cellphone1.setText(cellphone1);
				Cellphone2.setText(cellphone2);
				Cellphone3.setText(cellphone3);
				((success) mActivity.get()).showTip(MSG_SELECT_SUCCESS);
				break;

			default:
				break;
			}
		}
	}

	private IHandler handler = new IHandler(this);

	private void showTip(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
//		if (str == MSG_SELECT_SUCCESS) {
//			username.setText(loginName);
//			Realname.setText(realname);
//			Cellphone1.setText(cellphone1);
//			Cellphone2.setText(cellphone2);
//			Cellphone3.setText(cellphone3);
//
//		}

	}

}
