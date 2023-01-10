package com.ruoyi.system.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.*;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 切面相关工具类
 *
 */
@Slf4j
public class AopUtils {



    /**
     * 获取AOP代理的方法的参数名称和参数值
     *
     * @param cls
     * @param clazzName
     * @param methodName
     * @param args
     * @return
     */
    public static Map<String, Object> getNameAndArgsMap(Class<?> cls, String clazzName, String methodName, Object[] args) {

        Map<String, Object> nameAndArgs = new HashMap<>();

        try {

            ClassPool pool = ClassPool.getDefault();
            ClassClassPath classPath = new ClassClassPath(cls);
            pool.insertClassPath(classPath);

            CtClass cc = pool.get(clazzName);
            CtMethod cm = cc.getDeclaredMethod(methodName);
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr == null) {
                // exception
            }
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for (int i = 0; i < cm.getParameterTypes().length; i++) {
                String variableName = attr.variableName(i + pos);
                System.out.println("参数名" + attr.variableName(i + pos));
                // paramNames即参数名
                nameAndArgs.put(attr.variableName(i + pos), args[i]);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return nameAndArgs;
    }

}
