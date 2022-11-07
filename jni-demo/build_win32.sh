#!/bin/sh

# 生成.h头文件
# javac -h . ./com/baeldung/jni/HelloWorldJNI.java

# 编译native动态库
g++ -c -I./jni com_baeldung_jni_HelloWorldJNI.cpp -o com_baeldung_jni_HelloWorldJNI.o
g++ -shared -o native.dll com_baeldung_jni_HelloWorldJNI.o -Wl,--add-stdcall-alias