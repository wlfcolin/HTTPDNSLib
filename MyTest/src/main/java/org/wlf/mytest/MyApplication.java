package org.wlf.mytest;

import android.app.Application;

import com.sina.util.dnscache.DNSCache;

/**
 * @author wlf(Andy)
 * @datetime 2016-04-19 16:06 GMT+8
 * @email 411086563@qq.com
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化调用
        DNSCache.Init(this.getApplicationContext());

        // 预加载域名解析（可选）
        // DNSCache.getInstance().preLoadDomains(new String[]{"api.camera.weibo.com", "domain2", "domain3"});
    }
}
