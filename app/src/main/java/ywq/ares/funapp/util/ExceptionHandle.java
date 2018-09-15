package ywq.ares.funapp.util;

import android.net.ParseException;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;


/**
 *
 * 用于处理 rxJava中可能出现的异常，优化提示
 * Created by ares on 2017/4/27.
 */

public class ExceptionHandle {

    private static final int UNAUTHORIZED = 401;
    private static final int NOT_EXIST = 204;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;


    public static final String NOT_EXIST_MSG="该记录不存在或已被删除";

    public static ResponseThrowable handleException(Throwable e) {
        ResponseThrowable ex;
       // CrashHandlerTool.getInstance().handleException(e);
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseThrowable(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case NOT_EXIST:
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.message = "网络异常，请检查网络是否可用";
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponseThrowable(resultException, resultException.code);
            ex.message = resultException.message;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ResponseThrowable(e, ERROR.PARSE_ERROR);
            ex.message = "解析错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponseThrowable(e, ERROR.NETWORK_ERROR);
            ex.message = "网络连接失败,请检查网络";

            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponseThrowable(e, ERROR.SSL_ERROR);
            ex.message = "证书验证失败";
            return ex;
        } else if(e instanceof SocketTimeoutException){
            ex = new ResponseThrowable(e, ERROR.TIME_OUT);
            ex.message = "连接超时";
            return ex;
        }else if(e instanceof ProtocolException){
            ex = new ResponseThrowable(e, ERROR.PROTOCOL_ERROR);
            ex.message = "网络文件流协议有误";
            return ex;
        }
        else if(e instanceof NullPointerException){
            ex = new ResponseThrowable(e, ERROR.NONE_DATA);
            ex.message = "无更多数据";
            return ex;
        }
        else {
            ex = new ResponseThrowable(e, ERROR.UNKNOWN);
            ex.message = "未知错误";
            return ex;
        }
    }


    /**
     * 约定异常
     */
    public static class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORK_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIME_OUT =1006;

        public static final int PROTOCOL_ERROR=1007;


        public static final int NONE_DATA=1008;

        public static final int NOT_EXIST =1009;
    }

    public static class ResponseThrowable extends Exception {
        public int code;
        public String message;

        public ResponseThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

    public class ServerException extends RuntimeException {
        public int code;
        public String message;
    }
}
