package com.nbcb.mytomcat.chap5;

import com.nbcb.mytomcat.chap4.HttpConnector;
import org.apache.catalina.*;

import java.io.IOException;

/**
 * the bootstrap of Chap5
 * Ϊ�˿�������ļ�������Ҫ���Chap5���������ܹ�����
 * �ο�visio�ܹ�ͼ��
 * TomcatWin\doc\Tomcat����ܹ�ͼ.vsd\Chap5 ������
 */
public class BootStrap1 {
    public static void main(){
        /**
         * ����Chap4��HttpConnector����ServerSocket���������Կͻ��˵�����
         */
        HttpConnector connector = new HttpConnector();

        /**
         * �������ֻ����һ��Servlet
         * ����ֻҪ����һ��Wrapper���������������Servlet������
         */
        Wrapper wrapper = new SimpleWrapper();

        /**
         * ���������Լ������SimpleLoader�࣬�����ر��ص�Servlet��
         */
        Loader loader = new SimpleLoader();

        /**
         * new ����Valve����
         * ��������Wrapper����Servlet��ʱ�򣬻������������ˮ�߽ڵ�
         */
        Valve valve1 = new HeaderLoggerValve();
        Valve valve2 = new ClientIPLoggerValve();

        /**
         * ����wrapper��Loader
         * ���ڼ���Servletʵ��
         */
        wrapper.setLoader(loader);


        /**
         * ������ָ��Ҫ���õ�ServletName
         * ���ʣ����ServletName�ѵ����Ǵ�request url�л�ȡ�𣿣�
         * ���������Ȼ��Ҫ�Ż���
         */
        wrapper.setServletClass("");

        /**
         * ������֮ǰ�����������Valve���õ�wrapper��ˮ����
         */
        ((Pipeline)wrapper).addValve(valve1);
        ((Pipeline)wrapper).addValve(valve2);

        /**
         * ��������ܹؼ������ǰ�wrapper����Ϊconnector
         * ����connector������ͻ���http����֮�󣬾ͻ����wrapper.invoke()����
         * ������ο�chap4/HttpProcessor.process(): container.invoke(request,response);
         */
        connector.setContainer(wrapper);
        try {
            connector.initialize();
            connector.start();

            /**
             * wait until we press any key
             */
            System.in.read();

        } catch (LifecycleException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
