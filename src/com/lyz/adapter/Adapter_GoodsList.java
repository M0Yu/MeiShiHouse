package com.lyz.adapter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.example.activity.MeiShiWuDetailActivity;
import com.example.classfy.R;
import com.example.override.RoundRectImageView;
import com.loopj.android.image.SmartImageView;
import com.lyz.activity.Activity_DailySpecial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter_GoodsList extends BaseAdapter {

	private Context mcontext;
	private LayoutInflater mlayoutInflater;
	private List<Map<String, Object>> mdata;

	public Adapter_GoodsList(Context context, List<Map<String, Object>> data) {
		mcontext = context;
		mlayoutInflater = LayoutInflater.from(context);
		mdata = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mdata.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mdata.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Map<String, Object> map = mdata.get(position);
		viewHolder mviewHolder = null;
		if (convertView == null) {
			mviewHolder = new viewHolder();
			convertView = mlayoutInflater.inflate(R.layout.cell_goodslist,
					null, false);
		} else {
			mviewHolder = (viewHolder) convertView.getTag();
		}

		mviewHolder.pic = (RoundRectImageView) convertView
				.findViewById(R.id.homepage_imageList);
		mviewHolder.favView = (ImageView) convertView
				.findViewById(R.id.favView);
		mviewHolder.favNum = (TextView) convertView.findViewById(R.id.favNum);

		mviewHolder.pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mcontext,
						MeiShiWuDetailActivity.class);
				Bundle bund = new Bundle();
				bund.putSerializable("map", (Serializable) mdata.get(position));
				intent.putExtra("bund", bund);
				mcontext.startActivity(intent);
			}
		});
		mviewHolder.pic
				.setImageUrl((String) mdata.get(position).get("pic_url"));
		;
		mviewHolder.favNum.setText((String) mdata.get(position).get(
				"collect_nums"));
		if ("0".equals((String) mdata.get(position).get("is_collect"))) {
			mviewHolder.favView.setImageResource(R.drawable.beforeheart);
		} else {
			mviewHolder.favView.setImageResource(R.drawable.afterheart);
		}

		convertView.setTag(mviewHolder);
		return convertView;
	}

	class viewHolder {
		private RoundRectImageView pic;
		private ImageView favView;
		private TextView favNum;
	}
}
