package com.xebia.eda.controller;

import com.xebia.common.domain.Claim;
import com.xebia.common.domain.ProductClaim;
import com.xebia.common.service.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(value = "/inventory-api/v2")
//TODO: add hooks for async logic
public class EdaInventoryController {

    private ClaimService claimService;

    @Autowired
    public EdaInventoryController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping("/claims")
    @ResponseBody
    public Claim claimInventory(@Valid @RequestBody List<ProductClaim> claims) {
        //TODO: Process claim
        return claimService.saveClaim(new Claim(claims));
    }

    @PostMapping("/shippment")
    public void shipOrder(Long claimId) {
        //TODO schedule shippment
    }


}
