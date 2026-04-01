package com.etm.modules.recruitment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.common.result.PageResult;
import com.etm.modules.recruitment.dto.NoticeCreateDTO;
import com.etm.modules.recruitment.dto.NoticeQueryDTO;
import com.etm.modules.recruitment.entity.RecruitmentNotice;

/**
 * 招聘公告Service接口
 */
public interface RecruitmentNoticeService extends IService<RecruitmentNotice> {

    /**
     * 分页查询招聘公告
     */
    PageResult<RecruitmentNotice> getNoticePage(NoticeQueryDTO query);

    /**
     * 创建招聘公告
     */
    RecruitmentNotice createNotice(NoticeCreateDTO dto);

    /**
     * 更新招聘公告
     */
    RecruitmentNotice updateNotice(Long id, NoticeCreateDTO dto);

    /**
     * 发布招聘公告
     */
    void publishNotice(Long id);

    /**
     * 删除招聘公告
     */
    void deleteNotice(Long id);
}
