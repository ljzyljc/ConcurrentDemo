package com.example.concurrentdemo.concurrent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.function.Supplier;

public class ThreadLocalTest {

    public static void main(String[] args){

    }


    private static class SafeDateFormat{

        static final ThreadLocal<DateFormat> threadLocal = ThreadLocal.withInitial(new Supplier<DateFormat>() {
            @Override
            public DateFormat get() {
                return new SimpleDateFormat("yyyy-mm-dd");
            }
        });

        static DateFormat get(){
            return threadLocal.get();
        }


    }


}
