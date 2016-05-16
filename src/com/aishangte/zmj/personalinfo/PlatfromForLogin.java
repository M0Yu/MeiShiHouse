package com.aishangte.zmj.personalinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.aishangte.zmj.meishiwu.utils.ZmjConstant;
import com.aishangte.zmj.meishiwu.utils.ZmjPictureToBytesData;
import com.aishangte.zmj.meishiwu.utils.ZmjPictureTools;
import com.example.classfy.R;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View.OnClickListener;

public class PlatfromForLogin extends Activity implements OnClickListener,
		Callback, PlatformActionListener {

	private static final int MSG_AUTH_CANCEL = 2;
	private static final int MSG_AUTH_ERROR = 3;
	private static final int MSG_AUTH_COMPLETE = 4;
	private Handler handler;
	private Platform platform;

	private byte[] paramArrayOfByte;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zmj_platfrom_login);
		ShareSDK.initSDK(this);
		handler = new Handler(this);
	};

	// 执行授权,获取用户信息
	// 文档：http://wiki.mob.com/Android_%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99
	private void authorize(Platform plat) {
		if (plat == null) {
			popupOthers();
			return;
		}

		plat.setPlatformActionListener(this);
		// 关闭SSO授权
		plat.SSOSetting(true);
		plat.showUser(null);
	}

	// 其他登录对话框
	private void popupOthers() {
		Dialog dlg = new Dialog(PlatfromForLogin.this, R.style.WhiteDialog);
		View dlgView = View.inflate(PlatfromForLogin.this,
				R.layout.tpl_other_plat_dialog, null);
		View tvFacebook = dlgView.findViewById(R.id.tvFacebook);
		tvFacebook.setTag(dlg);
		tvFacebook.setOnClickListener(this);
		View tvTwitter = dlgView.findViewById(R.id.tvTwitter);
		tvTwitter.setTag(dlg);
		tvTwitter.setOnClickListener(this);

		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setContentView(dlgView);
		dlg.show();
	}

	@Override
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			Message msg = new Message();
			msg.what = MSG_AUTH_COMPLETE;
			msg.obj = new Object[] { platform.getName(), res };
			handler.sendMessage(msg);
		}
	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(MSG_AUTH_ERROR);
		}
		t.printStackTrace();
	}
	@Override
	public void onCancel(Platform platform, int action) {
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(MSG_AUTH_CANCEL);
		}
	}

	@SuppressWarnings("unchecked")
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_AUTH_CANCEL: {
			// 取消授权
			Toast.makeText(PlatfromForLogin.this, R.string.auth_cancel,
					Toast.LENGTH_SHORT).show();
		}
			break;
		case MSG_AUTH_ERROR: {
			// 授权失败
			Toast.makeText(PlatfromForLogin.this, R.string.auth_error,
					Toast.LENGTH_SHORT).show();
		}
			break;
		case MSG_AUTH_COMPLETE: {
			// 授权成功
			Toast.makeText(PlatfromForLogin.this, R.string.auth_complete,
					Toast.LENGTH_SHORT).show();
			Object[] objs = (Object[]) msg.obj;
			String platformString = (String) objs[0];
			HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
			//
			// if (signupListener != null & signupListener.onSignin(platform,
			// res)) {
			// SignupPage signupPage = new SignupPage();
			// signupPage.setOnLoginListener(signupListener);
			// signupPage.setPlatform(platform);
			// signupPage.show(activity, null);
			// }

			setPlatform(platformString);

			ZmjConstant.userInfo.setUserIcon(platform.getDb().getUserIcon());
			ZmjConstant.userInfo.setUserName(platform.getDb().getUserName());
			ZmjConstant.userInfo.setUserNote(platform.getDb().getUserId());
			// 先初始化 用户头像图片保存路径 调用此方法时
			ZmjPictureTools.initImageViewSaveAddress(PlatfromForLogin.this);
			// 将返回信息 url 对应的 用户头像 写入到 已经初始化的路径中 ZmjConstant.imagePath 保存图片本地路径
			ZmjPictureTools.writeUserImageToSDPath(ZmjConstant.userInfo
					.getUserIcon());

			// 将本地保存的 图片 转化成 byte[]型数据
			paramArrayOfByte = ZmjPictureToBytesData
					.SetImageToByteArray(ZmjConstant.imagePath);
			// 执行 服务器 注册用户的异步任务 ,同时将用户图片上传到服务器 异步任务完成后 在 onPostExecute中对返回结果处理
			// 并准备 跳转
			new LoginAndUploadUserImageTask().execute();

		}
			break;

		}
		return false;
	}

	public void setPlatform(String platName) {
		platform = ShareSDK.getPlatform(platName);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.zmjQQLoginButton:

			Platform qq = ShareSDK.getPlatform(QQ.NAME);
			authorize(qq);

			break;
		case R.id.zmjSinaWeiBoButton:
			Toast.makeText(PlatfromForLogin.this, "新浪微博登录尚不支持,请您见谅", 1000)
					.show();

			break;
		case R.id.zmjQQWeiXinButton:

			Toast.makeText(PlatfromForLogin.this, "qq微信登录尚不支持,请您见谅", 1000)
					.show();
			break;
		}

	}

	// 异步线程 这里获取数据
	private class LoginAndUploadUserImageTask extends
			AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			String result = "";
			try {

				// 将图片的字节流数据加密成base64字符输出
				String photo = Base64.encodeToString(paramArrayOfByte, 0,
						paramArrayOfByte.length, Base64.DEFAULT);

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(ZmjConstant.loginUrl);

				// 设置参数，仿html表单提交
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				paramList.add(new BasicNameValuePair("user_id",
						ZmjConstant.userInfo.getUserNote()));
				paramList.add(new BasicNameValuePair("user_name",
						ZmjConstant.userInfo.getUserName()));
				paramList.add(new BasicNameValuePair("photo", photo));
				httpPost.setEntity(new UrlEncodedFormEntity(paramList,
						HTTP.UTF_8));
				// 发送HttpPost请求，并返回HttpResponse对象
				HttpResponse httpResponse = httpClient.execute(httpPost);
				// 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					// 获取返回结果
					result = EntityUtils.toString(httpResponse.getEntity());

				}

			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			if (result.equals("true")) {

				Toast.makeText(PlatfromForLogin.this, "注册美食屋用户成功!o(￣▽￣)d", 1000)
						.show();
				// 用sharePreferences存放数据
				ZmjConstant.preference = getSharedPreferences("userLoginInfo",
						MODE_WORLD_READABLE);
				ZmjConstant.editor = ZmjConstant.preference.edit();

				ZmjConstant.editor.putString("user_id",
						ZmjConstant.userInfo.getUserIcon());
				ZmjConstant.editor.putString("user_name",
						ZmjConstant.userInfo.getUserName());
				ZmjConstant.editor.putString("userIconUrl",
						ZmjConstant.userInfo.getUserIcon());
				ZmjConstant.editor.putString("userIconSDPath",
						ZmjConstant.imagePath);
				ZmjConstant.editor.commit();

				Intent intent = new Intent(PlatfromForLogin.this,
						PersonalInfoActivity.class);
				intent.putExtra("user_id", ZmjConstant.userInfo.getUserNote());
				intent.putExtra("user_name", ZmjConstant.userInfo.getUserName());
				PlatfromForLogin.this.startActivity(intent);

			} else {

				Toast.makeText(PlatfromForLogin.this,
						"注册美食屋用户失败!o((⊙﹏⊙))o.您不要气馁,请您再试一次,如果不行,请您及时联系我们!阿里嘎妥!",
						1000).show();

			}

			super.onPostExecute(result);
		}

	}

}
