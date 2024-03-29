package com.example.carrendalapp.config;

/**
 * @author WD
 */
public class UrlAddress {
    /**
     * 基础的URL
     */
    public final static String BASE_URL = "http://10.0.2.2:8080/CarRendalWeb_war_exploded/";
    /**
     * 上传图片的URL
     */
    public final static String UPLOAD_IMAGE_URL = "http://10.0.2.2:8080/CarRendalWeb_war_exploded/ImageServlet";
    /**
     * 插入用户的URL
     */
    public final static String INSERT_USER_URL = "http://10.0.2.2:8080/CarRendalWeb_war_exploded/UserServlet";
    /**
     * 检查用户是否存在的URL
     */
    public final static String CHECK_USER_URL = "http://10.0.2.2:8080/CarRendalWeb_war_exploded/UserServlet";
    /**
     * 检查账号密码是否正确的URL
     */
    public final static String CHECK_PASSWORD_URL = "http://10.0.2.2:8080/CarRendalWeb_war_exploded/LoginServlet";
    /**
     * 忘记密码使用的URL
     */
    public final static String FORGET_PASSWORD_URL = "http://10.0.2.2:8080/CarRendalWeb_war_exploded/ForgetPasswordServlet";
    /**
     * 发布车辆的URL
     */
    public final static String INSERT_CAR_URL = "http://10.0.2.2:8080/CarRendalWeb_war_exploded/CarServlet";
    /**
     * 首页查询可预约车辆的URL
     */
    public final static String HOME_QUERY_CAR_URL = "http://10.0.2.2:8080/CarRendalWeb_war_exploded/CarServlet?operation=homeQuery";
    /**
     * 插入订单的URL
     */
    public final static String INSERT_ORDER_URL = "http://10.0.2.2:8080/CarRendalWeb_war_exploded/OrderServlet";
    /**
     * 查询订单的URL
     */
    public final static String QUERY_ORDER_URL = "http://10.0.2.2:8080/CarRendalWeb_war_exploded/OrderServlet";
    /**
     * 取消订单的URL
     */
    public final static String CANCEL_ORDER_URL = "http://10.0.2.2:8080/CarRendalWeb_war_exploded/OrderServlet";
    /**
     * 跟新空闲时间的URL
     */
    public final static String UPDATE_FREE_TIME_URL = "http://10.0.2.2:8080/CarRendalWeb_war_exploded/CarServlet";
    /**
     * 查询车辆审核情况
     */
    public final static String QUERY_CARCHECK_URL="http://10.0.2.2:8080/CarRendalWeb_war_exploded/CarServlet";
    /**
     * 更新车辆审核
     */
    public final static String Update_CHECK_URL="http://10.0.2.2:8080/CarRendalWeb_war_exploded/CarServlet";
}
