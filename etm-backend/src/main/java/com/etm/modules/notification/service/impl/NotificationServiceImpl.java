package com.etm.modules.notification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.common.result.PageResult;
import com.etm.modules.notification.entity.Notification;
import com.etm.modules.notification.mapper.NotificationMapper;
import com.etm.modules.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    private final NotificationMapper notificationMapper;

    @Override
    public PageResult<Notification> getNotificationPage(Long userId, String msgType, Integer isRead, Integer pageNum, Integer pageSize) {
        QueryWrapper<Notification> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("deleted", 0);

        if (msgType != null && !msgType.isEmpty()) {
            wrapper.eq("msg_type", msgType);
        }

        if (isRead != null) {
            wrapper.eq("is_read", isRead);
        }

        wrapper.orderByDesc("create_time");

        Page<Notification> page = new Page<>(pageNum, pageSize);
        Page<Notification> result = notificationMapper.selectPage(page, wrapper);

        return new PageResult<>(result.getRecords(), result.getTotal(), result.getSize(), result.getCurrent());
    }

    @Override
    @Transactional
    public void markRead(Long id) {
        Notification notification = notificationMapper.selectById(id);
        if (notification != null) {
            notification.setIsRead(1);
            notificationMapper.updateById(notification);
        }
    }

    @Override
    @Transactional
    public void markBatchRead(List<Long> ids) {
        for (Long id : ids) {
            markRead(id);
        }
    }

    @Override
    public long getUnreadCount(Long userId) {
        QueryWrapper<Notification> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId)
                .eq("is_read", 0)
                .eq("deleted", 0);
        return notificationMapper.selectCount(wrapper);
    }

    @Override
    @Transactional
    public void sendNotification(Long userId, String title, String content, String msgType) {
        Notification notification = Notification.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .msgType(msgType)
                .isRead(0)
                .sendChannel("system")
                .sendTime(LocalDateTime.now())
                .createTime(LocalDateTime.now())
                .deleted(0)
                .build();

        notificationMapper.insert(notification);
    }

    @Override
    @Transactional
    public void sendBatchNotification(List<Long> userIds, String title, String content, String msgType) {
        for (Long userId : userIds) {
            sendNotification(userId, title, content, msgType);
        }
    }
}
