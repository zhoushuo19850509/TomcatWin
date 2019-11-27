package com.nbcb.mytomcat.chap5;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * SimplePipeline��Pipeline�ӿڵľ���ʵ����
 * ������ĳ��Wrapper(Servlet)����Ҫִ�е���ˮ������
 */
public class SimplePipeline implements Pipeline {

    private Container container;

    private SimpleWrapperValve basicValve;

    private List<Valve> valves;
    /**
     * constructor
     * ָ����ǰ���SimplePipeline�ǹҿ����ĸ�Container��
     * �����������µ������У��ҿ���SimpleWrapper��
     */
    public SimplePipeline(Container container){
        this.container = container;
    }

    @Override
    public Valve getBasic() {
        return null;
    }

    @Override
    public void setBasic(Valve valve) {
        this.basicValve = (SimpleWrapperValve)valve;

        /**
         * ��������basic valve�����������baisc valve�ҿ����ĸ�container��
         */
        this.basicValve.setContainer(this.container);
    }

    @Override
    public void addValve(Valve valve) {

    }

    @Override
    public Valve[] getValves() {
        return new Valve[0];
    }

    /**
     * ���invoke()����ʵ����Pipeline��invoke()����
     * �����Pipelineʵ����ĺ��ģ������ڲ���SimplePipelineValveContext��invokeNext������
     * ���ִ�е�ǰpipeline�е�valveʵ��
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        (new SimplePipelineValveContext()).invokeNext(request,response);

    }

    @Override
    public void removeValve(Valve valve) {

    }


    /**
     * inner class
     */
    protected class SimplePipelineValveContext implements ValveContext {


        public SimplePipelineValveContext(){

        }
        /**
         * ���stage��ʾ���Ѿ�ִ�е��ĸ�valve��
         */
        protected int stage = 0;
        @Override
        public String getInfo() {
            return null;
        }

        /**
         * ʵ����ValveContext�ӿڵķ���
         * ��Ҫ���������Ǳ������Pipeline�µ�Valve�б��������ִ��
         * ��Ҫ�ر�ע�����Valveִ�е�˳����������ͨValve�������ִ��Basic Valve
         * @param request The request currently being processed
         * @param response The response currently being created
         *
         * @throws IOException
         * @throws ServletException
         */
        @Override
        public void invokeNext(Request request, Response response) throws IOException, ServletException {


            int subscript = stage;
            stage = stage + 1;
            /**
             * ��ִ����ͨValves
             */
            if( subscript < valves.size()){
                valves.get(subscript).invoke(request,response,this);
            }

            /**
             * ���ִ��basic valve�������Ҫ��invoke Servlet
             */
            basicValve.invoke(request,response,this);
        }
    }

}

