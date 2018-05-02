package com.pospi.paxprinter;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.pax.api.PrintException;
import com.pax.api.PrintManager;
import com.pax.api.model.ST_FONT;

public class PrnTest {
    private static PrintManager inst;

    /**
     * 获取PrintManager实例
     */
    static {
        try {
            inst = PrintManager.getInstance();
        } catch (PrintException e) {
            Log.i("PAYexception", "exception");
            e.printStackTrace();
        }
    }

    /**
     * 打印缓存区内容
     */
    public static void prnStart() {
        try {
            inst.prnStart();
        } catch (PrintException e) {
            e.printStackTrace();

        }
    }

    /**
     * 打印图片
     *
     * @param bitmap
     */
    public static void prnBitmap(Bitmap bitmap) {
        try {
            inst.prnBitmap(bitmap);
        } catch (PrintException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取打印机状态
     *
     * @return 状态码
     */
    public static byte prnStatus() {
//        if (inst == null) {
//            Log.i("instinst", "null");
//        } else {
//            Log.i("instinst", "not_null");
//        }
        return inst.prnStatus();
    }

    /**
     * 打印字符串
     *
     * @param str     打印字符串
     * @param charset Str字符集，为null时，默认为UTF-8字符集
     */
    public static void prnStr(String str, String charset) {
        try {
            inst.prnStr(str, charset);
        } catch (PrintException e) {
            e.printStackTrace();
        }
    }

    /**
     * 走纸
     *
     * @param pixel 像素点个数
     */
    public static void prnStep(short pixel) {
        try {
            inst.prnInit();
            inst.prnStep(pixel);
        } catch (PrintException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置打印黑度等级
     */
    public static void prnSetGray(int Level) {
        inst.prnSetGray(Level);
    }

    /**
     * 设置字符打印左边界
     *
     * @param Indent 左边界空白像素点，范围：0~300
     */
    public static void prnLeftIndent(short Indent) {
        inst.prnLeftIndent(Indent);
    }

    /**
     * 设置打印字体高度，可在基本字体上实现双倍高度的打印
     */
    public static void prnDoubleHeight(int AscDoubleHeight, int LocalDoubleHeight) {
        inst.prnDoubleHeight(AscDoubleHeight, LocalDoubleHeight);
    }

    /**
     * 设置打印字体宽度，可在基本字体上实现双倍宽度的打印
     */
    public static void prnDoubleWidth(int AscDoubleWidth, int LocalDoubleWidth) {
        inst.prnDoubleWidth(AscDoubleWidth, LocalDoubleWidth);
    }

    /**
     * 设置打印字间距、行间距
     */
    public static void prnSpaceSet(byte charSpace, byte lineSpace) {
        inst.prnSpaceSet(charSpace, lineSpace);
    }

    /**
     * 设置打印的字体
     */
    public static void prnFontSet(byte Ascii, byte Cfont) {
        inst.prnFontSet(Ascii, Cfont);
    }

    /**
     * 选择打印机打印字体
     */
    public static void prnSelectFont(ST_FONT singleCodeFontAttr, ST_FONT multiCodeFontAttr) {
        try {
            inst.prnSelectFont(singleCodeFontAttr, multiCodeFontAttr);
        } catch (PrintException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印机初始化
     */
    public static void prnInit() {
        try {
            inst.prnInit();
        } catch (PrintException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭打印机
     */
    public static void finish() {
        try {
            inst.finalize();
        } catch (PrintException e) {
            e.printStackTrace();
        }
    }
}
