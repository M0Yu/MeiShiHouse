package com.aishangte.zmj.personalinfo;

import java.io.File;

import com.aishangte.zmj.meishiwu.utils.ZmjConstant;
import com.aishangte.zmj.meishiwu.utils.ZmjPictureTools;
import com.example.activity.ClassifyMainActivity;
import com.example.classfy.R;
import com.example.override.ScllorTabView;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class PersonalInfoActivity extends ActionBarActivity {

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zmj_personal_info);
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
					fragmentPosition = 0;
					radioGroup.check(R.id.first);

					if (ZmjConstant.whichFragmentIsDoingDeleteOperation == fragmentPosition) {
						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.wancheng);
					} else {

						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.delete_personalcollection_button_background);
					}
					break;

				case 1:
					fragmentPosition = 1;
					radioGroup.check(R.id.second);

					if (ZmjConstant.whichFragmentIsDoingDeleteOperation == fragmentPosition) {
						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.wancheng);
					} else {

						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.delete_personalcollection_button_background);
					}
					break;
				case 2:
					fragmentPosition = 2;
					radioGroup.check(R.id.third);

					if (ZmjConstant.whichFragmentIsDoingDeleteOperation == fragmentPosition) {
						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.wancheng);
					} else {

						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.delete_personalcollection_button_background);
					}
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

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.first:
					scllorTabView.setOffset(0, 0);
					pager.setCurrentItem(0);

					fragmentPosition = 0;

					if (ZmjConstant.whichFragmentIsDoingDeleteOperation == fragmentPosition) {
						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.wancheng);
					} else {

						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.delete_personalcollection_button_background);
					}
					break;

				case R.id.second:
					scllorTabView.setOffset(1, 0);
					pager.setCurrentItem(1);

					fragmentPosition = 1;

					if (ZmjConstant.whichFragmentIsDoingDeleteOperation == fragmentPosition) {
						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.wancheng);
					} else {

						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.delete_personalcollection_button_background);
					}
					break;
				case R.id.third:
					scllorTabView.setOffset(2, 0);
					pager.setCurrentItem(2);

					fragmentPosition = 2;

					if (ZmjConstant.whichFragmentIsDoingDeleteOperation == fragmentPosition) {
						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.wancheng);
					} else {

						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.delete_personalcollection_button_background);
					}
					break;

				}

			}
		});

		deletePersonalCollectButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("====================================");

				switch (fragmentPosition) {
				case 0:
					if (ZmjConstant.LiWuAlreadyShowFrameLayoutNumbersofView < ZmjConstant.LiWuTotalViewShowNumbers) {

						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.wancheng);
						deleteLiWuCollectionListener
								.OnIsReadyToDeleteLiWuCollection();
					} else {

						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.delete_personalcollection_button_background);
						ZmjConstant.whichFragmentIsDoingDeleteOperation = 100;
						deleteLiWuCollectionListener
								.OnCompletedDeleteLiWuCollection();
					}

					break;
				case 1:

					if (ZmjConstant.ShiHeAlreadyShowFrameLayoutNumbersofView < ZmjConstant.ShiHeTotalViewShowNumbers) {

						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.wancheng);
						deleteShiHeCollectionListener
								.OnIsReadyToDeleteShiHeCollection();
					} else {

						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.delete_personalcollection_button_background);
						ZmjConstant.whichFragmentIsDoingDeleteOperation = 100;
						deleteShiHeCollectionListener
								.OnCompletedDeleteShiHeCollection();
					}

					break;
				case 2:
					if (ZmjConstant.MeiShiMeiWenAlreadyShowFrameLayoutNumbersofView < ZmjConstant.MeiShiMeiWenTotalViewShowNumbers) {

						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.wancheng);
						deleteMeiShiMeiWenCollectionListener
								.OnIsReadyToDeleteMeiShiMeiWenCollection();
					} else {

						deletePersonalCollectButton
								.setBackgroundResource(R.drawable.delete_personalcollection_button_background);
						ZmjConstant.whichFragmentIsDoingDeleteOperation = 100;
						deleteMeiShiMeiWenCollectionListener
								.OnCompletedDeleteMeiShiMeiWenCollection();
					}
					break;

				}

			}
		});

		personalSettingButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(PersonalInfoActivity.this,
						PersonalSettingActivity.class);
				startActivity(intent);

			}
		});

		userHeadPictureImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (ZmjConstant.preference != null) {
					getPicture();
				} else {

					Intent intent = new Intent(PersonalInfoActivity.this,
							PlatfromForLogin.class);
					startActivity(intent);
				}
			}
		});

	}

	/** 打开相册，并截图 */
	private static final int INTENT_ACTION_PICTURE = 0;

	// /** 打开相机照相 */
	// private static final int INTENT_ACTION_CAREMA = 1;
	// /** 照相后，截图 */
	// private static final int INTENT_ACTION_CROP = 2;

	/** 从相册获取图片 */
	private void getPicture() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, INTENT_ACTION_PICTURE);
		// TODO
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == INTENT_ACTION_PICTURE
				&& resultCode == Activity.RESULT_OK && null != data) {
			Cursor c = PersonalInfoActivity.this.getContentResolver().query(
					data.getData(), null, null, null, null);
			c.moveToNext();
			String path = c.getString(c
					.getColumnIndex(MediaStore.MediaColumns.DATA));
			c.close();
			System.out.println("onActivityResult == " + path);
			if (new File(path).exists()) {
				System.out.println("onActivityResult == " + path + " == exist");
				ZmjConstant.userInfo.setUserIcon(path);
				ZmjConstant.editor.remove("userIconSDPath");
				ZmjConstant.editor.putString("userIconSDPath", path);

				userHeadPictureImageView.setImageBitmap(ZmjPictureTools
						.compressImageFromFile(path));
			}
		}
		// else if (requestCode == INTENT_ACTION_CAREMA
		// && resultCode == Activity.RESULT_OK) {
		//
		// ZmjConstant.userInfo.setUserIcon(ZmjConstant.imagePath);
		//
		//
		// userHeadPictureImageView.setImageDrawable(Drawable.createFromPath(ZmjConstant.imagePath));
		// } else if (requestCode == INTENT_ACTION_CROP
		// && resultCode == Activity.RESULT_OK && null != data) {
		//
		//
		// userHeadPictureImageView.setImageDrawable(Drawable.createFromPath(picturePath));
		// }
	}

	private void setAdapter() {
		PersonalInfoThreeFragmentAdapter adapter = new PersonalInfoThreeFragmentAdapter(
				getSupportFragmentManager());
		pager.setAdapter(adapter);

	}

	private ScllorTabView scllorTabView;
	private ViewPager pager;
	private RadioGroup radioGroup;
	private Button deletePersonalCollectButton;
	private Button personalSettingButton;
	private CircleImageView userHeadPictureImageView;
	private TextView userNameTextView;
	private OnDeleteLiWuCollectionListener deleteLiWuCollectionListener;
	private OnDeleteShiHeCollectionListener deleteShiHeCollectionListener;
	private OnDeleteMeiShiMeiWenCollectionListener deleteMeiShiMeiWenCollectionListener;

	private long lastClickTime = 0;
	// 默认情况下加载第一个fragment
	public int fragmentPosition;

	private void initView() {
		// TODO Auto-generated method stub

		Intent intent = getIntent();

		String user_id = intent.getStringExtra("user_id");
		String user_name = intent.getStringExtra("user_name");

		deletePersonalCollectButton = (Button) this
				.findViewById(R.id.deletePersonalCollectButton);
		personalSettingButton = (Button) this
				.findViewById(R.id.personalSettingButton);
		userHeadPictureImageView = (CircleImageView) this
				.findViewById(R.id.userHeadPictureImageView);

		userNameTextView = (TextView) this.findViewById(R.id.userNameTextView);
		scllorTabView = (ScllorTabView) this.findViewById(R.id.scllorTabView);
		scllorTabView.setCurrentNum(0);
		scllorTabView.setTabNum(3);
		scllorTabView.setSelectedColor(Color.rgb(255, 126, 102), Color.GRAY);
		pager = (ViewPager) this.findViewById(R.id.viewpager);
		radioGroup = (RadioGroup) this.findViewById(R.id.radiogroup);

		// 判断用户是否登录
		if (ZmjConstant.preference != null) {
			if (ZmjConstant.preference.getString("user_id", "") != ""
					&& null != ZmjConstant.preference.getString("user_id", "")) {

				userNameTextView.setText(user_name);
				Uri uri = Uri.fromFile(new File(ZmjConstant.preference
						.getString("userIconSDPath", "")));
				userHeadPictureImageView.setImageURI(uri);
			}
		}

	}

	public void setOnDeleteLiWuCollectionListener(
			OnDeleteLiWuCollectionListener l) {
		deleteLiWuCollectionListener = l;

	}

	public void setOnDeleteShiHeCollectionListener(
			OnDeleteShiHeCollectionListener l) {
		deleteShiHeCollectionListener = l;

	}

	public void setOnDeleteMeiShiMeiWenCollectionListener(
			OnDeleteMeiShiMeiWenCollectionListener l) {
		deleteMeiShiMeiWenCollectionListener = l;

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (event.getEventTime() - lastClickTime > 2000) {
				Toast.makeText(PersonalInfoActivity.this, "再次点击退出",
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
