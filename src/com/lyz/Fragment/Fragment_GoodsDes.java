package com.lyz.Fragment;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.classfy.R;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.AnalogClock;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class Fragment_GoodsDes extends Fragment {

	private LinearLayout sg_viewslayout;
	private String id;
	private String pro_url;

	public Fragment_GoodsDes(String pro_url) {
		super();
		// TODO Auto-generated constructor stub
		this.pro_url = pro_url;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_goodsdes, null);
		initView(view);

		Loadpic();
		return view;
	}

	private void initView(View view) {
		sg_viewslayout = (LinearLayout) view.findViewById(R.id.sg_viewslayout);
	}

	private void Loadpic() {
		// TODO Auto-generated method stub
		getid();

		Thread mThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(
						"http://hws.m.taobao.com/cache/mtop.wdetail.getItemDescx/4.1/?data=%7B%22item_num_id%22%3A%22"
								+ id + "%22%7D");
				try {
					HttpResponse result = client.execute(get);
					if (result.getStatusLine().getStatusCode() == 200) {
						String res = EntityUtils.toString(result.getEntity(),
								"utf-8");
						Message msg = new Message();
						msg.obj = res;
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
		});

		mThread.start();
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			analysis(msg.obj.toString());
		}
	};

	private void analysis(String result) {
		// TODO Auto-generated method stub
		String[] pics = null;

		try {
			JSONObject obj = new JSONObject(result);
			JSONObject data = obj.getJSONObject("data");
			JSONArray arr = data.getJSONArray("images");

			pics = new String[arr.length()];
			for (int i = 0; i < arr.length(); i++) {
				pics[i] = arr.getString(i);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addView(pics);
	}

	private void addView(String[] pic) {
		if (pic == null) {
			TextView tv = new TextView(getActivity());
			tv.setText("获取商品信息出错");
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			sg_viewslayout.addView(tv);
		} else if (pic.length == 0) {
			TextView tv = new TextView(getActivity());
			tv.setText("该商品暂无图片详情");
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			sg_viewslayout.addView(tv);
		} else {
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			WebView wv = null;
			for (int i = 0; i < pic.length; i++) {
				wv = new WebView(getActivity());
				// wv.setLayoutParams(params);
				wv.getSettings().setJavaScriptEnabled(true);
				wv.getSettings().setUseWideViewPort(true);
				// wv.getSettings().setSupportZoom(true);
				 wv.getSettings().setDisplayZoomControls(false);
				// wv.getSettings().setLayoutAlgorithm(
				// LayoutAlgorithm.SINGLE_COLUMN);
				wv.getSettings().setLoadWithOverviewMode(true);

				wv.loadUrl(pic[i]);
				sg_viewslayout.addView(wv);
			}
		}
	}

	private void getid() {
		String paramString = pro_url.split("\\?")[1];
		String[] params = paramString.split("&");
		for (String param : params) {
			if (pro_url.contains("ju.taobao")) {
				String[] kvp = param.split("=");
				if ("item_id".equals(kvp[0])) {
					id = kvp[1];
				}
			} else {
				String[] kvp = param.split("=");
				if ("id".equals(kvp[0])) {
					id = kvp[1];
				}
			}
		}
	}
}
