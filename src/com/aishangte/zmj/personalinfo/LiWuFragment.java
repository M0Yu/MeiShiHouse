package com.aishangte.zmj.personalinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.aishangte.zmj.meishiwu.utils.ZmjAnalysisTools;
import com.aishangte.zmj.meishiwu.utils.ZmjConstant;
import com.example.classfy.R;
import com.example.override.RoundRectImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lyz.activity.Activity_ShowGoods;

import android.app.Activity;
import android.content.Intent;
import android.graphics.SumPathEffect;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class LiWuFragment extends Fragment {

	private View rootView;
	private PullToRefreshScrollView mPullRefreshScrollView;
	// 可上拉刷新 下拉加载的scrollview 下的两个子布局。
	private LinearLayout colOne;
	private LinearLayout colTwo;
	// 子布局所应该适配的数据
	private ArrayList<HashMap<String, String>> dataSumList = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
	private ScrollView mScrollView;
	// 暂时为 zmj520
	private String User_id;
	// min = 0 max = 4 根据显示情况 来改
	// max 表示获得的数据条数 初始化将其置为 0,4
	private int min = 0;
	private int max = 4;
	// 每次显示数据条数
	private int pageSize = 4;
	// 当前是第几次刷新或者是第几个 page ;
	private int currentPage = 1;
	private ArrayList<FrameLayout> framelayoutArrayList = new ArrayList<FrameLayout>();
	private ArrayList<View> viewArrayList = new ArrayList<View>();

	private PersonalInfoActivity perInfAc;

	// flag数据 用于表示当前的view点击之后执行什么操作，
	// 0 点击view执行删除操作,1 点击view执行跳转操作
	private HashMap<FrameLayout, Integer> flag = new HashMap<FrameLayout, Integer>();

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		perInfAc = (PersonalInfoActivity) activity;
		System.out
				.println("LiWuFragment==========================被加载=========");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(
				R.layout.zmj_personal_liwu_pulltorefresh_scrollview, null);
		initView();
		initData();
		return rootView;
	}

	public void initView() {

		mPullRefreshScrollView = (PullToRefreshScrollView) rootView
				.findViewById(R.id.liwu_pull_refresh_scrollview);
		colOne = (LinearLayout) rootView.findViewById(R.id.colOne);
		colTwo = (LinearLayout) rootView.findViewById(R.id.colTwo);

		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						caculateMinMax();
						new GetLiWuCollectionDataTask().execute(
								ZmjConstant.getPersonalLiWuCollectionUrl,
								User_id, min + "", max + "");

					}

				});

		mScrollView = mPullRefreshScrollView.getRefreshableView();
	}

	public void caculateMinMax() {
		currentPage++;
		min = (currentPage - 1) * pageSize;
		max = pageSize;

	}

	private void initData() {
		// TODO Auto-generated method stub

		if (ZmjConstant.preference != null) {
			User_id = ZmjConstant.preference.getString("user_id", "");
		} else {
			User_id = "";
		}

		new GetLiWuCollectionDataTask().execute(
				ZmjConstant.getPersonalLiWuCollectionUrl, User_id, min + "",
				max + "");

	}

	private void setData() {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		View view = null;
		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < dataList.size(); i++) {
			dataSumList.add(dataList.get(i));

			view = LayoutInflater.from(getActivity()).inflate(
					R.layout.zmj_liwu_cell, null);

			final int ssss = i;
			view.setLayoutParams(params);
			final FrameLayout closeViewFrameLayout = (FrameLayout) view
					.findViewById(R.id.closeViewFrameLayout);
			// 为每一个framelayout设置标签 1,2,3.....
			closeViewFrameLayout.setTag((currentPage - 1) * pageSize + (i + 1));
			view.setTag((currentPage - 1) * pageSize + (i + 1));
			// int c = (Integer) closeViewFrameLayout.getTag();

			framelayoutArrayList.add(closeViewFrameLayout);
			viewArrayList.add(view);

			// 初始化 一开始 全部为正常状态
			flag.put(closeViewFrameLayout, 1);

			// if (dataList.size() == 4) {
			//
			// ZmjConstant.LiWuTotalViewShowNumbers = currentPage * pageSize;
			// } else {
			//
			// ZmjConstant.LiWuTotalViewShowNumbers = dataSumList.size();
			// }

			// perInfAc.setOnDeleteLiWuCollectionListener(new
			// OnDeleteLiWuCollectionListener() {
			//
			// @Override
			// public void OnIsReadyToDeleteLiWuCollection() {
			// // TODO Auto-generated method stub
			// for (FrameLayout a : framelayoutArrayList) {
			// a.setVisibility(View.VISIBLE);
			//
			// // 第一个fragment正在执行 delete操作，button状态 已经改变
			// // 如果此时执行 滑动或点击到其他fragment 则 button的图片要改变
			// ZmjConstant.whichFragmentIsDoingDeleteOperation = 0;
			//
			// // 当前的view在显示范围内,则flag = 0,点击执行删除
			// // 如果当前的view不在范围内,点击了删除按钮,
			// // 该view仍然为正常状态
			// if ((Integer) a.getTag() <= currentPage * pageSize) {
			//
			// flag.put(a, 0);
			//
			// } else {
			//
			// flag.put(a, 1);
			// }
			//
			// }
			//
			// ZmjConstant.LiWuAlreadyShowFrameLayoutNumbersofView =
			// framelayoutArrayList
			// .size();
			//
			// }
			//
			// @Override
			// public void OnCompletedDeleteLiWuCollection() {
			// // TODO Auto-generated method stub
			// // for (FrameLayout a : framelayoutArrayList) {
			// // a.setVisibility(View.GONE);
			// //
			// // flag.put(a, 1);
			// // }
			//
			// ZmjConstant.LiWuAlreadyShowFrameLayoutNumbersofView = 0;
			// ZmjConstant.LiWuTotalViewShowNumbers = 4;
			// // 此时要重新适配数据 原有的数据 清空 以免 发生判断异常
			// framelayoutArrayList.clear();
			// flag.clear();
			// dataList.clear();
			// dataSumList.clear();
			// currentPage = 1;
			// colOne.removeAllViews();
			// colTwo.removeAllViews();
			// // 调用此接口 说明删除完成 ，那么原来的view 要重新 排列位置
			// // 每个view framelayout的tag不同了 对 点击完成什么操作 有英雄，
			// min = 0;
			// max = 4;
			// new GetLiWuCollectionDataTask().execute(
			// ZmjConstant.getPersonalLiWuCollectionUrl, User_id,
			// min + "", max + "");
			//
			// }
			// });

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (flag.get(closeViewFrameLayout) == 1) {

						int temp = (Integer) closeViewFrameLayout.getTag();
						// 此处跳转到商品详情的activity
						// Toast.makeText(
						// getActivity(),
						// "点击我跳转到商品详情"
						// + dataList.get(ssss).get("store_id")
						// + " tag=" + temp, Toast.LENGTH_SHORT)
						// .show();
						Intent it = new Intent(getActivity(),
								Activity_ShowGoods.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("map",
								dataSumList.get((Integer) v.getTag() - 1));
						it.putExtra("bund", bundle);
						getActivity().startActivity(it);
						// Intent intent = new Intent();
						// HashMap<String, String> map = new HashMap<String,
						// String>();
						// map = dataList.get(ssss);
						// intent.putExtra("store_info", map);
						// intent.setClass(getActivity(), );
						// startActivity(intent);

					}
					if (flag.get(closeViewFrameLayout) == 0) {

						// 1 3 5 7 在第一列的Linelayout布局里面
						// 2 4 6 8在第二列的Linelayout布局里面
						int temp = (Integer) closeViewFrameLayout.getTag();
						// Toast.makeText(
						// getActivity(),
						// "点击我执行删除操作"
						// + dataList.get(ssss).get("store_id")
						// + " tag=" + temp,
						//
						// Toast.LENGTH_SHORT).show();
						if (temp % 2 == 0) {// 当前的view在第二列

							new DeleteCollectonTask(temp - 1, 2)
									.execute(
											ZmjConstant.deletePersonalLiWuCollectionUrl,
											User_id,
											dataSumList
													.get((Integer) closeViewFrameLayout
															.getTag() - 1).get(
															"store_id"));
							// 移除当前在 flag标记中的数据
							// flag.remove(closeViewFrameLayout);

						} else { // 当前的veiw在第一列

							new DeleteCollectonTask(temp - 1, 1)
									.execute(
											ZmjConstant.deletePersonalLiWuCollectionUrl,
											User_id,
											dataSumList
													.get((Integer) closeViewFrameLayout
															.getTag() - 1).get(
															"store_id"));

							// 移动当前在flag标记中的数据
							// flag.remove(closeViewFrameLayout);
						}

					}

				}
			});

			RoundRectImageView picture = (RoundRectImageView) view
					.findViewById(R.id.ds_goodsPic);
			TextView description = (TextView) view
					.findViewById(R.id.ds_goodsDes);
			TextView price = (TextView) view.findViewById(R.id.ds_goodsPrice);

			map = dataList.get(i);
			picture.setImageUrl(map.get("pic_url"));
			description.setText(map.get("pro_desc"));
			price.setText("¥" + map.get("pro_price"));

			addImage(view);
		}

		perInfAc.setOnDeleteLiWuCollectionListener(new OnDeleteLiWuCollectionListener() {

			@Override
			public void OnIsReadyToDeleteLiWuCollection() {
				// TODO Auto-generated method stub
				for (FrameLayout a : framelayoutArrayList) {
					a.setVisibility(View.VISIBLE);

					// 第一个fragment正在执行 delete操作，button状态 已经改变
					// 如果此时执行 滑动或点击到其他fragment 则 button的图片要改变
					ZmjConstant.whichFragmentIsDoingDeleteOperation = 0;

					// 当前的view在显示范围内,则flag = 0,点击执行删除
					// 如果当前的view不在范围内,点击了删除按钮,
					// 该view仍然为正常状态
					if ((Integer) a.getTag() <= currentPage * pageSize) {

						flag.put(a, 0);

					} else {

						flag.put(a, 1);
					}

				}

				ZmjConstant.LiWuAlreadyShowFrameLayoutNumbersofView = framelayoutArrayList
						.size();

			}

			@Override
			public void OnCompletedDeleteLiWuCollection() {
				// TODO Auto-generated method stub
				// for (FrameLayout a : framelayoutArrayList) {
				// a.setVisibility(View.GONE);
				//
				// flag.put(a, 1);
				// }

				ZmjConstant.LiWuAlreadyShowFrameLayoutNumbersofView = 0;
				ZmjConstant.LiWuTotalViewShowNumbers = 4;
				// 此时要重新适配数据 原有的数据 清空 以免 发生判断异常
				framelayoutArrayList.clear();
				flag.clear();
				dataList.clear();
				dataSumList.clear();
				viewArrayList.clear();
				currentPage = 1;
				colOne.removeAllViews();
				colTwo.removeAllViews();
				// 调用此接口 说明删除完成 ，那么原来的view 要重新 排列位置
				// 每个view framelayout的tag不同了 对 点击完成什么操作 有英雄，
				min = 0;
				max = 4;
				new GetLiWuCollectionDataTask().execute(
						ZmjConstant.getPersonalLiWuCollectionUrl, User_id, min
								+ "", max + "");

			}
		});

		ZmjConstant.LiWuTotalViewShowNumbers = dataSumList.size();
	}

	private void addImage(View view) {
		if (colOne.getChildCount() == colTwo.getChildCount()) {
			colOne.addView(view);
		} else {
			colTwo.addView(view);
		}
	}

	private class DeleteCollectonTask extends AsyncTask<String, String, String> {

		int viewPostionInLineLayout;
		int whichLineLayout;// =1 表示 colOne =2 表示 colTwo

		public DeleteCollectonTask(int a, int b) {

			viewPostionInLineLayout = a;
			whichLineLayout = b;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = "";
			try {

				HttpClient httpClient = new DefaultHttpClient();
				// params[0]:url
				HttpPost httpPost = new HttpPost(params[0]);
				// 设置参数，仿html表单提交
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				paramList.add(new BasicNameValuePair("user_id", params[1]));
				paramList.add(new BasicNameValuePair("store_id", params[2]));

				httpPost.setEntity(new UrlEncodedFormEntity(paramList,
						HTTP.UTF_8));
				// 发送HttpPost请求，并返回HttpResponse对象
				HttpResponse httpResponse = httpClient.execute(httpPost);
				// 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					// 获取返回结果
					result = EntityUtils.toString(httpResponse.getEntity());

				}

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return result;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result.equals("true")) {

				if (whichLineLayout == 1) {// 第一列 调用 remove
					// colOne.removeViewAt(viewPostionInLineLayout);
					colOne.removeView(viewArrayList
							.get(viewPostionInLineLayout));
					ZmjConstant.LiWuAlreadyShowFrameLayoutNumbersofView -= 1;
					ZmjConstant.LiWuTotalViewShowNumbers -= 1;
				}
				if (whichLineLayout == 2) {// 第二列调用 remove
					// colTwo.removeViewAt(viewPostionInLineLayout);
					colTwo.removeView(viewArrayList
							.get(viewPostionInLineLayout));
					ZmjConstant.LiWuAlreadyShowFrameLayoutNumbersofView -= 1;
					ZmjConstant.LiWuTotalViewShowNumbers -= 1;
				}
				flag.remove(framelayoutArrayList.get(viewPostionInLineLayout));
			}

		}

	}

	// 异步线程 这里获取数据
	private class GetLiWuCollectionDataTask extends
			AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			// Simulates a background job.
			String result = "";
			try {

				HttpClient httpClient = new DefaultHttpClient();
				// params[0]:url
				HttpPost httpPost = new HttpPost(params[0]);
				// 设置参数，仿html表单提交
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				paramList.add(new BasicNameValuePair("user_id", params[1]));
				paramList.add(new BasicNameValuePair("min", params[2]));
				paramList.add(new BasicNameValuePair("max", params[3]));

				httpPost.setEntity(new UrlEncodedFormEntity(paramList,
						HTTP.UTF_8));
				// 发送HttpPost请求，并返回HttpResponse对象
				HttpResponse httpResponse = httpClient.execute(httpPost);
				// 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					// 获取返回结果
					result = EntityUtils.toString(httpResponse.getEntity());

				}

			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// Do some stuff here

			// Call onRefreshComplete when the list has been refreshed.

			ZmjAnalysisTools zmjAnalysisTools = new ZmjAnalysisTools(result);
			dataList = zmjAnalysisTools.getPersonalLiWuCollectionDataList();
			// 异步任务完成后 ，适配数据
			setData();
			mPullRefreshScrollView.onRefreshComplete();
			super.onPostExecute(result);
		}

	}
}
