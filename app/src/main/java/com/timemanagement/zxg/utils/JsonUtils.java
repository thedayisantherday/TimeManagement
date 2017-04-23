package com.timemanagement.zxg.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON格式通用解析工具类，使用GSON可以实现相同功能
 * Created by zxg on 2016/10/8.
 * QQ:1092885570
 */
public class JsonUtils {
    /**
     * 将JSON字符对象转化成JavaBean
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T parseJsonToJavaBean(String jsonStr, Class<T> clazz) throws Exception {
        JSONObject jsonObject = (JSONObject) new JSONObject(jsonStr);

        T tClass = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if ("_id".equals(fieldName) || "$change".equals(fieldName)){
                continue;
            }
            Method method = clazz.getDeclaredMethod(
                    "set" + Tools.upperCaseFirstOne(fieldName), field.getType());
            Object arg = jsonObject.opt(fieldName);
            if (method != null && method.getName() != null){
                if (arg != null && !arg.toString().equals("null")  && !Tools.isEmpty(arg.toString())){
                    if (field.getType().getName().equals("int")) {
                        method.invoke(tClass, Integer.valueOf(arg.toString().trim()));
                    } else if (field.getType().getName().equals("long")) {
                        method.invoke(tClass, Long.valueOf(arg.toString().trim()));
                    } else if (field.getType().getName().equals("short")) {
                        method.invoke(tClass, Short.valueOf(arg.toString().trim()));
                    } else {
                        method.invoke(tClass, arg);
                    }
                }
            }
        }
        return tClass;
    }

    /**
     * 将JSON字符对象转化成JavaBean list
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> parseJsonToJavaBeanList(String jsonStr, Class<T> clazz) throws Exception {
        JSONArray jsonArray = new JSONArray(jsonStr);

        List<T> list = new ArrayList<T>();
        for (int i = 0; i < jsonArray.length(); i++) {
            T tClass = parseJsonToJavaBean(jsonArray.opt(i).toString(), clazz);
            list.add(tClass);
        }

        return list;
    }

    /**
     * 将一般的string类型的数据转换成Json格式的String数据
     * @param jsonStr
     * @return
     */
    public static String string2Jsonstring(String jsonStr) {
        int level = 0;
        StringBuffer sb_json = new StringBuffer();

        for (int i = 0; i < jsonStr.length(); i++) {
            char c = jsonStr.charAt(i);
            if (level>0 && '\n'==sb_json.charAt(sb_json.length()-1)){
                sb_json.append(getLevelString(level));
            }
            switch (c) {
                case '{':
                case '[':
                    sb_json.append(c + "\n");
                    level++;
                    break;
                case ',':
                    sb_json.append(c + "\n");
                    break;
                case '}':
                case ']':
                    sb_json.append("\n" );
                    level--;
                    sb_json.append(getLevelString(level) + c);
                    break;
                default:
                    sb_json.append(c);
                    break;
            }
        }

        return sb_json.toString();
    }

    /**
     * 根据层级决定多少个Tab空格
     * @param level
     * @return
     */
    public static String getLevelString(int level) {
        StringBuffer sb_level = new StringBuffer();
        for (int i = 0; i < level; i++) {
            sb_level.append("\t");
        }
        return sb_level.toString();
    }
}
