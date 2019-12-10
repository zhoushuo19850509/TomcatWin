package com.nbcb.mytomcat.chap5;

import com.nbcb.mytomcat.chap4.HttpConnector;
import org.apache.catalina.*;

import java.io.IOException;

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

        Context context = new SimpleContext();
        context.addChild(wrapper1);
        context.addChild(wrapper2);

        /**
         * ���������Լ������SimpleLoader�࣬�����ر��ص�Servlet��
         * Ȼ�����ø�SimpleContext
         */
        Loader loader = new SimpleLoader();
        context.setLoader(loader);

        /**
         * new ����Valve����
         * ��������Wrapper����Servlet��ʱ�򣬻������������ˮ�߽ڵ�
         */
        Valve valve1 = new HeaderLoggerValve();
        Valve valve2 = new ClientIPLoggerValve();

        Mapper mapper = new SimpleContextMapper();
        mapper.setProtocol("http");
        context.addMapper(mapper);

        /**
         * ����Ҫ����һ��ӳ���ϵ
         * ���ͻ���URL��Servlet��صĵ�ַ("/Modern")��Wrapper����("Modern")ӳ������
         */
        context.addServletMapping("/ModernServlet","Modern");
        context.addServletMapping("/PrimitiveServlet","Primitive");

        /**
         * ������֮ǰ�����������Valve���õ�SimpleContext��ˮ����
         */
        ((Pipeline)context).addValve(valve1);
        ((Pipeline)context).addValve(valve2);

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
