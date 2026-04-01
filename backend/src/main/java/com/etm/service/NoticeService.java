package com.etm.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.entity.Notice;

public interface NoticeService extends IService<Notice> {
    Page<Notice> pageList(int current, int size, String keyword, String type);
}
