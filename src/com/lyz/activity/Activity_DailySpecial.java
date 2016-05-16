package com.lyz.activity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.aishangte.zmj.meishiwu.utils.ZmjConstant;
import com.example.Utils.Utils;
import com.example.activity.MeiShiMeiWenActivity;
import com.example.classfy.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.image.SmartImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_DailySpecial extends Activity {

	private static final int DAILYSPECIAL_GET_ID = 0;
	private static final int DAILYSPECIAL_GET_DATA_FINISH = 1;
	private static final String GET_STORE_List = "http://1.zmj520.sinaapp.com/servlet/GetStoreList";
	private static final String GET_STORE_ID = "http://1.zmj520.sinaapp.com/servlet/GetThemeList";

	private PullToRefreshScrollView pictureFall;
	private LinearLayout colOne;
	private LinearLayout colTwo;
	private ImageView ds_backView;
	private TextView ds_titleView;

	private String itemlist_id;
	private String store_list_id;

	private Map<String, Object> map;
	private List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
	private String User_id;

	private int min = 0;
	private int max = 6;
	private int loadIndex = 0;
	private int lastIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.actvity_dailyspecial);

		initView();
		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub
		getUserId();

		Intent intent = getIntent();
		Bundle bund = intent.getBundleExtra("bund");
		map = (Map<String, Object>) bund.getSerializable("map");
		itemlist_id = (String) map.get("itemlist_id");

		getStoreId();
	}

	private void getUserId() {
		if (ZmjConstant.preference != null) {
			User_id = ZmjConstant.preference.getString("user_id", "");
		} else {

			User_id = "";
		}

	}

	private void getDataList() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpClient client = new DefaultHttpClient();
					HttpPost post = new HttpPost(GET_STORE_List);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("user_id", User_id));
					params.add(new BasicNameValuePair("store_list_id",
							store_list_id));
					params.add(new BasicNameValuePair("min", max * loadIndex
							+ ""));
					params.add(new BasicNameValuePair("max", max + ""));

					post.setEntity(new UrlEncodedFormEntity(params));
					HttpResponse response = client.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(
								response.getEntity(), "utf-8");

						lastIndex = loadIndex;

						Message msg = new Message();
						msg.what = DAILYSPECIAL_GET_DATA_FINISH;
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

	private void getStoreId() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpClient client = new DefaultHttpClient();
					HttpPost post = new HttpPost(GET_STORE_ID);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("user_id", User_id));
					params.add(new BasicNameValuePair("itemlist_id",
							itemlist_id));
					params.add(new BasicNameValuePair("min", min + ""));
					params.add(new BasicNameValuePair("max", max + ""));

					post.setEntity(new UrlEncodedFormEntity(params));
					HttpResponse response = client.execute(post);

					if (response.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(
								response.getEntity(), "utf-8");

						Message msg = new Message();
						msg.what = DAILYSPECIAL_GET_ID;
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

	private void setData() {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		View view = null;

		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < dataList.size(); i++) {
			map = dataList.get(i);

			view = LayoutInflater.from(this).inflate(R.layout.cell_dailspecial,
					null);
			view.setLayoutParams(params);

			// view.setPadding(0, 0, 0, 15);
			view.setTag(map);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Map<String, Object> c_map = (Map<String, Object>) v
							.getTag();
					Intent intent = new Intent(Activity_DailySpecial.this,
							Activity_ShowGoods.class);
					Bundle bund = new Bundle();
					bund.putSerializable("map", (Serializable) c_map);
					intent.putExtra("bund", bund);
					startActivity(intent);
				}
			});

			SmartImageView pic = (SmartImageView) view
					.findViewById(R.id.ds_goodsPic);
			TextView name = (TextView) view.findViewById(R.id.ds_goodsName);
			TextView price = (TextView) view.findViewById(R.id.ds_goodsPrice);
			ImageView isFav = (ImageView) view.findViewById(R.id.ds_goodsIsFav);
			TextView fav = (TextView) view.findViewById(R.id.ds_goodsFav);

			pic.setImageUrl((String) map.get("pic_url"));
			name.setText((String) map.get("pro_name"));
			price.setText("¥ " + map.get("pro_price"));
			String isfav = (String) map.get("is_collect");
			if ("0".equals(isfav)) {
				isFav.setBackgroundResource(R.drawable.beforeheart);
			} else {
				isFav.setBackgroundResource(R.drawable.afterheart);
			}
			fav.setText((String) map.get("collect_nums"));

			addImage(view);
		}
		pictureFall.onRefreshComplete();
	}

	private void addImage(View view) {
		if (colOne.getChildCount() == colTwo.getChildCount()) {
			colOne.addView(view);
		} else {
			colTwo.addView(view);
		}
	}

	private void initView() {
		// TODO Auto-generated method stub

		pictureFall = (PullToRefreshScrollView) this
				.findViewById(R.id.pictureFall);
		pictureFall.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				executeReloadData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub

				if (loadIndex != lastIndex) {
					getDataList();
				} else {
					Toast.makeText(Activity_DailySpecial.this, "没有更多了...",
							Toast.LENGTH_SHORT).show();
					pictureFall.onRefreshComplete();
				}
			}

		});

		colOne = (LinearLayout) this.findViewById(R.id.colOne);
		colTwo = (LinearLayout) this.findViewById(R.id.colTwo);

		ds_titleView = (TextView) this.findViewById(R.id.ds_titleView);
		ds_titleView.setText("今日特价");

		ds_backView = (ImageView) this.findViewById(R.id.ds_backView);
		ds_backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String result = msg.obj.toString();
			if (msg.what == DAILYSPECIAL_GET_ID) {
				dataList = Utils.Json(result, "themeList",
						new String[] { "store_list_id" });
				store_list_id = (String) dataList.get(0).get("store_list_id");

				getDataList();
			}

			if (msg.what == DAILYSPECIAL_GET_DATA_FINISH) {
				dataList = Utils.Json(result, "storeList", new String[] {
						"pro_name", "is_collect", "detail_pic_url",
						"pro_price", "pro_desc", "collect_nums", "store_id",
						"pro_url", "pic_url" });
				if (dataList.size() != 0) {
					loadIndex++;
				}
				setData();
			}
		}
	};

	// @Override
	// protected void onRestart() {
	// // TODO Auto-generated method stub
	// super.onRestart();
	// executeReloadData();
	// }

	// ====================重新获取数据=======================
	private void executeReloadData() {
		// TODO Auto-generated method stub
		min = 0;
		max = 6;
		loadIndex = 0;
		lastIndex = -1;
		colOne.removeAllViews();
		colTwo.removeAllViews();
		dataList.clear();
		getDataList();
	}

}
