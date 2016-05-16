package com.example.activity;

import com.aishangte.zmj.personalinfo.PersonalInfoActivity;
import com.example.classfy.R;
import com.lyz.activity.Activity_FirstPage;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabWidget;

public class MainActivity extends ActivityGroup {

	private TabHost tabHost;
	private LinearLayout linearLayout;
	private FrameLayout frameLayout;
	private RadioGroup radioGroup;
	private InputMethodManager inputManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		tabHost = (TabHost) this.findViewById(R.id.tabhost);
		radioGroup = (RadioGroup) this
				.findViewById(R.id.activity_main_radiogroup);
		tabHost.setup(getLocalActivityManager());
		final TabWidget tabWidget = tabHost.getTabWidget();
		tabWidget.setStripEnabled(false);
		addTabIntent();
		tabHost.setCurrentTab(0);
		inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.activity_main_radio_first_page:
					tabHost.setCurrentTab(0);
					break;
				case R.id.activity_main_radio_second_page:
					tabHost.setCurrentTab(1);
					break;
				case R.id.activity_main_radio_thired_page:
					tabHost.setCurrentTab(2);
					break;
				case R.id.activity_main_radio_four_page:
					tabHost.setCurrentTab(3);
					break;
				}

			}
		});

	}

	private TabHost.TabSpec buildTabSpec(String tag, String m,
			final Intent content) {
		return this.tabHost.newTabSpec(tag).setIndicator(m).setContent(content);
	}

	private void addTabIntent() {
		this.tabHost.addTab(buildTabSpec("tab1", "0", new Intent(this,
				Activity_FirstPage.class)));
		this.tabHost.addTab(buildTabSpec("tab2", "1", new Intent(this,
				ClassifyMainActivity.class)));
		this.tabHost.addTab(buildTabSpec("tab3", "2", new Intent(this,
				MeiShiShuActivity.class)));
		this.tabHost.addTab(buildTabSpec("tab4", "3", new Intent(this,
				PersonalInfoActivity.class)));
	}

}
