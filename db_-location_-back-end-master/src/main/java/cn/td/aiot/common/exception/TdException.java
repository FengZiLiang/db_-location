package cn.td.aiot.common.exception;

/**
 * TD 系统内部异常
 */
public class TdException extends Exception {

    private static final long serialVersionUID = -994962710559017255L;

    public TdException(String message) {
        super(message);
    }
}
