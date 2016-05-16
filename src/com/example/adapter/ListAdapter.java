package com.example.adapter;

import java.util.List;
import java.util.Map;

import com.example.classfy.R;
import com.example.classfy.R.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, Object>> dataList;
	private int layout;
	private String[] dataName;
	private int[] dataId;

	public ListAdapter(Context context, List<Map<String, Object>> dataList,
			int layout, String[] dataName, int[] dataId) {
		super();
		this.context = context;
		this.dataList = dataList;
		this.layout = layout;
		this.dataName = dataName;
		this.dataId = dataId;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(layout, null);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView.findViewById(dataId[0]);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// Resources res = context.getResources();
		Drawable drawable = context.getResources().getDrawable(
				(Integer) dataList.get(position).get(dataName[0]));
		// drawable.setBounds(0, 0, drawable.getMinimumWidth(),
		// drawable.getMinimumHeight());
		// holder.textView.setCompoundDrawables(drawable, null, null, null);
		holder.textView.setText((String) dataList.get(position)
				.get(dataName[1]));
		Drawable drawable2 = context.getResources().getDrawable(
				R.drawable.arrow_right);
		// drawable2.setBounds(0, 0, drawable.getMinimumWidth(),
		// drawable.getMinimumHeight());
		holder.textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null,
				drawable2, null);
		return convertView;
	}

	private static class ViewHolder {
		TextView textView;
	}
}
