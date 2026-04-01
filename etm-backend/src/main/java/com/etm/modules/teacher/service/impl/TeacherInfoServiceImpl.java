package com.etm.modules.teacher.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etm.common.exception.BusinessException;
import com.etm.common.result.PageResult;
import com.etm.modules.teacher.dto.CertificateDTO;
import com.etm.modules.teacher.dto.TeacherCreateDTO;
import com.etm.modules.teacher.dto.TeacherQueryDTO;
import com.etm.modules.teacher.dto.WorkExperienceDTO;
import com.etm.modules.teacher.entity.TeacherCertificate;
import com.etm.modules.teacher.entity.TeacherInfo;
import com.etm.modules.teacher.entity.TeacherWorkExperience;
import com.etm.modules.teacher.mapper.TeacherCertificateMapper;
import com.etm.modules.teacher.mapper.TeacherInfoMapper;
import com.etm.modules.teacher.mapper.TeacherWorkExperienceMapper;
import com.etm.modules.teacher.service.TeacherInfoService;
import com.etm.modules.teacher.vo.TeacherVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherInfoServiceImpl extends ServiceImpl<TeacherInfoMapper, TeacherInfo> implements TeacherInfoService {

    private final TeacherWorkExperienceMapper workExperienceMapper;
    private final TeacherCertificateMapper certificateMapper;

    @Override
    public PageResult<TeacherInfo> getTeacherPage(TeacherQueryDTO query) {
        IPage<TeacherInfo> page = baseMapper.selectTeacherPage(new Page<>(query.getPageNum(), query.getPageSize()), query);
        return PageResult.of(page);
    }

    @Override
    public TeacherVO getTeacherDetail(Long id) {
        TeacherVO vo = baseMapper.selectTeacherDetail(id);
        if (vo == null) {
            throw BusinessException.of("老师信息不存在");
        }
        return vo;
    }

    @Override
    @Transactional
    public TeacherInfo createTeacher(TeacherCreateDTO dto) {
        TeacherInfo teacher = new TeacherInfo();
        teacher.setTeacherNo(generateTeacherNo());
        teacher.setName(dto.getName());
        teacher.setGender(dto.getGender());
        teacher.setBirthDate(dto.getBirthDate());
        teacher.setIdCard(dto.getIdCard());
        teacher.setNationality(dto.getNationality());
        teacher.setPoliticalStatus(dto.getPoliticalStatus());
        teacher.setEducation(dto.getEducation());
        teacher.setDegree(dto.getDegree());
        teacher.setMajor(dto.getMajor());
        teacher.setGraduateSchool(dto.getGraduateSchool());
        teacher.setGraduateDate(dto.getGraduateDate());
        teacher.setTitle(dto.getTitle());
        teacher.setTitleLevel(dto.getTitleLevel());
        teacher.setPhone(dto.getPhone());
        teacher.setEmail(dto.getEmail());
        teacher.setAddress(dto.getAddress());
        teacher.setEmergencyContact(dto.getEmergencyContact());
        teacher.setEmergencyPhone(dto.getEmergencyPhone());
        teacher.setBankName(dto.getBankName());
        teacher.setBankAccount(dto.getBankAccount());
        teacher.setPhotoUrl(dto.getPhotoUrl());
        teacher.setResumeUrl(dto.getResumeUrl());
        teacher.setSpecialty(dto.getSpecialty());
        teacher.setTeacherStatus(dto.getTeacherStatus() != null ? dto.getTeacherStatus() : 0);

        save(teacher);

        // Save work experiences
        if (dto.getWorkExperiences() != null && !dto.getWorkExperiences().isEmpty()) {
            for (WorkExperienceDTO workExp : dto.getWorkExperiences()) {
                TeacherWorkExperience experience = new TeacherWorkExperience();
                experience.setTeacherId(teacher.getId());
                experience.setCompanyName(workExp.getCompanyName());
                experience.setPosition(workExp.getPosition());
                experience.setStartDate(workExp.getStartDate());
                experience.setEndDate(workExp.getEndDate());
                experience.setWorkContent(workExp.getWorkContent());
                experience.setLeaveReason(workExp.getLeaveReason());
                experience.setCreateTime(LocalDateTime.now());
                workExperienceMapper.insert(experience);
            }
        }

        // Save certificates
        if (dto.getCertificates() != null && !dto.getCertificates().isEmpty()) {
            for (CertificateDTO cert : dto.getCertificates()) {
                TeacherCertificate certificate = new TeacherCertificate();
                certificate.setTeacherId(teacher.getId());
                certificate.setCertType(cert.getCertType());
                certificate.setCertName(cert.getCertName());
                certificate.setCertNo(cert.getCertNo());
                certificate.setIssueDate(cert.getIssueDate());
                certificate.setExpireDate(cert.getExpireDate());
                certificate.setIssueOrg(cert.getIssueOrg());
                certificate.setFileUrl(cert.getFileUrl());
                certificate.setCreateTime(LocalDateTime.now());
                certificateMapper.insert(certificate);
            }
        }

        return teacher;
    }

    @Override
    @Transactional
    public TeacherInfo updateTeacher(Long id, TeacherCreateDTO dto) {
        TeacherInfo teacher = getById(id);
        if (teacher == null) {
            throw BusinessException.of("老师信息不存在");
        }

        teacher.setName(dto.getName());
        teacher.setGender(dto.getGender());
        teacher.setBirthDate(dto.getBirthDate());
        teacher.setIdCard(dto.getIdCard());
        teacher.setNationality(dto.getNationality());
        teacher.setPoliticalStatus(dto.getPoliticalStatus());
        teacher.setEducation(dto.getEducation());
        teacher.setDegree(dto.getDegree());
        teacher.setMajor(dto.getMajor());
        teacher.setGraduateSchool(dto.getGraduateSchool());
        teacher.setGraduateDate(dto.getGraduateDate());
        teacher.setTitle(dto.getTitle());
        teacher.setTitleLevel(dto.getTitleLevel());
        teacher.setPhone(dto.getPhone());
        teacher.setEmail(dto.getEmail());
        teacher.setAddress(dto.getAddress());
        teacher.setEmergencyContact(dto.getEmergencyContact());
        teacher.setEmergencyPhone(dto.getEmergencyPhone());
        teacher.setBankName(dto.getBankName());
        teacher.setBankAccount(dto.getBankAccount());
        teacher.setPhotoUrl(dto.getPhotoUrl());
        teacher.setResumeUrl(dto.getResumeUrl());
        teacher.setSpecialty(dto.getSpecialty());
        if (dto.getTeacherStatus() != null) {
            teacher.setTeacherStatus(dto.getTeacherStatus());
        }

        updateById(teacher);

        // Delete and re-insert work experiences
        workExperienceMapper.deleteByTeacherId(id);
        if (dto.getWorkExperiences() != null && !dto.getWorkExperiences().isEmpty()) {
            for (WorkExperienceDTO workExp : dto.getWorkExperiences()) {
                TeacherWorkExperience experience = new TeacherWorkExperience();
                experience.setTeacherId(id);
                experience.setCompanyName(workExp.getCompanyName());
                experience.setPosition(workExp.getPosition());
                experience.setStartDate(workExp.getStartDate());
                experience.setEndDate(workExp.getEndDate());
                experience.setWorkContent(workExp.getWorkContent());
                experience.setLeaveReason(workExp.getLeaveReason());
                experience.setCreateTime(LocalDateTime.now());
                workExperienceMapper.insert(experience);
            }
        }

        // Delete and re-insert certificates
        certificateMapper.deleteByTeacherId(id);
        if (dto.getCertificates() != null && !dto.getCertificates().isEmpty()) {
            for (CertificateDTO cert : dto.getCertificates()) {
                TeacherCertificate certificate = new TeacherCertificate();
                certificate.setTeacherId(id);
                certificate.setCertType(cert.getCertType());
                certificate.setCertName(cert.getCertName());
                certificate.setCertNo(cert.getCertNo());
                certificate.setIssueDate(cert.getIssueDate());
                certificate.setExpireDate(cert.getExpireDate());
                certificate.setIssueOrg(cert.getIssueOrg());
                certificate.setFileUrl(cert.getFileUrl());
                certificate.setCreateTime(LocalDateTime.now());
                certificateMapper.insert(certificate);
            }
        }

        return teacher;
    }

    @Override
    @Transactional
    public void deleteTeacher(Long id) {
        TeacherInfo teacher = getById(id);
        if (teacher == null) {
            throw BusinessException.of("老师信息不存在");
        }
        removeById(id);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        TeacherInfo teacher = getById(id);
        if (teacher == null) {
            throw BusinessException.of("老师信息不存在");
        }
        teacher.setTeacherStatus(status);
        updateById(teacher);
    }

    private String generateTeacherNo() {
        return "T" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
