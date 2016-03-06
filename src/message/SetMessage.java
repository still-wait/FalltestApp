package message;

import tools.AlertDialog;
import userlogin.BaseApplication;
import userlogin.LoginMainActivity;

import com.example.start_one.MainActivity;
import com.example.start_one.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class SetMessage extends FragmentActivity {
	private EditText txt_context;
	private TextView txt_right;
	private BaseApplication mApplication;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private Context context = SetMessage.this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_message);
		mApplication = (BaseApplication) getApplication();
		BaseApplication.getInstance().addActivity(this);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		txt_context= (EditText) findViewById(R.id.change_msg_edt);
		txt_right = (TextView) findViewById(R.id.txt_right);
		txt_right.setVisibility(View.VISIBLE);
		pref = getSharedPreferences("service", MODE_PRIVATE);
		txt_context.setText(""+pref.getString("JJmessage", ""));
		
		
		txt_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String saveTxt=txt_context.getText().toString();
				Log.e("---xinxi---", "-----"+saveTxt);
				editor = pref.edit();
				if(saveTxt==null){
					new AlertDialog(context).builder().setTitle("警告")
					.setMsg("您设置的消息为空，将影响服务，确定保存吗？")
					.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(View v) {
							editor.putString("JJmessage", saveTxt);
							editor.commit();
							Intent intent = new Intent(SetMessage.this,
									MainActivity.class);
							mApplication.setId(2);
							startActivity(intent);
//							txt_right.setVisibility(View.GONE);
							finish();
						}
					}).setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(View v) {
						}
					}).show();
				}else{
					editor.putString("JJmessage", saveTxt);
					editor.commit();
					Intent intent = new Intent(SetMessage.this,
							MainActivity.class);
					mApplication.setId(2);
					startActivity(intent);
//					txt_right.setVisibility(View.GONE);
					finish();
				}
			}
		});
	}

	
	
	

}
