package com.castellan.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String TYPE_USERNAME = "username";

    public static final String TYPE_EMAIL = "email";

    public static final Integer ROOT_NODE = new Integer(0);


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
