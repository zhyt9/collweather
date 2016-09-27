package com.zhyt.coolweather;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpRequest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import coolweather.db.CoolWeatherDBOperator;
import coolweather.model.City;
import coolweather.model.County;
import coolweather.model.Province;
import coolweather.util.HandleResponseUtil;
import coolweather.util.HttpCallBackListener;
import coolweather.util.HttpRequestUtil;
import coolweather.util.LogUtil;

public class ChooseAreaActivity extends Activity {

	private ListView listView;
	private TextView titleText;
	private List<String> dataList = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private CoolWeatherDBOperator dbOperator;
	private int currentLevel;
	private static final int LEVEL_PROVINCE = 0;
	private static final int LEVEL_CITY = 1;
	private static final int LEVEL_COUNT = 2;
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	private Province selectedProvince;
	private City selectedCity;
	private County selectedCounty;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.tv_title);

		// 使用Android提供的子项布局，就不需要重写ArrayAdapter中的gerView()方法了？！？
		adapter = new ArrayAdapter<>(ChooseAreaActivity.this, android.R.layout.simple_list_item_1, dataList);
		// 设置监听器
		listView.setAdapter(adapter);
		dbOperator = CoolWeatherDBOperator.getInstance(this);

		// 监听点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(arg2);
					LogUtil.d("onItemClick", "to--> queryAndShowCities()");
					queryAndShowCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(arg2);
					queryAndShowCounties();
				} else if (currentLevel == LEVEL_COUNT) {
					Toast.makeText(ChooseAreaActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();
				}
			}
		});
		queryAndShowProvinces();
	}

	private void queryAndShowProvinces() {
		provinceList = dbOperator.loadProvince();
		if (provinceList.size() > 0) {
			dataList.clear();
			LogUtil.d("queryAndShowProvinces", "provinceList.size() > 0");
			for (Province p : provinceList) {
				dataList.add(p.getProvince_name());
			}
			// 通过一个外部的方法控制如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容,
			// 可以实现动态的刷新列表的功能
			adapter.notifyDataSetChanged();

			/**
			 * listview.setselection(position)，表示将列表移动到指定的Position处。 举例说明：
			 * 1、listView中有100条记录，如果想定位到某一条上面去就可以直接调用listView.setSelection(position);
			 * 2、记录listView滚动到的position的坐标，然后利用listView.scrollTo精确的进行恢复
			 * 3、记录listView显示在屏幕上的item的位置，然后利用listView.setSelection恢复 4、
			 * 通知listView的适配器数据变更，这种适用于listView追加数据的情况，严格说不是恢复listView滚动的位置，只是保持滚动位置不错
			 */
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
			LogUtil.d("queryAndShowProvinces", "设置currentLevel = LEVEL_PROVINCE");
		} else {
			LogUtil.d("queryAndShowProvinces()", "to→queryFromServer");
			queryFromServer(null, "province");
		}
	}

	private void queryAndShowCities() {
		cityList = dbOperator.loadCity(selectedProvince.getId());
		LogUtil.d("queryAndShowCities", "入口");
		if (cityList.size() > 0) {
			dataList.clear();
			LogUtil.d("queryAndShowCities", "cityList.size() > 0");
			for (City c : cityList) {
				dataList.add(c.getCity_name());
			}
			// 通过一个外部的方法控制如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容,
			// 可以实现动态的刷新列表的功能
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvince_name());
			currentLevel = LEVEL_CITY;
		} else {
			LogUtil.d("queryAndShowCities", "to→queryFromServer");
			queryFromServer(selectedProvince.getProvince_code(), "city");
		}
	}

	private void queryAndShowCounties() {
		countyList = dbOperator.loadCounty(selectedCity.getId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County c : countyList) {
				dataList.add(c.getCounty_name());
			}
			// 通过一个外部的方法控制如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容,
			// 可以实现动态的刷新列表的功能
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCity_name());
			currentLevel = LEVEL_COUNT;
		} else {
			queryFromServer(selectedCity.getCity_code(), "county");
		}
	}

	private void queryFromServer(String code, final String type) {
		LogUtil.d("queryFromServer", "入口");
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		} else {
			LogUtil.d("ChooseAreaActivity", "address查省份的");
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		ShowProgressDialog();
		HttpRequestUtil.sendHttpRequest(address, new HttpCallBackListener() {

			boolean result = false;

			@Override
			public void onFinish(String response) {
				LogUtil.d("queryFromServer", "onFinish跳转！");
				// TODO Auto-generated method stub
				if ("province".equals(type)) {
					LogUtil.d("queryFromServer_sendHttpRequest", "onFinish_province.equals(type)");
					result = HandleResponseUtil.handleProvinceResponse(dbOperator, response);
				} else if ("city".equals(type)) {
					LogUtil.d("queryFromServer_sendHttpRequest", "onFinish_city.equals(type)");
					result = HandleResponseUtil.handleCityResponse(dbOperator, response, selectedProvince.getId());
				} else if ("county".equals(type)) {
					LogUtil.d("queryFromServer_sendHttpRequest", "onFinish_county.equals(type)");
					result = HandleResponseUtil.handleCountyResponse(dbOperator, response, selectedCity.getId());
				}
				if (result) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							CloseProgressDialog();
							if ("province".equals(type)) {
								LogUtil.d("ChooseAreaActivity", "if(result)里,to→queryAndShowProvinces()");
								queryAndShowProvinces();
							} else if ("city".equals(type)) {
								queryAndShowCities();
							} else if ("county".equals(type)) {
								queryAndShowCounties();
							}

						}
					});
				}

			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						CloseProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "Load failed", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});

	}

	private void ShowProgressDialog() {
		LogUtil.d("ShowProgressDialog()", "1");
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("Loading");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	private void CloseProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (currentLevel == LEVEL_COUNT) {
			queryAndShowCities();
		} else if (currentLevel == LEVEL_CITY) {
			queryAndShowProvinces();
		} else {
			finish();
		}
	}
}
