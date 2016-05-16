package com.example.activity;

import java.io.IOException;
import java.io.Serializable;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aishangte.zmj.meishiwu.utils.ZmjConstant;
import com.example.Utils.Utils;
import com.example.classfy.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.image.SmartImageView;
import com.lyz.activity.Activity_ShowGoods;

public class MeiShiWuDetailActivity extends ActionBarActivity {

	private LinearLayout layout;
	private SmartImageView imaeView;
	private TextView titleText;
	private TextView descriptText;
	private ImageView collect;
	private Map<String, Object> map;
	private TextView shapTitle;
	private LinearLayout layout2;
	private PullToRefreshScrollView pulltorefreshScrollView;
	private String user_id;
	private int index = 0;
	private int lastIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mei_shi_wu_detail);

		layout = (LinearLayout) this.findViewById(R.id.layout1);
		pulltorefreshScrollView = (PullToRefreshScrollView) this
				.findViewById(R.id.detail_pullToRefreshScrollView);
		imaeView = (SmartImageView) this.findViewById(R.id.imageView);
		titleText = (TextView) this.findViewById(R.id.mei_shi_wu_detail_title);
		descriptText = (TextView) this
				.findViewById(R.id.mei_shi_wu_detail_descript);

		ImageView backButton = (ImageView) this.findViewById(R.id.de_backView);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		ImageView collect = (ImageView) this
				.findViewById(R.id.de_CollectButton);
		collect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		Intent it = getIntent();
		Bundle bundle = it.getBundleExtra("bund");
		map = (Map<String, Object>) bundle.getSerializable("map");

		getUserId();

		imaeView.setImageUrl((String) map.get("pic_url"));
		titleText.setText((CharSequence) map.get("title"));
		descriptText.setText((CharSequence) map.get("description"));

		initData(0, 3);
		pulltorefreshScrollView
				.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

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
						if (lastIndex != index) {
							initData(index * 3, 3);
							lastIndex = index;
						} else {
							Toast.makeText(MeiShiWuDetailActivity.this, "沒有更多",
									0).show();
							Message msg = new Message();
							msg.obj = null;
							msg.what = 1;
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

	private void executeReloadData() {
		// TODO Auto-generated method stub
		index = 0;
		lastIndex = -1;
		dataList.clear();
		layout.removeViews(1, layout.getChildCount() - 1);
		initData(0, 3);
	}

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
						msg.what = 1;
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
	private List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				list = Utils.Json((String) msg.obj, "storeList", new String[] {
						"pro_name", "is_collect", "detail_pic_url",
						"pro_price", "pro_desc", "store_id", "pro_url",
						"pic_url" });
				if (list.size() > 0) {
					index++;

					for (int i = 0; i < list.size(); i++) {
						dataList.add(list.get(i));
						View view = LayoutInflater.from(
								MeiShiWuDetailActivity.this).inflate(
								R.layout.activity_mei_shi_shu, null);
						TextView shapTitle = (TextView) view
								.findViewById(R.id.activity_mei_shi_shu_title);
						LinearLayout layout2 = (LinearLayout) view
								.findViewById(R.id.activity_mei_shi_shu_layout);
						TextView descriptText = (TextView) view
								.findViewById(R.id.activity_mei_shi_shu_descript);
						TextView moneyText = (TextView) view
								.findViewById(R.id.activity_mei_shi_shu_money);
						ImageView shouchang = (ImageView) view
								.findViewById(R.id.activity_mei_shi_shu_shouchang);
						RelativeLayout shouchangLayout = (RelativeLayout) view
								.findViewById(R.id.shouchanglayout);
						RelativeLayout xiangqingLayot = (RelativeLayout) view
								.findViewById(R.id.descripto);
						shouchang.setTag((index - 1) * 3 + i);
						shouchangLayout.setTag((index - 1) * 3 + i);
						xiangqingLayot.setTag((index - 1) * 3 + i);
						shouchangLayout
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										if ("".equals(user_id)) {
											Toast.makeText(
													MeiShiWuDetailActivity.this,
													"请登录先！|ω・）",
													Toast.LENGTH_SHORT).show();
										} else {
											postChouChang((Integer) v.getTag());
										}
									}
								});
						xiangqingLayot
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										Intent it = new Intent(
												MeiShiWuDetailActivity.this,
												Activity_ShowGoods.class);
										Bundle bundle = new Bundle();
										bundle.putSerializable("map",
												(Serializable) dataList
														.get((Integer) v
																.getTag()));
										it.putExtra("bund", bundle);
										MeiShiWuDetailActivity.this
												.startActivity(it);
									}
								});
						shapTitle.setText((CharSequence) list.get(i).get(
								"pro_name"));
						String urls = (String) list.get(i).get("pic_url");
						String arr[] = urls.split("\\|");
						for (int j = 0; j < arr.length; j++) {
							SmartImageView smartImageView = new SmartImageView(
									MeiShiWuDetailActivity.this);
							smartImageView
									.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT,
											LayoutParams.WRAP_CONTENT));
							smartImageView.setScaleType(ScaleType.FIT_XY);
							smartImageView.setImageUrl(arr[j]);
							layout2.addView(smartImageView);
						}

						descriptText.setText((CharSequence) list.get(i).get(
								"pro_desc"));
						moneyText.setText(" ￥" + list.get(i).get("pro_price"));
						if (!("0"
								.equals((String) list.get(i).get("is_collect")))) {
							// Drawable drawable = MeiShiWuDetailActivity.this
							// .getResources().getDrawable(
							// R.drawable.afterheart);
							// moneyText.setCompoundDrawablesWithIntrinsicBounds(
							// drawable, null, null, null);
							shouchang.setImageResource(R.drawable.afterheart);
						}
						layout.addView(view);
					}
				}

				pulltorefreshScrollView.onRefreshComplete();

			} else if (msg.what == 2) {
				String result = (String) msg.obj;
				Toast.makeText(MeiShiWuDetailActivity.this, result, 0).show();
				if ("添加商品收藏成功".equals(result)) {
					View view = layout.getChildAt(msg.arg1);
					Map<String, Object> map = dataList.remove(msg.arg1 - 1);
					map.remove("is_collect");
					map.put("is_collect", "1");
					dataList.add(msg.arg1 - 1, map);
					ImageView shouchang = (ImageView) view
							.findViewById(R.id.activity_mei_shi_shu_shouchang);
					shouchang.setImageResource(R.drawable.afterheart);
				}
			}
		}
	};

	private void postChouChang(final int index) {
		new Thread(new Runnable() {
			String url = "http://1.zmj520.sinaapp.com/servlet/DealWithStoreCollectNums";

			@Override
			public void run() {

				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost post = new HttpPost(url);
					List<NameValuePair> parm = new ArrayList<NameValuePair>();
					// String user_id;
					// if (ZmjConstant.preference != null) {
					// user_id = ZmjConstant.preference.getString("user_id",
					// "zmj520");
					// } else {
					//
					// user_id = "";
					// }
					parm.add(new BasicNameValuePair("user_id", user_id));
					parm.add(new BasicNameValuePair("store_id",
							(String) dataList.get(index).get("store_id")));
					post.setEntity(new UrlEncodedFormEntity(parm));
					HttpResponse respone = httpClient.execute(post);
					if (respone.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(respone
								.getEntity());
						Message msg = new Message();
						msg.obj = result;
						msg.arg1 = index + 1;
						msg.what = 2;
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
	// @Override
	// protected void onRestart() {
	// // TODO Auto-generated method stub
	// super.onRestart();
	// initData(0, index * 3);
	// }

}
