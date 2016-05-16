package com.lyz.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.classfy.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Adapter_GoodsEvaList extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private List<?> mData;
	private int mResources;

	public Adapter_GoodsEvaList(Context context, List<?> data, int resource) {
		super();
		// TODO Auto-generated constructor stub
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mData = data;
		mResources = resource;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		viewHolder mViewHolder = null;
		Map<String, Object> map = (Map<String, Object>) mData.get(position);
		if (convertView == null) {
			mViewHolder = new viewHolder();
			convertView = mLayoutInflater.inflate(mResources, null);
			convertView.setPadding(20, 10, 20, 10);
		} else {
			mViewHolder = (viewHolder) convertView.getTag();
		}
		mViewHolder.sg_evaIcon = (SmartImageView) convertView
				.findViewById(R.id.sg_evaIcon);
		mViewHolder.sg_evaIcon.setImageUrl((String) map.get("pic_url"));

		mViewHolder.sg_evaUserName = (TextView) convertView
				.findViewById(R.id.sg_evaUserName);
		mViewHolder.sg_evaUserName.setText((String) map.get("user_name"));

		mViewHolder.sg_evaTime = (TextView) convertView
				.findViewById(R.id.sg_evaTime);
		mViewHolder.sg_evaTime.setText(((String) map.get("comments_time"))
				.substring(0, 16));

		mViewHolder.sg_evaDes = (TextView) convertView
				.findViewById(R.id.sg_evaDes);
		mViewHolder.sg_evaDes.setText((String) map.get("content"));

		convertView.setTag(mViewHolder);
		return convertView;
	}

	// private String caculateTime(String time){
	// String marginTime = null;
	// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	// String curTime = df.format(new Date());
	//
	// return marginTime;
	// }

	class viewHolder {
		SmartImageView sg_evaIcon;
		TextView sg_evaUserName;
		TextView sg_evaTime;
		TextView sg_evaDes;
	}

}
