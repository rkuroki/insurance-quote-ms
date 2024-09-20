package com.insurance.insurancequote.service;

import com.insurance.insurancequote.dto.InsuranceQuoteDTO;
import com.insurance.insurancequote.dto.mapper.InsuranceQuoteMapper;
import com.insurance.insurancequote.entity.InsuranceQuote;
import com.insurance.insurancequote.external.OfferService;
import com.insurance.insurancequote.external.ProductService;
import com.insurance.insurancequote.messaging.dto.InsuranceQuoteReceivedEvent;
import com.insurance.insurancequote.messaging.publisher.InsuranceQuoteReceivedPub;
import com.insurance.insurancequote.repository.InsuranceQuoteRepository;
import com.insurance.insurancequote.validator.InsuranceQuoteValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.insurance.insurancequote.entity.InsuranceQuoteStatus.*;

@Service
@Slf4j
public class InsuranceQuoteServiceImpl implements InsuranceQuoteService {

    private final InsuranceQuoteMapper mapper = InsuranceQuoteMapper.INSTANCE;

    private final InsuranceQuoteRepository repository;
    private final ProductService productService;
    private final OfferService offerService;
    private final InsuranceQuoteValidator validator;
    private final InsuranceQuoteReceivedPub quoteReceivedPub;

    public InsuranceQuoteServiceImpl(InsuranceQuoteRepository repository, ProductService productService,
                                     OfferService offerService, InsuranceQuoteValidator validator,
                                     InsuranceQuoteReceivedPub quoteReceivedPub) {
        this.repository = repository;
        this.productService = productService;
        this.offerService = offerService;
        this.validator = validator;
        this.quoteReceivedPub = quoteReceivedPub;
    }

    @Override
    @Transactional
    public InsuranceQuoteDTO create(InsuranceQuoteDTO quoteDTO) {
        var product = productService.getProductById(quoteDTO.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("Product not found with id: " + quoteDTO.getProductId());
        }

        var offer = offerService.getOfferById(quoteDTO.getOfferId());
        if (offer == null) {
            throw new IllegalArgumentException("Offer not found with id: " + quoteDTO.getOfferId());
        }

        validator.validateInsuranceQuote(quoteDTO, product, offer);

        quoteDTO.setStatus(RECEIVED);
        var quote = repository.save(mapper.toEntity(quoteDTO));

        this.notifyQuoteReceived(quote);

        return mapper.toDTO(quote);
    }

    private void notifyQuoteReceived(InsuranceQuote quote) {
        try {
            quoteReceivedPub.publish(new InsuranceQuoteReceivedEvent(quote.getId()));
        } catch (Exception e) {
            log.error("Error publishing Insurance Quote Created message. insuranceQuoteId=" + quote, e);
            quote.setStatus(FAILED);
            repository.save(quote);
        }
    }

    @Override
    public InsuranceQuote getInsuranceQuote(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Insurance Quote not found with id: " + id));
    }

    public void setInsurancePolicy(Long id, Long insurancePolicyId) {
        var quote = getInsuranceQuote(id);
        quote.setInsurancePolicyId(insurancePolicyId);
        quote.setStatus(POLICY_CREATED);
        repository.save(quote);
    }

    @Override
    public List<InsuranceQuote> getAllInsuranceQuotes() {
        return repository.findAll();
    }

}
