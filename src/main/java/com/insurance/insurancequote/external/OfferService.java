package com.insurance.insurancequote.external;

import com.insurance.insurancequote.external.dto.OfferDTO;
import jakarta.annotation.Nullable;

public interface OfferService {

    @Nullable
    OfferDTO getOfferById(String offerId);

}
