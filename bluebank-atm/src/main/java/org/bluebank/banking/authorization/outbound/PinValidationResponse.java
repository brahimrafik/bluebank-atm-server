package org.bluebank.banking.authorization.outbound;

import java.util.UUID;

public class PinValidationResponse {
    public final UUID id;
    public final ValidationStatus validationStatus;

    public PinValidationResponse(UUID id, ValidationStatus validationStatus) {
        this.id = id;
        this.validationStatus = validationStatus;
    }

    public enum ValidationStatus {
        VALID, WRONG_PIN, DISABLED
    }
}
