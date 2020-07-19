package com.nbcb.mytomcat.chap8;

import com.nbcb.mytomcat.util.Constants;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLStreamHandler;

public class Test {
    public static void main(String[] args) throws IOException {
        URL[] urls = new URL[1];
        URLStreamHandler streamHandler = null;
        File classPath = new File(Constants.WEB_ROOT);

        String repository = null;
        repository = (new URL("file",null,
                classPath.getCanonicalPath() + File.separator)).toString();

        /**
         * respository: 这是一个url地址，
         * 地址形式主要是按照file协议访问WEB_ROOT下编译好的servlet class，比如：
         * file:/home/zhoushuo/Documents/testGit/TomcatTest/webroot/
         */
//            System.out.println("repository: " + repository);
        urls[0] = new URL(null,repository,streamHandler);
        System.out.println(repository);
    }
}
