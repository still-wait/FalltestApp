package userlogin;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


public class UserServiceImpl implements UserService {
	private static final String TAG = "UserServiceImpl";

	// MainActivity person=new MainActivity();

	@Override
	public void userLogin(String loginName, String loginPassword)
			throws Exception {

		Log.d(TAG, loginName);
		Log.d(TAG, loginPassword);

		// Thread.sleep(3000);

		// 创建http对象
		HttpClient client = new DefaultHttpClient();

		/**
		 * post/json传值
		 */
		// String uri =
		// "http://10.138.108.131:8080/Test_MyService/login.action?username="
		// + loginName + "&password=" + loginPassword;
		// HttpGet get = new HttpGet(uri);
		// 响应
		// HttpResponse response = client.execute(get);
		String uri = "http://192.168.191.1:8080/MyService/login.action";
		
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		HttpPost post = new HttpPost(uri);
		List<NameValuePair> pair = new ArrayList<NameValuePair>();
		try {
			JSONObject obj = new JSONObject();
			obj.put("username", loginName);
			obj.put("password", loginPassword);
			// JSONObject json = new JSONObject();
			// Gson gson = new Gson();
			// String str = gson.toJson(obj);
			pair.add(new BasicNameValuePair("json", obj.toString()));
			post.setEntity(new UrlEncodedFormEntity(pair, HTTP.UTF_8));
			post.setParams(httpParams);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// String result = EntityUtils.toString(response.getEntity(),
			// "UTF-8");
			// System.out.println(result);

			String responseStr = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(responseStr).getJSONObject("json");
			String result = json.getString("result");
			Log.d(TAG, result);
			if (result.equals("success")) {
				// return true;

			} else {
				// return false;
				throw new ServiceRulesException(LoginMainActivity.MSG_LOGIN_FAILED);
			}

		} else {
			throw new ServiceRulesException(LoginMainActivity.MSG_SERVER_ERROR);
		}

		// int statusCode = response.getStatusLine().getStatusCode();
		// if (statusCode != HttpStatus.SC_OK) {
		// throw new ServiceRulesException(MainActivity.MSG_SERVER_ERROR);
		// }
		// String result = EntityUtils.toString(response.getEntity(), "UTF-8");
		//
		// if (result.equals("success")) {
		//
		// } else {
		// throw new ServiceRulesException(MainActivity.MSG_LOGIN_FAILED);
		// }

	}

	@Override
	public void userResign(String loginName, String loginPassword,
			String surePassword, String realName, String cellphone1,
			String cellphone2, String cellphone3) throws Exception {

		// if(loginPassword==surePassword){
		// }else{
		// throw new ServiceRulesException(ResignActivity.MSG_RESIGN_FAILED2);
		// }

		// Thread.sleep(3000);
		// 创建http对象
		/**
		 * GET 传值
		 */
		// String uri =
		// "http://10.138.106.30:8080/MyService/regist.action?username="
		// + loginName
		// + "&password="
		// + loginPassword
		// + "&realName="
		// + realName
		// + "&cellphone1="
		// + cellphone1
		// + "&cellphone2="
		// + cellphone2 + "&cellphone3=" + cellphone3;
		// http://10.138.108.131:8080/Test_MyService/login.action
		String uri = "http://192.168.1.106:8080/MyService/regist.action";
		HttpClient client = new DefaultHttpClient();
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000); 
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		HttpPost post = new HttpPost(uri);
		List<NameValuePair> pair = new ArrayList<NameValuePair>();
		try {
			JSONObject obj = new JSONObject();
			obj.put("username", loginName);
			obj.put("password", loginPassword);
			obj.put("realname", realName);
			obj.put("cellphone1", cellphone1);
			obj.put("cellphone2", cellphone2);
			obj.put("cellphone3", cellphone3);
			// Gson gson = new Gson();
			// String str = gson.toJson(obj);
			pair.add(new BasicNameValuePair("json", obj.toString()));
			post.setEntity(new UrlEncodedFormEntity(pair, HTTP.UTF_8));
			post.setParams(httpParams);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String responseStr = EntityUtils.toString(response.getEntity());
			JSONObject json = new JSONObject(responseStr).getJSONObject("json");
			String result = json.getString("result");

			if (result.equals("success")) {
			} else {
				throw new ServiceRulesException(
						ResignActivity.MSG_RESIGN_FAILED);
			}
		}

		// 响应
		// HttpResponse response = client.execute(get);

		// int statusCode = response.getStatusLine().getStatusCode();
		// if (statusCode != HttpStatus.SC_OK) {
		// throw new ServiceRulesException(ResignActivity.MSG_SERVER_ERROR);
		// }
		// String result = EntityUtils.toString(response.getEntity(), "UTF-8");
		//
		// if (result.equals("success")) {
		//
		// } else {
		// throw new ServiceRulesException(ResignActivity.MSG_RESIGN_FAILED);
		// }

	}

	public JSONObject selectUser(String loginName, String realname,
			String cellphone1, String cellphone2, String cellphone3)
			throws Exception {
//		String userName = "tom";
		// userName = person.setUserName(userName);
//		Thread.sleep(3000);
		// 创建http对象
		HttpClient client = new DefaultHttpClient();
		String uri = "http://10.138.126.115:8080/MyService/select.action";
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		HttpPost post = new HttpPost(uri);
		List<NameValuePair> pair = new ArrayList<NameValuePair>();
		try {
			JSONObject obj = new JSONObject();
			obj.put("username", loginName);
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
			JSONObject json1 = new JSONObject(responseStr)
					.getJSONObject("json");
			JSONObject object = json1.getJSONObject("user");
			return object;
			

		} else {
			throw new ServiceRulesException(success.MSG_SERVER_ERROR);
		}
	}
}
