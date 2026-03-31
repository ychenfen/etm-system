package com.etm.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.etm.dto.Result;
import com.etm.entity.Approval;
import com.etm.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/approval")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    /** 分页查询审批列表 */
    @GetMapping("/page")
    public Result<?> page(@RequestParam(defaultValue = "1") int current,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) Long teacherId,
                          @RequestParam(required = false) String status) {
        Page<Approval> page = approvalService.pageList(current, size, teacherId, status);
        return Result.success(page);
    }

    /** 获取单条审批详情 */
    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        Approval approval = approvalService.getById(id);
        if (approval == null) return Result.error("审批记录不存在");
        return Result.success(approval);
    }

    /** 发起审批申请 */
    @PostMapping
    public Result<?> submit(@RequestBody Approval approval) {
        return Result.success(approvalService.submit(approval));
    }

    /**
     * 节点审批
     * body: { "node": "college_leader", "pass": true, "remark": "同意", "operatorName": "张三" }
     */
    @PutMapping("/{id}/approve")
    public Result<?> approve(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String node = (String) body.get("node");
        boolean pass = Boolean.TRUE.equals(body.get("pass"));
        String remark = (String) body.getOrDefault("remark", "");
        String operatorName = (String) body.getOrDefault("operatorName", "");
        approvalService.approve(id, node, pass, remark, operatorName);
        return Result.success("审批操作成功");
    }

    /** 撤回审批 */
    @PutMapping("/{id}/revoke")
    public Result<?> revoke(@PathVariable Long id) {
        approvalService.revoke(id);
        return Result.success("已撤回");
    }
}
