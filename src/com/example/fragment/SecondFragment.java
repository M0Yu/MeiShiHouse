package com.example.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.Utils.Utils;
import com.example.activity.MeiShiActivity;
import com.example.adapter.ListAdapter;
import com.example.classfy.R;
import com.example.classfy.R.drawable;
import com.example.classfy.R.id;
import com.example.classfy.R.layout;
import com.example.override.MyViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.AdapterView.OnItemClickListener;

public class SecondFragment extends Fragment {

	public static SecondFragment newInstance() {
		SecondFragment homeFragment = new SecondFragment();
		Bundle bundle = new Bundle();
		homeFragment.setArguments(bundle);
		return homeFragment;
	}

	private View view;
	private int[] viewPagerImages = { R.drawable.viewpager1,
			R.drawable.viewpager2, R.drawable.viewpager3, R.drawable.viewpager4 };
	private int[] listViewImages = { R.drawable.ganhuai, R.drawable.lanman,
			R.drawable.momoda, R.drawable.tianmi, R.drawable.wenluan,
			R.drawable.wenyi, R.drawable.xiaoqingxing };
	private String[] texts = { "感怀", "浪漫", "萌萌哒", "甜蜜", "温暖", "文艺", "小清新" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.second_fragment, null);
		initView();
		// initData();
		initAdapter();
		getResult();
		addListener();
		// scrollView.smoothScrollTo(0, 0);
		return view;
	}

	private void initAdapter() {
		// TODO Auto-generated method stub
		ListAdapter adapter = new ListAdapter(getActivity(), listViewList,
				R.layout.list_item, new String[] { "image", "text" },
				new int[] { R.id.list_item_text });
		listView.setAdapter(adapter);
		// Utils.setListViewHeightBasedOnChildren(listView);
	}

	private void addListener() {
		// TODO Auto-generated method stub
		if (listViewList.size() == 0) {
			for (int i = 0; i < listViewImages.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("image", listViewImages[i]);
				map.put("text", texts[i]);
				listViewList.add(map);
			}
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Toast.makeText(getActivity(), "helll", 0).show();
				Intent it = new Intent(getActivity(), MeiShiActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("itemlist_id", "2-" + (position + 1));
				bundle.putString("name", texts[position]);
				it.putExtra("bundle", bundle);
				getActivity().startActivity(it);

			}
		});
	}

	private void getResult() {
		final String url = "http://1.zmj520.sinaapp.com/servlet/GetPersonalityRecommendList";
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost post = new HttpPost(url);
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	private ArrayList<Map<String, Object>> list;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			list = Utils.Json((String) msg.obj, "personalityList",
					new String[] { "pro_name", "detail_pic_url", "pro_price",
							"pro_desc", "classify_recommand", "store_list_id",
							"collect_nums", "store_id", "recommand_pic_url",
							"pro_url", "pic_url" });
			initData();
		}

	};
	
	private void setData() {
		// TODO Auto-generated method stub
		viewPager.setData(pageDataList);

	}

	private List<Map<String, Object>> pageDataList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> listViewList = new ArrayList<Map<String, Object>>();

	private void initData() {
		// TODO Auto-generated method stub
		if (pageDataList.size() == 0) {
			for (int i = 0; i < list.size(); i++) {
				pageDataList.add(list.get(i));
			}

		}
		setData();
	}

	private MyViewPager viewPager;
	private ListView listView;

	// private ScrollView scrollView;

	private void initView() {
		// TODO Auto-generated method stub
		viewPager = (MyViewPager) view.findViewById(R.id.second_myViewPager1);
		listView = (ListView) view.findViewById(R.id.second_mylistView1);
		// scrollView = (ScrollView) view.findViewById(R.id.second_scrollview);

	}

	public void smoothToTop() {
		// scrollView.smoothScrollTo(0, 0);
	}
}
