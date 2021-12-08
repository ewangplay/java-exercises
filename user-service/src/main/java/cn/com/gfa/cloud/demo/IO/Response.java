package cn.com.gfa.cloud.demo.IO;

import lombok.Data;

@Data
public class Response {
    private int code;
    private String message;
    private Object data;
}
