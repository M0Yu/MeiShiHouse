package com.example.override;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.Utils.Utils;
import com.example.classfy.R;
import com.example.classfy.R.drawable;
import com.example.classfy.R.id;
import com.example.classfy.R.layout;
import com.loopj.android.image.SmartImage;
import com.lyz.activity.Activity_ShowGoods;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MyViewPager extends RelativeLayout {

	private Context context;

	private boolean isRuning = false;

	public MyViewPager(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView(context);

	}

	public MyViewPager(Context context) {
		super(context);
		initView(context);
	}

	private ViewPager viewPager;
	private LinearLayout point_group;
	private List<RoundRectImageView> imageList;

	private void initView(Context context) {
		RelativeLayout view = (RelativeLayout) View.inflate(context,
				R.layout.myviewpager, this);
		viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		point_group = (LinearLayout) view.findViewById(R.id.point_group);

		imageList = new ArrayList<RoundRectImageView>();
	}

	public void setData(final List<Map<String, Object>> data) {
		if (data == null) {
			throw new RuntimeException("传人数据为空");
		}
		// if (data.get(0) instanceof integer) {
		for (int i = 0; i < data.size(); i++) {
			RoundRectImageView image = new RoundRectImageView(context);
			image.setImageUrl((String) data.get(i).get("pic_url"));
			image.setTag(i);
			image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent it = new Intent(context, Activity_ShowGoods.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("map",
							(Serializable) data.get((Integer) v.getTag()));
					it.putExtra("bund", bundle);
					context.startActivity(it);
				}
			});
			imageList.add(image);

			ImageView point = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			params.rightMargin = 20;
			point.setLayoutParams(params);

			point.setBackgroundResource(R.drawable.viewpager_point_bg);
			if (i == 0) {
				point.setEnabled(true);
			} else {
				point.setEnabled(false);
			}
			point_group.addView(point);
		}

		setAdapter();
		viewPager.setCurrentItem(0);// Integer.MAX_VALUE / 2 -
									// (Integer.MAX_VALUE / 2 % imageList.size()

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				int position = arg0 % data.size();
				// viewPager.setCurrentItem(position, true);
				point_group.getChildAt(position).setEnabled(true);
				point_group.getChildAt(lastIndex).setEnabled(false);
				lastIndex = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private int lastIndex;

	private void setAdapter() {
		viewPager.setAdapter(new MyPagerAdapter());
		isRuning = true;
		handler.sendEmptyMessageDelayed(0, 2000);
	}

	private Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
			if (isRuning) {
				handler.sendEmptyMessageDelayed(0, 2000);
			}
		};
	};

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		/**
		 * ���ҳ�������
		 */
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			// System.out.println("instantiateItem  ::" + position);
			// ImageView iamgeView = imageList.get(position % imageList.size());
			container.addView(imageList.get(position % imageList.size()));

			// ����һ���͸�view��Ե�object
			return imageList.get(position % imageList.size());
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			if (view == object) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// System.out.println("destroyItem  ::" + position);

			container.removeView(imageList.get(position % imageList.size()));
			object = null;
		}

	}

	public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight()));
			final float roundPx = Utils.dip2px(context, 5);
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

			final Rect src = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());

			canvas.drawBitmap(bitmap, src, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}

}
