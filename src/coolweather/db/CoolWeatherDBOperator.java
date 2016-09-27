package coolweather.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import coolweather.model.City;
import coolweather.model.County;
import coolweather.model.Province;
import coolweather.util.LogUtil;

//1、设计成：通过saveProvince（pronvince），loadProvince返回LIst<Province>等方法用于存储和读取数据库省、市、县的数据
//2、用于封装一些常用的数据库操作，操作的数据库是下面新建的SQLiteDatabase db
//3、设计为单例类，保证全局范围内只有一个实例，即下面定义的coolWeatherDB
//4、coolWeatherDB不是数据库，而是类似于数据库的操作类（所以说封装了一些常用的数据库操作）
public class CoolWeatherDBOperator {

	public static final String DB_NAME = "cool_weather";
	public static final int VERSION = 1;
	// 单例！！！
	private static CoolWeatherDBOperator coolWeatherDBOperator;
	private SQLiteDatabase db;

	@Override
	public String toString() {
		return "CoolWeatherDB []";
	}

	public CoolWeatherDBOperator() {
		super();
	}

	// 将构造方法私有化，目的：单例模式
	private CoolWeatherDBOperator(Context context) {
		db = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION).getReadableDatabase();
	}

	// 单例获取方法，synchronized能够保证在同一时刻最多只有一个线程执行该段方法or代码
	public synchronized static CoolWeatherDBOperator getInstance(Context context) {
		if (coolWeatherDBOperator == null) {
			coolWeatherDBOperator = new CoolWeatherDBOperator(context);
		}
		return coolWeatherDBOperator;
	}

	// 数据库插入操作，db.insert
	// 借助ContentValues, 用来代替sql语句进行数据库的操作,只能存储基本类型的数据(键值对！)
	// 第二个参数用于未指定添加数据的情况下给某些可为空的列自动赋值NULL,一般用不到这个功能
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvince_name());
			values.put("province_code", province.getProvince_code());
			db.insert("Province", null, values);
			// 参数：db.insert("数据库表的名称", 一般为null, ContentValues对象);
			// 插入成功就返回记录的id否则返回-1；
		}
	}

	// 数据库的查询or读取操作，db.query,返回一个cursor,遍历cursor得到ArrayList
	public List<Province> loadProvince() {
		LogUtil.d("loadProvince", "入口");
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		// 遍历cursor
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvince_code(cursor.getString(cursor.getColumnIndex("province_code")));
				province.setProvince_name(cursor.getString(cursor.getColumnIndex("province_name")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		LogUtil.d("loadProvince", "结束");
		return list;

	}

	// 数据库插入操作，db.insert
	// 借助ContentValues, 用来代替sql语句进行数据库的操作,只能存储基本类型的数据(键值对！)
	// 第二个参数用于未指定添加数据的情况下给某些可为空的列自动赋值NULL,一般用不到这个功能
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCity_name());
			values.put("city_code", city.getCity_code());
			values.put("province_id", city.getProvince_id());
			db.insert("City", null, values);
			// 参数：db.insert("数据库表的名称", 一般为null, ContentValues对象);
			// 插入成功就返回记录的id否则返回-1；
		}
	}

	// 数据库的查询or读取操作，db.query,返回一个cursor,遍历cursor得到ArrayList
	public List<City> loadCity(int provinceId) {
		LogUtil.d("loadCity", "入口");
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id=?", new String[] { String.valueOf(provinceId) }, null, null,
				null);
		// String.valueOf(),基本数据型态转换成 String
		// 遍历cursor
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCity_code(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
				//city.setProvince_id(cursor.getInt(cursor.getColumnIndex("province_id")));
				city.setProvince_id(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}

		return list;

	}

	// 数据库插入操作，db.insert
	// 借助ContentValues, 用来代替sql语句进行数据库的操作,只能存储基本类型的数据(键值对！)
	// 第二个参数用于未指定添加数据的情况下给某些可为空的列自动赋值NULL,一般用不到这个功能
	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCounty_name());
			values.put("county_code", county.getCounty_code());
			values.put("city_id", county.getCity_id());
			db.insert("County", null, values);
			// 参数：db.insert("数据库表的名称", 一般为null, ContentValues对象);
			// 插入成功就返回记录的id否则返回-1；
		}
	}

	// 数据库的查询or读取操作，db.query,返回一个cursor,遍历cursor得到ArrayList
	public List<County> loadCounty(int cityId) {
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id=?", new String[] { String.valueOf(cityId) }, null, null, null);
		// 遍历cursor
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCounty_code(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCounty_name(cursor.getString(cursor.getColumnIndex("county_name")));
				//county.setCity_id(cursor.getInt(cursor.getColumnIndex("city_id")));
				county.setCity_id(cityId);
				list.add(county);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}

		return list;

	}
}
