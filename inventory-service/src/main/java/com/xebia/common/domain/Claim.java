package com.xebia.common.domain;

import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "claims")
@Immutable
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "claim_id", referencedColumnName = "id")
    private List<ProductClaim> claims;

    public Claim() {
    }

    public Claim(List<ProductClaim> claims) {
        this.claims = claims;
    }

    public Long getId() {
        return id;
    }

    public List<ProductClaim> getClaims() {
        return claims;
    }

    public Claim withId(Long id) {
        this.id = id;
        return this;
    }





}