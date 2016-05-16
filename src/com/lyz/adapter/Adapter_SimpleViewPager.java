package com.lyz.adapter;

import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class Adapter_SimpleViewPager extends PagerAdapter {

	private Context mContext;
	private String[] images;
	private SmartImageView[] imgViews;

	public Adapter_SimpleViewPager(Context context, String[] images) {
		super();
		// TODO Auto-generated constructor stub
		mContext = context;
		this.images = images;

		imgViews = new SmartImageView[images.length];
		for (int i = 0; i < images.length; i++) {
			SmartImageView pic = new SmartImageView(mContext);
			pic.setScaleType(ScaleType.FIT_XY);
			pic.setImageUrl(images[i]);
			imgViews[i] = pic;
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.length;
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
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(imgViews[position], 0);
		return imgViews[position];
	}

}
