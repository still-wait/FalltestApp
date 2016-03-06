package fragment;

import tools.AlertDialog;
import userlogin.BaseApplication;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.start_one.R;

public class PatternFragment extends Fragment {
	private Activity ctx;
	private View layout;
	private BaseApplication mApplication;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private ToggleButton mDDService;
	/** Notification构造器 */
	private NotificationCompat.Builder mBuilder;
	/** Notification的ID */
	int notifyId = 100;
	boolean isfirst = true;
	public static NotificationManager mNotificationManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mApplication = (BaseApplication) getActivity().getApplication();
		if (layout == null) {
			ctx = this.getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.tab02, null);
			initService();
			mDDService = (ToggleButton) layout
					.findViewById(R.id.diedao_service);
			if(mApplication.getisIsopenDD()){
				Log.e("---getisopenDD---","----"+mApplication.getisIsopenDD() );
				mDDService.setChecked(true);
				if(isfirst){
					showCzNotify();
					//开启服务
				}
				
			}
			mDDService
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								final boolean isChecked) {
							if (isChecked) {
								showCzNotify();
								editor.putBoolean("isopenDD", true);
								editor.commit();
								mApplication.setIsopenDD(true);
								mApplication.setId(1);
								// 开启服务

							} else {
								new AlertDialog(ctx)
										.builder()
										.setTitle("关闭服务")
										.setMsg("确定要关闭服务吗？")
										.setPositiveButton("取消",
												new OnClickListener() {
													@Override
													public void onClick(View v) {
														mDDService
																.setChecked(!isChecked);
													}
												})
										.setNegativeButton("确定",
												new OnClickListener() {
													@Override
													public void onClick(View v) {
														mDDService
																.setChecked(isChecked);
														clearNotify(notifyId);
														editor.putBoolean("isopenDD", false);
														editor.commit();
														mApplication.setIsopenDD(false);
														// 关闭服务
													}
												}).show();
							}
						}

					});

		} else {
			ViewGroup parent = (ViewGroup) layout.getParent();

			if (parent != null) {
				parent.removeView(layout);
			}
		}
		return layout;
	}

	@SuppressWarnings("static-access")
	private void initService() {
		mNotificationManager = (NotificationManager) ctx
				.getSystemService(ctx.NOTIFICATION_SERVICE);
		pref = ctx.getSharedPreferences("service", ctx.MODE_PRIVATE);
		editor = pref.edit();
	}

	protected void showCzNotify() {
		// TODO Auto-generated method stub
		isfirst=false;
		mBuilder = new Builder(ctx);
		// //PendingIntent 跳转动作
		PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0,
				ctx.getIntent(), 0);
		mBuilder.setSmallIcon(R.drawable.ic_launcher1).setTicker("监听服务已开启")
				.setContentTitle("监听服务正在运行")
				.setContentText("有不正常状态发生时会及时发出警报...")
				.setContentIntent(pendingIntent);
		Notification mNotification = mBuilder.build();
		// 设置通知 消息 图标
		mNotification.icon = R.drawable.ic_launcher1;
		// 在通知栏上点击此通知后自动清除此通知
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;// FLAG_ONGOING_EVENT
																// 在顶部常驻，可以调用下面的清除方法去除
																// FLAG_AUTO_CANCEL
																// 点击和清理可以去调
		// 设置显示通知时的默认的发声、震动、Light效果
		mNotification.defaults = Notification.DEFAULT_VIBRATE;
		// 设置发出消息的内容
		mNotification.tickerText = "监听服务已开启";
		// 设置发出通知的时间
		mNotification.when = System.currentTimeMillis();
		mNotificationManager.notify(notifyId, mNotification);
	}

	private void clearNotify(int notifyId) {
		mNotificationManager.cancel(notifyId);// 删除一个特定的通知ID对应的通知
	}

}
