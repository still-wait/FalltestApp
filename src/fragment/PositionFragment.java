package fragment;

import java.util.ArrayList;

import myview.Contacts;
import myview.Install;
import myview.MyMessage;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import baiduMap.MapMainActivity;

import com.example.start_one.R;

public class PositionFragment extends Fragment {
	
	private ListView mylistview;
	private ArrayList<String> list = new ArrayList<String>();
	private int flag=1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tab01, container, false);
		
		
		return v;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		
//		mylistview = (ListView) getActivity().findViewById(R.id.maplistview);
//		if(flag==1){
//			flag=0;
//		}
//		ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(
//				getActivity(), android.R.layout.simple_list_item_1, list);
//		mylistview.setAdapter(myArrayAdapter);
//		mylistview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				if (list.get(arg2).equals("我的位置")) {
//					Intent intent = new Intent(getActivity(), MyMessage.class);
//					startActivity(intent);
//				}
//				if (list.get(arg2).equals("发送位置")) {
//					Intent intent = new Intent(getActivity(), Contacts.class);
//					startActivity(intent);
//				}
//				if (list.get(arg2).equals("设置")) {
//					Intent intent = new Intent(getActivity(), Install.class);
//					startActivity(intent);
//				}
//				
//				if (list.get(arg2).equals("进入地图")) {
//					Intent intent = new Intent(getActivity(), MapMainActivity.class);
//					startActivity(intent);
//				}
//			}
//
//		});
//		button1 = (Button) getActivity().findViewById(R.id.button1);
//		button1.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(getActivity(),MapMainActivity.class);
//				startActivity(intent);
//
//			}
//		});

		
	}

}
