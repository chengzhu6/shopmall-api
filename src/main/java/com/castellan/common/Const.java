package com.castellan.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String TYPE_USERNAME = "username";

    public static final String TYPE_EMAIL = "email";

    public interface CookieUtil{
        String TOKEN = "token";
        int MAX_AGE = 180000;
    }

    public interface FilterConst{
        String[] NEED_LOGIN_PATH = {"/user/get_user_info.do","/cart/**"};
    }

    public static final Integer ROOT_NODE = new Integer(0);


    public enum PaymentTypeEnum{
        ONLINE_PAY(1,"在线支付");

        PaymentTypeEnum(int code,String value){
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }


        public static PaymentTypeEnum codeOf(int code){
            for(PaymentTypeEnum paymentTypeEnum : values()){
                if(paymentTypeEnum.getCode() == code){
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("么有找到对应的枚举");
        }

    }

    public interface Role{
        Integer ROLE_CUSTUMER = 1;
        Integer ROLE_ADMIN = 0;
    }

    public interface Cart{
        Integer CHECKED = 1;
        Integer UNCHECKED = 0;
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
        String LIMIT_NUM_FAIl = "LIMIT_NUM_FAIL";
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }


    public enum ProductStatusEnum{
        ON_SALE(1,"在线");
        private String value;
        private int code;
        ProductStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public interface AliPayRESPONSE {
        String SUCCESS = "success";
        String FAILE = "failed";
    }

    public interface AliPayCallBack{
        String TRADE_SUCCESS = "TRADE_SUCCESS";
    }



    public enum OrderStatus{

        CANCELED(0,"已取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已付款"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成"),
        ORDER_CLOSE(60,"订单关闭");
        private String value;
        private int status;



        OrderStatus(int status,String value){
            this.status = status;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }


        public static OrderStatus codeOf(int status){
            for(OrderStatus orderStatusEnum : values()){
                if(orderStatusEnum.getStatus() == status){
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }


    public enum PayPlatForm{
        ALIPAY("支付宝",1);
        private String value;
        private int code;



        PayPlatForm(String value,int status){
            this.code = status;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    public enum ProductStatus{
        ON_SALE("在线",1);
        private String value;
        private int status;



        ProductStatus(String value,int status){
            this.status = status;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
