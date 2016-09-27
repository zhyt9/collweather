package coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

//��Ƴɣ�1���������ݿ⣬��ͨ��getReadable/WritableDatabase()����������ݿ�ʵ����
//2��AndroidΪ�˸��ӷ���Ĺ������ݿ⣬ר���ṩ��һ��SQLiteOpenHelper�����ࣨ�����ࣩ
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	// Province�������(�����ǽ����ݿ�Ŷ��)
	public static final String CREATE_PROVINCE = "create table province(" + "id integer primary key autoincrement,"
			+ "province_name text," + "province_code text)";

	// City�������(�����ǽ����ݿ�Ŷ��)
	public static final String CREATE_CITY = "create table city(" + "id integer primary key autoincrement,"
			+ "city_name text," + "city_code text," + "province_id integer)";

	// County�������(�����ǽ����ݿ�Ŷ��)
	public static final String CREATE_COUNTY = "create table county(" + "id integer primary key autoincrement,"
			+ "county_name text," + "county_code text," + "city_id integer)";

	// SQLiteOpenHelper���������췽��֮һ��
	public CoolWeatherOpenHelper(Context context, String name, CursorFactory factory, int version) {
		// �ڶ�������name�����ݿ������������������������ڲ�ѯ���ݵ�ʱ�򷵻�һ���Զ����Cursor,���ĸ����������ݿ�汾��
		// SQLiteDatabase.CursorFactory����һ������Cursor����Ĺ�����
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	// ��OnCreate����д�������ݿ��߼�
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
	}

	// ��onUpgrade����д�������ݿ��߼�
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
