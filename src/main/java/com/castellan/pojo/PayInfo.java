package com.castellan.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayInfo {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pay_info.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pay_info.user_id
     *
     * @mbggenerated
     */
    private Integer userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pay_info.order_no
     *
     * @mbggenerated
     */
    private Long orderNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pay_info.pay_platform
     *
     * @mbggenerated
     */
    private Integer payPlatform;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pay_info.platform_number
     *
     * @mbggenerated
     */
    private String platformNumber;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pay_info.platform_status
     *
     * @mbggenerated
     */
    private String platformStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pay_info.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pay_info.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;


}