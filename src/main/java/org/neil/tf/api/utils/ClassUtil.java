package org.neil.tf.api.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: Neil
 * @Description:
 * @CreateDate: 2020/11/9 23:20
 */
public class ClassUtil {

    public static Object runMethodByName(String name) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String classAndMethodName=name.substring(0,name.lastIndexOf("("));
        String className=classAndMethodName.substring(0,classAndMethodName.lastIndexOf("."));
        String methodName=classAndMethodName.substring(classAndMethodName.lastIndexOf(".")+1);
        String params=name.substring(name.indexOf("(")+1,name.indexOf(")"));
        Object[] objects=params.split(",");
        Class[] classes=new Class[objects.length];
        for (int j=0;j<objects.length;j++){
            classes[j]=String.class;
        }
        Class initClass=Class.forName(className);
        Object object=initClass.newInstance();
        Method method=initClass.getDeclaredMethod(methodName,classes);
        Object result=method.invoke(object,objects);
        return result;
    }
}
