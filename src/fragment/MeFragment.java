package fragment;

import positionHandle.MyPosition;
import positionHandle.PositionHistory;
import myview.About_Us;
import myview.Contacts;
import myview.Install;
import myview.LogoutDialogFragment;
import myview.MyMessage;
import tools.Utils;
import userlogin.BaseApplication;
import userlogin.LoginMainActivity;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.start_one.R;

public class MeFragment extends Fragment implements OnClickListener {
	// 找到view
	private Activity ctx;
	private View layout;
	private TextView user;
	private BaseApplication mApplication;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mApplication = (BaseApplication) getActivity().getApplication();
		if (layout == null) {
			ctx = this.getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.tab04, null);
			user = (TextView) layout.findViewById(R.id.personal_login_button);
			setOnListener();
			Intent intent = ctx.getIntent();
			final String muser = intent.getStringExtra("loginname");
			Log.e("---用户 ---", "----" + muser);
			if (muser != null) {
				user.setText(muser);
			}
			String getu = user.getText().toString();
			if (getu != "登录/注册" & mApplication.getislogin() == true) {
				 user.setText(mApplication.getLoginUserName());
				user.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Toast.makeText(ctx,
								"当前用户为：" + mApplication.getLoginUserName(),
								Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				user.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Utils.start_Activity(getActivity(),
								LoginMainActivity.class);
					}
				});
			}
			Log.e("eeee", "11111111111111111111111");
		} else {
			ViewGroup parent = (ViewGroup) layout.getParent();
			Log.e("222", "2222222222222222222222222");
			// Toast.makeText(ctx, "当前用户为：" + mApplication.getLoginUserName(),
			// Toast.LENGTH_SHORT).show();

			if (parent != null) {
				parent.removeView(layout);
			}
		}
		return layout;
	}

	private void setOnListener() {
		layout.findViewById(R.id.My_message).setOnClickListener(this);
		layout.findViewById(R.id.contacts_man).setOnClickListener(this);
		layout.findViewById(R.id.my_poision).setOnClickListener(this);
		layout.findViewById(R.id.travel_history).setOnClickListener(this);
		layout.findViewById(R.id.my_set).setOnClickListener(this);
		layout.findViewById(R.id.my_regard).setOnClickListener(this);
		layout.findViewById(R.id.personal_login_button)
				.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.My_message:
			Utils.start_Activity(getActivity(), MyMessage.class);
			break;
		case R.id.contacts_man:
			Utils.start_Activity(getActivity(), Contacts.class);
			break;
		case R.id.my_poision:
			Utils.start_Activity(getActivity(), MyPosition.class);
			break;
		case R.id.travel_history:
			Utils.start_Activity(getActivity(), PositionHistory.class);
			break;
		case R.id.my_set:
			Utils.start_Activity(getActivity(), Install.class);
			break;
		case R.id.my_regard:
			Utils.start_Activity(getActivity(), About_Us.class);
			break;
		default:
			break;
		}
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mApplication = (BaseApplication) getActivity().getApplication();
		user = (TextView) getActivity()
				.findViewById(R.id.personal_login_button);

		// 登录事件
		// IntentFilter filter = new IntentFilter(LoginMainActivity.action);
		// getActivity().registerReceiver(broadcastReceiver1, filter);
		// // 注销事件
		// IntentFilter filter2 = new IntentFilter(LogoutDialogFragment.action);
		// getActivity().registerReceiver(broadcastReceiver2, filter2);

	}

	//
	// BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// // TODO Auto-generated method stub
	// user.setText(intent.getExtras().getString("data"));
	// final String muser = user.getText().toString();
	// if (mApplication.getLoginUserName() != null
	// & mApplication.getislogin() == true) {
	// user.setOnClickListener(new OnClickListener() {
	// public void onClick(View v) {
	// Toast.makeText(ctx, "当前用户为：" + muser,
	// Toast.LENGTH_SHORT).show();
	// }
	// });
	// }
	// }
	//
	// };
	//
	// // 注销成功后
	// BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// // TODO Auto-generated method stub
	// user.setText(intent.getExtras().getString("data"));
	// user.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	// Utils.start_Activity(getActivity(), LoginMainActivity.class);
	// }
	// });
	// }
	//
	// };

	public void onDestroy() {
		// getActivity().unregisterReceiver(broadcastReceiver1);
		// getActivity().unregisterReceiver(broadcastReceiver2);
	};
}