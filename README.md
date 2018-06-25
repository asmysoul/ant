## Ant


![](https://api.travis-ci.org/asmysoul/ant.svg?branch=master) ![](https://img.shields.io/badge/language-java-green.svg) ![](https://img.shields.io/badge/jdk-1.8-green.svg)

### 简介

    Ant, 之所以选择这么一个名字第一是因为够短, 
    第二是最近工作大部分在写前端, 用到Ant-Design,
    因而选了这么一个名字。￣□￣｜｜

    写这么一个东西主要是兴趣, 还有就是工作中也会用到, 
    想要一个接入代理平台方便的小型框架, 
    看了一些爬虫框架的源码, 借鉴（抄袭￣□￣｜｜）其中的主要核心逻辑。


### 使用方式
   ![example](http://image.fzqblog.top/ant-example.png)

    极其简单,几行代码即可实现一个简单的爬虫。
    其中的网页节点内容抽取用的JSOUP, 主要是个人写前端, 觉得JSOUP相对简单,
    http客户端使用的是HttpClient, 基于接口实现http抓取, 可自由替换, 
    日志的话采用logback。

* 引入方式
    > maven
    > jdk8
    ```xml
    <dependency>
        <groupId>top.fzqblog</groupId>
        <artifactId>ant</artifactId>
        <version>1.0.2</version>
    </dependency>
    ```

### hello-world

```java
    try {
        AntQueue antQueue = TaskQueue.of();//初始化默认一个任务队列
        antQueue.push(new Task("https://github.com/"));//往队列列表里放一个任务
        Ant ant = Ant
                .create()//创建一个ant,
                .startQueue(antQueue)//并将任务给它,
                .thread(1);// 使用单线程爬取
        ant.run();//发车 滴滴滴
    } catch (Exception e) {
        e.printStackTrace();
    }
```



*   TaskQueue支持自定义初始化装载因子 (num)
    ```java
    AntQueue antQueue = TaskQueue.of(num);
    ```

*   Ant自定义更多的爬取属性
    ```java
    Ant ant = Ant
            .create()//创建一个ant,
            .startQueue(antQueue)//并将任务给它,
            .thread(1);// 使用单线程爬取
            .httpKit(自定义的客户端)//实现IHttpKit 并覆盖doGet方法
            .autoClose(false)//取消自动关闭 默认为自动关闭
            .sleep(1000)//抓取间隔 默认为0
    ```


*   支持使用自定义的pipeline, 处理抓取到的数据, 
    进行数据格式化, 结构化, 持久化的接口
    总之就是拿到数据后自己爱咋咋地, 
    默认使用ConsolePipeline,默认输出任务抓取结果到控制台
    ```java
    @Override
    public void stream(TaskResponse taskResponse) 
        throws InterruptedException {
        logger.info("TaskResponse------------------------" + taskResponse);
    }
    ```
*   使用自定义的pipeline
    ```java
    public class SubPipeline implements IPipeline {//实现IPipeline接口

        private transient Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public void stream(TaskResponse taskResponse) 
            throws InterruptedException {//覆盖stream方法, 这里面即可自定义处理逻辑
            Document document = taskResponse.getDoc();
            logger.info("taskResponse----------=" + document.title());
        }
    }
    ```
*   创建爬虫Ant的地方使用该pipeline
    ```java
    Ant ant = Ant
                .create()
                .startQueue(antQueue)
                .pipeline(new SubPipeline())
                .thread(1);
    ```
    
   
*   自定义抓取错误处理Handler, 
    主要是各个网站返回的错误是不一样的, 
    所以开放处理方式进行错误处理, 进行重试或者放弃该任务,
    默认task的重试次数为3次, 且默认的处理器为
    ```java
    public class DefaultHandler implements IHandler {

        private transient Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public void handle(TaskErrorResponse taskErrorResponse) {

            try {
                Task task = taskErrorResponse.getTask();
                if(task.getRetry() <= 0){
                    logger.error("重试次数已用完--------"+task);
                }else{
                    taskErrorResponse
                    .getQueue()
                    .failed(taskErrorResponse.getTask());//默认每次重试次数减1,重新加入任务队列
                }
            }catch (Exception e){
                logger.error("DefaultHandler-----handle-----error", e);
            }

        }
    }
    ```
    
*   修改默认任务的重试次数
    ```java
    Task task = new Task(url).retry(num);//num为重试次数
    ```
*   自定义错误任务处理器
    ```java
    public class ErrorHandler implements IHandler {//实现IHandler接口

        @Override
        public void handle(TaskErrorResponse taskErrorResponse) {
            if(taskErrorResponse.getE() != null
            && taskErrorResponse.getE() instanceof Exception){
            //根据一些判断条件来检测任务并不是真的失败,可能由于网站的反扒机智,出现的, 此时可以自定义重试策略
                try {
                    System.out.println("----------=" + taskErrorResponse.getTask());
                    taskErrorResponse
                        .getQueue()
                        .fakerFailed(taskErrorResponse.getTask());
                        //fakerFailed假失败, 重置重试次数，并且重现放回任务队列
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    ```
*   创建爬虫Ant的地方使用该Handler
    ```java
    Ant ant = Ant
            .create()
            .startQueue(antQueue)
            .withHandler(new ErrorHandler())//使用上面的错误处理器处理
            .thread(1);
    ```

### 几个概念
*   Task 任务类 以下的参数都可以自定义
    ```java
    private String group = Constants.APP_TASK_GROUP_DEFAULT;//每一个任务都会有一个分组，如果没有设置，默认为 default
    private String url;//url
    private Map<String, Object> headers = new HashMap<>();//请求头
    private Map<String, Object> params = new HashMap<String, Object>();//请求参数
    private TaskResponse taskResponse;//执行任务后的返回
    private Object extr;//额外的数据
    private Integer retry = Constants.DEFAULT_TASK_RETRY;//重试次数
    private Integer deep = Constants.DEFAULT_TASK_DEEP;//任务的优先级
    private String userAgent;//这个就如同名字了
    ```

*   TaskResponse 任务返回类 
    ```java
    private boolean failed = false;//任务是否失败了
    private Task task;//任务
    private String content;//任务返回的文本内容
    private AntQueue queue;//队列
    private String failMsg;//失败的信息
    public Document getDoc(){//调用该方法直接获取 jsoup的Document对象
        if(StringUtil.isNotEmpty(this.content)){
            return Jsoup.parse(this.content);
        }
        return null;
    }
    ```

*   监控(后续打算开放这一块, 自定义要监控的属性)

    ```java
    AntQueue antQueue = TaskQueue.of();
    antQueue.push(new Task("https://github.com/"));
    Ant ant = Ant
            .create()
            .startQueue(antQueue)
            .pipeline(new SubPipeline())
            .withHandler(new ErrorHandler())
            .thread(1);
    AntMonitor
    .getInstance()
    .regist(ant);//注册jmx监控 使用jconsole即可观察 爬虫的运行线程个数, 成功个数, 爬虫的开始时间
    ant.run();
    ```
