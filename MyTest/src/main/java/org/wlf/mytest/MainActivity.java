package org.wlf.mytest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sina.util.dnscache.DNSCache;
import com.sina.util.dnscache.DomainInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author wlf(Andy)
 * @datetime 2016-04-19 16:08 GMT+8
 * @email 411086563@qq.com
 */
public class MainActivity extends Activity {

    private EditText mEtInput;
    private TextView mTvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main__activity_main);

        mEtInput = (EditText) findViewById(R.id.et_input);
        mTvShow = (TextView) findViewById(R.id.tv_show);
    }

    public void btnClick(View v) {

        mTvShow.setText("");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // String oUrl = "http://www.baidu.com";
                    String orginalUrl = mEtInput.getText().toString().trim();
                    // String oUrl = "http://www.baidu.com";
                    orginalUrl = orginalUrl.toLowerCase();
                    if (!orginalUrl.startsWith("http://")) {
                        orginalUrl = "http://" + orginalUrl;
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
                                mTvShow.setText(mTvShow.getText() + "\n" + "域名:" + oUrl);
                                mTvShow.setText(mTvShow.getText() + "\n" + "host:" + domainModel.host);
                                mTvShow.setText(mTvShow.getText() + "\n" + "ip:" + domainModel.url);
                            }
                        });

                        final String reallyUrl = oUrl.replaceFirst(oUrl, domainModel.url);

                        Log.e("wlf", "reallyUrl:" + reallyUrl);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTvShow.setText(mTvShow.getText() + "\n" + "ipUrl:" + reallyUrl);
                            }
                        });

                        URL url = new URL(reallyUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestProperty("host", domainModel.host);
                        Log.e("wlf", "responseCode:" + connection.getResponseCode());
                        Log.e("wlf", "responseMessage:" + connection.getResponseMessage());

                        final int responseCode = connection.getResponseCode();
                        final String responseMessage = connection.getResponseMessage();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTvShow.setText(mTvShow.getText() + "\n" + "responseCode:" + responseCode);
                                mTvShow.setText(mTvShow.getText() + "\n" + "responseMessage:" + responseMessage);
                            }
                        });

                        HttpGet getMethod = new HttpGet(domainModel.url);
                        getMethod.setHeader("host", domainModel.host);
                        HttpClient httpClient = new DefaultHttpClient();
                        httpClient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, false);
                        HttpResponse response = httpClient.execute(getMethod);

                        StringBuffer buffer = new StringBuffer();

                        InputStream inputStream = response.getEntity().getContent();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf8"));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line);
                        }

                        String result = buffer.toString();

                        Log.e("wlf", "response:" + result);

                        mTvShow.setText(mTvShow.getText() + "\n\n" + "-----访问" + domainModel.url + "的结果-----" + "\n"
                                + result);
                    }
                } catch (final Exception e) {
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvShow.setText(mTvShow.getText() + "\n" + "出现异常:" + e);
                        }
                    });

                }
            }
        }).start();
    }
}
