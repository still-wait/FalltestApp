package myview;

import tools.SystemBarTintManager;
import tools.Utils;
import userlogin.BaseApplication;

import com.example.start_one.R;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Install extends FragmentActivity implements OnClickListener{
	private TextView txt_title;

	private TextView mTvCacheSize;
	private SeekBar mSeekBarBrightness;
	private View mViewNightMode;
	private ToggleButton mTgLight;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_set);
		
		
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.TitleCA);// 通知栏颜色
		}
		
		findViewById();
		setOnListener();
		BaseApplication.getInstance().addActivity(this);
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
		mTvCacheSize = (TextView) findViewById(R.id.tv_cache_size);
		findViewById(R.id.img_back).setVisibility(View.VISIBLE);
		findViewById(R.id.txt_left).setVisibility(View.VISIBLE);
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("设置");
	}

	private void setOnListener() {
		findViewById(R.id.img_back).setOnClickListener(this);
		findViewById(R.id.txt_left).setOnClickListener(this);
		findViewById(R.id.layout_clear_cache).setOnClickListener(this);
		findViewById(R.id.btn_logout).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(Install.this);
			break;
		case R.id.txt_left:
			Utils.finish(Install.this);
			break;
		case R.id.layout_clear_cache: // 清除缓存
			clearCache();
			break;
		case R.id.btn_logout: // 注销
			LogoutDialogFragment fragment = new LogoutDialogFragment();
			fragment.show(getSupportFragmentManager(), null);
			break;	
		}
	}
	
	private void clearCache() {
		mTvCacheSize.setText("0KB");
//		CacheUtils.clearCache();
		Toast.makeText(Install.this, "缓存已清除", Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
