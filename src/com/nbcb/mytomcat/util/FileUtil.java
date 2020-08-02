package com.nbcb.mytomcat.util;

import java.io.File;
import java.util.List;

public class FileUtil {
    /**
     * ���Util����ҪǶ�ױ���WEB-INF/classesĿ¼�µ�����class�ļ�
     * ��ȡ��Щ�ļ���lastModified�ֶΣ��ŵ�һ��List��
     * ����Loader��reload����
     * @param rootPath  WEB-INF/classesĿ¼�ľ���·��������:
     *                   /Users/zhoushuo/Documents/workspace/TomcatWin/WEB-INF/classes
     * @param lastModiyiedDates  ����WEB-INF/classesĿ¼�µ�class�ļ���lastModified�ֶη������list��
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
