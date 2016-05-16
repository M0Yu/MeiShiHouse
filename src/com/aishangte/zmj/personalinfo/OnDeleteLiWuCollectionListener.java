package com.aishangte.zmj.personalinfo;

public interface OnDeleteLiWuCollectionListener {

	//点击 删除Collection的TextView回调此接口，让当前显示的 fragment的子view都显示
	// 出隐藏的 framelayout
	public void OnIsReadyToDeleteLiWuCollection();
	//点击完成Collection的TextView回调此接口，让当前显示的fragment的子view都隐藏
	//显示带叉的framelayout
	public void OnCompletedDeleteLiWuCollection();
	
}

