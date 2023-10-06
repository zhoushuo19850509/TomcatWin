package com.nbcb.athena.pipelinedemo;

public class StandardPipeline
        implements Pipeline, Lifecycle {

    // basic valve
    private Valve basic;

    // first valve
    private Valve first;


    // valve1->valve2-> ... current valve->basic valve
    public void addValve(Valve valve) {
        if(null == this.first){
            this.first = valve;
            valve.setNext(basic);
        }else{
            Valve current = first;
            while(null != current){
                if(current.getNext() == basic){
                    current.setNext(valve);
                    valve.setNext(basic);
                    break;
                }
                current = current.getNext();
            }
        }
    }

    public void setBasic(Valve valve) {
        this.basic = valve;
    }

    public void start() {
        System.out.println("start StandardPipeline ...");

        Valve current = first;

        // 如果current为null ，说明只有一个basic valve，没有其他valve
        if(null == current){
            current = basic;
        }

        // 遍历按照列表的顺序，一个个执行valve
        while(null != current){
            if(current instanceof Lifecycle){
                ((Lifecycle) current).start();
            }
            current = current.getNext();
        }
    }
}
