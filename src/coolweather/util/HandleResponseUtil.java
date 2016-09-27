package coolweather.util;

import android.text.TextUtils;
import coolweather.db.CoolWeatherDBOperator;
import coolweather.model.City;
import coolweather.model.County;
import coolweather.model.Province;

//1、设计成提供handleProvinceAResponse()等方法解析和处理服务器返回的省、市、县的响应，解析数据并保存到数据库
//2、通过数据库操作工具类来保存到数据库
public class HandleResponseUtil {

	// 返回“省”的响应格式是01|北京，02|上海，03|天津，04|重庆……
	public static synchronized boolean handleProvinceResponse(CoolWeatherDBOperator dbOperator, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allSingleProvince = response.split(",");
			// AllsingleProvince是{01|北京，02|上海，03|天津，04|重庆……}

			if (allSingleProvince != null && allSingleProvince.length > 0) {
				for (String p : allSingleProvince) {
					// p循环（轮流代表/迭代）是每个一省份（格式是：01|北京）

					String[] provinceInfo = p.split("\\|");
					// provinceInfo循环（轮流代表/迭代）是{01，北京}，{02，上海}，{03，天津，……}
					// provinceInfo[0]是省代码，provinceInfo[1]是省名称
					Province province = new Province();
					province.setProvince_code(provinceInfo[0]);
					province.setProvince_name(provinceInfo[1]);

					// 接下来就是想办法把解析出来的province保存到数据库中（CoolWeatherDBOperator中的db）
					// 外界不能接触到db，但可以通过CoolWeatherDBOperator中提供了saveProvince方法来保存
					dbOperator.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}

	public static boolean handleCityResponse(CoolWeatherDBOperator dbOperator, String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			// 返回“市”的响应格式是1901|南京，1902|无锡……
			String[] allSingleCity = response.split(",");
			if (allSingleCity != null && allSingleCity.length > 0) {
				for (String c : allSingleCity) {
					// 假设c是1902|无锡（会轮流迭代的）
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
