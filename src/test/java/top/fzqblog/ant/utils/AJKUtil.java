package top.fzqblog.ant.utils;

import com.alibaba.fastjson.JSONArray;


import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 抽离 on 2018/6/29.
 */
public class AJKUtil {


    public static String[] getAJKLoginStr(String document) {
        String regex = "var attrs = \\[[\\s\\S]*}]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(document);
        String result[] = new String[2];
        if (matcher.find()) {
            String scriptStr = matcher.group();
            scriptStr = scriptStr != null ? scriptStr.replaceAll("var attrs = ", "") : "";
            System.out.println("originArray----------=" + scriptStr);
            try {
                JSONArray jsonArray = JSONArray.parseArray(scriptStr);
                System.out.println("jsonArray----------=" + jsonArray);
                Map<String, String> nameMap = (Map<String, String>) jsonArray.get(0);
                Map<String, String> valueMap = (Map<String, String>) jsonArray.get(1);
                String name = nameMap.get("value");
                String value = valueMap.get("value");
                System.out.println("name----------=" + name);
                System.out.println("value----------=" + value);
                result[0] = name;
                result[1] = value;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String getVerifyUrl(String document){
        Pattern pattern = Pattern.compile("https://login.anjuke.com/login/verify.*\'");
        Matcher matcher = pattern.matcher(document);
        String verifyUrl = "";
        if(matcher.find()){
            verifyUrl = matcher.group();
            verifyUrl = verifyUrl.replaceAll("\'", "");
            System.out.println("verifyUrl----------=" + verifyUrl);
        }
        return verifyUrl;
    }

    public static String getNewVerifyUrl(String document){
        Pattern pattern = Pattern.compile("URL=.*\"");
        Matcher matcher = pattern.matcher(document);
        String newVerifyUrl = "";
        if(matcher.find()){
            newVerifyUrl = matcher.group();
            newVerifyUrl = newVerifyUrl.replaceAll("\"", "").replaceAll("URL=", "");
            System.out.println("newVerifyUrl----------=" + newVerifyUrl);
        }
        return newVerifyUrl;
    }

}