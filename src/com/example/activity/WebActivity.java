package com.example.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aishangte.zmj.meishiwu.utils.ZmjConstant;
import com.example.Utils.Utils;
import com.example.classfy.R;

public class WebActivity extends ActionBarActivity {

	private WebView webView;
	private LinearLayout layout;
	private Map<String, Object> map;
	private String user_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_web);
		webView = (WebView) this.findViewById(R.id.webview);
		layout = (LinearLayout) this.findViewById(R.id.web_title);

		TextView title = (TextView) layout.findViewById(R.id.ds_titleView);
		title.setText("美食美文");

		ImageView backButton = (ImageView) layout
				.findViewById(R.id.ds_backView);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WebActivity.this.finish();
			}
		});

		Intent it = getIntent();
		Bundle bundle = it.getBundleExtra("bund");
		map = (Map<String, Object>) bundle.get("map");
		initData(0, 3);
	}

	private void getUserId() {
		if (ZmjConstant.preference != null) {
			user_id = ZmjConstant.preference.getString("user_id", "");
		} else {
			user_id = "";
		}
	}

	// http://1.zmj520.sinaapp.com/servlet/GetStoreList
	private void initData(final int min, final int max) {
		new Thread(new Runnable() {
			String url = "http://1.zmj520.sinaapp.com/servlet/GetStoreList";

			@Override
			public void run() {

				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost post = new HttpPost(url);
					List<NameValuePair> parm = new ArrayList<NameValuePair>();

					parm.add(new BasicNameValuePair("user_id", user_id));
					parm.add(new BasicNameValuePair("store_list_id",
							(String) map.get("store_list_id")));
					parm.add(new BasicNameValuePair("min", min + ""));
					parm.add(new BasicNameValuePair("max", max + ""));
					post.setEntity(new UrlEncodedFormEntity(parm));
					HttpResponse respone = httpClient.execute(post);
					if (respone.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(respone
								.getEntity());
						Message msg = new Message();
						msg.obj = result;

						handler.sendMessage(msg);
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();

	}

	List<Map<String, Object>> dataList;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String result = (String) msg.obj;
			dataList = Utils.Json(result, "storeList", new String[] {
					"pro_name", "is_collect", "detail_pic_url", "pro_price",
					"pro_desc", "store_id", "pro_url", "pic_url" });
			webView.loadUrl((String) dataList.get(0).get("detail_pic_url"));
		};
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
