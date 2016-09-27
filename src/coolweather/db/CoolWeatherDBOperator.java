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

//1����Ƴɣ�ͨ��saveProvince��pronvince����loadProvince����LIst<Province>�ȷ������ڴ洢�Ͷ�ȡ���ݿ�ʡ���С��ص�����
//2�����ڷ�װһЩ���õ����ݿ���������������ݿ��������½���SQLiteDatabase db
//3�����Ϊ�����࣬��֤ȫ�ַ�Χ��ֻ��һ��ʵ���������涨���coolWeatherDB
//4��coolWeatherDB�������ݿ⣬�������������ݿ�Ĳ����ࣨ����˵��װ��һЩ���õ����ݿ������
public class CoolWeatherDBOperator {

	public static final String DB_NAME = "cool_weather";
	public static final int VERSION = 1;
	// ����������
	private static CoolWeatherDBOperator coolWeatherDBOperator;
	private SQLiteDatabase db;

	@Override
	public String toString() {
		return "CoolWeatherDB []";
	}

	public CoolWeatherDBOperator() {
		super();
	}

	// �����췽��˽�л���Ŀ�ģ�����ģʽ
	private CoolWeatherDBOperator(Context context) {
		db = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION).getReadableDatabase();
	}

	// ������ȡ������synchronized�ܹ���֤��ͬһʱ�����ֻ��һ���߳�ִ�иöη���or����
	public synchronized static CoolWeatherDBOperator getInstance(Context context) {
		if (coolWeatherDBOperator == null) {
			coolWeatherDBOperator = new CoolWeatherDBOperator(context);
		}
		return coolWeatherDBOperator;
	}

	// ���ݿ���������db.insert
	// ����ContentValues, ��������sql���������ݿ�Ĳ���,ֻ�ܴ洢�������͵�����(��ֵ�ԣ�)
	// �ڶ�����������δָ��������ݵ�����¸�ĳЩ��Ϊ�յ����Զ���ֵNULL,һ���ò����������
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvince_name());
			values.put("province_code", province.getProvince_code());
			db.insert("Province", null, values);
			// ������db.insert("���ݿ�������", һ��Ϊnull, ContentValues����);
			// ����ɹ��ͷ��ؼ�¼��id���򷵻�-1��
		}
	}

	// ���ݿ�Ĳ�ѯor��ȡ������db.query,����һ��cursor,����cursor�õ�ArrayList
	public List<Province> loadProvince() {
		LogUtil.d("loadProvince", "���");
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		// ����cursor
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
		LogUtil.d("loadProvince", "����");
		return list;

	}

	// ���ݿ���������db.insert
	// ����ContentValues, ��������sql���������ݿ�Ĳ���,ֻ�ܴ洢�������͵�����(��ֵ�ԣ�)
	// �ڶ�����������δָ��������ݵ�����¸�ĳЩ��Ϊ�յ����Զ���ֵNULL,һ���ò����������
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCity_name());
			values.put("city_code", city.getCity_code());
			values.put("province_id", city.getProvince_id());
			db.insert("City", null, values);
			// ������db.insert("���ݿ�������", һ��Ϊnull, ContentValues����);
			// ����ɹ��ͷ��ؼ�¼��id���򷵻�-1��
		}
	}

	// ���ݿ�Ĳ�ѯor��ȡ������db.query,����һ��cursor,����cursor�õ�ArrayList
	public List<City> loadCity(int provinceId) {
		LogUtil.d("loadCity", "���");
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id=?", new String[] { String.valueOf(provinceId) }, null, null,
				null);
		// String.valueOf(),����������̬ת���� String
		// ����cursor
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

	// ���ݿ���������db.insert
	// ����ContentValues, ��������sql���������ݿ�Ĳ���,ֻ�ܴ洢�������͵�����(��ֵ�ԣ�)
	// �ڶ�����������δָ��������ݵ�����¸�ĳЩ��Ϊ�յ����Զ���ֵNULL,һ���ò����������
	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCounty_name());
			values.put("county_code", county.getCounty_code());
			values.put("city_id", county.getCity_id());
			db.insert("County", null, values);
			// ������db.insert("���ݿ�������", һ��Ϊnull, ContentValues����);
			// ����ɹ��ͷ��ؼ�¼��id���򷵻�-1��
		}
	}

	// ���ݿ�Ĳ�ѯor��ȡ������db.query,����һ��cursor,����cursor�õ�ArrayList
	public List<County> loadCounty(int cityId) {
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id=?", new String[] { String.valueOf(cityId) }, null, null, null);
		// ����cursor
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
