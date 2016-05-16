package com.aishangte.zmj.meishiwu.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class ZmjPictureTools {
	/** 图片名字 */
	private static final String PICTURE_NAME = "userIcon.jpg";
	
	public  static void writeImageFromAssetsToSDFile(Context context, String fileName) {

		// 获取应用的包名
		String packageName = context.getPackageName();

		File sdcardDir = Environment.getExternalStorageDirectory();
		// 得到一个路径，内容是sdcard的文件夹路径和名字
		String path = sdcardDir.getPath() +"/" +packageName;
		File file = new File(path);

		Bitmap image = null;
		if (!file.exists()) {
			file.mkdirs();
		}
		// 获取assets下的资源
		AssetManager am = context.getAssets();
		try {
			// 图片放在img文件夹下
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			FileOutputStream out = new FileOutputStream(path + "/" + fileName);
			// 这个方法非常赞
			image.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	@SuppressLint("SdCardPath")
	public static void shareMsg(Context context,String activityTitle, String msgTitle, String msgText,
			String imgPath) {

		Intent intent = new Intent(Intent.ACTION_SEND);
		if (imgPath == null || imgPath.equals("")) {
			intent.setType("text/plain"); // 纯文本
		} else {
			File f = new File(imgPath);
			if (f != null && f.exists() && f.isFile()) {
				intent.setType("image/*");
				Uri u = Uri.fromFile(f);
				intent.putExtra(Intent.EXTRA_STREAM, u);
				
			}
		}
		
		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, activityTitle));
	}
	
	
	public static String getPackageName(Context context){
		
		return context.getPackageName();

	}
	
	public static String getApplicationAbsolutePath(String packageName){
		
		File sdcardDir = Environment.getExternalStorageDirectory();
		// 得到一个路径，内容是sdcard的文件夹路径和名字
		String path = sdcardDir.getPath() + "/" + packageName;
		return path;
	}
	
	
	// 初始化照片保存地址
	public static void initImageViewSaveAddress(Context context){
		

		String picturePath = null;
	if (Environment.getExternalStorageState().equals(
			Environment.MEDIA_MOUNTED)) {
		String thumPicture = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/"
				+ getPackageName(context)
				+ "/download";
		File pictureParent = new File(thumPicture);
		File pictureFile = new File(pictureParent, PICTURE_NAME);
		if (!pictureParent.exists()) {
			pictureParent.mkdirs();
		}
		try {
			if (!pictureFile.exists()) {
				pictureFile.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		picturePath = pictureFile.getAbsolutePath();

		ZmjConstant.imagePath = picturePath;

		Log.e("picturePath ==>>", picturePath);
	} else {
		Log.e("change user icon ==>>", "there is not sdcard!");
	}
	}
	
	// 将 第三方返回的   url对象的用户头像写入  已经初始化的图片的路径
	public static void writeUserImageToSDPath(String imageUrl){
		
		try {
			URL picUrl = new URL(imageUrl);
			Bitmap userIcon = BitmapFactory.decodeStream(picUrl
					.openStream());
			FileOutputStream b = null;
			try {
				b = new FileOutputStream(ZmjConstant.imagePath);
				userIcon.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 图片压缩
		public static Bitmap compressImageFromFile(String srcPath) {
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inJustDecodeBounds = true;// 只读边,不读内容
			Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			float hh = 800f;//
			float ww = 480f;//
			int be = 1;
			if (w > h && w > ww) {
				be = (int) (newOpts.outWidth / ww);
			} else if (w < h && h > hh) {
				be = (int) (newOpts.outHeight / hh);
			}
			if (be <= 0)
				be = 1;
			newOpts.inSampleSize = be;// 设置采样率

			newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
			newOpts.inPurgeable = true;// 同时设置才会有效
			newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

			bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
			// return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
			// 其实是无效的,大家尽管尝试
			return bitmap;
		}

	

}
