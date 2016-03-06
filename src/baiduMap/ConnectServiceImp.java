package baiduMap;

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
import org.json.JSONObject;

import tools.ServiceRulesException;

public class ConnectServiceImp implements ConnectService {

	@Override
	public void sendMessage(String username, String nowtime, String poision,
			double Latitude, double Longtitude) throws Exception {
		// TODO Auto-generated method stub
		HttpClient client = new DefaultHttpClient();
		String uri = "http://192.168.191.1:8080/MyService/poision.action";
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		HttpPost post = new HttpPost(uri);
		List<NameValuePair> pair = new ArrayList<NameValuePair>();
		try {
			JSONObject obj = new JSONObject();
			obj.put("username", username);
			obj.put("nowtime", nowtime);
			obj.put("poision", poision);
			obj.put("latitude", Latitude);
			obj.put("longtitude", Longtitude);
			pair.add(new BasicNameValuePair("json", obj.toString()));
			post.setEntity(new UrlEncodedFormEntity(pair, HTTP.UTF_8));
			post.setParams(httpParams);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			String responseStr = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(responseStr).getJSONObject("json");
			String result = json.getString("result");

			if (result.equals("success")) {
			} else {
				throw new ServiceRulesException(
						MapMainActivity.MSG_SERVER_ERROR);
			}
		}

	}
}
