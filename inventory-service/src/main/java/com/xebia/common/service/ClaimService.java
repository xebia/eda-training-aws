package com.xebia.common.service;

import com.xebia.common.domain.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;

    @Autowired
    public ClaimService(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    public Optional<Claim> getClaim(Long id) {
        return claimRepository.findById(id);
    }

    public List<Claim> getClaims() {
        return claimRepository.findAll();
    }

    public Claim saveClaim(Claim claim) {
        return claimRepository.save(claim);
    }

    public Claim updateClaim(Claim claim, Long id) {
        Assert.isTrue((claim.getId() == null) || (claim.getId() == id), "Conflicting claim id");
        return claimRepository.save(claim);
    }


}
