package com.simplecash.projet_akoum_mohamad.application.port.in;

import com.simplecash.projet_akoum_mohamad.application.dto.TransferCommand;
import com.simplecash.projet_akoum_mohamad.domain.model.Transfer;

public interface TransferUseCase {
    Transfer transfer(TransferCommand command);
}

