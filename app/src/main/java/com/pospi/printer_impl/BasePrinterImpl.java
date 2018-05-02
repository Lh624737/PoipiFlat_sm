package com.pospi.printer_impl;

/**
 * Created by Qiyan on 2016/5/17.
 */
public interface BasePrinterImpl {

    void initPos();//初始化打印机

    void close();//关闭所有流，链接

    void bold(boolean flag);//加粗

    void printText(String text);//打印文字

    void printLine(int lineNum);//打印空行

    void printLocation(int position);//调整打印位置

    void printLocation(int light, int weight);//绝对打印位置

    void printTextNewLine(String text);//新起一行，打印文字

}
