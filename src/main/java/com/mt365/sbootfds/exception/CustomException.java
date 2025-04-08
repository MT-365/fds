package com.mt365.sbootfds.exception;

/**
 * 自定义异常类，继承自 RuntimeException
 */
public class CustomException extends RuntimeException {

    /**
     * 构造函数，传递异常信息给父类
     *
     * @param msg 异常信息
     */
    public CustomException(String msg) {
        super(msg); // 调用父类构造函数，确保异常信息正确传递
    }

    /**
     * 获取异常信息，直接调用父类的 getMessage 方法
     *
     * @return 异常信息
     */
    @Override
    public String getMessage() {
        return super.getMessage(); // 使用父类的 message 字段
    }
}
