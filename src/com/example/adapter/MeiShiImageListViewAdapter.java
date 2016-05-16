package com.example.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.override.RoundRectImageView;

public class MeiShiImageListViewAdapter extends BaseAdapter {

	private Context context;
	private int layout;
	private List<Map<String, Object>> datalist;
	private String[] from;
	private int[] to;

	public MeiShiImageListViewAdapter(Context context, int layout,
			List<Map<String, Object>> datalist, String[] from, int[] to) {
		super();
		this.context = context;
		this.layout = layout;
		this.datalist = datalist;
		this.from = from;
		this.to = to;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datalist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datalist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(layout, null);
			viewHolder.image = (RoundRectImageView) convertView
					.findViewById(to[0]);
			// viewHolder.textView = (TextView) convertView.findViewById(to[1]);
			 convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// Resources res = context.getResources();
		// Bitmap bitmap = BitmapFactory.decodeResource(res, (Integer) datalist
		// .get(position).get(from[0]));
		// viewHolder.image.setImageBitmap(getRoundedCornerBitmap(bitmap));
		viewHolder.image.setImageUrl((String) datalist.get(position).get(
				from[0]));
		// if (!("0".equals(datalist.get(position).get(from[2])))) {
		// Drawable drawable = context.getResources().getDrawable(
		// R.drawable.afterheart);
		// viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(
		// drawable, null, null, null);
		// }
		//
		// viewHolder.textView.setText(" " +
		// datalist.get(position).get(from[1]));
		// viewHolder.textView.setTag(position);
		// viewHolder.textView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// postChouChang((int) v.getTag());
		// }
		// });
		return convertView;
	}

	class ViewHolder {
		RoundRectImageView image;
		// TextView textView;
	}

	// private void postChouChang(final int index) {
	// new Thread(new Runnable() {
	// String url =
	// "http://1.zmj520.sinaapp.com/servlet/DealWithThemeCollectNums";
	//
	// @Override
	// public void run() {
	//
	// try {
	// HttpClient httpClient = new DefaultHttpClient();
	// HttpPost post = new HttpPost(url);
	// List<NameValuePair> parm = new ArrayList<>();
	// parm.add(new BasicNameValuePair("user_id", "ff"));
	// parm.add(new BasicNameValuePair("store_list_id",
	// (String) datalist.get(index).get("store_list_id")));
	// post.setEntity(new UrlEncodedFormEntity(parm));
	// HttpResponse respone = httpClient.execute(post);
	// if (respone.getStatusLine().getStatusCode() == 200) {
	// String result = EntityUtils.toString(respone
	// .getEntity());
	// Message msg = new Message();
	// msg.obj = result;
	// msg.arg1 = index;
	// handler.sendMessage(msg);
	// }
	// } catch (ClientProtocolException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	// }).start();
	//
	// }

	// private Handler handler = new Handler() {
	// public void handleMessage(Message msg) {
	// String result = (String) msg.obj;
	//
	// if ("添加食盒收藏成功".equals(result)) {
	// datalist.get(msg.arg1).put("is_collect", "1");
	// notifyDataSetChanged();
	// }else{
	// Toast.makeText(context, "收藏失败", 0).show();
	// }
	// };
	// };

}
