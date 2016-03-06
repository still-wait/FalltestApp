package positionHandle;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import tools.ServiceRulesException;
import android.util.Log;
import baiduMap.MapMainActivity;


public class history_positionImp implements positionService{

	@Override
	public List<PositionBean> sendMessage(String username) throws Exception {
		// TODO Auto-generated method stub
		List<PositionBean> list = new ArrayList<PositionBean>();
		
		HttpClient client = new DefaultHttpClient();
//		String uri = "http://10.138.126.115:8080/MyService/Respoision.action";
		String uri = "http://192.168.191.1:8080/MyService/Respoision.action";
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		HttpPost post = new HttpPost(uri);
		List<NameValuePair> pair = new ArrayList<NameValuePair>();
		try {
			JSONObject obj = new JSONObject();
			obj.put("username", username);
			pair.add(new BasicNameValuePair("json", obj.toString()));
			post.setEntity(new UrlEncodedFormEntity(pair, HTTP.UTF_8));
			post.setParams(httpParams);
			Log.e("---访问服务器------", "----------");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			Log.e("--返回----", "1111111111");
			String responseStr = EntityUtils.toString(response.getEntity());
			JSONArray jsonArray = new JSONArray(responseStr);
			Log.e("---2从服务器取得12-----", "----"+jsonArray.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
	            JSONObject json2 = jsonArray.getJSONObject(i);
	            PositionBean bean = new PositionBean();
	            bean.setNowtime(json2.getString("nowtime"));
	            bean.setPosition(json2.getString("poision"));
	            list.add(bean);
	            Log.e("-----从服务器取得------", "----"+list);
			}
			return list;
		} else {
				throw new ServiceRulesException(
						PositionHistory.MSG_SERVER_ERROR);
			}
	}
	
	

}
