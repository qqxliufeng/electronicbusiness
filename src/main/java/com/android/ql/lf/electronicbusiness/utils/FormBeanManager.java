package com.android.ql.lf.electronicbusiness.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import com.android.ql.lf.electronicbusiness.component.Description;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by feng on 2017/7/7.
 */

/**
 * 此类是校验模型类的工具类，将模型类直接转化为要上传的Form表单，而且还可以校验模型类中各个字段的值情况，是否为空，字段值是不是合法的，如手机号，邮箱的校验
 * @param <T> 泛型类
 */
public class FormBeanManager<T> {

    public static final String phonePattern = "(13\\d|14[57]|15[^4,\\D]|17[13678]|18\\d)\\d{8}|170[0589]\\d{7}";

    private T t;
    private final ArrayList<Field> descriptionFields;

    public FormBeanManager(T t) {
        this.t = t;
        Field[] allFields = t.getClass().getDeclaredFields();
        descriptionFields = new ArrayList<>();
        if (allFields != null && allFields.length > 0) {
            for (Field field : allFields) {
                Annotation[] annotations = field.getAnnotations();
                if (annotations != null && annotations.length > 0) {
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof Description) {
                            descriptionFields.add(field);
                            break;
                        }
                    }
                }
            }
            Collections.sort(descriptionFields, new Comparator<Field>() {
                @Override
                public int compare(Field o1, Field o2) {
                    return ((Integer) o1.getAnnotation(Description.class).index()).compareTo(o2.getAnnotation(Description.class).index());
                }
            });
        }
    }

    @SuppressLint("WrongConstant")
    public String checkField() {
        try {
            for (Field field : descriptionFields) {
                if (field.getModifiers() == Modifier.PRIVATE) {
                    field.setAccessible(true);
                }
                Object o = field.get(t);
                Description description = field.getAnnotation(Description.class);
                if (o == null || TextUtils.isEmpty(o.toString())) {
                    return description.description();
                }
                if (description.isPhone()) {
                    Pattern r = Pattern.compile(phonePattern);
                    Matcher m = r.matcher(o.toString());
                    if (!m.matches()) {
                        return "正确的手机号";
                    }
                }
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return "";
    }


    public HashMap<String, Object> form(HashMap<String, Object> apiParams) {
        try {
            if (apiParams == null) {
                apiParams = new HashMap<>();
            }
            for (Field field : descriptionFields) {
                if (field.getModifiers() == Modifier.PRIVATE) {
                    field.setAccessible(true);
                }
                String key = field.getAnnotation(Description.class).formAlias();
                if ("".equals(key)) {
                    key = field.getName();
                }
                apiParams.put(key, field.get(t));
            }
            return apiParams;
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}