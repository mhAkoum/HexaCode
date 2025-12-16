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
    private final AuditWebMapper mapper;
    
    public AuditController(AuditUseCase auditUseCase, AuditWebMapper mapper) {
        this.auditUseCase = auditUseCase;
        this.mapper = mapper;
    }
    
    @GetMapping("/accounts")
    public ResponseEntity<AuditReportDTO> auditAccounts() {
        AuditReportDTO report = mapper.toDTO(auditUseCase.auditAllAccounts());
        return ResponseEntity.ok(report);
    }
}

