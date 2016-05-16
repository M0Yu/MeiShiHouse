package com.lyz.activity;

import com.example.activity.MainActivity;
import com.example.classfy.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class Activity_WelcomePage extends Activity {

	private ImageView welcome;
	private AnimationSet as_f;
	private AnimationSet as_s;
	private AnimationSet as_t;
	private AnimationSet as_fo;
	private Button enter;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcomepage);

		initView();
		animation1();
	}

	private void initView() {
		welcome = (ImageView) this.findViewById(R.id.welcomeImage);
		enter = (Button) this.findViewById(R.id.Enter);
		enter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				skip();
			}
		});
	}

	private void animation1() {
		welcome.setImageResource(R.drawable.welcome1);

		as_f = new AnimationSet(true);
		AlphaAnimation aa = new AlphaAnimation(1f, 0.01f);
		ScaleAnimation sa = new ScaleAnimation(1.0f, 1.05f, 1.0f, 1.05f,
				Animation.RELATIVE_TO_PARENT, 0.5f,
				Animation.RELATIVE_TO_PARENT, 0.5f);
		as_f.addAnimation(aa);
		as_f.addAnimation(sa);
		as_f.setDuration(2000);
		welcome.setAnimation(as_f);

		as_f.setAnimationListener(new myanimationListener());
	}

	@SuppressLint("NewApi")
	private void animation2() {
		welcome.setImageResource(R.drawable.welcome2);

		as_s = new AnimationSet(true);
		AlphaAnimation aaf = new AlphaAnimation(0.01f, 2f);
		ScaleAnimation sa = new ScaleAnimation(1.3f, 1.3f, 1f, 1f,
				Animation.RELATIVE_TO_PARENT, 0.5f,
				Animation.RELATIVE_TO_PARENT, 0.5f);
		TranslateAnimation ta = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.05f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		AlphaAnimation aa = new AlphaAnimation(2f, 0.01f);
		as_s.addAnimation(aaf);
		as_s.addAnimation(ta);
		as_s.addAnimation(sa);
		as_s.addAnimation(aa);

		as_s.setInterpolator(new AccelerateDecelerateInterpolator());
		as_s.setDuration(3000);
		welcome.setAnimation(as_s);

		as_s.setAnimationListener(new myanimationListener());
	}

	private void animation3() {
		// TODO Auto-generated method stub
		welcome.setImageResource(R.drawable.welcome3);

		as_t = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(1.1f, 1f, 1.1f, 1f,
				Animation.RELATIVE_TO_PARENT, 0.5f,
				Animation.RELATIVE_TO_PARENT, 0.5f);
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1f);
		as_t.addAnimation(sa);
		as_t.addAnimation(aa);
		as_t.setDuration(2000);
		welcome.setAnimation(as_t);

		as_t.setAnimationListener(new myanimationListener());
	}

	private void animation4() {
		as_fo = new AnimationSet(true);
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1f);
		as_fo.addAnimation(aa);
		as_fo.setDuration(1000);

		enter.setAnimation(as_fo);
	}

	class myanimationListener implements AnimationListener {

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			if (animation.equals(as_f)) {
				animation2();
			}
			if (animation.equals(as_s)) {
				animation3();
			}
			if (animation.equals(as_t)) {
				enter.setVisibility(View.VISIBLE);
				animation4();
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

	}

	private void skip() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(Activity_WelcomePage.this, MainActivity.class);
		startActivity(intent);
		Activity_WelcomePage.this.finish();
	}
}
