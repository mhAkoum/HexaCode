package com.simplecash.projet_akoum_mohamad.adapter.in.web.controller;

import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.AuditReportDTO;
import com.simplecash.projet_akoum_mohamad.adapter.in.web.mapper.AuditWebMapper;
import com.simplecash.projet_akoum_mohamad.application.port.in.AuditUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audit")
public class AuditController {
    
    private final AuditUseCase auditUseCase;
    
    public AuditController(AuditUseCase auditUseCase) {
        this.auditUseCase = auditUseCase;
    }
    
    @GetMapping("/accounts")
    public ResponseEntity<AuditReportDTO> auditAccounts() {
        AuditReportDTO report = AuditWebMapper.toDTO(auditUseCase.auditAllAccounts());
        return ResponseEntity.ok(report);
    }
}

