package com.pospi.callbacklistener;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Created by huangqi on 2016/12/28.
 */
public interface JnaTest extends Library {
    JnaTest instanceDll  = (JnaTest) Native.loadLibrary("dll地址",JnaTest.class);
    public int jsonSign(String jsonStr, byte[] sign);
}
