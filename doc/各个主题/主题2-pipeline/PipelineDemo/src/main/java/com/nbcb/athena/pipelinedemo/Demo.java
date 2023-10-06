package com.nbcb.athena.pipelinedemo;

/**
 * PipelineDemo���
 */
public class Demo {
    public static void main(String[] args) {
        Pipeline pipeline = new StandardPipeline();
        pipeline.setBasic(new MyBasicValve());
        pipeline.addValve(new MyValve1());
        pipeline.addValve(new MyValve2());

        ((StandardPipeline) pipeline).start();
    }
}
