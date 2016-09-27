package coolweather.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//1、设计成：HttpRequestUtil.sendHttpRequest()发送服务器请求
//2、通过url.openConnection()得到一个HttpsURLConnection,通过IO流输出response(connection.getInputStream())
//3、HttpCallBackListener接口的作用，《第一行代码》P406,防止服务器还没有来得及响应子线程就已经结束了，这样无法返回响应的数据
//4、服务器响应返回的response传动HttpCallBackListener接口onFinish()方法里了
public class HttpRequestUtil {
	public static void sendHttpRequest(final String address, final HttpCallBackListener listener) {
		LogUtil.d("sendHttpRequest", "入口");
		new Thread(new Runnable() {
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);

					// 不用写成HttpsURLConnection!不用写成HttpsURLConnection!不用写成HttpsURLConnection!
					// 不用写成HttpsURLConnection!不用写成HttpsURLConnection!不用写成HttpsURLConnection!
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					// 给一个url请求，追加一个合适的超时时间是必须的，以保证正常的数据请求都能正确地处理，
					// 防止由于暂时的连接错误导致系统不能及时地响应
					connection.setReadTimeout(8000);
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					// connection.getInputStream()→→InputStream→→InputStreamReader→→BufferedReader（包装）
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						LogUtil.d("sendHttpRequest", "while循环处理response");
						response.append(line);
					}
					if (listener != null) {
						LogUtil.d("sendHttpRequest", "onFinish跳转吗1→");
						listener.onFinish(response.toString());

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					listener.onError(e);
				} finally {
					if (connection != null) {
						connection.disconnect();
					}

				}
			}
		}).start();

	}
}
