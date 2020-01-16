package com.nbcb.mytomcat.chap6;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * SimplePipeline是Pipeline接口的具体实现类
 * 代表了某个Wrapper(Servlet)下需要执行的流水线任务
 */
public class SimplePipeline implements Pipeline,Lifecycle{


    private Container container;

    /**
     * 这个pipeline的basic valve
     * 这个valve主要的职责就是基本的调用servlet
     * 需要特别注意：这里不能定义具体的Valve，
     * 比如SimpleContextValve或者SimpleWrapperValve
     */
    private Valve basicValve;

    private List<Valve> valves;
    /**
     * constructor
     * 指定当前这个SimplePipeline是挂靠在哪个Container下
     * 比如我们这章的例子中，挂靠在SimpleWrapper下
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
         * 1.处理设置basic valve，还告诉这个baisc valve挂靠在哪个container下
         * 因为后续basic valve还要访问container的Loader，用来加载Servlet
         * 2.又因为basicValve在这里定义的时候仅仅只是实现了Valve接口
         * 所以，如果要调用setContainer()方法的话，必须先强制类型转化为Contained
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
         * 需要特别注意的是Valve执行的顺序：先执行普通Valve，最后再执行Basic Valve
         *
         * 等等！说好的要遍历Pipeline下的Valve列表，
         * 可是invokeNext()方法中并没有循环啊喂！
         * 那么Pipeline下的Valve列表是如何串行执行的呢？
         * 我们只要随便参考一个Valve就行了，以ClientIPLoggerValve为例：
         * ClientIPLoggerValve.invoke()方法的逻辑是这样的：
         * 先调用valveConext.invokeNext()方法，然后再执行当前Valve本身的逻辑
         * 也就是说，每个valve.invoke()执行之前，先调用下一个valve
         * 这就是典型的逆序模式。在这个模式下,越排在后面的valve越先执行
         * 也就是说basic valve最先被执行
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
             * 先执行普通Valves
             */
            if( subscript < valves.size()){
                valves.get(subscript).invoke(request,response,this);
            }else if( (subscript == valves.size()) && (basicValve != null) ){
                /**
                 * 最后执行basic valve，这个主要是invoke Servlet
                 */
                basicValve.invoke(request,response,this);
            }

        }
    }


    /**
     * 下面这5个方法主要是实现了Lifecycle接口的方法
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

