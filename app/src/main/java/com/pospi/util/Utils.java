package com.pospi.util;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.IBinder;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.gprinter.aidl.GpService;
import com.gprinter.service.GpPrintService;
import com.pospi.dao.PayWayDao;
import com.pospi.dto.GoodsDto;
import com.pospi.util.constant.PayWay;

import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by huangqi on 2016/11/22.
 */
public class Utils {
    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**
     * md5加密
     *
     * @param plaintext 明文
     * @return ciphertext 密文
     */
    public static String md5Encrypt(String plaintext) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = plaintext.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 按照字段名的ascii码从小到大排序后使用QueryString的格式
     */
    public static String getSign(Map<String, String> params) {
        Map<String, String> sortMap = new TreeMap<String, String>();
        sortMap.putAll(params);
        // 以k1=v1&k2=v2...方式拼接参数
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> s : sortMap.entrySet()) {
            String k = s.getKey();
            String v = s.getValue();
            if (StringUtils.isBlank(v)) {// 过滤空值
                continue;
            }
            builder.append(k).append("=").append(v).append("&");
        }
        if (!sortMap.isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    static class PrinterServiceConnection implements ServiceConnection {
        private GpService mGpService;

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "Main_Login onServiceDisconnected()");
            mGpService = null;
            connection();
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("ServiceConnection", "Main_Login onServiceConnected()");
            Log.i("ServiceConnection", "Main_Login onServiceConnected()");
            mGpService = GpService.Stub.asInterface(service);
            App.setmGpService(mGpService);
        }
    }

    public static void connection() {
        PrinterServiceConnection conn = new PrinterServiceConnection();
        Intent intent = new Intent(App.getContext(), GpPrintService.class);
        App.getContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);// bindService
    }

    /**
     * 得到小票图片路径
     *
     * @param context
     * @param maxNO
     * @param payway
     * @param payMoney
     * @return
     */
    public static String getTextBitmap(Context context, String maxNO, String payway, String payMoney) {
        String file_path = null;
        String str;

        // 计算小票图片的高度: 标题高度50 + 固定行数14 *30 + 商品清单数 *2 *30 + 支付方式数*30
        final String goods = context.getSharedPreferences("goodsdto_json", Context.MODE_PRIVATE).getString("json", null);//
        List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(goods);
        int height = 50 + 14 * 30 + goodsDtos.size() * 2 * 30;
        System.out.print("height:" + height);

        // 打印高度计算：每多一行加30
        Bitmap bitmap = Bitmap.createBitmap(450, 1000, Bitmap.Config.ARGB_8888);
        try {
            SharedPreferences preferences = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE);//StoreMessage  存储的店铺的信息
            String shop_name = preferences.getString("Name", "");//得到存储的店铺的名字
            String shop_address = preferences.getString("Address", "");//得到店铺的地址
            String shop_phone = preferences.getString("Phone", "");//得到店铺的联系电话


            SharedPreferences preferences1 = context.getSharedPreferences("Token", Context.MODE_PRIVATE);//得到第一个登陆之后存储的Token  里面存储的有机器的设备号 店铺的id
            String value = preferences1.getString("value", "");
            String[] names = value.split("\\,");
            String shop_id = names[3];//得到店铺的id 也就是门店号

            int whichOne = context.getSharedPreferences("islogin", Context.MODE_PRIVATE).getInt("which", 0);
            String cashier_name = new CashierLogin_pareseJson().parese(
                    context.getSharedPreferences("cashierMsgDtos", Context.MODE_PRIVATE)
                            .getString("cashierMsgDtos", ""))
                    .get(whichOne).getName();

            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);//设置白色背景
            TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(1);
            paint.setColor(Color.BLACK);
            paint.setTypeface(Typeface.SANS_SERIF);

            String content = "";
            StaticLayout layout;

            // 打印标题--购物小票
            paint.setTextSize(32);
            content = shop_name;
            layout = new StaticLayout(content, paint, (int) (400),
                    Layout.Alignment.ALIGN_CENTER, 1F, 0, false);
            canvas.translate(0, 10);
            layout.draw(canvas);

            // 打印单号
            paint.setTextSize(22);
            content = "单号: " + maxNO + "\r\n";
            layout = new StaticLayout(content, paint, (int) (400),
                    Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
            canvas.translate(0, 50);
            layout.draw(canvas);

            // 打印交易时间
            content = "时间: " + GetData.getDataTime() + "\r\n";
            layout = new StaticLayout(content, paint, (int) (400),
                    Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
            canvas.translate(0, 30);
            layout.draw(canvas);

            // 打印收银员
            content = "收银员： " + cashier_name + "\r\n";
            layout = new StaticLayout(content, paint, (int) (400),
                    Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
            canvas.translate(0, 30);
            layout.draw(canvas);

            // 分割线
            content = "----------------------------------------------------------------\r\n";
            layout = new StaticLayout(content, paint, (int) (400),
                    Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
            canvas.translate(0, 30);
            layout.draw(canvas);

            // 列名称
            content = "商品名            单价     数量     金额     折扣\r\n";
            layout = new StaticLayout(content, paint, (int) (400),
                    Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
            canvas.translate(0, 30);
            layout.draw(canvas);

            // 商品明细
            int nums = 0;
            double moneys = 0.00;
            double discounts = 0.00;
            for (GoodsDto goodsDto : goodsDtos) {
                double price = goodsDto.getPrice();//单价
                int num = goodsDto.getNum();
                double discount = goodsDto.getDiscount();
                discounts += discount;
                nums += num;
                moneys = moneys + num * price - discount;

                // 商品名称
                content = goodsDto.getName() + "\r\n";
                layout = new StaticLayout(content, paint, (int) (400),
                        Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
                canvas.translate(0, 30);
                layout.draw(canvas);

                // 价格明细
                content = "                " + price + "     " + num + "     "
                        + (num * price - discount) + "     " + discount + "\r\n";
                layout = new StaticLayout(content, paint, (int) (400),
                        Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
                canvas.translate(0, 30);
                layout.draw(canvas);
            }

            // 分割线
            content = "----------------------------小计----------------------------\r\n";
            layout = new StaticLayout(content, paint, (int) (400),
                    Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
            canvas.translate(0, 30);
            layout.draw(canvas);

            // 小计内容
            content = "小计数量：  " + nums + "    小计金额：  " + moneys
                    + "\r\n";
            layout = new StaticLayout(content, paint, (int) (400),
                    Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
            canvas.translate(0, 30);
            layout.draw(canvas);

            // 分割线
            content = "-------------------------付款方式-------------------------\r\n";
            layout = new StaticLayout(content, paint, (int) (400),
                    Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
            canvas.translate(0, 30);
            layout.draw(canvas);

            // 付款方式内容
            content = new PayWayDao(context).findPayName(payway) + "\r\n";
            layout = new StaticLayout(content, paint, (int) (400),
                    Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
            canvas.translate(0, 30);
            layout.draw(canvas);

            // 分割线
            content = "----------------------------------------------------------------\r\n";
            layout = new StaticLayout(content, paint, (int) (400),
                    Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
            canvas.translate(0, 30);
            layout.draw(canvas);

            // 应收-实收
            content = "应收：   " + moneys + "   实收：    "
                    + payMoney + "\r\n";
            layout = new StaticLayout(content, paint, (int) (400),
                    Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
            canvas.translate(0, 30);
            layout.draw(canvas);

            // 找零-优惠
            if (payway.equals(String.valueOf(PayWay.CASH)) || payway.equals(String.valueOf(PayWay.OTHER))) {
                content = "找零：   " + (Double.parseDouble(payMoney) - moneys) + "   总优惠：    "
                        + discounts + "\r\n";
                layout = new StaticLayout(content, paint, (int) (400),
                        Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
                canvas.translate(0, 30);
                layout.draw(canvas);
            }

            // 分割线
            content = "----------------------------------------------------------------\r\n\r\n\r\n\r";
            layout = new StaticLayout(content, paint, (int) (400),
                    Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
            canvas.translate(0, 30);
            layout.draw(canvas);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
            String path = Environment.getExternalStorageDirectory()
                    + "/image.jpeg";
            System.out.println(path);
            FileOutputStream os = new FileOutputStream(new File(path));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            file_path = path;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file_path;
    }

    public static String getContent(Context context, String maxNO, String payway, String payMoney) {
        String str = "";
        // 计算小票图片的高度: 标题高度50 + 固定行数14 *30 + 商品清单数 *2 *30 + 支付方式数*30
        final String goods = context.getSharedPreferences("goodsdto_json", Context.MODE_PRIVATE).getString("json", null);//
        List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(goods);
        try {
            SharedPreferences preferences = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE);//StoreMessage  存储的店铺的信息
            String shop_name = preferences.getString("Name", "");//得到存储的店铺的名字
            String shop_address = preferences.getString("Address", "");//得到店铺的地址
            String shop_phone = preferences.getString("Phone", "");//得到店铺的联系电话

            SharedPreferences preferences1 = context.getSharedPreferences("Token", Context.MODE_PRIVATE);//得到第一个登陆之后存储的Token  里面存储的有机器的设备号 店铺的id
            String value = preferences1.getString("value", "");
            String[] names = value.split("\\,");
            String shop_id = names[3];//得到店铺的id 也就是门店号

            int whichOne = context.getSharedPreferences("islogin", Context.MODE_PRIVATE).getInt("which", 0);
            String cashier_name = new CashierLogin_pareseJson().parese(
                    context.getSharedPreferences("cashierMsgDtos", Context.MODE_PRIVATE)
                            .getString("cashierMsgDtos", ""))
                    .get(whichOne).getName();


            str += "            " + shop_name + "\n";
            str += "单号: " + maxNO + "\r\n";
            str += "时间: " + GetData.getDataTime() + "\r\n";
            str += "收银员：" + cashier_name + "\r\n";
            str += "--------------------------------\r\n";
            str += "品名   单价   数量   金额   折扣\r\n";

            // 商品明细
            int nums = 0;
            double moneys = 0.00;
            double discounts = 0.00;
            for (GoodsDto goodsDto : goodsDtos) {
                double price = goodsDto.getPrice();//单价
                int num = goodsDto.getNum();
                double discount = goodsDto.getDiscount();
                discounts += discount;
                nums += num;
                moneys = moneys + num * price - discount;

                // 商品名称
                str += goodsDto.getName() + "\r\n";
                str += "      " + price + "    " + num + "    "
                        + (num * price - discount) + "    " + discount + "\r\n";
            }

            // 分割线
            str += "--------------小计--------------\r\n";
            str += "小计数量：" + nums + "  小计金额：" + moneys
                    + "\r\n";
            str += "------------付款方式------------\r\n";
            str += new PayWayDao(context).findPayName(payway) + "\r\n";
            str += "--------------------------------\r\n";
            str += "应收： " + moneys + "   实收： "
                    + payMoney + "\r\n";

            // 找零-优惠
            if (payway.equals(String.valueOf(PayWay.CASH)) || payway.equals(String.valueOf(PayWay.OTHER))) {
                str += "找零： " + (Double.parseDouble(payMoney) - moneys) + "   总优惠： "
                        + discounts + "\r\n";
            }

            // 分割线
            str += "--------------------------------\r\n\r\n\r\n";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getContent(Context context, String maxNO, String payway, String payMoney, boolean refund) {
        String str = "";
        // 计算小票图片的高度: 标题高度50 + 固定行数14 *30 + 商品清单数 *2 *30 + 支付方式数*30
        final String goods = context.getSharedPreferences("goodsdto_json", Context.MODE_PRIVATE).getString("json", null);//
        List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(goods);
        try {
            SharedPreferences preferences = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE);//StoreMessage  存储的店铺的信息
            String shop_name = preferences.getString("Name", "");//得到存储的店铺的名字
            String shop_address = preferences.getString("Address", "");//得到店铺的地址
            String shop_phone = preferences.getString("Phone", "");//得到店铺的联系电话

            SharedPreferences preferences1 = context.getSharedPreferences("Token", Context.MODE_PRIVATE);//得到第一个登陆之后存储的Token  里面存储的有机器的设备号 店铺的id
            String value = preferences1.getString("value", "");
            String[] names = value.split("\\,");
            String shop_id = names[3];//得到店铺的id 也就是门店号

            int whichOne = context.getSharedPreferences("islogin", Context.MODE_PRIVATE).getInt("which", 0);
            String cashier_name = new CashierLogin_pareseJson().parese(
                    context.getSharedPreferences("cashierMsgDtos", Context.MODE_PRIVATE)
                            .getString("cashierMsgDtos", ""))
                    .get(whichOne).getName();


            str += "            " + shop_name + "\n";
            str += "单号: " + maxNO + "\r\n";
            str += "时间: " + GetData.getDataTime() + "\r\n";
            str += "收银员：" + cashier_name + "\r\n";
            str += "--------------------------------\r\n";
            str += "品名   单价   数量   金额   折扣\r\n";

            // 商品明细
            int nums = 0;
            double moneys = 0.00;
            double discounts = 0.00;
            for (GoodsDto goodsDto : goodsDtos) {
                double price = goodsDto.getPrice();//单价
                int num = goodsDto.getNum();
                double discount = goodsDto.getDiscount();
                discounts += discount;
                nums += num;
                moneys = moneys + num * price - discount;

                // 商品名称
                str += goodsDto.getName() + "\r\n";
                str += "      " + price + "    " + (-num) + "    "
                        + (-(num * price - discount)) + "    " + discount + "\r\n";
            }

            // 分割线
            str += "--------------小计--------------\r\n";
            str += "小计数量：" + (-nums) + "  小计金额：" + (-moneys)
                    + "\r\n";
            str += "------------付款方式------------\r\n";
            str += new PayWayDao(context).findPayName(payway) + "\r\n";
            str += "--------------------------------\r\n";
            str += "应收： " + (-moneys) + "   实收： "
                    + payMoney + "\r\n";

            // 找零-优惠
            if (payway.equals(String.valueOf(PayWay.CASH)) || payway.equals(String.valueOf(PayWay.OTHER))) {
                str += "找零： " + (Double.parseDouble(payMoney) - moneys) + "   总优惠： "
                        + discounts + "\r\n";
            }

            // 分割线
            str += "--------------------------------\r\n\r\n\r\n";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    // 将InputStream转换成byte[]
    public static byte[] InputStream2Bytes(InputStream is) {
        String str = "";
        byte[] readByte = new byte[1024];
        int readCount = -1;
        try {
            while ((readCount = is.read(readByte, 0, 1024)) != -1) {
                str += new String(readByte).trim();
            }
            return str.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 将Bitmap转换成InputStream
    public static InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    public static void cashopen() {
        try {
            Runtime.getRuntime()
                    .exec("echo 1 > /sys/devices/platform/power_ctrl/printer")
                    .waitFor();
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

//    public static void cashopen() {
//        String[] coms = new String[]{"mount -o rw,remount /sys", "echo \"1\" > /sys/devices/platform/power_ctrl/printer"};
//        ShellUtils.CommandResult com = ShellUtils.execCommand(coms, true);
//    }

    /**
     * 拷贝数据库至file文件夹下
     *
     * @param dbName 数据库名称
     */
    public static void initAddressDB(Context context, String dbName) {
        //1,在files文件夹下创建同名dbName数据库文件过程
        File file = new File(Environment.getExternalStorageDirectory()
                + "/ipos/data", "database.db");//创建名为dbName的文件
        if (file.exists()) {
            return;
        }
        //2.输入流读取第三方资产目录下的文件
        InputStream stream = null;
        FileOutputStream fos = null;
        try {
            stream = context.getAssets().open("data/data/com.pospi.pai.pospiflat/databases/orderinfo.db");
            //3,将读取的内容写入到指定文件夹的文件中去
            fos = new FileOutputStream(file);
            byte[] bs = new byte[1024];
            int temp = -1;
            while ((temp = stream.read(bs)) != -1) {
                fos.write(bs, 0, temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null && fos != null) {
                try {
                    stream.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
