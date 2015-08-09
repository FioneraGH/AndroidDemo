package com.fionera.wechatdemo.util;

import android.nfc.NdefRecord;

import java.util.Arrays;

/**
 * Created by fionera on 15-8-9.
 */
public class TextNfcRecord {

    private String text;

    public TextNfcRecord(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static TextNfcRecord parse(NdefRecord ndefRecord) {
        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }

        try {
            byte[] payload = ndefRecord.getPayload();

            String textEncoding;
            if ((payload[0] & 0x80) != 0) {
                textEncoding = "utf-16";
            } else {
                textEncoding = "utf-8";
            }
            int langCodeLength = payload[0] & 0x3f;
            String langEncoding = new String(payload, 1, langCodeLength, "US-ASCII");
            String text = new String(payload, langCodeLength + 1,
                    payload.length - langCodeLength - 1/*编码*/, textEncoding);
            return new TextNfcRecord(text);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
