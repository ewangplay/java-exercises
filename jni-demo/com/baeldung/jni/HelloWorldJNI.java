package com.baeldung.jni;

public class HelloWorldJNI {
    static {
        System.loadLibrary("native");
    }
    
    public static void main(String[] args) {
        HelloWorldJNI example = new HelloWorldJNI();

        example.sayHello();

        long result = example.sumIntegers(5, 8);
        System.out.println(result);

        String hello = example.sayHelloToMe("Tom", false);
        System.out.println(hello);
    }
 
    private native void sayHello();

    public native long sumIntegers(int first, int second);
    
    public native String sayHelloToMe(String name, boolean isFemale);
}