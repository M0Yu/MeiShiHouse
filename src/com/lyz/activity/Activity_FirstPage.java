package com.lyz.activity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aishangte.zmj.meishiwu.utils.ZmjConstant;
import com.example.Utils.Utils;
import com.example.activity.ClassifyMainActivity;
import com.example.classfy.R;
import com.loopj.android.image.SmartImageView;
import com.lyz.adapter.Adapter_GoodsList;
import com.lyz.adapter.Adapter_SimpleViewPager;
import com.lyz.adapter.Adapter_fpViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_FirstPage extends Activity implements
		OnPageChangeListener {

	private static final int FIRST_PAGE_VIEWPAGE_SCROLL = 0;
	private static final int FIRST_PAGE_GET_HTTP_DATA = 1;
	private static final int FIRST_PAGE_GET_DATA_FINISH = 2;
	private static final String GET_HOME_PAGE_DATA = "http://1.zmj520.sinaapp.com/servlet/GetHomePageInfo";

	private ViewPager mViewPager;
	private ListView mListView;
	private LinearLayout pointsGroup;
	private ScrollView fp_scrollView;
	private TextView titleText;
	private SmartImageView fp_hotgoods;

	private ImageView[] pointViews;

	private List<Map<String, Object>> homePageFiveList;
	private List<Map<String, Object>> todaySaleList;
	private List<Map<String, Object>> deliciousHamperList;

	private int lastPostion = 0;
	private long lastClickTime = 0;
	private String user_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_firstpage);
		getUserId();
		getData();
		initView();
	}

	private void setData() {
		// TODO Auto-generated method stub
		fp_hotgoods.setImageUrl((String) todaySaleList.get(0).get("pic_url"));
		fp_hotgoods.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Activity_FirstPage.this,
						Activity_ShowGoods.class);
				Bundle bund = new Bundle();
				bund.putSerializable("map", (Serializable) todaySaleList.get(0));
				intent.putExtra("bund", bund);
				startActivity(intent);
			}
		});

		Adapter_GoodsList madapter = new Adapter_GoodsList(this,
				deliciousHamperList);
		mListView.setAdapter(madapter);

		pointViews = new ImageView[homePageFiveList.size()];
		for (int i = 0; i < homePageFiveList.size(); i++) {
			ImageView point = new ImageView(this);
			point.setLayoutParams(new LayoutParams(10, 10));
			pointViews[i] = point;
			if (i == 0) {
				point.setBackgroundResource(R.drawable.point_checked);
			} else {
				point.setBackgroundResource(R.drawable.point_normal);
			}

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			params.leftMargin = 10;
			params.rightMargin = 10;

			pointsGroup.addView(point, params);
		}

		mViewPager.setAdapter(new Adapter_fpViewPager(this, homePageFiveList));
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setCurrentItem(lastPostion);

		mHandler.sendEmptyMessageDelayed(FIRST_PAGE_VIEWPAGE_SCROLL, 2000);
	}

	private void getUserId() {
		if (ZmjConstant.preference != null) {
			user_id = ZmjConstant.preference.getString("user_id", "");
		} else {
			user_id = "";
		}
	}

	private void getData() {
		// TODO Auto-generated method stub

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(GET_HOME_PAGE_DATA);
				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("user_id", user_id));
					post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));

					HttpResponse response = client.execute(post);
					if (response.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(
								response.getEntity(), "utf-8");

						Message msg = new Message();
						msg.what = FIRST_PAGE_GET_HTTP_DATA;
						msg.obj = result;

						mHandler.sendMessage(msg);
					}

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void initView() {
		// TODO Auto-generated method stub
		fp_scrollView = (ScrollView) this.findViewById(R.id.fp_scrollView);
		mListView = (ListView) this.findViewById(R.id.fp_goodsListView);
		mViewPager = (ViewPager) this.findViewById(R.id.fp_viewPager);
		pointsGroup = (LinearLayout) this.findViewById(R.id.fp_pointsgroup);
		titleText = (TextView) this.findViewById(R.id.fp_titleView);
		fp_hotgoods = (SmartImageView) this.findViewById(R.id.fp_hotgoods);

		titleText.setText("美食屋");

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		lastPostion = arg0;
		changePoints(arg0);
	}

	private void changePoints(int postion) {
		// TODO Auto-generated method stub
		for (int i = 0; i < pointViews.length; i++) {
			if (i == postion) {
				pointViews[i].setBackgroundResource(R.drawable.point_checked);
			} else {
				pointViews[i].setBackgroundResource(R.drawable.point_normal);
			}
		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == FIRST_PAGE_VIEWPAGE_SCROLL) {

				++lastPostion;
				if (lastPostion == homePageFiveList.size()) {
					lastPostion = 0;
				}
				mViewPager.setCurrentItem(lastPostion);
				mHandler.sendEmptyMessageDelayed(FIRST_PAGE_VIEWPAGE_SCROLL,
						2000);
			}

			if (msg.what == FIRST_PAGE_GET_HTTP_DATA) {
				String result = msg.obj.toString();
				analysis(result);
			}

			if (msg.what == FIRST_PAGE_GET_DATA_FINISH) {
				if ("success".equals(msg.obj)) {
					setData();
				} else {

				}
			}
		}

	};

	private void analysis(String result) {
		Message msg = new Message();
		msg.what = FIRST_PAGE_GET_DATA_FINISH;

		try {
			homePageFiveList = Utils.Json(result, "homePageFiveList",
					new String[] { "id", "itemlist_id", "upload_date",
							"pic_url" });
			todaySaleList = Utils.Json(result, "todaySaleList", new String[] {
					"pro_name", "is_collect", "detail_pic_url", "pro_price",
					"pro_desc", "collect_nums", "store_id", "pro_url",
					"pic_url" });
			deliciousHamperList = Utils.Json(result, "deliciousHamperList",
					new String[] { "title", "is_collect", "itemlist_id",
							"description", "store_list_id", "collect_nums",
							"pic_url" });
			msg.obj = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			msg.obj = "fail";
			e.printStackTrace();
		}

		mHandler.sendMessage(msg);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		fp_scrollView.smoothScrollTo(0, 0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getEventTime() - lastClickTime > 2000) {
				System.out.println("press");
				Toast.makeText(Activity_FirstPage.this, "再次点击退出",
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
