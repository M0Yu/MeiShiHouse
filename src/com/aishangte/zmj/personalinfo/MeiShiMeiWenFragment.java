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

import com.aishangte.zmj.meishiwu.utils.ZmjAnalysisTools;
import com.aishangte.zmj.meishiwu.utils.ZmjConstant;
import com.example.activity.WebActivity;
import com.example.classfy.R;
import com.example.override.RoundRectImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MeiShiMeiWenFragment extends Fragment {

	private View rootView;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ScrollView mScrollView;
	// 可上拉刷新 下拉加载的scrollview 下的一个子布局。
	private LinearLayout colOne;
	// 子布局所应该适配的数据
	private ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> dataSumList = new ArrayList<HashMap<String, String>>();
	// 暂时为 zmj520
	private String User_id;
	// min = 0 max = 4 根据显示情况 来改
	// max 表示获得的数据条数 初始化将其置为 0,4
	private int min = 0;
	private int max = 4;
	private int currentPage = 1;
	private int pageSize = 4;
	private ArrayList<FrameLayout> framelayoutArrayList = new ArrayList<FrameLayout>();
	private ArrayList<View> viewArrayList = new ArrayList<View>();
	// flag数据 用于表示当前的view点击之后执行什么操作，
	// 0 点击view执行删除操作,1 点击view执行跳转操作
	private HashMap<FrameLayout, Integer> flag = new HashMap<FrameLayout, Integer>();

	private PersonalInfoActivity perInfAc;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		perInfAc = (PersonalInfoActivity) activity;
		System.out
				.println("MeiShiMeiWenFragment==========================被加载=========");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(
				R.layout.zmj_personal_meishimeiwen_pulltorefresh_scrollview,
				null);
		initView();
		initData();
		return rootView;

	}

	public void initView() {

		mPullRefreshScrollView = (PullToRefreshScrollView) rootView
				.findViewById(R.id.meishimeiwen_pull_refresh_scrollview);
		colOne = (LinearLayout) rootView
				.findViewById(R.id.meishimeiwenColOneLinelayout);
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						caculateMinMax();
						new GetMeiShiMeiWenCollectionDataTask()
								.execute(
										ZmjConstant.getPersonalMeiShiMeiWenCollectionUrl,
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

	public void initData() {

		if (ZmjConstant.preference != null) {
			User_id = ZmjConstant.preference.getString("user_id", "");
		} else {

			User_id = "";
		}
		new GetMeiShiMeiWenCollectionDataTask().execute(
				ZmjConstant.getPersonalMeiShiMeiWenCollectionUrl, User_id, min
						+ "", max + "");

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
					R.layout.zmj_meishimeiwen_cell, null);

			final int ssss = i;
			view.setLayoutParams(params);
			final FrameLayout closeViewFrameLayout = (FrameLayout) view
					.findViewById(R.id.meiShimeiWen_closeViewFrameLayout);
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
			// ZmjConstant.MeiShiMeiWenTotalViewShowNumbers = currentPage
			// * pageSize;
			// } else {
			//
			// ZmjConstant.MeiShiMeiWenTotalViewShowNumbers = dataList.size();
			// }

			// perInfAc.setOnDeleteMeiShiMeiWenCollectionListener(new
			// OnDeleteMeiShiMeiWenCollectionListener() {
			//
			// @Override
			// public void OnIsReadyToDeleteMeiShiMeiWenCollection() {
			// // TODO Auto-generated method stub
			// for (FrameLayout a : framelayoutArrayList) {
			// a.setVisibility(View.VISIBLE);
			//
			// // 第一个fragment正在执行 delete操作，button状态 已经改变
			// // 如果此时执行 滑动或点击到其他fragment 则 button的图片要改变
			// ZmjConstant.whichFragmentIsDoingDeleteOperation = 2;
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
			// ZmjConstant.MeiShiMeiWenAlreadyShowFrameLayoutNumbersofView =
			// framelayoutArrayList
			// .size();
			//
			// }
			//
			// @Override
			// public void OnCompletedDeleteMeiShiMeiWenCollection() {
			// // TODO Auto-generated method stub
			// // for (FrameLayout a : framelayoutArrayList) {
			// // a.setVisibility(View.GONE);
			// //
			// // flag.put(a, 1);
			// // }
			//
			// ZmjConstant.MeiShiMeiWenAlreadyShowFrameLayoutNumbersofView = 0;
			// ZmjConstant.MeiShiMeiWenTotalViewShowNumbers = 4;
			// // 此时要重新适配数据 原有的数据 清空 以免 发生判断异常
			// framelayoutArrayList.clear();
			// flag.clear();
			// dataList.clear();
			// currentPage = 1;
			// colOne.removeAllViews();
			// // 调用此接口 说明删除完成 ，那么原来的view 要重新 排列位置
			// // 每个view framelayout的tag不同了 对 点击完成什么操作 有英雄，
			// min = 0;
			// max = 4;
			// new GetMeiShiMeiWenCollectionDataTask().execute(
			// ZmjConstant.getPersonalMeiShiMeiWenCollectionUrl,
			// User_id, min + "", max + "");
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
						// "点击我跳转到美食美文列表"
						// + dataList.get(ssss).get(
						// "store_list_id") + " tag="
						// + temp, Toast.LENGTH_SHORT).show();

						Intent it = new Intent();
						it.setClass(getActivity(), WebActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("map",
								dataSumList.get((Integer) v.getTag() - 1));
						it.putExtra("bund", bundle);
						getActivity().startActivity(it);
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
						// + dataList.get(ssss).get(
						// "store_list_id") + " tag="
						// + temp,
						//
						// Toast.LENGTH_SHORT).show();

						new DeleteCollectonTask(temp - 1)
								.execute(
										ZmjConstant.deletePersonalMeiShiMeiWenCollectionUrl,
										User_id,
										dataSumList.get(temp - 1).get(
												"store_list_id"));

					}

				}
			});

			RoundRectImageView picture = (RoundRectImageView) view
					.findViewById(R.id.MeiShiMeiWenRectImageViewPic);
			ImageView meiShiMeiWenCollectionDivider = (ImageView) view
					.findViewById(R.id.meishimeiwenCollectionDivider);
			meiShiMeiWenCollectionDivider
					.setBackgroundResource(R.drawable.shihe_collection_divider);
			map = dataList.get(i);
			picture.setImageUrl(map.get("pic_url"));

			addImage(view);
		}

		perInfAc.setOnDeleteMeiShiMeiWenCollectionListener(new OnDeleteMeiShiMeiWenCollectionListener() {

			@Override
			public void OnIsReadyToDeleteMeiShiMeiWenCollection() {
				// TODO Auto-generated method stub
				for (FrameLayout a : framelayoutArrayList) {
					a.setVisibility(View.VISIBLE);

					// 第一个fragment正在执行 delete操作，button状态 已经改变
					// 如果此时执行 滑动或点击到其他fragment 则 button的图片要改变
					ZmjConstant.whichFragmentIsDoingDeleteOperation = 2;

					// 当前的view在显示范围内,则flag = 0,点击执行删除
					// 如果当前的view不在范围内,点击了删除按钮,
					// 该view仍然为正常状态
					if ((Integer) a.getTag() <= currentPage * pageSize) {

						flag.put(a, 0);

					} else {

						flag.put(a, 1);
					}

				}

				ZmjConstant.MeiShiMeiWenAlreadyShowFrameLayoutNumbersofView = framelayoutArrayList
						.size();

			}

			@Override
			public void OnCompletedDeleteMeiShiMeiWenCollection() {
				// TODO Auto-generated method stub
				// for (FrameLayout a : framelayoutArrayList) {
				// a.setVisibility(View.GONE);
				//
				// flag.put(a, 1);
				// }

				ZmjConstant.MeiShiMeiWenAlreadyShowFrameLayoutNumbersofView = 0;
				ZmjConstant.MeiShiMeiWenTotalViewShowNumbers = 4;
				// 此时要重新适配数据 原有的数据 清空 以免 发生判断异常
				framelayoutArrayList.clear();
				viewArrayList.clear();
				flag.clear();
				dataList.clear();
				dataSumList.clear();
				currentPage = 1;
				colOne.removeAllViews();
				// 调用此接口 说明删除完成 ，那么原来的view 要重新 排列位置
				// 每个view framelayout的tag不同了 对 点击完成什么操作 有英雄，
				min = 0;
				max = 4;
				new GetMeiShiMeiWenCollectionDataTask().execute(
						ZmjConstant.getPersonalMeiShiMeiWenCollectionUrl,
						User_id, min + "", max + "");

			}
		});

		ZmjConstant.MeiShiMeiWenTotalViewShowNumbers = dataSumList.size();
	}

	private void addImage(View view) {
		colOne.addView(view);
	}

	private class DeleteCollectonTask extends AsyncTask<String, String, String> {

		int viewPostionInLineLayout;

		public DeleteCollectonTask(int a) {

			viewPostionInLineLayout = a;

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
				paramList
						.add(new BasicNameValuePair("store_list_id", params[2]));

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

				colOne.removeView(viewArrayList.get(viewPostionInLineLayout));
				ZmjConstant.MeiShiMeiWenAlreadyShowFrameLayoutNumbersofView -= 1;
				ZmjConstant.MeiShiMeiWenTotalViewShowNumbers -= 1;
				flag.remove(framelayoutArrayList.get(viewPostionInLineLayout));
			}

		}

	}

	// 异步线程 这里获取数据
	private class GetMeiShiMeiWenCollectionDataTask extends
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
			dataList = zmjAnalysisTools
					.getPersonalMeiShiMeiWenCollectionDataList();
			// 异步任务完成后 ，适配数据
			setData();
			mPullRefreshScrollView.onRefreshComplete();
			super.onPostExecute(result);
		}

	}
}
