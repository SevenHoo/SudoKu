package com.github.seven.sudoku.utils;

/**
 * Description: 自定义Log输出类 <br/>
 * Date: 2017/3/16 <br/>
 *
 * @author seven.hu@ubtrobot.com
 */

import android.text.TextUtils;
import android.util.Log;

public class MyLogger {

    /**
     * 是否打印LOG
     */
    private final static boolean DEBUG = true;
    /**
     * 指定log的TAG
     */
    public final static String TAG = "Sudoku";

    /**
     * 打印LOG的级别
     */
    private final static int LEVEL = Log.VERBOSE;
    /**
     * 一条LOG最长的字符数
     */
    private final static int LOG_MAX_LENGTH = 4000;
    /**
     * 自定义的LOG关键字
     */
    private static final String KEY = "@hyq@";


    private MyLogger(){

    }
    private static class InstanceHolder{
        private final static MyLogger INSTANCE = new MyLogger();
    }
    public static MyLogger log(){
        return InstanceHolder.INSTANCE;
    }

    /**
     * Get The Current Function Name
     *
     * @return
     */
    private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }
//
//            return KEY + "[ " + Thread.currentThread().getName() + ": " + st.getFileName() + ":"
//                    + st.getClassName() + ":" + st.getMethodName() + ":" + st.getLineNumber() + " ]";
            return "";
        }
        return null;
    }

    /**
     * The Log Level:i
     * @param str 输出内容
     */
    public void i(Object str) {

        if(!DEBUG){
            return;
        }

        if (LEVEL <= Log.INFO) {

            String name = getFunctionName();
            if(TextUtils.isEmpty(name)){
                printLog(Log.INFO, str.toString());
                return;
            }
            printLog(Log.INFO, name + " - " + str);
        }


    }

    /**
     * The Log Level:d
     * @param str 输出内容
     */
    public void d(Object str) {

        if(!DEBUG){
            return;
        }

        if (LEVEL <= Log.DEBUG) {

            String name = getFunctionName();
            if (TextUtils.isEmpty(name)) {
                printLog(Log.INFO, str.toString());
                return;
            }
            printLog(Log.INFO, name + " - " + str);
        }
    }

    /**
     * The Log Level:v
     * @param str 输出内容
     */
    public void v(Object str) {

        if(!DEBUG){
            return;
        }

        if (LEVEL <= Log.VERBOSE) {
            String name = getFunctionName();
            if (TextUtils.isEmpty(name)) {
                printLog(Log.VERBOSE, str.toString());
                return;
            }
            printLog(Log.VERBOSE, name + " - " + str);
        }
    }

    /**
     * The Log Level:w
     * @param str 输出内容
     */
    public void w(Object str) {

        if(!DEBUG){
            return;
        }

        if (LEVEL <= Log.WARN) {
            String name = getFunctionName();
            if (TextUtils.isEmpty(name)) {
                printLog(Log.WARN, str.toString());
                return;
            }
            printLog(Log.WARN, name + " - " + str);
        }
    }

    /**
     * The Log Level:e
     * @param str 输出内容
     */
    public void e(Object str) {

        if(!DEBUG){
            return;
        }

        if (LEVEL <= Log.ERROR) {
            String name = getFunctionName();
            if (TextUtils.isEmpty(name)) {
                printLog(Log.ERROR, str.toString());
                return;
            }
            printLog(Log.ERROR, name + " - " + str);
        }
    }

    /**
     * The Log Level:e
     * @param ex 打印异常
     */
    public void e(Exception ex) {

        if(!DEBUG){
            return;
        }

        if (LEVEL <= Log.ERROR) {
            Log.e(TAG, "error", ex);
        }

    }

    /**
     * The Log Level:e
     */
    public void e(String log, Throwable tr) {

        if(!DEBUG){
            return;
        }

        String line = getFunctionName();
        Log.e(TAG, "{Thread:" + Thread.currentThread().getName() + "}" + "[" + KEY + line
                + ":] " + log + "\n", tr);
    }

    /***
     *
     * printLog:因为logcat输出log的字符长度是4k，超过长度字符串会被丢弃，所以对超过4k长度的log做分段输出. <br/>
     * @param level
     * @param logText log内容
     */
    public void printLog(int level, String logText) {

        int index = 0;
        String sub;

        logText = logText.trim();

        while (index < logText.length()) {

            // java的字符不允许指定超过总的长度
            if (logText.length() <= index + LOG_MAX_LENGTH) {
                sub = logText.substring(index);
            } else {
                sub = logText.substring(index, index + LOG_MAX_LENGTH);
            }

            index += LOG_MAX_LENGTH;

            switch (level) {
                case Log.INFO:
                    Log.i(TAG, sub.trim());
                    break;
                case Log.DEBUG:
                    Log.d(TAG, sub.trim());
                    break;
                case Log.ERROR:
                    Log.e(TAG, sub.trim());
                    break;
                case Log.WARN:
                    Log.w(TAG, sub.trim());
                    break;
                case Log.VERBOSE:
                    Log.v(TAG, sub.trim());
                    break;
                default:
                    Log.e(TAG, sub.trim());
                    break;
            }
        }
    }

}

