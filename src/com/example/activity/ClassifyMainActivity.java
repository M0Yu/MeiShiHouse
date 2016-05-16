package com.example.activity;

import com.example.adapter.FirstActivityAdapter;
import com.example.classfy.R;
import com.example.classfy.R.id;
import com.example.classfy.R.layout;
import com.example.classfy.R.menu;
import com.example.override.ScllorTabView;

import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class ClassifyMainActivity extends ActionBarActivity {

	private long lastClickTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_first);
		initView();
		setAdapter();
		setListener();
	}

	private void setListener() {
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				scllorTabView.setCurrentNum(arg0);
				switch (arg0) {

				case 0:
					radioGroup.check(R.id.first);
					break;

				case 1:
					radioGroup.check(R.id.second);
					break;
				case 2:
					radioGroup.check(R.id.third);
					break;
				case 3:
					radioGroup.check(R.id.fourth);
					break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				scllorTabView.setOffset(arg0, arg1);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

//		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				switch (checkedId) {
//				case R.id.first:
//					scllorTabView.setOffset(0, 0);
//					pager.setCurrentItem(0);
//					break;
//
//				case R.id.second:
//					scllorTabView.setOffset(1, 0);
//					pager.setCurrentItem(1);
//					break;
//				case R.id.third:
//					scllorTabView.setOffset(2, 0);
//					pager.setCurrentItem(2);
//					break;
//				case R.id.fourth:
//					scllorTabView.setOffset(3, 0);
//					pager.setCurrentItem(3);
//					break;
//				}
//
//			}
//		});

	}

	private void setAdapter() {

		FirstActivityAdapter adapter = new FirstActivityAdapter(
				getSupportFragmentManager());
		pager.setAdapter(adapter);

	}

	private ScllorTabView scllorTabView;
	private ViewPager pager;
	private RadioGroup radioGroup;

	private void initView() {
		// TODO Auto-generated method stub
		scllorTabView = (ScllorTabView) this.findViewById(R.id.scllorTabView);
		scllorTabView.setCurrentNum(0);
		scllorTabView.setTabNum(4);
		scllorTabView.setSelectedColor(Color.rgb(255, 126, 102), Color.GRAY);
		pager = (ViewPager) this.findViewById(R.id.viewpager);
		radioGroup = (RadioGroup) this.findViewById(R.id.radiogroup);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getEventTime() - lastClickTime > 2000) {
				Toast.makeText(ClassifyMainActivity.this, "再次点击退出",
						Toast.LENGTH_SHORT).show();
			} else {
				finish();
				return true;
			}
			lastClickTime = event.getEventTime();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
