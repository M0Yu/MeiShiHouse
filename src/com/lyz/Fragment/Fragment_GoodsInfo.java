package com.lyz.Fragment;

import java.util.Map;

import com.example.classfy.R;
import com.lyz.adapter.Adapter_SimpleViewPager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Fragment_GoodsInfo extends Fragment implements
		OnPageChangeListener {

	private Map<String, Object> mdata;

	private ViewPager sg_goodsPic;
	private TextView sg_goodsName;
	private TextView sg_goodsPrice;
	private TextView sg_goodsDes;
	private LinearLayout sg_pointsGroup;

	private String[] images;
	private String pro_price;
	private String pro_desc;
	private String pro_name;
	private ImageView[] pointViews;

	private int lastPostion;

	public Fragment_GoodsInfo(Map<String, Object> mdata) {
		super();
		// TODO Auto-generated constructor stub
		this.mdata = mdata;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = (View) inflater.inflate(R.layout.fragment_goodsinfo, null);

		initView(view);
		getData();
		setData();

		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		sg_goodsPic = (ViewPager) view.findViewById(R.id.sg_goodsPicViewPager);
		sg_goodsName = (TextView) view.findViewById(R.id.sg_goodsNameText);
		sg_goodsPrice = (TextView) view.findViewById(R.id.sg_goodsPriceText);
		sg_goodsDes = (TextView) view.findViewById(R.id.sg_goodsDesText);
		sg_pointsGroup = (LinearLayout) view.findViewById(R.id.sg_pointsGroup);
	}

	private void getData() {
		// TODO Auto-generated method stub
		String detail_pic_url = (String) mdata.get("detail_pic_url");

		pro_name = (String) mdata.get("pro_name");
		pro_price = (String) mdata.get("pro_price");
		pro_desc = (String) mdata.get("pro_desc");
		images = detail_pic_url.split("\\|");
	}

	private void setData() {
		// TODO Auto-generated method stub

		sg_goodsName.setText(pro_name);
		sg_goodsPrice.setText("¥ " + pro_price);
		sg_goodsDes.setText(pro_desc);

		pointViews = new ImageView[images.length];
		for (int i = 0; i < pointViews.length; i++) {
			ImageView point = new ImageView(getActivity());
			point.setLayoutParams(new LayoutParams(10, 10));
			pointViews[i] = point;
			if (i == 0) {
				point.setBackgroundResource(R.drawable.point_checked);
			} else {
				point.setBackgroundResource(R.drawable.point_normal);
			}

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			params.leftMargin = 5;
			params.rightMargin = 5;

			sg_pointsGroup.addView(point, params);
		}
		sg_goodsPic.setAdapter(new Adapter_SimpleViewPager(getActivity(),
				images));
		sg_goodsPic.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int postion) {
		// TODO Auto-generated method stub
		changePoints(postion);
	}

	private void changePoints(int postion) {
		// TODO Auto-generated method stub
		for (int i = 0; i < pointViews.length; i++) {
			if (i == postion) {
				pointViews[i].setBackgroundResource(R.drawable.point_checked);
			} else {
				pointViews[i].setBackgroundResource(R.drawable.point_normal);
			}
		}
	}

}
