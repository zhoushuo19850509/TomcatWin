package com.nbcb.mytomcat.chap6;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;

/**
 * ��������࣬��Ҫ�����ڼ���SimpleContext���� start()/stop()����ʱ�Ļص�
 */
public class SimpleContextLifecycleListener implements LifecycleListener {
    @Override
    public void lifecycleEvent(LifecycleEvent event) {
        Lifecycle lifecycle = event.getLifecycle();

        if(Lifecycle.START_EVENT.equals(event.getType())){
            System.out.println("SimpleContext starting ");
        }
        if(Lifecycle.STOP_EVENT.equals(event.getType())){
            System.out.println("SimpleContext stopping  ");
        }
    }
}
