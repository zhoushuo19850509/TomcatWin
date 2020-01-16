package com.nbcb.mytomcat.chap6.startup;

import com.nbcb.mytomcat.chap4.HttpConnector;
import com.nbcb.mytomcat.chap6.*;
import org.apache.catalina.*;

import java.io.IOException;

/**
 * ���BootStrap��ҪΪ����֤���ǵ�Lifecyle����
 */
public class BootStrap2 {

    public static void main(String[] args){
        /**
         * ����Chap4��HttpConnector����ServerSocket���������Կͻ��˵�����
         */
        HttpConnector connector = new HttpConnector();

        /**
         * ��BootSrap1��ͬ���ǣ����������Ҫ��������Servlet��
         * �ֱ�������Wrapper����������Wrapper�ҿ���һ��SimpleContext��
         */
        Wrapper wrapper1 = new SimpleWrapper();
        Wrapper wrapper2 = new SimpleWrapper();

        wrapper1.setName("Primitive");
        wrapper1.setServletClass("PrimitiveServlet");
        wrapper2.setName("Modern");
        wrapper2.setServletClass("ModernServlet");

        LifecycleListener wrapperListener = new SimpleWrapperLifecycleListener();
        ((SimpleWrapper) wrapper1).addLifecycleListener(wrapperListener);
        ((SimpleWrapper) wrapper2).addLifecycleListener(wrapperListener);

        Context context = new SimpleContext();
        context.addChild(wrapper1);
        context.addChild(wrapper2);

        /**
         * ���������Լ������SimpleLoader�࣬�����ر��ص�Servlet��
         * Ȼ�����ø�SimpleContext
         */
        Loader loader = new SimpleLoader();
        context.setLoader(loader);

        Mapper mapper = new com.nbcb.mytomcat.chap6.SimpleContextMapper();
        mapper.setProtocol("http");
        context.addMapper(mapper);

        /**
         * ����Ҫ����һ��ӳ���ϵ
         * ���ͻ���URL��Servlet��صĵ�ַ("/Modern")��Wrapper����("Modern")ӳ������
         */
        context.addServletMapping("/ModernServlet","Modern");
        context.addServletMapping("/PrimitiveServlet","Primitive");

        /**
         * ����һ��Listener��ר�������ڼ���SimpleContext��
         */
        LifecycleListener listener = new SimpleContextLifecycleListener();
        ((SimpleContext) context).addLifecycleListener(listener);
        /**
         * ��������ܹؼ������ǰ�context���ø�connector
         * ����connector������ͻ���http����֮�󣬾ͻ����context.invoke()����
         * ������ο�chap4/HttpProcessor.process(): container.invoke(request,response);
         */
        connector.setContainer(context);
        try {
            connector.initialize();
            connector.start();


            /**
             * ��ΪSimpleContextʵ����Lifecycle�ӿ�
             * ���Կ���ֱ�ӵ���start()�ӿ�
             */
            ((Lifecycle)context).start();
            /**
             * wait until we press any key
             */
            System.in.read();

            ((Lifecycle)context).stop();

        } catch (LifecycleException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
