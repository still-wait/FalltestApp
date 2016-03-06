package fragment;

import message.SetMessage;
import tools.Utils;
import userlogin.BaseApplication;

import com.example.start_one.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProtectFragment extends Fragment {
	private Activity ctx;
	private View layout;
	private BaseApplication mApplication;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private TextView set_mesage;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mApplication = (BaseApplication) getActivity().getApplication();
		if (layout == null) {
			ctx = this.getActivity();
			layout = ctx.getLayoutInflater().inflate(R.layout.tab03, null);
			set_mesage= (TextView) layout.findViewById(R.id.txt_mesage);
			set_mesage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Utils.start_Activity(ctx,SetMessage.class);
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

}
