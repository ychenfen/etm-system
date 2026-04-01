package com.etm.modules.teacher.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CertificateDTO {
    private String certType;
    private String certName;
    private String certNo;
    private LocalDate issueDate;
    private LocalDate expireDate;
    private String issueOrg;
    private String fileUrl;
}
