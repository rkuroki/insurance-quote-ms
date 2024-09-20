package com.insurance.insurancequote.external;

import com.insurance.insurancequote.external.dto.OfferDTO;

public interface OfferService {

    OfferDTO getOfferById(String offerId);

}
