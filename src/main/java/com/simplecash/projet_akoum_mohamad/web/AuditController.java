package com.simplecash.projet_akoum_mohamad.web;

import com.simplecash.projet_akoum_mohamad.dto.AuditReportDTO;
import com.simplecash.projet_akoum_mohamad.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audit")
public class AuditController {
    
    private final AuditService auditService;
    
    @Autowired
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }
    
    @GetMapping("/accounts")
    public ResponseEntity<AuditReportDTO> auditAccounts() {
        AuditReportDTO report = auditService.auditAllAccounts();
        return ResponseEntity.ok(report);
    }
}

