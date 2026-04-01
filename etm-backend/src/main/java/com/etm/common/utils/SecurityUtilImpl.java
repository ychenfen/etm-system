package com.etm.common.utils;

import org.springframework.stereotype.Component;

/**
 * SecurityUtil的Spring Bean包装（如需注入使用）
 */
@Component
public class SecurityUtilImpl {
    public Long getCurrentUserId() { return SecurityUtil.getCurrentUserId(); }
    public String getCurrentUsername() { return SecurityUtil.getCurrentUsername(); }
    public Long getCurrentCollegeId() { return SecurityUtil.getCurrentCollegeId(); }
}
