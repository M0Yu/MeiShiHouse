package com.aishangte.zmj.personalinfo;

public interface OnDeleteMeiShiMeiWenCollectionListener {

	//点击 删除Collection的TextView回调此接口，让当前显示的 fragment的子view都显示
	// 出隐藏的 framelayout
	public void OnIsReadyToDeleteMeiShiMeiWenCollection();
	//点击完成Collection的TextView回调此接口，让当前显示的fragment的子view都隐藏
	//显示带叉的framelayout
	public void OnCompletedDeleteMeiShiMeiWenCollection();
}
