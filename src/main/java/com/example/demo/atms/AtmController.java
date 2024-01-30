package com.example.demo.atms;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/atm")
public class AtmController {
    private final Atm atm;
    private final PaymentGateway paymentGateway;
    private final AtmRepository atmRepository;

    public AtmController(AtmRepository atmRepository) {
        this.atmRepository = atmRepository;
        this.paymentGateway = new PaymentGateway();
        this.atm = atmRepository.findById(1L).orElseThrow();
    }
    
    @GetMapping("/moneyInside")
    public ResponseEntity<String> getMoneyInsideAtm() {
        final String moneyInside = atm.getMoneyInside().toString();
        return ResponseEntity.ok().body(moneyInside);
    }

    @GetMapping("/moneyCharged")
    public ResponseEntity<String> getMoneyCharged() {
        final String moneyCharged = atm.getMoneyCharged().toString();
        return ResponseEntity.ok().body(moneyCharged);
    }

    @PostMapping("/takeMoney")
    public ResponseEntity<String> takeMoney(@RequestParam double amount) {
        final String message = takeMoneyFromAtm(BigDecimal.valueOf(amount));
        return ResponseEntity.ok().body(message);
    }

    protected String takeMoneyFromAtm(final BigDecimal amount) {
        String errorMessage = atm.canTakeMoney(amount);
        if(!errorMessage.isEmpty())
            return errorMessage;

        BigDecimal amountWithCommission = atm.calculateAmountWithCommission(amount);
        paymentGateway.chargePayment(amountWithCommission);
        atm.takeMoney(amount);
        atmRepository.save(atm);

        return "You have taken " + amount;
    }
}
