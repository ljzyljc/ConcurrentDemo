package com.example.concurrentdemo.concurrent;

import java.util.HashMap;

public class TestDisruptor {

    //指定RingBuffer大小，必须是2的N次方

    int bufferSize = 1024;

    //构建Disruptor




    public static void main(String[] args) {


    }


    //自定义Event

    class LongEvent {
        private long value;

        public void setValue(long value) {
            this.value = value;
        }
    }


}
