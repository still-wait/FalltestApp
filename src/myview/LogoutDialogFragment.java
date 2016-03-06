package myview;

import userlogin.BaseApplication;
import userlogin.LoginMainActivity;
import userlogin.ResignActivity;
import userlogin.success;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.example.start_one.MainActivity;
import com.example.start_one.R;

import fragment.MeFragment;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * 
 */
@SuppressLint("InflateParams")
public class LogoutDialogFragment extends DialogFragment implements
		OnClickListener {
	public static final String action = "ygh.broadcast.action";
	private FragmentActivity mActivity;
	private View btnLogout;
	private View divider;
	private BaseApplication mApplication;
	SharedPreferences pref;

	/** �ǳ�������Ӧ��listener */

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = getActivity();
		View inflate = inflater.inflate(R.layout.fragment_dialog_logout, null);
		inflate.findViewById(R.id.tv_exit).setOnClickListener(this);
		divider = inflate.findViewById(R.id.divider);
		btnLogout = inflate.findViewById(R.id.tv_logout);
		btnLogout.setOnClickListener(this);
		mApplication = (BaseApplication) mActivity.getApplication();
		initLogin();
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return inflate;
	}

	private void initLogin() {
		// ��ȡ��¼����
		pref = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
		boolean type = pref.getBoolean("islogin", false);
		if (type == false) {
			// δ��¼��ֻ��ʾ�˳��ͻ��˰�ť
			btnLogout.setVisibility(View.GONE);
			divider.setVisibility(View.GONE);
		} else {

		}
	}

	@Override
	public void onClick(View v) {
		dismiss();
		switch (v.getId()) {
		case R.id.tv_exit: // �˳�Ӧ��
			BaseApplication.getInstance().exit();
			break;
		case R.id.tv_logout: // ע��
			logout();
			break;

		default:
			break;
		}
	}

	private void logout() {
		anounceLogout();
		// Intent intent = new Intent(mActivity,MainActivity.class);
		// startActivity(intent);
		// System.exit(0);
		dismiss();
		Intent intent = new Intent(mActivity, MainActivity.class);
		startActivity(intent);

		Toast.makeText(mActivity, "��ע��", Toast.LENGTH_SHORT).show();
	}

	/**
	 * ���͹㲥ע��
	 */
	private void anounceLogout() {
		Editor edit = pref.edit();
		edit.putBoolean("islogin", false);
		edit.commit();
		mApplication.setislogin(false);
		mApplication.setId(3);
		Intent intent = new Intent(action);
		intent.putExtra("data", "��¼/ע��");
		mActivity.sendBroadcast(intent);
	}

}
