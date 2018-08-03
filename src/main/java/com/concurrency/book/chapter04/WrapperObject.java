package com.concurrency.book.chapter04;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WrapperObject {

    private class LegacyCode {
        int x;
        int y;

        public LegacyCode(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getX() {
            return x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getY() {
            return y;
        }

        public void m1() {
            setX(9);
            setY(0);
            System.out.println("m1() done");
        }

        public void m2() {
            setY(0);
            setX(9);
            System.out.println("m2() done");
        }

    }

    public WrapperObject() {
        this.legacyCode = new LegacyCode(1, 2);
    }

    private LegacyCode legacyCode;

    private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();

    private Thread processorThread;


    public void startTheActiveObject() {
        processorThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        queue.take().run();
                    } catch (InterruptedException e) {
                        // terminate
                        System.out.println("Active Object Done!");
                        break;
                    }
                }
            }
        }
        );
        processorThread.start();
    }

    private void invokeLegacyOp1() throws InterruptedException {
        queue.put(new Runnable() {
            @Override
            public void run() {
                legacyCode.m1();
                legacyCode.m2();
            }
        });
    }

    private void invokeLegacyOp2() throws InterruptedException {
        queue.put(new Runnable() {
            @Override
            public void run() {
                legacyCode.m2();
                legacyCode.m1();
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        WrapperObject wrapperObject = new WrapperObject();
        wrapperObject.startTheActiveObject();

        wrapperObject.invokeLegacyOp1();
        wrapperObject.invokeLegacyOp2();

        Thread.sleep(5000);
        wrapperObject.stop();
    }

    private void stop() {
        processorThread.interrupt();
    }

}
