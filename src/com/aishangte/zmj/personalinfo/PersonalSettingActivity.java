package com.aishangte.zmj.personalinfo;

import java.util.ArrayList;
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
import com.aishangte.zmj.meishiwu.utils.ZmjPictureTools;
import com.aishangte.zmj.meishiwu.utils.ZmjUpdateManager;


import com.example.classfy.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class PersonalSettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zmj_personal_setting);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		// 返回按钮
		case R.id.returnToPersoninfoImageView:

			this.finish();

			break;
		case R.id.sz_main_tuijian:
			ZmjPictureTools.writeImageFromAssetsToSDFile(
					PersonalSettingActivity.this, "app_picture.png");

			// ZmjPictureTools.shareMsg(
			// PersonalSettingActivity.this,
			// "com.aishangte.zmj.personalinfo.PersonalSettingActivity",
			// "美食屋",
			// "美食屋，欢迎大家关注!" + ZmjConstant.meiShiWuDownLoadUrl,
			// ZmjPictureTools.getApplicationAbsolutePath(ZmjPictureTools
			// .getPackageName(PersonalSettingActivity.this))
			// + "/app_picture.png");

			ZmjPictureTools
					.shareMsg(PersonalSettingActivity.this, "分享", "美食屋",
							"美食屋,欢迎大家关注!O(∩_∩)O~"
									+ ZmjConstant.meiShiWuDownLoadUrl, "");

			break;

		case R.id.sz_main_fankui:
			Intent intent = new Intent(PersonalSettingActivity.this,
					PersonalOpinionOfSetting.class);
			startActivity(intent);

			break;
		case R.id.sz_main_lianxi:

			Toast.makeText(PersonalSettingActivity.this,
					"美食屋客服尚未开通,请您敬请期待!(｡･∀･)ﾉﾞ嗨", 1000).show();

			break;
		case R.id.sz_main_genxing:

			PackageManager pm = PersonalSettingActivity.this
					.getPackageManager();
			PackageInfo pi = null;
			try {

				pi = pm.getPackageInfo(
						PersonalSettingActivity.this.getPackageName(), 0);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String versioncode = pi.versionCode + "";

			ZmjUpdateManager manager = new ZmjUpdateManager(
					PersonalSettingActivity.this, versioncode);
			// 检查软件更新
			manager.checkUpdate();

			break;
		case R.id.sz_main_qingli:

			Toast.makeText(PersonalSettingActivity.this, "功能开发中!O(∩_∩)O~", 1000)
					.show();

			break;

		case R.id.sz_main_aboutus:
			Toast.makeText(PersonalSettingActivity.this,
					"该版面尚在制作,请原谅O(∩_∩)O啊啊~", 1000).show();

			break;

		case R.id.sz_main_tuichu:
			// finish掉当前的 activity 并且 清理掉用户登录信息
			// 清除 preference中的值 并将 其赋值为 null
			if (ZmjConstant.preference != null) {

				new DeleteUserInfoFromServersTask().execute();
			} else {

				Toast.makeText(PersonalSettingActivity.this,
						"您当前并未登录!Hi~ o(*￣▽￣*)ブ", 1000).show();
			}

			break;
		}
	}

	// 异步线程 这里获取数据
	private class DeleteUserInfoFromServersTask extends
			AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {

			String result = "";
			try {

				// 将图片的字节流数据加密成base64字符输出

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(
						ZmjConstant.delteUserInfoFromServersUrl);

				// 设置参数，仿html表单提交
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				paramList.add(new BasicNameValuePair("user_id",
						ZmjConstant.preference.getString("user_id", "")));
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

				ZmjConstant.editor.clear();
				ZmjConstant.editor.commit();
				ZmjConstant.preference = null;
				ZmjConstant.editor = null;
				Toast.makeText(PersonalSettingActivity.this,
						"退出当前账号成功!Hi~ o(*￣▽￣*)ブ", 1000).show();
				PersonalSettingActivity.this.finish();
			}

			super.onPostExecute(result);
		}

	}

}
