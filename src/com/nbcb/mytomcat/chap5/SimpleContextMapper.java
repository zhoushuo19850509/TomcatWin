package com.nbcb.mytomcat.chap5;

import org.apache.catalina.Container;
import org.apache.catalina.Mapper;
import org.apache.catalina.Request;

/**
 * SimpleContextMapper实现类Mapper接口
 * 这个类是用于管理Context下多个Wrapper(servlet)
 */
public class SimpleContextMapper implements Mapper {
    @Override
    public Container getContainer() {
        return null;
    }

    @Override
    public void setContainer(Container container) {

    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public void setProtocol(String protocol) {

    }

    @Override
    public Container map(Request request, boolean update) {
        return null;
    }
}
