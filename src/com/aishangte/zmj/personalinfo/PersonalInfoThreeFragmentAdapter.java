package com.aishangte.zmj.personalinfo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class PersonalInfoThreeFragmentAdapter extends FragmentPagerAdapter {

	private int pageCount = 3;

	public PersonalInfoThreeFragmentAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
		
		
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			LiWuFragment fragment = new LiWuFragment();
//			fragment.smoothToTop();
			return fragment;
		case 1:
			ShiHeFragment fragment2 = new ShiHeFragment();
//			fragment2.smoothToTop();
			return fragment2;
		case 2:
			MeiShiMeiWenFragment fragment3 = new MeiShiMeiWenFragment();
//			fragment3.smoothToTop();
			return fragment3;

		}
		return null;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pageCount;
	}

}
