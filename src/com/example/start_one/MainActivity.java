package com.example.start_one;

import java.util.ArrayList;
import java.util.List;

import tools.AlertDialog;
import tools.SystemBarTintManager;
import userlogin.BaseApplication;
import userlogin.LoginMainActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import fragment.BaidumapFragment;
import fragment.MeFragment;
import fragment.PatternFragment;
import fragment.PositionFragment;
import fragment.ProtectFragment;

public class MainActivity extends FragmentActivity implements OnClickListener,
		OnPageChangeListener {
	private static final int TAB_page1 = 0;
	private static final int TAB_page2 = 1;
	private static final int TAB_page3 = 2;
	private static final int TAB_page4 = 3;

	private Context context = MainActivity.this;
	private BaseApplication mApplication;
	private ViewPager mViewPager;
	// private List<Fragment> mFragments;
	private FragmentPagerAdapter mAdapter;
	private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();
	private RadioGroup group;
	private int index = 0;
	private int tag = 0;
	private TextView txt_title;
	private RadioButton btn_left;
	private RadioButton btn_right;
	private ImageView img_right;
	private GradientDrawable drawable_left;
	private GradientDrawable drawable_right;
	private Fragment mTab01;
	private Fragment mTab05;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		BaseApplication.getInstance().addActivity(this);
		initTop();

		mApplication = (BaseApplication) getApplication();
		initView();
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setAdapter(mAdapter);
		int ID = mApplication.getId();
		index = ID;
		mTabIndicators.get(ID).setIconAlpha(1.0f);
		mViewPager.setCurrentItem(ID);
		initEvent();
		Log.e("测试", "现在在MainActivity.hhhhhhhhhhhh");
		/**
		 * 子线程
		 */
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				if (!mApplication.getislogin()) {
					mHandler.sendMessage(mHandler.obtainMessage());
				}
			}
		});
		thread.start();
	}

	private void initTop() {
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.TitleCA);// 通知栏颜色
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			new AlertDialog(context).builder().setTitle("登录到系统")
					.setMsg("系统检测到您尚未登录，这将影响您的使用效果，是否现在登录？")
					.setPositiveButton("现在登录", new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(MainActivity.this,
									LoginMainActivity.class);
							startActivity(intent);
						}
					}).setNegativeButton("稍后登录", new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					}).show();
		}
	};

	/**
	 * 
	 */
	private void initEvent() {
		mViewPager.setOnPageChangeListener(this);
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		txt_title = (TextView) findViewById(R.id.txt_title);
		group = (RadioGroup) findViewById(R.id.group);
		btn_left = (RadioButton) findViewById(R.id.btn_left);
		btn_right = (RadioButton) findViewById(R.id.btn_right);
		img_right = (ImageView) findViewById(R.id.img_right);
		drawable_left = (GradientDrawable) btn_left.getBackground();
		drawable_left.setColor(getResources().getColor(R.color.Azure));
		drawable_right = (GradientDrawable) btn_right.getBackground();
		drawable_right.setColor(getResources().getColor(R.color.green1));
		ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
		mTabIndicators.add(one);
		ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
		mTabIndicators.add(two);
		ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
		mTabIndicators.add(three);
		ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
		mTabIndicators.add(four);

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		img_right.setOnClickListener(this);
		// one.setIconAlpha(1.0f);

		// mFragments = new ArrayList<Fragment>();
		mTab05 = new BaidumapFragment();
		// mTab01 = new PositionFragment();
		// PatternFragment mTab02 = new PatternFragment();
		// ProtectFragment mTab03 = new ProtectFragment();
		// MeFragment mTab04 = new MeFragment();
		// mFragments.addAll(mTab01);
		// mFragments.add(mTab02);
		// mFragments.add(mTab03);
		// mFragments.add(mTab04);
		// mFragments.add(mTab05);
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				// return mFragments.size();
				return 4;
			}

			@Override
			public Fragment getItem(int id) {
				// return mFragments.get(arg0);
				switch (id) {
				case TAB_page1:
					if (tag == 0) {
						mTab01 = new PositionFragment();
						return mTab01;
					} else {
						mTab05 = new BaidumapFragment();
						return mTab05;
					}

				case TAB_page2:
					PatternFragment mTab02 = new PatternFragment();
					return mTab02;
				case TAB_page3:
					ProtectFragment mTab03 = new ProtectFragment();
					return mTab03;
				case TAB_page4:
					MeFragment mTab04 = new MeFragment();
					return mTab04;
				}
				return null;
			}
		};

	}

	@Override
	public void onClick(View v) {
		clickTab(v);

	}

	/**
	 * @param v
	 */
	private void clickTab(View v) {
		resetOtherTabs();
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		switch (v.getId()) {
		case R.id.id_indicator_one:
			index = 0;
			PositionTopturn();
			mTabIndicators.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, false);
			break;
		case R.id.id_indicator_two:
			index = 1;
			otherTopturn();
			mTabIndicators.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			break;
		case R.id.id_indicator_three:
			index = 2;
			otherTopturn();
			mTabIndicators.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			break;
		case R.id.id_indicator_four:
			index = 3;
			otherTopturn();
			mTabIndicators.get(3).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(3, false);
			break;
		case R.id.btn_right:
			mTabIndicators.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0);
			mTab05 = new BaidumapFragment();
			if (tag == 0) {
				transaction.replace(R.id.tab1_content, mTab05);
				transaction.addToBackStack(null);
				transaction.commit();
				tag = 1;
			}
			//

			drawable_left = (GradientDrawable) btn_left.getBackground();
			drawable_left.setColor(getResources().getColor(R.color.green1));
			btn_left.setTextColor(getResources().getColor(R.color.Azure));
			btn_right.setTextColor(getResources().getColor(R.color.green1));
			drawable_right = (GradientDrawable) btn_right.getBackground();
			drawable_right.setColor(getResources().getColor(R.color.Azure));
			img_right.setVisibility(View.VISIBLE);
			// Utils.start_Activity(MainActivity.this, MapMainActivity.class);
			break;
		case R.id.btn_left:
			drawable_left = (GradientDrawable) btn_left.getBackground();
			drawable_left.setColor(getResources().getColor(R.color.Azure));
			btn_left.setTextColor(getResources().getColor(R.color.green1));
			btn_right.setTextColor(getResources().getColor(R.color.Azure));
			drawable_right = (GradientDrawable) btn_right.getBackground();
			drawable_right.setColor(getResources().getColor(R.color.green1));
			img_right.setVisibility(View.GONE);
			// btn_right.setBackgroundColor(R.color.green1);
			mTabIndicators.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0);
			mTab01 = new PositionFragment();

			if (tag == 1) {
				transaction.replace(R.id.tab1_content, mTab01);
				transaction.addToBackStack(null);
				transaction.commit();
				tag = 0;
			}
		case R.id.img_right:
			
			break;
		}
		
			
	}

	/**
	 * 
	 */
	private void resetOtherTabs() {
		for (int i = 0; i < mTabIndicators.size(); i++) {
			mTabIndicators.get(i).setIconAlpha(0);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

		if (position == 0) {
			group.setVisibility(View.VISIBLE);
			txt_title.setVisibility(View.GONE);
			if (tag == 1) {
				img_right.setVisibility(View.VISIBLE);
			} else {
				img_right.setVisibility(View.GONE);
			}
		} else {
			group.setVisibility(View.GONE);
			img_right.setVisibility(View.GONE);
			txt_title.setVisibility(View.VISIBLE);
			switch (position) {
			case 1:
				txt_title.setText("模式识别");
				break;
			case 2:
				txt_title.setText("消息");
				break;
			case 3:
				txt_title.setText("护理专家");
				break;
			default:
				break;
			}
		}
		if (positionOffset > 0) {
			ChangeColorIconWithText left = mTabIndicators.get(position);
			ChangeColorIconWithText right = mTabIndicators.get(position + 1);
			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}
	}

	private void PositionTopturn() {

		group.setVisibility(View.VISIBLE);
		txt_title.setVisibility(View.GONE);
		if (tag == 1) {
			img_right.setVisibility(View.VISIBLE);
		}else {
			img_right.setVisibility(View.GONE);
		}
	}

	private void otherTopturn() {
		group.setVisibility(View.GONE);
		img_right.setVisibility(View.GONE);
		txt_title.setVisibility(View.VISIBLE);
		switch (index) {
		case 1:
			txt_title.setText("模式识别");
			break;
		case 2:
			txt_title.setText("消息");
			break;
		case 3:
			txt_title.setText("护理专家");
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog(context).builder().setTitle("退出系统")
					.setMsg("确定要退出吗？")
					.setPositiveButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {
						}
					}).setNegativeButton("确定", new OnClickListener() {
						@Override
						public void onClick(View v) {
							BaseApplication.getInstance().exit();
						}
					}).show();
			// return true;
		}
		return super.onKeyDown(keyCode, event);
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

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		// return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.help:
			Toast.makeText(this, "help", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
