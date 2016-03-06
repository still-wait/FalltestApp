package userlogin;

import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

import myview.Install;

import org.apache.http.conn.ConnectTimeoutException;

import tools.SystemBarTintManager;
import tools.Utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.start_one.MainActivity;
import com.example.start_one.R;

public class LoginMainActivity extends ActionBarActivity implements
		OnCheckedChangeListener {
	private EditText username1;
	private EditText password1;
	Button login;
	private TextView txt_title;
	private ToggleButton mTgBtnShowPsw;
	private ImageView mBtnClearUid;
	private ImageView mBtnClearPsw;

	public static final String action = "ygh.login.broadcast.action";

	private UserService userService = new UserServiceImpl();
	private static final int FLAG_LOGIN_SUCCESS = 1;
	private static final String MSG_LOGIN_ERROR = "登录出错。";
	private static final String MSG_LOGIN_SUCCESS = "登录成功。";
	public static final String MSG_LOGIN_FAILED = "用户名或密码错误。";
	public static final String MSG_SERVER_ERROR = "请求服务器错误。";
	public static final String MSG_SERVER_TIMEOUT = "请求服务器超时。";
	public static final String MSG_RESPONCE_TIMEOUT = "服务器响应超时。";
	private static ProgressDialog dialog;
	// public static String loginName;

	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private BaseApplication mApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_login);
		mApplication = (BaseApplication) getApplication();
		initTop();
		initUI();
		initUID();// 初始化记住的用户名
		setOnListener();
	}

	private void initTop() {
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.TitleCA);// 通知栏颜色
		}
		findViewById(R.id.img_back).setVisibility(View.VISIBLE);
		findViewById(R.id.txt_left).setVisibility(View.VISIBLE);
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("登录/注册");
	}

	private void setOnListener() {
		// TODO Auto-generated method stub
		username1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (username1.getText().toString().length() > 0) {
					mBtnClearUid.setVisibility(View.VISIBLE);
					if (password1.getText().toString().length() > 0) {
						login.setEnabled(true);
					} else {
						login.setEnabled(false);
					}
				} else {
					login.setEnabled(false);
					mBtnClearUid.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		password1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (password1.getText().toString().length() > 0) {
					mBtnClearPsw.setVisibility(View.VISIBLE);
					if (username1.getText().toString().length() > 0) {
						login.setEnabled(true);
					} else {
						login.setEnabled(false);
					}
				} else {
					login.setEnabled(false);
					mBtnClearPsw.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}
		});

		login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final String loginName = username1.getText().toString();
				final String loginPassword = password1.getText().toString();

				/**
				 * loading...
				 */

				if (dialog == null) {
					dialog = new ProgressDialog(LoginMainActivity.this);
				}
				dialog.setTitle("请等待");
				dialog.setMessage("登录中...");
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
							userService.userLogin(loginName, loginPassword);
							handler.sendEmptyMessage(FLAG_LOGIN_SUCCESS);
							mApplication.setLoginUserName(loginName);
							mApplication.setislogin(true);
							editor = pref.edit();
							editor.putString("username", loginName);
							editor.putString("password", loginPassword);
							editor.putBoolean("islogin", true);
							editor.commit();
							// 发送广播
//							Intent intent = new Intent(action);
//							intent.putExtra("data", loginName);
//							sendBroadcast(intent);
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
							data.putSerializable("ErrorMsg", MSG_LOGIN_ERROR);
							msg.setData(data);
							handler.sendMessage(msg);
						}
					}
				});
				thread.start();
			}
		});

		findViewById(R.id.tv_quick_sign_up).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(LoginMainActivity.this,
								ResignActivity.class);
						startActivity(intent);
						finish();
					}
				});
		mBtnClearUid.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				clearText(username1);
			}
		});
		mBtnClearPsw.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				clearText(password1);
			}
		});
		findViewById(R.id.img_back).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Utils.finish(LoginMainActivity.this);
			}
		});
		findViewById(R.id.txt_left).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Utils.finish(LoginMainActivity.this);
			}
		});

		mTgBtnShowPsw.setOnCheckedChangeListener(this);
	}

	private void initUI() {
		// TODO Auto-generated method stub
		login = (Button) findViewById(R.id.btn_login);
		username1 = (EditText) findViewById(R.id.edit_uid);
		password1 = (EditText) findViewById(R.id.edit_psw);
		mBtnClearUid = (ImageView) findViewById(R.id.img_login_clear_uid);
		mBtnClearPsw = (ImageView) findViewById(R.id.img_login_clear_psw);
		mTgBtnShowPsw = (ToggleButton) findViewById(R.id.tgbtn_show_psw);
	}

	/**
	 * 初始化记住的用户名
	 */
	private void initUID() {
		// TODO Auto-generated method stub
		pref = getSharedPreferences("user", MODE_PRIVATE);
		String Username = pref.getString("username", "");
		username1.setText(Username);
	}

	/**
	 * 清空控件文本
	 * 
	 */
	private void clearText(EditText edit) {
		edit.setText("");
	}

	private void showTip(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		if (str == MSG_LOGIN_SUCCESS) {
			Log.e("111111111", "7777777777777777");
			pref = getSharedPreferences("user", MODE_PRIVATE);
			String Username = pref.getString("username", "");
			Intent intent = new Intent(LoginMainActivity.this,
					MainActivity.class);
			intent.putExtra("loginname",Username);
			mApplication.setId(3);
			startActivity(intent);
			finish();
			Log.e("111111111", "22222222222222222222222");
		}

	}

	private static class IHandler extends Handler {

		private final WeakReference<ActionBarActivity> mActivity;

		public IHandler(LoginMainActivity activity) {
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
				((LoginMainActivity) mActivity.get()).showTip(errorMsg);
				break;
			case FLAG_LOGIN_SUCCESS:
				((LoginMainActivity) mActivity.get())
						.showTip(MSG_LOGIN_SUCCESS);
				break;

			default:
				break;
			}
		}
	}

	private IHandler handler = new IHandler(this);

	/**
	 * 是否显示密码
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			// 显示密码
			password1.setTransformationMethod(HideReturnsTransformationMethod
					.getInstance());
		} else {
			// 隐藏密码
			password1.setTransformationMethod(PasswordTransformationMethod
					.getInstance());
		}
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

}
