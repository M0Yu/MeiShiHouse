package com.aishangte.zmj.meishiwu.utils;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ZmjAnalysisTools {

	private String analysisStr;
	public ZmjAnalysisTools(String str) {
		// TODO Auto-generated constructor stub
		analysisStr = str;
		
	}
	
	public ArrayList<HashMap<String, String>> getPersonalLiWuCollectionDataList(){
		ArrayList<HashMap<String, String>> pLWCDataList = new ArrayList<HashMap<String,String>>();
		try {
			JSONArray jsonLiWuList = new JSONObject(analysisStr).getJSONArray("liWuCollectionList");
			for (int i = 0; i < jsonLiWuList.length(); i++) {
				
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject jsonObject = (JSONObject) jsonLiWuList.opt(i);
				map.put("pro_name", jsonObject.getString("pro_name"));
				map.put("is_collect", jsonObject.getString("is_collect"));
				map.put("detail_pic_url", jsonObject.getString("detail_pic_url"));
				map.put("pro_price", jsonObject.getString("pro_price"));
				map.put("pro_desc", jsonObject.getString("pro_desc"));
				map.put("store_id", jsonObject.getString("store_id"));
				map.put("pro_url", jsonObject.getString("pro_url"));
				map.put("pic_url", jsonObject.getString("pic_url"));
				pLWCDataList.add(map);
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pLWCDataList;
	}
	
	
	public ArrayList<HashMap<String, String>> getPersonalShiHeCollectionDataList(){
		ArrayList<HashMap<String, String>> pShiHeCDataList = new ArrayList<HashMap<String,String>>();
		try {
			JSONArray jsonShiHeList = new JSONObject(analysisStr).getJSONArray("shiHeCollectionList");
			for (int i = 0; i < jsonShiHeList.length(); i++) {
				
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject jsonObject = (JSONObject) jsonShiHeList.opt(i);
				map.put("title", jsonObject.getString("title"));
				map.put("is_collect", jsonObject.getString("is_collect"));
				map.put("description", jsonObject.getString("description"));
				map.put("store_list_id", jsonObject.getString("store_list_id"));
				map.put("collect_nums", jsonObject.getString("collect_nums"));
				map.put("pic_url", jsonObject.getString("pic_url"));
				pShiHeCDataList.add(map);
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pShiHeCDataList;
	}
	
	public ArrayList<HashMap<String, String>> getPersonalMeiShiMeiWenCollectionDataList(){
		ArrayList<HashMap<String, String>> pMeiShiMeiWenDataList = new ArrayList<HashMap<String,String>>();
		try {
			JSONArray jsonMeiShiMeiWenList = new JSONObject(analysisStr).getJSONArray("meiShiMeiWenCollectionList");
			for (int i = 0; i < jsonMeiShiMeiWenList.length(); i++) {
				
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject jsonObject = (JSONObject) jsonMeiShiMeiWenList.opt(i);
				map.put("title", jsonObject.getString("title"));
				map.put("is_collect", jsonObject.getString("is_collect"));
				map.put("description", jsonObject.getString("description"));
				map.put("store_list_id", jsonObject.getString("store_list_id"));
				map.put("collect_nums", jsonObject.getString("collect_nums"));
				map.put("pic_url", jsonObject.getString("pic_url"));
				pMeiShiMeiWenDataList.add(map);
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pMeiShiMeiWenDataList;
	}
	
	
	public ArrayList<HashMap<String, String>> getVersionInfoDataList(){
		ArrayList<HashMap<String, String>> vsersionDataList = new ArrayList<HashMap<String,String>>();
		try {
			JSONArray jsonVersionist = new JSONObject(analysisStr).getJSONArray("versionList");
			for (int i = 0; i < jsonVersionist.length(); i++) {
				
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject jsonObject = (JSONObject) jsonVersionist.opt(i);
				map.put("version", jsonObject.getString("version"));
				map.put("info", jsonObject.getString("info"));
				map.put("up_time", jsonObject.getString("up_time"));
				map.put("url", jsonObject.getString("url"));
				vsersionDataList.add(map);
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return vsersionDataList;
	}
	
	
	
}
