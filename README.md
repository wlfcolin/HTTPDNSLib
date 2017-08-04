# HTTPDNSLib

a fork from https://github.com/CNSRE/HTTPDNSLib, I make it to support android 6.0+ and create MyTest module

* gradle
``` java
compile 'me.andy5:DNSCache:1.0.0'
```

----------------------------------------------------------------------
README from https://github.com/CNSRE/HTTPDNSLib
----------------------------------------------------------------------


该项目是手机微博研发团队和手机微博技术保障部共同努力的成果。

在此特别感谢：

- 张杰 微博:[@木泽水](http://weibo.com/klans)
- 王春生 微博:[@平凡的香草](http://weibo.com/chunshengster)
- 胡波 微博:[@胡波_](http://weibo.com/coolbor)
- 韩超 微博:[@超朝炒觘](http://weibo.com/weibochao)
- 赵星宇 微博:[@淘淘不逃008 ](http://weibo.com/5251978872)
- 聂钰 微博:[@古夜](http://weibo.com/206541012)
- 冯磊 微博:[@冯磊424](http://weibo.com/myfenglei)

等同学的支持。

感谢大家提出的宝贵意见，感谢大家为该项目付出的努力！

项目中有任何问题欢迎大家来吐槽，一起完善、一起提高、一起使用！
email： xingyu10@staff.weibo.com fenglei1@staff.sina.com.cn

接入说明
-----------------------------------

由于该工程需要用户自定义部分配置文件，所以建议以源码方式使用。（同时也支持项目中设定`Lib`库的参数）

`Lib`库目前还没有打包成`jar`文件， 大家测试使用的话可以直接将工程包含到自己的工程内即可。

`apk`文件夹下内有打包好的对`httpDNS`库进行测试的程序。
该测试程序模拟了用户使用的场景，并且记录了相关统计数据，以及`Lib`库的时时的状态信息。

###以下配置请按照顺序进行：

1.配置清单文件 - AndroidManifest.xml
```xml
<!-- 权限信息 -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```
2.初始化调用 - 【建议放在Application中处理】
```java
DNSCache.Init(context);
```
3.预加载域名解析（可选） - 【建议放在Application中处理】
可提前预加载需要解析的域名，并将解析数据缓存到内存中。
```java
DNSCache.getInstance().preLoadDomains(new String[]{"api.camera.weibo.com","domain2","domain3"});
```
4.开始使用

直接调用该方法获取 A记录对象
```java
DomainInfo[] infoList = DNSCache.getInstance().getDomainServerIp( "http://api.camera.weibo.com/index.html" ) ;

//DomainInfo 返回有可能为null，如果为空则使用域名直接请求数据吧~ 因为在http server 故障的时候会出现这个问题。
if( infoList != null ) {
//A记录可能会返回多个， 没有特殊需求直接使用第一个即可。  这个数组是经过排序的。
DomainInfo domainModel = infoList[0] ;
//这里是 android 请求网络。  只需要在http头里添加一个数据即可。 省下的请求数据和原先一样。
HttpGet getMethod = new HttpGet( domainModel.url );
getMethod.setHeader("host", domainModel.host);
HttpClient httpClient = new DefaultHttpClient();
HttpResponse response = httpClient.execute(getMethod);
}
```

### 更多配置

你可以创建一个`com.sina.util.dnscache.DNSCacheConfig.Data`对象分别设置下面属性。

然后调用`DNSCacheConfig.saveLocalConfigAndSync`方法传入设定好的配置选项。

```java
/**
* 是否启用udpdns服务器 默认不启用 | 1启用 0不启用
*/
public String IS_UDPDNS_SERVER = "";
/**
* udp dnsserver的地址，如8.8.8.8
*/
public String UDPDNS_SERVER_API = "";
/**
* 日志采样率。如设为50，则为50次数据采样一次
*/
public String HTTPDNS_LOG_SAMPLE_RATE = "";
/**
* lib库开关，“1”启用，“0”不启用。默认启用
*/
public String HTTPDNS_SWITCH = "";
/**
* 测速间隔时间
*/
public String SCHEDULE_SPEED_INTERVAL = "";

/**
* timer轮询器的间隔时间
*/
public String SCHEDULE_TIMER_INTERVAL = "";
/**
* ip数据过期延迟差值
*/
public String IP_OVERDUE_DELAY = "";
/**
* 是否启用自己家的HTTP_DNS服务器 默认不启用 | 1启用 0不启用
*/
public String IS_MY_HTTP_SERVER = null;
/**
* 自己家HTTP_DNS服务API地址 使用时直接在字符串后面拼接domain地址 |
* 示例（http://202.108.7.153/dns?domain=）+ domain
*/
public ArrayList<String> HTTPDNS_SERVER_API = new ArrayList<String>();
/**
* 是否启用dnspod服务器 默认不启用 | 1启用 0不启用
*/
public String IS_DNSPOD_SERVER = null;
/**
* DNSPOD HTTP_DNS 服务器API地址 | 默认（http://119.29.29.29/d?ttl=1&dn=）
*/
public String DNSPOD_SERVER_API = null;
/**
* DNSPOD 企业级ID配置选项
*/
public String DNSPOD_ID = null;
/**
* DNSPOD 企业级KEY配置选项
*/
public String DNSPOD_KEY = null;

/**
* 是否开启 本地排序插件算法 默认开启 | 1开启 0不开启
*/
public String IS_SORT = null;
/**
* 速度插件 比重分配值：默认40%
*/
public String SPEEDTEST_PLUGIN_NUM = null;
/**
* 服务器推荐优先级插件 比重分配：默认30% （需要自家HTTP_DNS服务器支持）
*/
public String PRIORITY_PLUGIN_NUM = null;
/**
* 历史成功次数计算插件 比重分配：默认10%
*/
public String SUCCESSNUM_PLUGIN_NUM = null;
/**
* 历史错误次数计算插件 比重分配：默认10%
*/
public String ERRNUM_PLUGIN_NUM = null;
/**
* 最后一次成功时间计算插件 比重分配：默认10%
*/
public String SUCCESSTIME_PLUGIN_NUM = null;
```

### 动态更新参数

`DNSCacheConfig`类下的`ConfigText_API`字段可以配置成自动更新配置参数的接口。该接口返回`json`数据类型 具体数据格式详见`DNSCacheConfig.Data toJson()`方法。

### 自己家HttpDNS服务接入

1. 开启`DNSCacheConfig.Data.IS_MY_HTTP_SERVER = 1;`
2. 设定 `DNSCacheConfig.Data.HTTPDNS_SERVER_API`接口地址（示例`http://XXX.XXX.XXX.XXX/dns?domain=`）+ `domain`，该接口返回格式。
    （重载`com.sina.util.dnscache.httpdns.IJsonParser`类可以自定义格式）

```javascript
{
    "domain": "api.weibo.cn",
    "device_ip": "10.209.70.192",
    "device_sp": "0",
    "dns": [
        {
            "priority": "0",
            "ip": "123.125.105.231",
            "ttl": "60"
        },
        {
            "priority": "0",
            "ip": "123.125.105.246",
            "ttl": "60"
        },
        {
            "priority": "0",
            "ip": "202.108.7.133",
            "ttl": "60"
        }
    ]
}
```

PS： `priority`字段是服务器推荐优先级。`device_sp`字段是该设备出口运营商。

HttpDns是什么？
-----------------------------------

如果你对 httpdns 还不了解他是什么！

你可以阅读：[【鹅厂网事】全局精确流量调度新思路-HttpDNS服务详解](http://mp.weixin.qq.com/s?__biz=MzA3ODgyNzcwMw==&mid=201837080&idx=1&sn=b2a152b84df1c7dbd294ea66037cf262&scene=2&from=timeline&isappinstalled=0&utm_source=tuicool)

传统DNS解析 和 HTTPDNS解析 本质的区别
-----------------------------------
### 传统DNS解析

客户端发送udp数据包到dns服务器,dns服务器返回该域名的相关A记录信息。

### HTTPDNS解析

客户端发起http请求携带需要查询的域名,通过IP直接访问服务器,该Http服务器接倒请求后返回域名对应的A记录。

HttpDns sdk （android版本）
-----------------------------------

### 希望解决的问题：

    1. LocalDNS劫持
    2. 平均访问延迟下降
    3. 用户连接失败率下降

### 目录结构说明：

	HttpDns/src/DNSCache --- HttpDns lib库主工程。
	HttpDns/src/DNSCacheTest --- HttpDns库测试工程。
	HttpDns/doc --- 项目相关的一些文档、流程图、结构图等。
	HttpDns/ui/DNSCacheTest --- 存放HttpDns测试项目UI源文件以及切图文件。


### HttpDns 交互流程
![image](https://github.com/SinaMSRE/HTTPDNSLib/raw/master/doc/httpdns%20lib%E5%BA%93%E4%BA%A4%E4%BA%92%E6%B5%81%E7%A8%8B.png)



### HttpDns Lib库交互流程
![image](https://github.com/SinaMSRE/HTTPDNSLib/raw/master/doc/DNSLib%E5%BA%93%E4%BA%A4%E4%BA%92%E6%B5%81%E7%A8%8B%E5%9B%BE.png)


<br>
<br>

### 查询模块
    检测本地是否有相应的域名缓存
    没有记录则根据当前运营商返回内置的ip节点
    从httpdns查询域名相应A记录，缓存域名信息
    查询模块必须保证响应速度，基于已有设备测试平均在5毫秒左右


### 数据缓存模块
    根据sp（或wifi名）缓存域名信息
    根据sp（或wifi名）缓存服务器ip信息、优先级
    记录服务器ip每次请求成功数、错误数
    记录服务器ip最后成功访问时间、最后测速
    添加 内存 -》数据库 之间的缓存层


### 评估模块
	根据本地数据，对一组ip排序
	处理用户反馈回来的请求明细，入库
	针对用户反馈是失败请求，进行分析上报预警
	给HttpDns服务端智能分配A记录提供数据依据


### 评估算法插件
	本次测速 - 对ip组的每个ip测速打分
	官方推荐 - HttpDns接口 A记录中返回的优先级
	历史成功 - 该ip7天内成功访问次数
	历史错误 - 该ip7天内访问错误次数
	最后成功时间 - 该ip最后一次成功时间，阈值24


### 打分比重权值分配
	对每个IP打分，总分100分。
	本次测速 - 40分
	官方推荐 - 30分
	历史成功 - 10分
	历史错误 - 10分
	最后成功时间 - 10分
	总分=本次测速+官方推荐+历史成功次数+历史错误次数+最后成功时间

目前权重分配完全基于主观认识，后期会根据建立的相应基线进行权重分配调整。 <br>
> 使用者需要自己权衡，有可能随机的ip速度都好于权重打分的ip。

<br>
<br>
PS:给出一副算法计算分数时的细节图，有兴趣的朋友可以一起探讨研究。 <br><br>
![image](https://github.com/SinaMSRE/HTTPDNSLib/raw/master/doc/%E7%AE%97%E6%B3%95%E6%8F%92%E4%BB%B6%E8%AE%A1%E7%AE%97%E5%9B%BE.png) <br><br>


<br><br>

HttpDns Test （android版本）
-----------------------------------

测试工程主要最初为了模拟用户使用APP发出的网络请求，进行数据记录和对比。<br>
在页面中能很直观的看到每个任务的相关信息。<br>
比如：任务总耗时，httpdns lib库耗时、http请求耗时、以及设备当前环境信息 等。。<br>
由于UE，UI都是自己设计，对于表达信息的布局和美观可能还有欠缺，本程序猿的能力有限，大家多多包涵。 <br><br>

你未必会需要“它”，上传几张测试工程的截图，提供参考。<br>
![image](https://github.com/SinaMSRE/HTTPDNSLib/raw/master/doc/img/1.png) <br><br>
![image](https://github.com/SinaMSRE/HTTPDNSLib/raw/master/doc/img/2.png) <br><br>
![image](https://github.com/SinaMSRE/HTTPDNSLib/raw/master/doc/img/3.png) <br><br>
![image](https://github.com/SinaMSRE/HTTPDNSLib/raw/master/doc/img/4.png) <br><br>
![image](https://github.com/SinaMSRE/HTTPDNSLib/raw/master/doc/img/5.png) <br><br>
![image](https://github.com/SinaMSRE/HTTPDNSLib/raw/master/doc/img/6.png) <br><br>
