package com.java.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * Hello world!
 *
 */
public class App 
{
	
	private static ApplicationContext applicationContext;

    private static CountDownLatch countDownLatch;
	
    public static void main( String[] args )
    {

        System.out.println( "Hello World!" );


        init();


        Log logger = LogFactory.getLog(App.class);
        if (logger.isDebugEnabled()) {
            logger.debug("xxxx");
        } else {
            logger.debug("xxxx11111");
        }


        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                System.out.println(" server is running ....");
                while (true) {

                }

            }
        }).start();




    }
    

    //开启并发线程测试
    public static void doConcurentTest (int threadCounts,final ITestListener testListener){
        final Object waitObj = new Object();
        countDownLatch = new CountDownLatch(threadCounts);
        for (int i=0 ;i< threadCounts ;i++) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    synchronized (waitObj) {
                        try {
                            waitObj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                    if (testListener != null) {
                        testListener.doTest();
                    }


                    countDownLatch.countDown();
                }
            }).start();
        }


        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (waitObj) {
            waitObj.notifyAll();
        }

    }


    //测试方法回调
    public interface ITestListener {
        public void doTest();
    }
    
    public static void init(){

    	applicationContext = new FileSystemXmlApplicationContext("/resources/applicationContext.xml"); 
    	
    }
}
