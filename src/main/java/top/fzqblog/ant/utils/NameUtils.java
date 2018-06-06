package top.fzqblog.ant.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NameUtils {

    private static Map<String, Integer> nameMap = new HashMap<>();
    private static Map<String, String> idMap = new HashMap<>();

    public synchronized static String name(Class clazz){
        String name = clazz.getSimpleName();
        Integer index = nameMap.getOrDefault(name, 1);
        nameMap.put(name, index + 1);
        return buildName(name,index);
    }

    public synchronized static String id(Class clazz){
        String name = clazz.getSimpleName();
        return idMap.getOrDefault(name, UUID.randomUUID().toString());
    }

    private static String buildName(String name, Integer index) {
        return name + "-" + index;
    }

}