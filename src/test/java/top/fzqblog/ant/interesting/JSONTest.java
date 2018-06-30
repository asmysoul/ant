package top.fzqblog.ant.interesting;

import com.alibaba.fastjson.JSON;
import top.fzqblog.ant.task.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 抽离 on 2018/6/30.
 */
public class JSONTest {
    
    public static void main(String[] args){
        Task task = new Task("");
        Map<String, Object> params = new HashMap<>();
        params.put("password", "THdmMTIzNDU2");
        params.put("telphone", "18950148923");
        params.put("url", "aHR0cHM6Ly93d3cuYW5qdWtlLmNvbQ==");
        params.put("signExpiresTime", "1800000");
        params.put("verifyType", "sms");
        task.setParams(params);
        String str = JSON.toJSONString(params);
        System.out.println("----------=" + str);
    }
}
