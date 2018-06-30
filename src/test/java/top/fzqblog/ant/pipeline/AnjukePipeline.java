package top.fzqblog.ant.pipeline;

import org.apache.http.cookie.Cookie;
import top.fzqblog.ant.model.HttpRequestBody;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.task.TaskResponse;
import top.fzqblog.ant.utils.AJKUtil;
import top.fzqblog.ant.utils.Constants;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 抽离 on 2018/6/29.
 */
public class AnjukePipeline implements IPipeline {

    @Override
    public void stream(TaskResponse taskResponse) throws InterruptedException, IOException {

        if(taskResponse.isGroup("preLogin")){//获取验证之前的参数
            String doc = taskResponse.getContent();
            String result[] = AJKUtil.getAJKLoginStr(doc);
            String targetUrl = "https://vip.anjuke.com/login";
            Task task = Task.create(targetUrl);
            task.setGroup("login");
            task.setMehod(Constants.HTTP_POST);
            Map<String, Object> params = new HashMap<>();
            params.put("password", "THdmMTIzNDU2");
            params.put("telphone", "18950148923");
            params.put("url", "aHR0cHM6Ly93d3cuYW5qdWtlLmNvbQ==");
            params.put("signExpiresTime", "1800000");
            params.put("verifyType", "sms");
            params.put(result[0], result[1]);//页面参数
            task.setParams(params);
            task.setRequestBody(HttpRequestBody.form(params, Constants.DEFAULT_ENCODING));
            try {
                taskResponse.getQueue().push(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(taskResponse.isGroup("login")){//第一步验证 TODO 得先判断是否有登录成功的标志
            String doc = taskResponse.getContent();
            String verifyUrl = AJKUtil.getVerifyUrl(doc);
            Task task = Task.create(verifyUrl);
            String newVerifyUrl = AJKUtil.getNewVerifyUrl(doc);
            task.setExtr(newVerifyUrl);
            task.setGroup("verify");
            try {
                taskResponse.getQueue().push(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(taskResponse.isGroup("verify")){//第二部验证
            System.out.println("verify结果----------=" + taskResponse.getDoc());
            String newVerifyUrl = (String) taskResponse.getTask().getExtr();
            Task task = Task.create(newVerifyUrl);
            task.setGroup("newVerify");
            try {
                taskResponse.getQueue().push(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(taskResponse.isGroup("newVerify")){//调转首页
            List<Cookie> cookieList = taskResponse.getCookieList();
            String cookieString = "";
            for (Cookie cookie : cookieList) {
                String cookieName = cookie.getName();
                String cookieValue = cookie.getValue();
                cookieString += (cookieName + "=" + cookieValue + "; ");
            }
            System.out.println("newVerify结果----------=\r" + cookieString);
        }

    }


}
