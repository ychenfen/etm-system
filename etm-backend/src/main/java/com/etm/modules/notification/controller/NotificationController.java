package com.etm.modules.notification.controller;

import com.etm.common.result.PageResult;
import com.etm.common.result.Result;
import com.etm.common.utils.SecurityUtil;
import com.etm.modules.notification.entity.Notification;
import com.etm.modules.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final SecurityUtil securityUtil;

    @GetMapping("/page")
    public Result<PageResult<Notification>> getNotificationPage(
            @RequestParam(required = false) String msgType,
            @RequestParam(required = false) Integer isRead,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = securityUtil.getCurrentUserId();
        return Result.ok(notificationService.getNotificationPage(userId, msgType, isRead, pageNum, pageSize));
    }

    @PutMapping("/{id}/read")
    public Result<Void> markRead(@PathVariable Long id) {
        notificationService.markRead(id);
        return Result.ok();
    }

    @PutMapping("/batch-read")
    public Result<Void> markBatchRead(@RequestBody Map<String, List<Long>> body) {
        List<Long> ids = body.get("ids");
        notificationService.markBatchRead(ids);
        return Result.ok();
    }

    @GetMapping("/unread-count")
    public Result<Map<String, Long>> getUnreadCount() {
        Long userId = securityUtil.getCurrentUserId();
        long count = notificationService.getUnreadCount(userId);
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return Result.ok(response);
    }
}
