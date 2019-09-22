package com.nbcb.mytomcat.util;

import java.io.File;

/**
 * ±£¥Ê±‰¡ø
 */
public class Constants {

    /**
     * the web root directory of the java container
     *
     * this dir contains :
     * 1.the static resource of the app server
     * 2.the compiledservlet classes
     */
    public static final String WEB_ROOT = System.getProperty("user.dir") +
            File.separator +"webroot";



}
