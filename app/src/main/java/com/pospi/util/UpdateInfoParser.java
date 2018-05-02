package com.pospi.util;

import android.util.Xml;

import com.pospi.dto.UpdateInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * ����XML����
 */
public class UpdateInfoParser {
    public static UpdateInfo getUpdateInfo(InputStream is)
            throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "UTF-8");
        UpdateInfo info = new UpdateInfo();
        int type = parser.getEventType();

        while (type != XmlPullParser.END_DOCUMENT) {
            if (type == XmlPullParser.START_TAG) {

                if ("version".equals(parser.getName())) {
                    String version = parser.nextText();
                    info.setVersion(version);
//                } else if ("description".equals(parser.getName())) {
//                    String description = parser.nextText();
//                    info.setDescription(description);
                } else if ("url".equals(parser.getName())) {
                    String apkurl = parser.nextText();
                    info.setApkurl(apkurl);
                }
            }
            type = parser.next();
        }
        return info;
    }
}
