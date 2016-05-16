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

import com.aishangte.zmj.meishiwu.utils.ZmjAnalysisTools;
import com.aishangte.zmj.meishiwu.utils.ZmjConstant;
import com.example.classfy.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class PersonalOpinionOfSetting extends Activity {

	private EditText userOpinionsEditText, userConnectMethodEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zmj_personal_setting_opinions);
		userOpinionsEditText = (EditText) findViewById(R.id.opinionsEditText);
		userConnectMethodEditText = (EditText) findViewById(R.id.connectingMethodEditText);

	}

	public void onOpinionClick(View v) {

		switch (v.getId()) {
		case R.id.returnToPersonalSettingImageView:

			this.finish();

			break;

		case R.id.opinionSendButton:
			if (ZmjConstant.preference != null) {
				String opinionsOfUser = userOpinionsEditText.getText()
						.toString();
				String userConnectMethod = userConnectMethodEditText.getText()
						.toString();

				new SendOpinionsTask().execute(ZmjConstant.sendOpinionsUrl,
						ZmjConstant.preference.getString("user_id", ""),
						ZmjConstant.preference.getString("user_name", ""),
						opinionsOfUser, userConnectMethod);

			} else {

				Toast.makeText(PersonalOpinionOfSetting.this, "您还未登录(⊙o⊙)哦",
						1000).show();
			}

			break;

		}

	}

	// 异步线程 这里获取数据
	private class SendOpinionsTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// Simulates a background job.
			String result = "";
			try {

				HttpClient httpClient = new DefaultHttpClient();
				// params[0]:url
				HttpPost httpPost = new HttpPost(params[0]);
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				paramList.add(new BasicNameValuePair("user_id", params[1]));
				paramList.add(new BasicNameValuePair("user_name", params[2]));
				paramList.add(new BasicNameValuePair("opinion", params[3]));
				paramList.add(new BasicNameValuePair("connect_method",
						params[4]));
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
			// Do some stuff here

			// Call onRefreshComplete when the list has been refreshed.
			Toast.makeText(PersonalOpinionOfSetting.this, result, 1000).show();

			userOpinionsEditText.setText(R.string.defaultOpinions);
			userConnectMethodEditText
					.setText(R.string.defaultStringOfConnectingMetodEditText);
			super.onPostExecute(result);
		}

	}

}
