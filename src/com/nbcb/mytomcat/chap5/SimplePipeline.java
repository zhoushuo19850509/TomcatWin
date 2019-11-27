package com.nbcb.mytomcat.chap5;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * SimplePipeline是Pipeline接口的具体实现类
 * 代表了某个Wrapper(Servlet)下需要执行的流水线任务
 */
public class SimplePipeline implements Pipeline {

    private Container container;

    private SimpleWrapperValve basicValve;

    private List<Valve> valves;
    /**
     * constructor
     * 指定当前这个SimplePipeline是挂靠在哪个Container下
     * 比如我们这章的例子中，挂靠在SimpleWrapper下
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
         * 处理设置basic valve，还告诉这个baisc valve挂靠在哪个container下
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
     * 这个invoke()方法实现了Pipeline的invoke()方法
     * 是这个Pipeline实现类的核心：调用内部类SimplePipelineValveContext的invokeNext方法，
     * 逐个执行当前pipeline中的valve实例
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
         * 这个stage表示，已经执行到哪个valve了
         */
        protected int stage = 0;
        @Override
        public String getInfo() {
            return null;
        }

        /**
         * 实现类ValveContext接口的方法
         * 主要做的事情是遍历这个Pipeline下的Valve列表，并且逐个执行
         * 需要特别注意的是Valve执行的顺序：限制性普通Valve，最后再执行Basic Valve
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
             * 先执行普通Valves
             */
            if( subscript < valves.size()){
                valves.get(subscript).invoke(request,response,this);
            }

            /**
             * 最后执行basic valve，这个主要是invoke Servlet
             */
            basicValve.invoke(request,response,this);
        }
    }

}

