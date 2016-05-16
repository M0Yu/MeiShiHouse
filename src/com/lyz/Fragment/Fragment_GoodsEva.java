package com.lyz.Fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.aishangte.zmj.meishiwu.utils.ZmjConstant;
import com.example.Utils.Utils;
import com.example.classfy.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.loopj.android.image.SmartImageView;
import com.lyz.activity.Activity_EditCommons;
import com.lyz.adapter.Adapter_GoodsEvaList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_GoodsEva extends Fragment {

	private static final String GET_USERS_COMMONS = "http://1.zmj520.sinaapp.com/servlet/GetStoreComment";

	private PullToRefreshListView sg_goodsEvaList;
	private ImageButton sg_editEva;
	private TextView NoCommonsView;

	private Map<String, Object> mdata;
	private List<Map<String, Object>> dataSumList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
	private Adapter_GoodsEvaList mAdapter;

	private String user_id;

	private int min = 0;
	private int max = 10;
	private int index = 0;
	private int lastIndex = -1;

	public Fragment_GoodsEva(Map<String, Object> mdata) {
		super();
		// TODO Auto-generated constructor stub
		this.mdata = mdata;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_goodseva, null);
		initView(view);
		setAdapter();
		getData();
		return view;
	}

	private void getUserId() {
		if (ZmjConstant.preference != null) {
			user_id = ZmjConstant.preference.getString("user_id", "");
		} else {
			user_id = "";
		}
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		sg_goodsEvaList = (PullToRefreshListView) view
				.findViewById(R.id.sg_goodsEvaList);
		sg_editEva = (ImageButton) view.findViewById(R.id.sg_editEva);
		sg_editEva.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("".equals(user_id)) {
					Toast.makeText(getActivity(), "亲，请先登录！ |ω・）",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent();
					intent.setClass(getActivity(), Activity_EditCommons.class);
					Bundle bundle = new Bundle();
					bundle.putString("store_id", (String) mdata.get("store_id"));
					intent.putExtra("bund", bundle);
					getActivity().startActivity(intent);
				}
			}
		});

		NoCommonsView = (TextView) view.findViewById(R.id.NoCommonsView);
	}

	private void setAdapter() {
		// TODO Auto-generated method stub
		mAdapter = new Adapter_GoodsEvaList(getActivity(), dataSumList,
				R.layout.cell_goodsevalist);
		sg_goodsEvaList.setAdapter(mAdapter);
		sg_goodsEvaList
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						executeReloadData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						if (lastIndex != index) {
							caculateMin();
							executeGetCommons();
						} else {
							Toast.makeText(getActivity(), "没有更多了",
									Toast.LENGTH_SHORT).show();
						}
					}

				});
	}

	private void caculateMin() {
		// TODO Auto-generated method stub
		min = index * max;
		lastIndex = index;
	}

	private void executeReloadData() {
		// TODO Auto-generated method stub
		min = 0;
		index = 0;
		lastIndex = -1;
		dataSumList.clear();
		dataList.clear();
		executeGetCommons();
	}

	private void getData() {
		// TODO Auto-generated method stub
		getUserId();
		executeGetCommons();
	}

	private void executeGetCommons() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpPost post = new HttpPost(GET_USERS_COMMONS);
					HttpClient client = new DefaultHttpClient();
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("store_id",
							(String) mdata.get("store_id")));
					params.add(new BasicNameValuePair("min", min + ""));
					params.add(new BasicNameValuePair("max", max + ""));
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

			String result = (String) msg.obj;
			if (msg.what == 0) {
				dataList = Utils.Json(result, "commentList", new String[] {
						"content", "user_name", "comments_time", "pic_url" });
				if (dataList.size() != 0) {
					dataSumList.addAll(dataList);
					index++;
				}

				if (dataSumList.size() == 0) {
					NoCommonsView.setVisibility(View.VISIBLE);
					sg_goodsEvaList.setVisibility(View.GONE);
				} else {
					NoCommonsView.setVisibility(View.GONE);
					sg_goodsEvaList.setVisibility(View.VISIBLE);
				}
				mAdapter.notifyDataSetChanged();
				sg_goodsEvaList.onRefreshComplete();
			}
		}

	};
}
