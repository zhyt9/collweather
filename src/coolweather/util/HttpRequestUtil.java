package coolweather.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//1����Ƴɣ�HttpRequestUtil.sendHttpRequest()���ͷ���������
//2��ͨ��url.openConnection()�õ�һ��HttpsURLConnection,ͨ��IO�����response(connection.getInputStream())
//3��HttpCallBackListener�ӿڵ����ã�����һ�д��롷P406,��ֹ��������û�����ü���Ӧ���߳̾��Ѿ������ˣ������޷�������Ӧ������
//4����������Ӧ���ص�response����HttpCallBackListener�ӿ�onFinish()��������
public class HttpRequestUtil {
	public static void sendHttpRequest(final String address, final HttpCallBackListener listener) {
		LogUtil.d("sendHttpRequest", "���");
		new Thread(new Runnable() {
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);

					// ����д��HttpsURLConnection!����д��HttpsURLConnection!����д��HttpsURLConnection!
					// ����д��HttpsURLConnection!����д��HttpsURLConnection!����д��HttpsURLConnection!
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					// ��һ��url����׷��һ�����ʵĳ�ʱʱ���Ǳ���ģ��Ա�֤������������������ȷ�ش���
					// ��ֹ������ʱ�����Ӵ�����ϵͳ���ܼ�ʱ����Ӧ
					connection.setReadTimeout(8000);
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					// connection.getInputStream()����InputStream����InputStreamReader����BufferedReader����װ��
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						LogUtil.d("sendHttpRequest", "whileѭ������response");
						response.append(line);
					}
					if (listener != null) {
						LogUtil.d("sendHttpRequest", "onFinish��ת��1��");
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
