package coolweather.util;

import android.text.TextUtils;
import coolweather.db.CoolWeatherDBOperator;
import coolweather.model.City;
import coolweather.model.County;
import coolweather.model.Province;

//1����Ƴ��ṩhandleProvinceAResponse()�ȷ��������ʹ�����������ص�ʡ���С��ص���Ӧ���������ݲ����浽���ݿ�
//2��ͨ�����ݿ���������������浽���ݿ�
public class HandleResponseUtil {

	// ���ء�ʡ������Ӧ��ʽ��01|������02|�Ϻ���03|���04|���졭��
	public static synchronized boolean handleProvinceResponse(CoolWeatherDBOperator dbOperator, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allSingleProvince = response.split(",");
			// AllsingleProvince��{01|������02|�Ϻ���03|���04|���졭��}

			if (allSingleProvince != null && allSingleProvince.length > 0) {
				for (String p : allSingleProvince) {
					// pѭ������������/��������ÿ��һʡ�ݣ���ʽ�ǣ�01|������

					String[] provinceInfo = p.split("\\|");
					// provinceInfoѭ������������/��������{01������}��{02���Ϻ�}��{03����򣬡���}
					// provinceInfo[0]��ʡ���룬provinceInfo[1]��ʡ����
					Province province = new Province();
					province.setProvince_code(provinceInfo[0]);
					province.setProvince_name(provinceInfo[1]);

					// ������������취�ѽ���������province���浽���ݿ��У�CoolWeatherDBOperator�е�db��
					// ��粻�ܽӴ���db��������ͨ��CoolWeatherDBOperator���ṩ��saveProvince����������
					dbOperator.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}

	public static boolean handleCityResponse(CoolWeatherDBOperator dbOperator, String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			// ���ء��С�����Ӧ��ʽ��1901|�Ͼ���1902|��������
			String[] allSingleCity = response.split(",");
			if (allSingleCity != null && allSingleCity.length > 0) {
				for (String c : allSingleCity) {
					// ����c��1902|�����������������ģ�
					String[] cityInfo = c.split("\\|");
					City city = new City();
					city.setCity_code(cityInfo[0]);
					city.setCity_name(cityInfo[1]);
					city.setProvince_id(provinceId);
					dbOperator.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}

	public static boolean handleCountyResponse(CoolWeatherDBOperator dbOperator, String response, int CityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allSingleCounty = response.split(",");
			if (allSingleCounty != null && allSingleCounty.length > 0) {
				for (String c : allSingleCounty) {
					String[] countyInfo = c.split("\\|");
					County county = new County();
					county.setCounty_code(countyInfo[0]);
					county.setCounty_name(countyInfo[1]);
					county.setCity_id(CityId);
					dbOperator.saveCounty(county);
				}

				return true;
			}
		}
		return false;
	}
}
