package com.example.activity;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aishangte.zmj.meishiwu.utils.ZmjConstant;
import com.example.Utils.Utils;
import com.example.classfy.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.image.SmartImageView;
import com.lyz.activity.Activity_DailySpecial;
import com.lyz.activity.Activity_FirstPage;

public class MeiShiShuActivity extends ActionBarActivity {

	private PullToRefreshScrollView pullToRefreshScrollView;
	private ScrollView scrollView;
	private TextView meiShiText, choujiangText, tejiaText, genxingText;
	private SmartImageView meishiImage, choujiangImage, tejiaImage,
			genxingImage;
	private long lastClickTime;
	private String user_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.meishi_activity);
		meiShiText = (TextView) this.findViewById(R.id.meishi_time);
		choujiangText = (TextView) this.findViewById(R.id.choujiang_time);
		tejiaText = (TextView) this.findViewById(R.id.tejia_time);
		genxingText = (TextView) this.findViewById(R.id.genxing_time);
		meishiImage = (SmartImageView) this.findViewById(R.id.meishi_image);
		choujiangImage = (SmartImageView) this
				.findViewById(R.id.choujiang_image);
		tejiaImage = (SmartImageView) this.findViewById(R.id.tejia_image);
		genxingImage = (SmartImageView) this.findViewById(R.id.genxing_image);

		getUserId();

		new GetDataTask().execute();
		pullToRefreshScrollView = (PullToRefreshScrollView) this
				.findViewById(R.id.pullToRefreshScrollView);
		pullToRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						new GetDataTask().execute();
					}
				});
		scrollView = pullToRefreshScrollView.getRefreshableView();
	}

	private void getUserId() {
		if (ZmjConstant.preference != null) {
			user_id = ZmjConstant.preference.getString("user_id", "");
		} else {
			user_id = "";
		}
	}

	// http://1.zmj520.sinaapp.com/servlet/GetDiscoveryInfo

	private class GetDataTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// Simulates a background job.
			String url = "http://1.zmj520.sinaapp.com/servlet/GetDiscoveryInfo";
			String result = null;
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost post = new HttpPost(url);
				List<NameValuePair> parm = new ArrayList<NameValuePair>();
				parm.add(new BasicNameValuePair("user_id", user_id));
				post.setEntity(new UrlEncodedFormEntity(parm));
				HttpResponse respone = httpClient.execute(post);
				if (respone.getStatusLine().getStatusCode() == 200) {
					result = EntityUtils.toString(respone.getEntity(), "utf-8");
				}

				return result;

			} catch (Exception e) {
			}
			return null;
		}

		private ArrayList<Map<String, Object>> meishiList;
		private ArrayList<Map<String, Object>> choujiangList;
		private ArrayList<Map<String, Object>> tejiaList;
		private ArrayList<Map<String, Object>> genxingList;

		@Override
		protected void onPostExecute(String result) {
			// Do some stuff here

			meishiList = Utils.Json(result, "MeiShiMeiWenList", new String[] {
					"id", "itemlist_id", "upload_date", "pic_url" });
			choujiangList = Utils.Json(result, "XianShiChouJiangList",
					new String[] { "id", "itemlist_id", "upload_date",
							"pic_url" });
			tejiaList = Utils.Json(result, "JinRiTeJiaList", new String[] {
					"id", "itemlist_id", "upload_date", "pic_url" });
			genxingList = Utils.Json(result, "DingYueGengXinList",
					new String[] { "id", "itemlist_id", "upload_date",
							"pic_url" });
			if (meishiList.size() != 0) {
				meiShiText.setText(getTime(meishiList));
				meishiImage.setImageUrl((String) meishiList.get(0).get(
						"pic_url"));
				meishiImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent it = new Intent(MeiShiShuActivity.this,
								MeiShiMeiWenActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("map",
								(Serializable) meishiList.get(0));
						it.putExtra("bund", bundle);
						MeiShiShuActivity.this.startActivity(it);
					}
				});
			}

			if (choujiangList.size() != 0) {
				choujiangImage.setImageUrl((String) choujiangList.get(0).get(
						"pic_url"));
				choujiangText.setText(getTime(choujiangList));
				choujiangImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(MeiShiShuActivity.this,
								"我们正在全力设计此功能，敬请期待！", Toast.LENGTH_SHORT).show();
					}
				});
			}

			if (tejiaList.size() != 0) {
				tejiaImage
						.setImageUrl((String) tejiaList.get(0).get("pic_url"));
				tejiaText.setText(getTime(tejiaList));
				tejiaImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent it = new Intent(MeiShiShuActivity.this,
								Activity_DailySpecial.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("map",
								(Serializable) tejiaList.get(0));
						it.putExtra("bund", bundle);
						MeiShiShuActivity.this.startActivity(it);
					}
				});
			}

			if (genxingList.size() != 0) {
				genxingImage.setImageUrl((String) genxingList.get(0).get(
						"pic_url"));
				genxingText.setText(getTime(genxingList));
			}

			pullToRefreshScrollView.onRefreshComplete();
			super.onPostExecute(result);
		}

		private String getTime(ArrayList<Map<String, Object>> meishiList) {
			String time = (String) meishiList.get(0).get("upload_date");
			String times[] = time.split(" ");
			String t[] = times[0].split("-");
			return t[1] + "-" + t[2];
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getEventTime() - lastClickTime > 2000) {
				Toast.makeText(MeiShiShuActivity.this, "再次点击退出",
						Toast.LENGTH_SHORT).show();
			} else {
				finish();
				return true;
			}
			lastClickTime = event.getEventTime();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
