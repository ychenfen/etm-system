package com.etm.modules.notification.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.common.result.PageResult;
import com.etm.modules.notification.entity.Notification;

import java.util.List;

public interface NotificationService extends IService<Notification> {

    PageResult<Notification> getNotificationPage(Long userId, String msgType, Integer isRead, Integer pageNum, Integer pageSize);

    void markRead(Long id);

    void markBatchRead(List<Long> ids);

    long getUnreadCount(Long userId);

    void sendNotification(Long userId, String title, String content, String msgType);

    void sendBatchNotification(List<Long> userIds, String title, String content, String msgType);
}
