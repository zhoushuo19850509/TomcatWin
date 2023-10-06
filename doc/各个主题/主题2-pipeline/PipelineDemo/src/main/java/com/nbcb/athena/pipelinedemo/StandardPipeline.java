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

        // ���currentΪnull ��˵��ֻ��һ��basic valve��û������valve
        if(null == current){
            current = basic;
        }

        // ���������б��˳��һ����ִ��valve
        while(null != current){
            if(current instanceof Lifecycle){
                ((Lifecycle) current).start();
            }
            current = current.getNext();
        }
    }
}
