package com.liangxunwang.unimanager.alipay;

/**
 * Created by zhl on 2016/7/27.
 */
public class AlipayConfig {
    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 合作身份者ID，以2088开头由16位纯数字组成的字符串

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2017041706780367";

    //商户PID
    public static final String PARTNER = "2088621762063706";

    //商户收款账号
    public static final String SELLER = "501177055@qq.com";

    /** 商户私钥，pkcs8格式 */
    public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCb1x9vKgs3/hKnxDER9r+rWP8CZDunbMMLqaRsYzUe5mtx7+6G2p+EW9fRh7NMfAb2Tdu3uaBWHrpn8KBWfbuzN9mxU36IfjwLyXhDBx203AiNs9+5J2FT+ma+LnVW9OyDbJKI4e8UqRoiAhJDbiSpNvDUrqoYrPkpB118azCEJfS3gee2j7MNHmc62p4QNjl0zAAD6DjzbyF+pfatJ/42QgyrDpp80icF7/w4hOp1uQjss26MPCovpTyyBiLsq4Pm/qDujhCsP/hO2YvLWmz/4e89f0fXUANbhAoGE86mvFY1Jj7pKZ5h9aVuAP8r5mimm4+3mqomEzvI44i9wTPnAgMBAAECggEAE4oIZe62k2GmwyQlJuRyKbCx4IHivN41Ikw10mUp7uLjC9kSZhQZNgYx7BKAl5FhahZe+w0qHkIiCxAvekivdQwGnmYikkiJjMzI6FaHEfsYk3O/FD7sxBO8OBIpIir5UI0XXlQ9nrCJF0R9N7kinZjG31O6/fcKEmva+9OQtLC+7o18AUrtayJcf+EOeLpoNRcHd4dqkRaQ7sYKT67JqaKcU1Xa5FMN8b4Rq5PR/uj6y3FbLkoWxiKRAdmGqydwFqFx3BV5OdeeqgVDAffeemnH+tqjTY2+5N5d4tIMWvbOJ1Md2vP0AlRCzevbUJzlPlhXIm2RDnSpwGf9oCWtoQKBgQDowMfnL5qXwWEIjZqMXriqqcdAuwa4TnxDAegKSclSioSdrL0v+9otpnw+5eYagMOx1BTkIaKzEES/X4Owu2spfzEy8KcwIuyv0hvyfygUzWqHy3G3NjjWttq8OiSPiZdjZ9Sq4gs2JXXCVdOH0YqV4sRmeXouIBvw8wo9qLr/VwKBgQCrZ8ZaaoPeExNmZUvVshYQKfisQ0bQulkWdkstWtSM0HaOrf65Sftck5Lpqs3+gr7EGBXAS1e5+Vt4G0FY55ERaEjJsgd8lON7x4vABBumWyWMQDeOkSs5ZchXgtjfL/zxHZizBliLHvzIc4jOGtPGM7sTWRXQe8152JE7IGLl8QKBgQDksZc51xJlDgNTrh8u0VtNhShct+ewbJ24EUV35mxECAqqCrKRVTtZI35tJuNjIyYlsqccSNhebRu/lhAFNQHkci9bSA5eQ9KgJyiCquItHnEQIJwJUuOiRWTDEbYeuyje6YSSqyws9b2xRzdi+kbbY0drEO02KSKLK6LiVsn2XQKBgD4aRmrCBu8yWCgUcYqGkKngmyHG6mR78AVMbOaM70/pw9rkPdNic7lSGrug0CvzsSyZUWAvrCRMrZBxx5ZvqEb3UtAJOd8wjs9tODwlAOOgSJVtOvPjsxaDwze5x9vtpiayQ34xwWj+nDYAbkPGwo4gdhqHf3jfMk6AYfZbQJnxAoGBAIKGn2Y+O0F4+fdWTP1bEkmKm6LpLbAUt6PsxpoT/Fpntj1yNW+eldqA4c0zPIYYFg940mSBqJSOsWVugK1js6GzxUCpGLkG48R20hHw1E5fAgyAPv4w8IFmIVkPB5q2QolmrxItkUG8ZBrmn4+sQgzxHMxZvIkdgU0ezdrqJ4Rr";
    public static final String RSA_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCb1x9vKgs3/hKnxDER9r+rWP8CZDunbMMLqaRsYzUe5mtx7+6G2p+EW9fRh7NMfAb2Tdu3uaBWHrpn8KBWfbuzN9mxU36IfjwLyXhDBx203AiNs9+5J2FT+ma+LnVW9OyDbJKI4e8UqRoiAhJDbiSpNvDUrqoYrPkpB118azCEJfS3gee2j7MNHmc62p4QNjl0zAAD6DjzbyF+pfatJ/42QgyrDpp80icF7/w4hOp1uQjss26MPCovpTyyBiLsq4Pm/qDujhCsP/hO2YvLWmz/4e89f0fXUANbhAoGE86mvFY1Jj7pKZ5h9aVuAP8r5mimm4+3mqomEzvI44i9wTPnAgMBAAECggEAE4oIZe62k2GmwyQlJuRyKbCx4IHivN41Ikw10mUp7uLjC9kSZhQZNgYx7BKAl5FhahZe+w0qHkIiCxAvekivdQwGnmYikkiJjMzI6FaHEfsYk3O/FD7sxBO8OBIpIir5UI0XXlQ9nrCJF0R9N7kinZjG31O6/fcKEmva+9OQtLC+7o18AUrtayJcf+EOeLpoNRcHd4dqkRaQ7sYKT67JqaKcU1Xa5FMN8b4Rq5PR/uj6y3FbLkoWxiKRAdmGqydwFqFx3BV5OdeeqgVDAffeemnH+tqjTY2+5N5d4tIMWvbOJ1Md2vP0AlRCzevbUJzlPlhXIm2RDnSpwGf9oCWtoQKBgQDowMfnL5qXwWEIjZqMXriqqcdAuwa4TnxDAegKSclSioSdrL0v+9otpnw+5eYagMOx1BTkIaKzEES/X4Owu2spfzEy8KcwIuyv0hvyfygUzWqHy3G3NjjWttq8OiSPiZdjZ9Sq4gs2JXXCVdOH0YqV4sRmeXouIBvw8wo9qLr/VwKBgQCrZ8ZaaoPeExNmZUvVshYQKfisQ0bQulkWdkstWtSM0HaOrf65Sftck5Lpqs3+gr7EGBXAS1e5+Vt4G0FY55ERaEjJsgd8lON7x4vABBumWyWMQDeOkSs5ZchXgtjfL/zxHZizBliLHvzIc4jOGtPGM7sTWRXQe8152JE7IGLl8QKBgQDksZc51xJlDgNTrh8u0VtNhShct+ewbJ24EUV35mxECAqqCrKRVTtZI35tJuNjIyYlsqccSNhebRu/lhAFNQHkci9bSA5eQ9KgJyiCquItHnEQIJwJUuOiRWTDEbYeuyje6YSSqyws9b2xRzdi+kbbY0drEO02KSKLK6LiVsn2XQKBgD4aRmrCBu8yWCgUcYqGkKngmyHG6mR78AVMbOaM70/pw9rkPdNic7lSGrug0CvzsSyZUWAvrCRMrZBxx5ZvqEb3UtAJOd8wjs9tODwlAOOgSJVtOvPjsxaDwze5x9vtpiayQ34xwWj+nDYAbkPGwo4gdhqHf3jfMk6AYfZbQJnxAoGBAIKGn2Y+O0F4+fdWTP1bEkmKm6LpLbAUt6PsxpoT/Fpntj1yNW+eldqA4c0zPIYYFg940mSBqJSOsWVugK1js6GzxUCpGLkG48R20hHw1E5fAgyAPv4w8IFmIVkPB5q2QolmrxItkUG8ZBrmn4+sQgzxHMxZvIkdgU0ezdrqJ4Rr";

    // 支付宝的公钥，无需修改该值
    public static String ali_public_key  = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1lT4nmLORqRNt8amNE+Qxxjlx7qX9L61wzXZs7gAUX7XTGBt0vq2b187xu122oZFSPPdsrWAaK8kQl1OGi1IhNzhz1smFddAfIhk1WOADTh5BEbB577lnzsq9oSXX/DLMMAZEP3arsM8JliIWySs2AW4ojrD26d9uwc4E0pRBHCCmJ/AQGn/T+rVhNz18vg5Ed3IkRYdOfnpxvZp4fGNknpVJ3xmsQIowTomLvDl2AVEB2vDIC+XZ+PR1DzOmuBFzzmMopQFyO3Y+KN7Py2yBZPIluOCII+I+gBiL5BlFbZf/D1CHjgSCx3ZRycn4NVPD3AXE8+WK4KJ+uG9P4CePQIDAQAB";

    //↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


    // 调试用，创建TXT日志文件夹路径
    public static String log_path = "D:\\";

    // 字符编码格式 目前支持 gbk 或 utf-8
    public static String input_charset = "utf-8";

    private static final int SDK_PAY_FLAG = 1;

}