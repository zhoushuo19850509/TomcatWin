package com.nbcb.mytomcat.chap4.client;

/**
 * ���������Ҫ��ģ��HttpClient�������ʱ���Tomcat����
 * ���Ե��������߳�����ÿ���߳��ظ����д���
 *
 * Ŀ���ǣ�
 * 1.��֤����Tomcat֧�ֲ������ܣ�
 * 2.��֤����Tomcat http persistent connection�Ƿ��������������
 */
public class StartBatch {
    public static void main(String[] args){

        /**
         * �����߳���
         */
        int threadCount = 60;

        /**
         * ÿ���߳�ѭ��������ٴ�����
         */
        int loopCount = 500;

        for(int i = 0 ; i < threadCount ; i++){
//            Thread thread = new HttpClientThread(loopCount,i);
            new Thread(new HttpClientThread(loopCount,i)).start();

        }

    }
}
