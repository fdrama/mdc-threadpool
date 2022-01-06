package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * @author fdrama
 * @date 2022/01/06
 **/
public class IDCardUtil {

    private IDCardUtil() {

    }

    private final static int OLD_ID_CARD_LENGTH = 15;

    private final static int NEW_ID_CARD_LENGTH = 18;

    private final static String SIMPLE_DATE_FORMAT_PATTERN = "yyyyMMdd";
    /**
     * 18位身份证中最后一位校验码
     */
    private final static char[] VERIFY_CODE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    /**
     * 18位身份证中，各个数字的生成校验码时的权值
     */
    private final static int[] VERIFY_CODE_WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * <pre>
     *     省、直辖市代码表：
     *     11 : 北京  12 : 天津  13 : 河北       14 : 山西  15 : 内蒙古
     *     21 : 辽宁  22 : 吉林  23 : 黑龙江     31 : 上海  32 : 江苏
     *     33 : 浙江  34 : 安徽  35 : 福建       36 : 江西  37 : 山东
     *     41 : 河南  42 : 湖北  43 : 湖南       44 : 广东  45 : 广西      46 : 海南
     *     50 : 重庆  51 : 四川  52 : 贵州       53 : 云南  54 : 西藏
     *     61 : 陕西  62 : 甘肃  63 : 青海       64 : 宁夏  65 : 新疆
     *     71 : 台湾
     *     81 : 香港  82 : 澳门
     *     91 : 国外
     * </pre>
     */
    private final static String[] PROVINCE_CODE = {
            "11", "12", "13", "14", "15", "21",
            "22", "23", "31", "32", "33", "34",
            "35", "36", "37", "41", "42", "43",
            "44", "45", "46", "50", "51", "52",
            "53", "54", "61", "62", "63", "64",
            "65", "71", "81", "82", "91"};


    /**
     * 身份证号码构成：6位地址编码 + 8位生日 +3位顺序码+ 1位校验码
     *
     * @param idCard 身份证号码字符串
     * @return
     */
    public static boolean isValidateIdCard(String idCard) {

        if (Objects.isNull(idCard) || "".equals(idCard)) {
            return false;
        }
        if (idCard.length() == OLD_ID_CARD_LENGTH) {
            String newIdCard = convertToNewCardNumber(idCard);
            return validateIDCard(newIdCard);
        }
        if (idCard.length() == NEW_ID_CARD_LENGTH) {
            return validateIDCard(idCard);
        }

        return false;
    }

    /**
     * 校验身份证号码是否有效
     *
     * @param idCard
     * @return
     */
    private static boolean validateIDCard(String idCard) {

        // 获取前17位
        String idCard17 = idCard.substring(0, 17);

        // 校验前17位是否是数字
        if (!isDigital(idCard17)) {
            return false;
        }

        // 校验省份代码
        String provinceCode = idCard.substring(0, 2);
        if (!checkProvince(provinceCode)) {
            return false;
        }

        String birthDay = idCard.substring(6, 14);
        if (!checkBirthDay(birthDay)) {
            return false;
        }

        String idCard18Code = idCard.substring(17, 18);

        return calculateVerifyCode(idCard).equals(idCard18Code);
    }


    /**
     * 把15位身份证号码转换到18位身份证号码<br>
     * 15位身份证号码与18位身份证号码的区别为：<br>
     * 1、15位身份证号码中，"出生年份"字段是2位，转换时需要补入"19"，表示20世纪<br>
     * 2、15位身份证无最后一位校验码。18位身份证中，校验码根据根据前17位生成
     *
     * @param oldCardNumber
     * @return
     */
    private static String convertToNewCardNumber(String oldCardNumber) {
        StringBuilder buf = new StringBuilder(NEW_ID_CARD_LENGTH);
        buf.append(oldCardNumber, 0, 6);
        buf.append("19");
        buf.append(oldCardNumber.substring(6));
        buf.append(calculateVerifyCode(buf.toString()));
        return buf.toString();
    }

    /**
     * 校验第18位校验码
     * 十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0...16 ，先对前17位数字的权求和；
     * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * 计算模 Y = mod(S, 11) < 通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 找到对应校验码 '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'
     *
     * @param idCard
     * @return
     */
    private static String calculateVerifyCode(String idCard) {
        int sum = 0;
        for (int i = 0; i < NEW_ID_CARD_LENGTH - 1; i++) {
            char ch = idCard.charAt(i);
            sum += Integer.parseInt(String.valueOf(ch)) * VERIFY_CODE_WEIGHT[i];
        }
        char code = VERIFY_CODE[sum % 11];
        return String.valueOf(code);
    }


    /**
     * 校验生日是否合法
     *
     * @param birthDay
     * @return
     */
    private static boolean checkBirthDay(String birthDay) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT_PATTERN);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(birthDay);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 数字验证
     *
     * @param str
     * @return
     */
    private static boolean isDigital(String str) {
        return str.matches("^[0-9]*$");
    }

    /**
     * 省份校验
     *
     * @param provinceCode 省份编码
     * @return
     */
    private static boolean checkProvince(String provinceCode) {
        for (String str : PROVINCE_CODE) {
            if (provinceCode.equals(str)) {
                return true;
            }
        }
        return false;
    }
    
}
