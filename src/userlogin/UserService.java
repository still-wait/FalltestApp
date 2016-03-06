package userlogin;

import org.json.JSONObject;

public interface UserService {

	public void userLogin(String loginName, String loginPassword)
			throws Exception;

	public void userResign(String loginName, String loginPassword,String surePassword,
			String realName, String cellphone1, String cellphone2,
			String cellphone3) throws Exception;
	public JSONObject selectUser(String loginName, String realname,
			String cellphone1, String cellphone2, String cellphone3)
			throws Exception;

}

