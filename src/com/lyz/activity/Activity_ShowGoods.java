package com.lyz.activity;

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

import com.aishangte.zmj.meishiwu.utils.ZmjConstant;
import com.example.classfy.R;
import com.example.override.ScllorTabView;
import com.lyz.Fragment.Fragment_GoodsDes;
import com.lyz.Fragment.Fragment_GoodsEva;
import com.lyz.Fragment.Fragment_GoodsInfo;
import com.lyz.adapter.Adapter_GoodsViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_ShowGoods extends FragmentActivity implements
		OnClickListener {

	private static final String Collect_Store = "http://1.zmj520.sinaapp.com/servlet/DealWithStoreCollectNums";
	private static final String Delete_Store = "http://1.zmj520.sinaapp.com/servlet/DeleteLiWuCollection";
	private static final int COLLECT_STORE_FINISHED = 0;
	private static final int DELETE_STORE_FINISHED = 1;

	private ViewPager mViewPager;
	private ScllorTabView tabbar;
	private TextView goodsInfo;
	private TextView goodsDes;
	private TextView goodsEva;
	private TextView titleText;
	private ImageView sg_backView;
	private Button sg_purchs;
	private ImageView sg_favButton;
	private ImageView sg_favImg;

	private Map<String, Object> data;
	private List<Fragment> fragments;
	private String user_id;
	private String is_collect = "0";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_showgoods);

		initView();
		getData();
		initViewPager();
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
		getUserId();

		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bund");
		data = (Map<String, Object>) bundle.getSerializable("map");

	}

	private void initViewPager() {
		// TODO Auto-generated method stub
		fragments = new ArrayList<Fragment>();

		fragments.add(new Fragment_GoodsInfo(data));
		fragments.add(new Fragment_GoodsDes((String) data.get("pro_url")));
		fragments.add(new Fragment_GoodsEva(data));

		is_collect = (String) data.get("is_collect");
		if ("0".equals(is_collect)) {
			sg_favImg.setImageResource(R.drawable.beforeheart);
		} else {
			sg_favImg.setImageResource(R.drawable.afterheart);
		}

		Adapter_GoodsViewPager mAdapter = new Adapter_GoodsViewPager(
				getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(0);
		tabbar.setCurrentNum(0);
		mViewPager.setOnPageChangeListener(new mOnPageChangeListener());
	}

	private void initView() {
		// TODO Auto-generated method stub
		mViewPager = (ViewPager) this.findViewById(R.id.sg_goodsViewPager);

		tabbar = (ScllorTabView) this.findViewById(R.id.sg_scllorTab);
		tabbar.setTabNum(3);
		tabbar.setSelectedColor(Color.rgb(255, 126, 102), Color.GRAY);

		goodsInfo = (TextView) this.findViewById(R.id.sg_goodsInfoRadio);
		goodsInfo.setOnClickListener(this);

		goodsDes = (TextView) this.findViewById(R.id.sg_goodsDesRadio);
		goodsDes.setOnClickListener(this);

		goodsEva = (TextView) this.findViewById(R.id.sg_goodsEvaRadio);
		goodsEva.setOnClickListener(this);

		titleText = (TextView) this.findViewById(R.id.sg_titleView);
		titleText.setText("美食详情");

		sg_backView = (ImageView) this.findViewById(R.id.sg_backView);
		sg_backView.setOnClickListener(this);

		sg_purchs = (Button) this.findViewById(R.id.sg_purchs);
		sg_purchs.setOnClickListener(this);

		sg_favButton = (ImageView) this.findViewById(R.id.sg_favButton);
		sg_favButton.setOnClickListener(this);

		sg_favImg = (ImageView) this.findViewById(R.id.sg_favImg);
		sg_favImg.setOnClickListener(this);
	}

	// ====================添加和删除收藏的网络请求=====================================

	protected void executeDeleteLiWuCollection() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost post = new HttpPost(Delete_Store);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("user_id", user_id));
					params.add(new BasicNameValuePair("store_id", (String) data
							.get("store_id")));
					post.setEntity(new UrlEncodedFormEntity(params));
					HttpResponse respone = httpClient.execute(post);
					if (respone.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(respone
								.getEntity());
						Message msg = new Message();
						msg.obj = result;
						msg.what = DELETE_STORE_FINISHED;
						mHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	protected void executeDealWithStoreCollectNums() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost post = new HttpPost(Collect_Store);
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("user_id", user_id));
					params.add(new BasicNameValuePair("store_id", (String) data
							.get("store_id")));
					post.setEntity(new UrlEncodedFormEntity(params));
					HttpResponse respone = httpClient.execute(post);
					if (respone.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(
								respone.getEntity(), "utf-8");
						Message msg = new Message();
						msg.obj = result;
						msg.what = COLLECT_STORE_FINISHED;
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

			if (msg.what == COLLECT_STORE_FINISHED) {
				if ("添加商品收藏成功".equals(result)) {
					is_collect = "1";
					sg_favImg.setImageResource(R.drawable.afterheart);
					Toast.makeText(Activity_ShowGoods.this, "收藏成功ヾ(Ő∀Ő๑)ﾉ太好惹",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(Activity_ShowGoods.this, "收藏失败(っ╥╯﹏╰╥c)",
							Toast.LENGTH_SHORT).show();
				}
			}

			if (msg.what == DELETE_STORE_FINISHED) {
				if ("true".equals(result)) {
					is_collect = "0";
					sg_favImg.setImageResource(R.drawable.beforeheart);
					Toast.makeText(Activity_ShowGoods.this,
							"取消收藏成功٩(•̤̀ᵕ•̤́๑)ᵒᵏ", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(Activity_ShowGoods.this, "取消收藏失败(ﾉ)ﾟДﾟ(ヽ) ",
							Toast.LENGTH_SHORT).show();
				}
			}
		}

	};

	// =====================重写的viewpager的监听器=============================
	class mOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int index) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			tabbar.setOffset(arg0, arg1);
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			tabbar.setOffset(arg0, 0);
		}
	}

	// =====================页面的点击事件的分支=============================
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sg_goodsInfoRadio:
			mViewPager.setCurrentItem(0, true);
			tabbar.setOffset(0, 0);
			break;
		case R.id.sg_goodsDesRadio:
			mViewPager.setCurrentItem(1, true);
			tabbar.setOffset(1, 0);
			break;
		case R.id.sg_goodsEvaRadio:
			mViewPager.setCurrentItem(2, true);
			tabbar.setOffset(2, 0);
			break;
		case R.id.sg_purchs:
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri uri = Uri.parse((String) data.get("pro_url"));
			intent.setData(uri);
			startActivity(intent);
			break;
		case R.id.sg_favImg:
		case R.id.sg_favButton:
			if ("".equals(user_id)) {
				Toast.makeText(Activity_ShowGoods.this, "亲，请先登录! |ω・）",
						Toast.LENGTH_SHORT).show();
			} else {
				if ("0".equals(is_collect)) {
					executeDealWithStoreCollectNums();
				} else {
					executeDeleteLiWuCollection();
				}
			}
			break;
		case R.id.sg_backView:
			finish();
			break;
		default:
			break;
		}
	}
}
