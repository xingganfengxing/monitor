package com.letv.cdn.report.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 转换工具类
 * Created by liufeng1 on 2015/3/19.
 */
public class ConvertUtils {

    /**
     * -1 转换成 null
     *
     * @param platOrSplatId
     * @return
     */
    public static String convertPlatSplatId(String platOrSplatId) {
        if ("-1".equals(platOrSplatId)) {
            return null;
        }
        return platOrSplatId;
    }

    /**
     * Bps 转换成 Mbs
     *
     * @param bw
     * @return
     */
    public static String convertToMbs(Double bw) {
        if (bw == null) {
            bw = 0.0;
        }
        return String.format("%.2f", bw / 1000 / 1000 / 300 * 8);
    }

    /**
     * Bps 转换成 Mbs 三位逗号
     *
     * @param bw
     * @return
     */
    public static String convertToMbsWithDot(Double bw) {
        if (bw == null) {
            bw = 0.0;
        }
        return new DecimalFormat(",###.##").format(bw / 1000 / 1000 / 300 * 8);
    }

    /**
     * 带宽转换Mbs
     *
     * @param bw
     * @return
     */
    public static String convertBigDeciToMbs(String bw) {
        BigDecimal bigDecimal = null;
        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        if (!StringUtils.isEmpty(bw)) {
            bigDecimal = new BigDecimal(bw);
            BigDecimal kDecimal = new BigDecimal(1.0 / 1000 / 1000 / 300 * 8);
            bigDecimal = bigDecimal.multiply(kDecimal);
        } else {
            bigDecimal = new BigDecimal(0.0);
        }
        return decimalFormat.format(bigDecimal);
    }

}
