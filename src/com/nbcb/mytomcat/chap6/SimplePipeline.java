package com.nbcb.mytomcat.chap6;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SimplePipeline��Pipeline�ӿڵľ���ʵ����
 * ������ĳ��Wrapper(Servlet)����Ҫִ�е���ˮ������
 */
public class SimplePipeline implements Pipeline,Lifecycle{


    private Container container;

    /**
     * ���pipeline��basic valve
     * ���valve��Ҫ��ְ����ǻ����ĵ���servlet
     * ��Ҫ�ر�ע�⣺���ﲻ�ܶ�������Valve��
     * ����SimpleContextValve����SimpleWrapperValve
     */
    private Valve basicValve;

    private List<Valve> valves;
    /**
     * constructor
     * ָ����ǰ���SimplePipeline�ǹҿ����ĸ�Container��
     * �����������µ������У��ҿ���SimpleWrapper��
     */
    public SimplePipeline(Container container){
        this.container = container;
        valves = new ArrayList<Valve>();
    }

    @Override
    public Valve getBasic() {
        return null;
    }

    @Override
    public void setBasic(Valve valve) {
        this.basicValve = valve;

        /**
         * 1.��������basic valve�����������baisc valve�ҿ����ĸ�container��
         * ��Ϊ����basic valve��Ҫ����container��Loader����������Servlet
         * 2.����ΪbasicValve�����ﶨ���ʱ�����ֻ��ʵ����Valve�ӿ�
         * ���ԣ����Ҫ����setContainer()�����Ļ���������ǿ������ת��ΪContained
         */
        ((Contained)this.basicValve).setContainer(this.container);
    }

    @Override
    public void addValve(Valve valve) {
        valves.add(valve);
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
         * ��Ҫ�ر�ע�����Valveִ�е�˳����ִ����ͨValve�������ִ��Basic Valve
         *
         * �ȵȣ�˵�õ�Ҫ����Pipeline�µ�Valve�б�
         * ����invokeNext()�����в�û��ѭ����ι��
         * ��ôPipeline�µ�Valve�б�����δ���ִ�е��أ�
         * ����ֻҪ���ο�һ��Valve�����ˣ���ClientIPLoggerValveΪ����
         * ClientIPLoggerValve.invoke()�������߼��������ģ�
         * �ȵ���valveConext.invokeNext()������Ȼ����ִ�е�ǰValve������߼�
         * Ҳ����˵��ÿ��valve.invoke()ִ��֮ǰ���ȵ�����һ��valve
         * ����ǵ��͵�����ģʽ�������ģʽ��,Խ���ں����valveԽ��ִ��
         * Ҳ����˵basic valve���ȱ�ִ��
         * @param request The request currently being processed
         * @param response The response currently being created
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
            }else if( (subscript == valves.size()) && (basicValve != null) ){
                /**
                 * ���ִ��basic valve�������Ҫ��invoke Servlet
                 */
                basicValve.invoke(request,response,this);
            }

        }
    }


    /**
     * ������5��������Ҫ��ʵ����Lifecycle�ӿڵķ���
     */
    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return new LifecycleListener[0];
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void start() throws LifecycleException {
        System.out.println("starting SimplePipeline by Lifecycle");
    }

    @Override
    public void stop() throws LifecycleException {

    }

}

