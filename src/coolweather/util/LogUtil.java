package coolweather.util;

import android.util.Log;

public class LogUtil {

	public static final int VERBOSE = 1;// verbose��Ӧ�������Ϣ
	public static final int DEBUG = 2;// debug��Ӧ���Ե���Ϣ
	public static final int INFO = 3;// info��Ӧ�Ƚ���Ҫ����Ϣ
	public static final int WARN = 4;// warn��Ӧ������Ϣ
	public static final int ERROR = 5;// error��Ӧ������Ϣ
	public static final int NOTHING = 6;// nothing���ڲ���ӡ��Ϣ
	public static final int LEVEL = VERBOSE;// level �㼶����

	public static void v(String tag, String msg) {
		if (LEVEL <= VERBOSE) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (LEVEL <= DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (LEVEL <= INFO) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (LEVEL <= WARN) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (LEVEL <= ERROR) {
			Log.e(tag, msg);
		}
	}

}
