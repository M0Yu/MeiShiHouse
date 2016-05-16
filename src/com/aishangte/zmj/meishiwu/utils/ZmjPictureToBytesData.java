package com.aishangte.zmj.meishiwu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ZmjPictureToBytesData {
	public static byte[] SetImageToByteArray(String fileName) {
		// 创建流文件读
		File pic_uri = new File(fileName);
		if (!(pic_uri.exists())) {
			return null;
		}
		byte[] image = null;
		try {
			FileInputStream inStream = new FileInputStream(pic_uri);
			int streamLength = (int) inStream.getChannel().size();
			image = new byte[streamLength];
			inStream.read(image, 0, streamLength);
			inStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}





}
