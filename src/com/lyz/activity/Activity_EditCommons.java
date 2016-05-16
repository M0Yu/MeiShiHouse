package com.lyz.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.aishangte.zmj.meishiwu.utils.ZmjConstant;
import com.example.classfy.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_EditCommons extends Activity implements OnClickListener {

	private static final String SEND_USER_COMMONS = "http://1.zmj520.sinaapp.com/servlet/UpLoadStoreComment";

	private ImageView backButton;
	private TextView sendButton;
	private EditText commonText;

	private String user_id;
	private String store_id;
	private String user_name;
	private String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_editcommon);

		initview();
		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bund");
		store_id = bundle.getString("store_id");
	}

	private void getUserInfo() {
		if (ZmjConstant.preference != null) {
			user_id = ZmjConstant.preference.getString("user_id", "");
			user_name = ZmjConstant.preference.getString("user_name", "");
		} else {
			user_id = "";
			user_name = "";
		}

	}

	private void initview() {
		// TODO Auto-generated method stub
		backButton = (ImageView) this.findViewById(R.id.ec_backView);
		sendButton = (TextView) this.findViewById(R.id.ec_editView);
		commonText = (EditText) this.findViewById(R.id.ec_commonsEditText);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ec_backView:
			Activity_EditCommons.this.finish();
			break;
		case R.id.ec_editView:
			if ("".equals(user_id) || "".equals(user_name)) {
				Toast.makeText(Activity_EditCommons.this, "亲，请先登录！ |ω・）",
						Toast.LENGTH_SHORT).show();
			} else {
				getUserCommons();
				executeSendCommons();
			}
			break;
		default:
			break;
		}
	}

	private void getUserCommons() {
		// TODO Auto-generated method stub
		content = commonText.getText().toString();
	}

	private void executeSendCommons() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpPost post = new HttpPost(SEND_USER_COMMONS);
					HttpClient client = new DefaultHttpClient();
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("store_id", store_id));
					params.add(new BasicNameValuePair("user_id", user_id));
					params.add(new BasicNameValuePair("user_name", user_name));
					params.add(new BasicNameValuePair("content", content));
					post.setEntity(new UrlEncodedFormEntity(params));

					HttpResponse response = client.execute(post);
					if (response.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(
								response.getEntity(), "utf-8");
						Message msg = new Message();
						msg.what = 0;
						msg.obj = result;
						mHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String result = msg.obj.toString();
			if (msg.what == 0) {
				if ("fail".equals(result)) {
					Toast.makeText(Activity_EditCommons.this,
							"发送失败！(ﾉ)ﾟДﾟ(ヽ) ", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(Activity_EditCommons.this,
							"发送成功！٩(•̤̀ᵕ•̤́๑)ᵒᵏ ", Toast.LENGTH_SHORT).show();
					Activity_EditCommons.this.finish();
				}
			}
		}

	};
}
