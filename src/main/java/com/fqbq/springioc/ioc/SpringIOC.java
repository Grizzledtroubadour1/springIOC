package com.fqbq.springioc.ioc;

import com.fqbq.springioc.stereotype.Autowired;
import com.fqbq.springioc.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class SpringIOC {

    private List<String> beanNames;
    private List<String> filePaths;
    private String basePath;
    private String basePackage;

    private Map<String, Object> beans = new HashMap<>();

    /**
     * 扫描所有的文件信息信息，存到了 filePaths
     */
    private void scan() throws FileNotFoundException {
        File file = new File(basePath);
        filePaths = new ArrayList<>();
        if (file.exists()) {
            Queue<File> queue = new LinkedList<>();
            queue.add(file);
            while (!queue.isEmpty()) {
                File poll = queue.poll();
                if (poll == null) {
                    continue;
                }
                if (poll.isDirectory()) {
                    File[] files = poll.listFiles();
                    for (File f : files) {
                        queue.add(f);
                    }
                } else {
                    filePaths.add(poll.getPath());
                }
            }
        } else {
            throw new FileNotFoundException(basePath + " not found");
        }
    }

    /**
     * 讲所有的.java结尾的 全限定名放到 beanNames
     */
    public void initBeanNames() {
        for (String s : filePaths) {
            String replace = s.replace(basePath, "");
            if (replace.endsWith(".java")) {
                replace = replace.substring(0, replace.length() - 5);
            }

            char[] chars = replace.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '\\') {
                    chars[i] = '.';
                }
            }
            beanNames.add(basePackage + "." + new String(chars));
        }
    }

    public void initBeans() {
        for (String beanName : beanNames) {
            try {
                Class<?> aClass = Class.forName(beanName);
                Annotation[] declaredAnnotations = aClass.getDeclaredAnnotations();
                for (Annotation declaredAnnotation : declaredAnnotations) {
                    if (declaredAnnotation instanceof Component) {
                        Object o = aClass.newInstance();
                        beans.put(aClass.getName(), o);
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Field[] declaredFields = entry.getValue().getClass().getDeclaredFields();

            for (Field field : declaredFields) {

                Annotation[] declaredAnnotations = field.getDeclaredAnnotations();

                for (Annotation annotation : declaredAnnotations) {
                    if (annotation instanceof Autowired) {
                        //field 需要由我们来赋值
                        // 我们所持有的所有对象 在beans中
                        // 根据当前域中的类型的名字
                        String name = field.getType().getName();
                        // 从beans 中获得对应的对象
                        Object o = beans.get(name);
                        field.setAccessible(true);
                        try {
                            field.set(entry.getValue(), o);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }


        }
    }

    public Object getInstance(String beanName) {
        return beans.get(beanName);
    }

    private void initPath() {
        basePath = "F:\\qcby\\ioc\\src\\main\\java\\com\\fqbq\\springioc\\";
        basePackage = "com.fqbq.springioc";
    }

    public SpringIOC() {
        initPath();
        try {
            scan();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        beanNames = new ArrayList<>();
        initBeanNames();
    }
}
