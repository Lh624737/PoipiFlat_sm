package com.pospi.pai.yunpos.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.pax.dal.IDAL;
import com.pax.dal.IPrinter;
import com.pax.gl.page.IPage;
import com.pax.gl.page.PaxGLPage;
import com.pospi.dto.ValueCardDto;
import com.pospi.util.App;
import com.pospi.util.MyPrinter;

/**
 * Created by acer on 2018/1/17.
 */

public class EPrint implements MyPrinter{
    private Context context;
    private IDAL dal = null;
    public EPrint(Context context) {
        this.context = context;
        // get the instance of DAL
        dal = App.getInstance().getIdal();
    }

    public Bitmap generateMainInfo() {
        int FONT_BIG = 60;
        int FONT_NORMAL = 40;
        int FONT_SMALL = 30;
        PaxGLPage glPage = PaxGLPage.getInstance(context);
        IPage page = glPage.createPage();
        String temp = "";
        String title = "交易凭单";
        // 凭单抬头
        page.addLine().addUnit(title, FONT_BIG, IPage.EAlign.CENTER);

        // 商户编号
        page.addLine().addUnit("商户编号:12345678", FONT_NORMAL);

        // 终端编号/操作员号
        page.addLine().addUnit("终端编号:123", FONT_NORMAL).addUnit("操作员号:01", FONT_NORMAL, IPage.EAlign.RIGHT);

        // 批次号
        page.addLine().addUnit("批次号  :000012", FONT_NORMAL);
        page.addLine().addUnit("凭证号  :000012", FONT_NORMAL);
        page.addLine().addUnit("授权码  :123456", FONT_NORMAL);
        page.addLine().addUnit("参考号  :1234567891011", FONT_NORMAL);
        page.addLine().addUnit("金额:", FONT_NORMAL, IPage.EAlign.LEFT);
        page.addLine().addUnit("￥20", FONT_BIG, IPage.EAlign.CENTER);

        // 日期时间
        temp = "2017/10/18 12:00:00";
        page.addLine().addUnit("日期/时间:" + temp, FONT_NORMAL);

        page.addLine().addUnit("持卡人签名", FONT_NORMAL, IPage.EAlign.CENTER);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zxy);
//        page.addLine().addUnit(bitmap, EAlign.CENTER);
        page.addLine().addUnit("                       ", FONT_NORMAL, IPage.EAlign.CENTER);
        page.addLine().addUnit("                       ", FONT_NORMAL, IPage.EAlign.CENTER);
        page.addLine().addUnit("                       ", FONT_NORMAL, IPage.EAlign.CENTER);
        page.addLine().addUnit("本人确认以上交易，同意将其计入本卡账号", FONT_SMALL, IPage.EAlign.CENTER);
        page.addLine().addUnit("\n\n\n\n\n\n\n", FONT_NORMAL);
        return glPage.pageToBitmap(page, 576);
    }
    private void printerPrint() {

        Bitmap bitmap = generateMainInfo();
        dal.getPrinter().print(bitmap, new IPrinter.IPinterListener() {

            @Override
            public void onSucc() {
                Log.i("e500", "打印成功");

            }

            @Override
            public void onError(final int arg0) {
                Log.i("e500", "打印失败");
            }
        });

    }

    @Override
    public void starPrint() {
        printerPrint();
    }
}
