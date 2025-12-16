package com.simplecash.projet_akoum_mohamad.application.port.out;

import com.simplecash.projet_akoum_mohamad.domain.model.Transfer;

public interface TransferRepositoryPort {
    Transfer save(Transfer transfer);
}

