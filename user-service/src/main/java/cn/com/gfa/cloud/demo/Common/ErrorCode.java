package cn.com.gfa.cloud.demo.Common;

public enum ErrorCode {
    Success(0, "操作成功"),
    RequestParamInvalid(10000, "请求参数无效"),
    UserNotExist(10001, "用户不存在"),
    DatabaseException(10002, "数据库异常");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}