package com.example.adapter;

import com.example.fragment.FirstFragment;
import com.example.fragment.FourFragment;
import com.example.fragment.SecondFragment;
import com.example.fragment.ThirdFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FirstActivityAdapter extends FragmentPagerAdapter {

	private int pageCount = 4;
	FragmentManager fm;
	public FirstActivityAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.fm = fm;
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			FirstFragment fragment = FirstFragment.newInstance();
//			fragment.smoothToTop();
			return fragment;
		case 1:
			SecondFragment fragment2 = SecondFragment.newInstance();
//			fragment2.smoothToTop();
			return fragment2;
		case 2:
			ThirdFragment fragment3 = ThirdFragment.newInstance();
//			fragment3.smoothToTop();
			return fragment3;
		case 3:
			FourFragment fragment4 = FourFragment.newInstance();
//			fragment4.smoothToTop();
			return fragment4;

		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pageCount;
	}

}
