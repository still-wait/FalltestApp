package userlogin;

import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.start_one.R;

public class ResignActivity extends ActionBarActivity {

	private EditText account;
	private EditText password;
	private EditText sure_password;
	private EditText realname;
	private EditText phone1;
	private EditText phone2;
	private EditText phone3;
	private static ProgressDialog dialog;
	private UserService userService = new UserServiceImpl();

	private static final int FLAG_RESIGN_SUCCESS = 1;
	private static final String MSG_RESIGN_ERROR = "注册出错。";
	private static final String MSG_RESIGN_SUCCESS = "注册成功。";
	public static final String MSG_RESIGN_FAILED = "输入出现错误。";
	public static final String MSG_RESIGN_FAILED2 = "密码输入错误。";
	public static final String MSG_SERVER_ERROR = "请求服务器错误。";
	public static final String MSG_SERVER_TIMEOUT = "请求服务器超时。";
	public static final String MSG_RESPONCE_TIMEOUT = "服务器响应超时。";

	@Override
	protected void onCreate(Bundle savedInsanceState) {
		super.onCreate(savedInsanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.resign);

		account = (EditText) findViewById(R.id.account);
		password = (EditText) findViewById(R.id.password);
		sure_password = (EditText) findViewById(R.id.sure_password);
		realname = (EditText) findViewById(R.id.name);
		phone1 = (EditText) findViewById(R.id.phone1);
		phone2 = (EditText) findViewById(R.id.phone2);
		phone3 = (EditText) findViewById(R.id.phone3);

		Button register = (Button) findViewById(R.id.register);
		register.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final String loginName = account.getText().toString();
				final String loginPassword = password.getText().toString();
				final String surePassword = sure_password.getText().toString();
				final String realName = realname.getText().toString();
				final String cellphone1 = phone1.getText().toString();
				final String cellphone2 = phone2.getText().toString();
				final String cellphone3 = phone3.getText().toString();

				/**
				 * loading...
				 */

				if (dialog == null) {
					dialog = new ProgressDialog(ResignActivity.this);
				}
				dialog.setTitle("请等待");
				dialog.setMessage("注册中...");
				dialog.setCancelable(false);
				dialog.show();

				/**
				 * 子线程
				 */
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							userService.userResign(loginName, loginPassword,
									surePassword, realName, cellphone1,
									cellphone2, cellphone3);
							handler.sendEmptyMessage(FLAG_RESIGN_SUCCESS);
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
						}catch (ServiceRulesException e) {
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
							data.putSerializable("ErrorMsg", MSG_RESIGN_ERROR);
							msg.setData(data);
							handler.sendMessage(msg);
						}
					}
				});
				thread.start();
			}
		});
	}

	private void showTip(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		if (str == MSG_RESIGN_SUCCESS) {
			Intent intent = new Intent(ResignActivity.this, LoginMainActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private static class IHandler extends Handler {

		private final WeakReference<ActionBarActivity> mActivity;

		public IHandler(ResignActivity activity) {
			mActivity = new WeakReference<ActionBarActivity>(activity);
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
				((ResignActivity) mActivity.get()).showTip(errorMsg);
				break;
			case FLAG_RESIGN_SUCCESS:
				((ResignActivity) mActivity.get()).showTip(MSG_RESIGN_SUCCESS);
				break;

			default:
				break;
			}
		}
	}

	private IHandler handler = new IHandler(this);

}
