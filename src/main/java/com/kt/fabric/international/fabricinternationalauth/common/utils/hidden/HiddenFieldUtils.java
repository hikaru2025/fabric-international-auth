package com.kt.fabric.international.fabricinternationalauth.common.utils.hidden;

import com.kt.fabric.international.fabricinternationalauth.common.enums.AppHttpStatus;
import com.kt.fabric.international.fabricinternationalauth.common.exception.BusinessException;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @program: com.eastmoney.emis.utils
 * @description: 字段隐藏工具类
 * @author:
 * @create: 2020/05/14
 */
public class HiddenFieldUtils {
    /**
     * @Description 隐藏手机号码中间四位
     * @Version  1.0
     */
    public static String hiddenAccountId(String accountId){
        if(StringUtils.isEmpty(accountId)){
            throw new BusinessException(AppHttpStatus.DATA_EXCEPTION.getStatus(), "账号不可以为空");
        }
        return RegExUtils.replacePattern(accountId,"(\\d{1})\\d{6}(\\d{1})", "$1******$2");
    }
    /**
     * @Description 隐藏手机号码中间四位
     * @Version  1.0
     */
    public static String hiddenPhoneNum(String phone){
        if(StringUtils.isEmpty(phone)){
            throw new BusinessException(AppHttpStatus.DATA_EXCEPTION.getStatus(), "手机号码不可以为空");
        }
        //$1 $2 表示正则表达式里面的第一个和第二个，也就是括号里面的内容
        return RegExUtils.replacePattern(phone,"(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 隐藏身份证号码
     * @param cardNum
     * @return
     */
    public static String hiddenIdCardNum(String cardNum){
        if(StringUtils.isEmpty(cardNum)){
            throw new BusinessException(AppHttpStatus.DATA_EXCEPTION.getStatus(), "身份证号码不可以为空");
        }
        if(cardNum.length() != 15 && cardNum.length() != 18){
            throw new BusinessException(AppHttpStatus.DATA_EXCEPTION.getStatus(), "身份证号码位数必须为15位或者18位");
        }
        if(cardNum.length() == 18){
            return RegExUtils.replacePattern(cardNum, "(\\d{4})\\d{10}(\\d{4})", "$1**********$2");
        }
        return RegExUtils.replacePattern(cardNum, "(\\d{4})\\d{7}(\\d{4})", "$1*******$2");
    }
    public static void main(String[] args) {
        System.out.println(hiddenAccountId("10005806"));
        System.out.println(hiddenAccountId("1004615195"));
        System.out.println(hiddenIdCardNum("412822199305022728"));
        System.out.println(hiddenIdCardNum("412822199305022"));
        System.out.println(hiddenIdCardNum("412822199305022722"));
    }
}
