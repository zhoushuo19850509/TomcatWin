package com.nbcb.mytomcat.util;

import java.io.File;
import java.util.List;

public class FileUtil {
    /**
     * 这个Util类主要嵌套遍历WEB-INF/classes目录下的所有class文件
     * 获取这些文件的lastModified字段，放到一个List中
     * 用于Loader的reload功能
     * @param rootPath  WEB-INF/classes目录的绝对路径，比如:
     *                   /Users/zhoushuo/Documents/workspace/TomcatWin/WEB-INF/classes
     * @param lastModiyiedDates  所有WEB-INF/classes目录下的class文件的lastModified字段放在这个list下
     */
    public static void getClassFileLastModifies(String rootPath, List<Long> lastModiyiedDates){
        File newFile = new File(rootPath);
        if(newFile.exists()){
            File[] files = newFile.listFiles();
            if(null != files){
                for(File file: files){
                    if(file.isDirectory()){
                        getClassFileLastModifies(file.getAbsolutePath(), lastModiyiedDates);
                    }else {
                        if(file.getName().endsWith(".class")){
                            lastModiyiedDates.add(file.lastModified());
                        }
                    }
                }
            }
        }
    }
}
