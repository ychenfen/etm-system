package com.etm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.entity.Notice;
import com.etm.mapper.NoticeMapper;
import com.etm.service.NoticeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Override
    public Page<Notice> pageList(int current, int size, String keyword, String type) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Notice::getTitle, keyword);
        }
        if (StringUtils.hasText(type)) {
            wrapper.eq(Notice::getType, type);
        }
        wrapper.orderByDesc(Notice::getCreateTime);
        return this.page(new Page<>(current, size), wrapper);
    }
}
