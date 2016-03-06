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
	/** Notification������ */
	private NotificationCompat.Builder mBuilder;
	/** Notification��ID */
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
					//��������
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
								// ��������

							} else {
								new AlertDialog(ctx)
										.builder()
										.setTitle("�رշ���")
										.setMsg("ȷ��Ҫ�رշ�����")
										.setPositiveButton("ȡ��",
												new OnClickListener() {
													@Override
													public void onClick(View v) {
														mDDService
																.setChecked(!isChecked);
													}
												})
										.setNegativeButton("ȷ��",
												new OnClickListener() {
													@Override
													public void onClick(View v) {
														mDDService
																.setChecked(isChecked);
														clearNotify(notifyId);
														editor.putBoolean("isopenDD", false);
														editor.commit();
														mApplication.setIsopenDD(false);
														// �رշ���
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
		// //PendingIntent ��ת����
		PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0,
				ctx.getIntent(), 0);
		mBuilder.setSmallIcon(R.drawable.ic_launcher1).setTicker("���������ѿ���")
				.setContentTitle("����������������")
				.setContentText("�в�����״̬����ʱ�ἰʱ��������...")
				.setContentIntent(pendingIntent);
		Notification mNotification = mBuilder.build();
		// ����֪ͨ ��Ϣ ͼ��
		mNotification.icon = R.drawable.ic_launcher1;
		// ��֪ͨ���ϵ����֪ͨ���Զ������֪ͨ
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;// FLAG_ONGOING_EVENT
																// �ڶ�����פ�����Ե���������������ȥ��
																// FLAG_AUTO_CANCEL
																// ������������ȥ��
		// ������ʾ֪ͨʱ��Ĭ�ϵķ������𶯡�LightЧ��
		mNotification.defaults = Notification.DEFAULT_VIBRATE;
		// ���÷�����Ϣ������
		mNotification.tickerText = "���������ѿ���";
		// ���÷���֪ͨ��ʱ��
		mNotification.when = System.currentTimeMillis();
		mNotificationManager.notify(notifyId, mNotification);
	}

	private void clearNotify(int notifyId) {
		mNotificationManager.cancel(notifyId);// ɾ��һ���ض���֪ͨID��Ӧ��֪ͨ
	}

}
