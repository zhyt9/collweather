package coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

//设计成：1、创建数据库，并通过getReadable/WritableDatabase()方法输出数据库实例；
//2、Android为了更加方便的管理数据库，专门提供了一个SQLiteOpenHelper帮助类（抽象类）
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	// Province建表语句(建表不是建数据库哦！)
	public static final String CREATE_PROVINCE = "create table province(" + "id integer primary key autoincrement,"
			+ "province_name text," + "province_code text)";

	// City建表语句(建表不是建数据库哦！)
	public static final String CREATE_CITY = "create table city(" + "id integer primary key autoincrement,"
			+ "city_name text," + "city_code text," + "province_id integer)";

	// County建表语句(建表不是建数据库哦！)
	public static final String CREATE_COUNTY = "create table county(" + "id integer primary key autoincrement,"
			+ "county_name text," + "county_code text," + "city_id integer)";

	// SQLiteOpenHelper的两个构造方法之一；
	public CoolWeatherOpenHelper(Context context, String name, CursorFactory factory, int version) {
		// 第二个参数name是数据库名，第三个参数允许我们在查询数据的时候返回一个自定义的Cursor,第四个参数是数据库版本号
		// SQLiteDatabase.CursorFactory则是一个产生Cursor对象的工厂类
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	// 在OnCreate中书写创建数据库逻辑
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
	}

	// 在onUpgrade中书写升级数据库逻辑
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
