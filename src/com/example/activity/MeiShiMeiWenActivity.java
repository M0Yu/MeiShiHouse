package com.example.activity;

import java.io.Serializable;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aishangte.zmj.meishiwu.utils.ZmjConstant;
import com.example.Utils.Utils;
import com.example.adapter.MeiShiImageListViewAdapter;
import com.example.classfy.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MeiShiMeiWenActivity extends ActionBarActivity {

	private PullToRefreshListView listView;
	private MeiShiImageListViewAdapter adapter;

	private int index = 0;
	private int lastIndex = -1;
	private String user_id;
	private Map<String, Object> map;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mei_shi_mei_wen);
		layout = (LinearLayout) this.findViewById(R.id.meishimeiwen_title);

		TextView title = (TextView) layout.findViewById(R.id.ds_titleView);
		title.setText("美食美文");

		ImageView backButton = (ImageView) layout
				.findViewById(R.id.ds_backView);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MeiShiMeiWenActivity.this.finish();
			}
		});

		getUserId();

		listView = (PullToRefreshListView) this
				.findViewById(R.id.meishimeiwen_listView);
		adapter = new MeiShiImageListViewAdapter(this,
				R.layout.mei_shi_mei_wen_listitem, dataList,
				new String[] { "pic_url" },
				new int[] { R.id.mei_shi_mei_wen_image });
		listView.setAdapter(adapter);
		Intent it = getIntent();
		Bundle bundle = it.getBundleExtra("bund");
		map = (Map<String, Object>) bundle.get("map");
		new GetDataTask().execute((String) map.get("itemlist_id"), "0", "3");
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (index != lastIndex) {
					new GetDataTask().execute((String) map.get("itemlist_id"),
							index * 3 + "", (index + 1) * 3 - 1 + "");
					lastIndex = index;
				} else {
					Toast.makeText(MeiShiMeiWenActivity.this, "没有更多", 0).show();
					new GetDataTask().execute("");
				}

			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent it = new Intent(MeiShiMeiWenActivity.this,
						WebActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("map",
						(Serializable) dataList.get(position - 1));
				it.putExtra("bund", bundle);
				MeiShiMeiWenActivity.this.startActivity(it);
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

	private List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

	private class GetDataTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// Simulates a background job.
			String url = "http://1.zmj520.sinaapp.com/servlet/GetThemeList";
			String result = null;
			if (params[0].equals("")) {
				return null;
			}
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost post = new HttpPost(url);
				List<NameValuePair> parm = new ArrayList<NameValuePair>();

				parm.add(new BasicNameValuePair("user_id", user_id));
				parm.add(new BasicNameValuePair("itemlist_id", params[0]));
				parm.add(new BasicNameValuePair("min", params[1]));
				parm.add(new BasicNameValuePair("max", params[2]));
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

		private List<Map<String, Object>> list;

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				list = Utils.Json(result, "themeList", new String[] { "title",
						"is_collect", "description", "store_list_id",
						"collect_nums", "pic_url" });
				if (list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						dataList.add(list.get(i));
					}
					index++;
				}
			}
			adapter.notifyDataSetChanged();
			listView.onRefreshComplete();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mei_shi_mei_wen, menu);
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
