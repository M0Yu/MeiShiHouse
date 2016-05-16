package com.example.activity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aishangte.zmj.meishiwu.utils.ZmjConstant;
import com.example.Utils.Utils;
import com.example.adapter.ImageListViewAdapter;
import com.example.classfy.R;
import com.example.classfy.R.id;
import com.example.classfy.R.layout;
import com.example.classfy.R.menu;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MeiShiActivity extends ListActivity {

	private PullToRefreshListView listView;
	private List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
	private String itemlist_id;
	private int i = 0;
	private int lastindex = -1;
	private String user_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_jiemu);

		View view = this.findViewById(R.id.title);
		TextView textView = (TextView) view.findViewById(R.id.ds_titleView);
		ImageView backButton = (ImageView) view.findViewById(R.id.ds_backView);

		Intent it = getIntent();
		Bundle bundle = it.getBundleExtra("bundle");
		String name = bundle.getString("name");
		itemlist_id = bundle.getString("itemlist_id");

		textView.setText(name);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		getUserId();

		listView = (PullToRefreshListView) this.findViewById(R.id.listView);
		setAdapter();
		initData(0, 4);
		listView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				excuteReloadData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (lastindex != i) {
					initData(i * 4, (i + 1) * 4);
					lastindex = i;
				} else {
					Toast.makeText(MeiShiActivity.this, "沒有更多", 0).show();
					Message msg = new Message();
					msg.obj = null;
					handler.sendMessage(msg);
				}
			}
		});

	}

	private void getUserId() {
		if (ZmjConstant.preference != null) {
			user_id = ZmjConstant.preference.getString("user_id", "");
		} else {

			user_id = "";
		}
	}

	// http://1.zmj520.sinaapp.com/servlet/GetThemeList?user_id=&itemlist_id=4-1&min=0&max=3
	private void initData(final int min, final int max) {
		new Thread(new Runnable() {
			String url = "http://1.zmj520.sinaapp.com/servlet/GetThemeList";

			@Override
			public void run() {

				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost post = new HttpPost(url);
					List<NameValuePair> parm = new ArrayList<NameValuePair>();

					parm.add(new BasicNameValuePair("user_id", user_id));
					parm.add(new BasicNameValuePair("itemlist_id", itemlist_id));
					parm.add(new BasicNameValuePair("min", min + ""));
					parm.add(new BasicNameValuePair("max", max + ""));
					post.setEntity(new UrlEncodedFormEntity(parm));
					HttpResponse respone = httpClient.execute(post);
					if (respone.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(
								respone.getEntity(), "utf-8");
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

	private List<Map<String, Object>> list;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			list = Utils.Json((String) msg.obj, "themeList", new String[] {
					"title", "is_collect", "description", "store_list_id",
					"collect_nums", "pic_url" });
			if (list.size() != 0) {
				i++;
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					// map.put("pic_url", list.get(i).get("pic_url"));
					// map.put("collect_nums", list.get(i).get("collect_nums"));
					// map.put("is_collect", list.get(i).get("is_collect"));
					dataList.add(map);
				}

			}

			adapter.notifyDataSetChanged();
			listView.onRefreshComplete();

		}

	};

	private ImageListViewAdapter adapter;

	private void setAdapter() {
		// TODO Auto-generated method stub
		adapter = new ImageListViewAdapter(MeiShiActivity.this,
				R.layout.jiemu_list_item, dataList, new String[] { "pic_url",
						"collect_nums", "is_collect" }, new int[] {
						R.id.imageView1, R.id.shouchang_count });
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Intent it = new Intent(MeiShiActivity.this,
						MeiShiWuDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("map",
						(Serializable) dataList.get(position - 1));
				it.putExtra("bund", bundle);
				MeiShiActivity.this.startActivity(it);
			}
		});
	};

	private void excuteReloadData() {
		i = 0;
		lastindex = -1;
		dataList.clear();
		initData(0, 4);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
