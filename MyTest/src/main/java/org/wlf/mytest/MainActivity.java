package org.wlf.mytest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sina.util.dnscache.DNSCache;
import com.sina.util.dnscache.DomainInfo;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author wlf(Andy)
 * @datetime 2016-04-19 16:08 GMT+8
 * @email 411086563@qq.com
 */
public class MainActivity extends Activity {

    private EditText etInput;
    private TextView tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main__activity_main);

        etInput = (EditText) findViewById(R.id.etInput);
        tvShow = (TextView) findViewById(R.id.tvShow);
    }

    public void btnClick(View v) {

        tvShow.setText("");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // String oUrl = "http://www.baidu.com";
                     String orginalUrl = etInput.getText().toString().trim();
                    // String oUrl = "http://www.baidu.com";
                    orginalUrl = orginalUrl.toLowerCase();
                    if(!orginalUrl.startsWith("http://")){
                        orginalUrl = "http://"+orginalUrl;
                    }

                    final String oUrl = orginalUrl;

                    DomainInfo[] infoList = DNSCache.getInstance().getDomainServerIp(oUrl);

                    //DomainInfo 返回有可能为null，如果为空则使用域名直接请求数据吧~ 因为在http server 故障的时候会出现这个问题。
                    if (infoList != null) {
                        //A记录可能会返回多个， 没有特殊需求直接使用第一个即可。  这个数组是经过排序的。
                        final DomainInfo domainModel = infoList[0];
                        //这里是 android 请求网络。  只需要在http头里添加一个数据即可。 省下的请求数据和原先一样。

                        Log.e("wlf", "host:" + domainModel.host);
                        Log.e("wlf", "ip:" + domainModel.url);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvShow.setText(tvShow.getText() + "\n" + "域名:" + oUrl);
                                tvShow.setText(tvShow.getText() + "\n" + "host:" + domainModel.host);
                                tvShow.setText(tvShow.getText() + "\n" + "ip:" + domainModel.url);
                            }
                        });

                        final String reallyUrl = oUrl.replaceFirst(oUrl, domainModel.url);

                        Log.e("wlf", "reallyUrl:" + reallyUrl);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvShow.setText(tvShow.getText() + "\n" + "ipUrl:" + reallyUrl);
                            }
                        });

                        URL url = new URL(reallyUrl);
                         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestProperty("host", domainModel.host);
                        Log.e("wlf", "responseCode:" + connection.getResponseCode());
                        Log.e("wlf", "responseMessage:" + connection.getResponseMessage());

                        final int responseCode =  connection.getResponseCode();
                        final String responseMessage =  connection.getResponseMessage();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvShow.setText(tvShow.getText() + "\n" + "responseCode:" + responseCode);
                                tvShow.setText(tvShow.getText() + "\n" + "responseMessage:" + responseMessage);
                            }
                        });

                        /**
                         HttpGet getMethod = new HttpGet(domainModel.url);
                         getMethod.setHeader("host", domainModel.host);
                         HttpClient httpClient = new DefaultHttpClient();
                         httpClient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, false);
                         HttpResponse response = httpClient.execute(getMethod);

                         Log.e("wlf", "response:" + response.getEntity().getContent().toString());
                         */

                    }
                } catch (final Exception e) {
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvShow.setText(tvShow.getText() + "\n" + "出现异常:" + e);
                        }
                    });

                }
            }
        }).start();
    }
}
