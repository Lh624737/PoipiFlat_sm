package com.pospi.pai.yunpos.wifi_printer;

import com.pospi.dto.GoodsDto;
import com.pospi.dto.OrderDto;
import com.pospi.util.GetData;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Qiyan on 2016/7/25.
 */
public class PrintTest {
    private static Pos pos;

    public static void connevt(final String ip, final int dk, final List<GoodsDto> goodsDtos, final OrderDto orderDto) {
        // 开启一个子线程
        final int serial_number = Integer.parseInt(String.valueOf(orderDto.getMaxNo()).substring(6));

        new Thread() {
            public void run() {
                try {
                    pos = new Pos(ip, dk, "GBK");    //第一个参数是打印机网口IP
                    //初始化打印机
                    pos.initPos();
                    //初始化订单数据
                    pos.bold(true);
                    pos.printTabSpace(2);
                    pos.printWordSpace(1);
                    pos.printText("流水号：" + serial_number);
                    pos.printLocation(0);
                    pos.printTextNewLine("-------------------------------");
                    pos.bold(false);
                    pos.printTextNewLine("订 单 号：" + orderDto.getMaxNo());
                    pos.printTextNewLine("订单日期：" + GetData.getDataTime());
                    pos.printLine(2);
                    pos.printTextNewLine("--------------------------------");
                    pos.printText("品项");
                    pos.printWordSpace(1);
//                    pos.printLocation(10, 1);
                    pos.printText("单价");
                    pos.printWordSpace(1);
//                    pos.printLocation(10, 1);
                    pos.printWordSpace(1);
                    pos.printText("数量");
                    pos.printWordSpace(3);
                    pos.printText("小计");
                    pos.printTextNewLine("-------------------------------");
                    for (GoodsDto foods : goodsDtos) {
                        pos.printTextNewLine(foods.getName());
                        pos.printWordSpace(1);
                        pos.printText(String.valueOf(foods.getPrice()));
                        pos.printWordSpace(1);
                        pos.printWordSpace(1);
                        pos.printText(String.valueOf(foods.getNum()));
                        pos.printWordSpace(3);
                        pos.printText(String.valueOf(foods.getPrice() * foods.getNum()));
                    }
                    pos.printLocation(1);
                    pos.printLine(2);
                    pos.printText("\n\n\n");
                    //切纸
                    pos.feedAndCut();

                    pos.closeIOAndSocket();
                    pos = null;
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();

    }

    public static void test(final String ip, final int dk) {
        // 开启一个子线程
        new Thread() {
            public void run() {
                try {
                    pos = new Pos(ip, dk, "GBK");    //第一个参数是打印机网口IP
                    //初始化打印机
                    pos.initPos();
                    //初始化订单数据
                    pos.bold(true);
                    pos.printTabSpace(2);
                    pos.printWordSpace(1);
                    pos.printLocation(0);
                    pos.printTextNewLine("------------------------------------");
                    pos.bold(false);
                    pos.printTextNewLine("打印测试\n打印测试\n打印测试\n打印测试\n");

                    pos.printLocation(1);
                    pos.printLine(2);
                    //切纸
                    pos.feedAndCut();
                    pos.closeIOAndSocket();
                    pos = null;
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();

    }


    public static String CMC_QianXiang() {
        return new StringBuffer().append((char) 27).append((char) 112).append((char) 0).append((char) 60).append((char) 255).toString();
    }

    public static String CMD_CutPage() {
        return new StringBuffer().append((char) 27).append((char) 109).toString();
    }
}
