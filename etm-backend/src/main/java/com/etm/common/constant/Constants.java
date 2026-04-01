package com.etm.common.constant;

public class Constants {
    /** Token 前缀 */
    public static final String TOKEN_PREFIX = "Bearer ";
    /** 请求头 Token 字段 */
    public static final String HEADER_TOKEN = "Authorization";
    /** Redis token key 前缀 */
    public static final String REDIS_TOKEN_PREFIX = "etm:token:";
    /** 默认密码 */
    public static final String DEFAULT_PASSWORD = "etm123456";

    /** 外聘老师状态 */
    public static final String TEACHER_STATUS_PENDING = "0";    // 待聘用
    public static final String TEACHER_STATUS_EMPLOYED = "1";   // 聘用中
    public static final String TEACHER_STATUS_RENEWAL = "2";    // 待续聘
    public static final String TEACHER_STATUS_DISMISSED = "3";  // 已解聘

    /** 审批状态 */
    public static final String APPROVAL_STATUS_PENDING = "0";   // 审批中
    public static final String APPROVAL_STATUS_PASS = "1";      // 已通过
    public static final String APPROVAL_STATUS_REJECT = "2";    // 已驳回
    public static final String APPROVAL_STATUS_REVOKED = "3";   // 已撤回

    /** 审批节点 */
    public static final String NODE_COLLEGE = "college_leader";
    public static final String NODE_HR_SALARY = "hr_salary";
    public static final String NODE_HR_DIRECTOR = "hr_director";

    /** 合同状态 */
    public static final String CONTRACT_STATUS_VALID = "1";     // 有效
    public static final String CONTRACT_STATUS_EXPIRED = "2";   // 已到期
    public static final String CONTRACT_STATUS_TERMINATED = "3"; // 已终止

    /** 通用状态 */
    public static final String STATUS_NORMAL = "0";
    public static final String STATUS_DISABLE = "1";
    public static final String DEL_FLAG_EXIST = "0";
    public static final String DEL_FLAG_DELETE = "2";
}
