package com.nbcb.mytomcat.util;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class LifecycleSupport {

    public LifecycleSupport(Lifecycle lifecycle){
        this.lifecycle = lifecycle;

    }

    private Lifecycle lifecycle;
    private List<LifecycleListener> listeners = new ArrayList<LifecycleListener>();

    public void addLifecycleListener(LifecycleListener listener){
        listeners.add(listener);
    }

    public List<LifecycleListener> findLifecycleListener(){
        return listeners;
    }

    /**
     * ���������Ҫ�Ǹ���һ��listners���µ�List��
     * Ȼ�󴥷�����µ�List�е�ÿ���������ض��¼���
     * @param type
     * @param data
     */
    public void fireLifecycleEvent(String type , Object data){
        LifecycleEvent event = new LifecycleEvent(lifecycle,type,data);

        /**
         * clone the listeners to interested
         */
        List<LifecycleListener> interested = new ArrayList<LifecycleListener>();
        interested.addAll(listeners);

        for(LifecycleListener currentListener: interested){
            currentListener.lifecycleEvent(event);
        }

    }

    public void removeLifecycleListener(LifecycleListener listener){
        this.listeners.remove(listener);
    }
}
