package com.lyz.adapter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.example.activity.MeiShiActivity;
import com.example.activity.MeiShiWuDetailActivity;
import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class Adapter_fpViewPager extends PagerAdapter {

	private Context mContext;
	private List<Map<String, Object>> mdata;
	private SmartImageView[] imgViews;

	public Adapter_fpViewPager(Context context, List<Map<String, Object>> data) {
		super();
		// TODO Auto-generated constructor stub
		mContext = context;
		this.mdata = data;

		imgViews = new SmartImageView[mdata.size()];
		for (int i = 0; i < mdata.size(); i++) {
			SmartImageView pic = new SmartImageView(mContext);
			pic.setScaleType(ScaleType.FIT_XY);
			pic.setImageUrl((String) mdata.get(i).get("pic_url"));
			imgViews[i] = pic;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mdata.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(imgViews[position]);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		// TODO Auto-generated method stub
		container.addView(imgViews[position], 0);
		imgViews[position].setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, MeiShiActivity.class);
				Bundle bund = new Bundle();
				bund.putString("name", "特别推荐");
				bund.putString("itemlist_id",
						(String) mdata.get(position).get("itemlist_id"));
				intent.putExtra("bundle", bund);
				mContext.startActivity(intent);
			}
		});
		return imgViews[position];
	}

}
