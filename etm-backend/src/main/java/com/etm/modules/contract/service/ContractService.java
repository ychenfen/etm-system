package com.etm.modules.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.etm.common.result.PageResult;
import com.etm.modules.contract.dto.ContractCreateDTO;
import com.etm.modules.contract.dto.ContractQueryDTO;
import com.etm.modules.contract.dto.RenewalCreateDTO;
import com.etm.modules.contract.dto.RenewalQueryDTO;
import com.etm.modules.contract.dto.TerminationCreateDTO;
import com.etm.modules.contract.entity.EmploymentContract;
import com.etm.modules.contract.entity.RenewalApplication;
import com.etm.modules.contract.entity.TerminationRecord;

public interface ContractService extends IService<EmploymentContract> {

    PageResult<EmploymentContract> getContractPage(ContractQueryDTO query);

    EmploymentContract getContractDetail(Long id);

    EmploymentContract createContract(ContractCreateDTO dto);

    void signContract(Long id);

    PageResult<RenewalApplication> getRenewalPage(RenewalQueryDTO query);

    RenewalApplication createRenewal(RenewalCreateDTO dto);

    void approveRenewal(Long id, Integer status, String comment);

    PageResult<TerminationRecord> getTerminationPage(Long collegeId, Integer pageNum, Integer pageSize);

    TerminationRecord createTermination(TerminationCreateDTO dto);
}
