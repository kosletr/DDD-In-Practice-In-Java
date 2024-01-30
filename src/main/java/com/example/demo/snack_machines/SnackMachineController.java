package com.example.demo.snack_machines;

import com.example.demo.shared_kernel.Money;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/snackMachine")
public class SnackMachineController {
    private final SnackMachine snackMachine;
    private final SnackMachineRepository snackMachineRepository;

    public SnackMachineController(SnackMachineRepository snackMachineRepository) {
        this.snackMachineRepository = snackMachineRepository;
        this.snackMachine = snackMachineRepository.findById(1L).orElseThrow();
    }

    @GetMapping("/moneyInTransaction")
    public ResponseEntity<String> getMoneyInTransaction() {
        final String moneyInTransaction = snackMachine.getMoneyInTransaction().toString();
        return ResponseEntity.ok().body(moneyInTransaction);
    }

    @GetMapping("/moneyInside")
    public ResponseEntity<String> getMoneyInsideSnackMachine() {
        final String moneyInside = snackMachine.getMoneyInside().toString();
        return ResponseEntity.ok().body(moneyInside);
    }

    @GetMapping("/snackPiles")
    public ResponseEntity<List<SnackPileDTO>> getAllSnackPiles() {
        final List<SnackPileDTO> snackPiles = snackMachine.retrieveAllSnackPiles().stream()
                .map(sp -> new SnackPileDTO(
                       sp.getSnack().getName(),
                       sp.getQuantity(),
                       sp.getPrice()))
               .toList();
        return ResponseEntity.ok().body(snackPiles);
    }

    @PostMapping("/returnMoney")
    public ResponseEntity<String> returnMoney() {
        final String message = returnMoneyToSnackMachine();
        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/buySnack")
    public ResponseEntity<String> buySnack(@RequestParam int position) {
        final String message = buySnackFromSnackMachine(position);
        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/insertCent")
    public ResponseEntity<String> insertCent() {
        final String message = insertMoneyToSnackMachine(Money.Cent);
        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/insertTenCent")
    public ResponseEntity<String> insertTenCent() {
        final String message = insertMoneyToSnackMachine(Money.TenCent);
        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/insertQuarter")
    public ResponseEntity<String> insertQuarter() {
        final String message = insertMoneyToSnackMachine(Money.Quarter);
        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/insertDollar")
    public ResponseEntity<String> insertDollar() {
        final String message = insertMoneyToSnackMachine(Money.Dollar);
        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/insertFiveDollar")
    public ResponseEntity<String> insertFiveDollar() {
        final String message = insertMoneyToSnackMachine(Money.FiveDollar);
        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/insertTwentyDollar")
    public ResponseEntity<String> insertTwentyDollar() {
        final String message = insertMoneyToSnackMachine(Money.TwentyDollar);
        return ResponseEntity.ok().body(message);
    }

    private String insertMoneyToSnackMachine(final Money coinOrNote) {
        snackMachine.insertMoney(coinOrNote);
        return "You have inserted: " + coinOrNote;
    }

    private String returnMoneyToSnackMachine() {
        snackMachine.returnMoney();
        return "Money was returned.";
    }

    private String buySnackFromSnackMachine(final int position) {
        final String errorMessage = snackMachine.canBuySnack(position);
        if (!errorMessage.isEmpty())
            return errorMessage;

        snackMachine.buySnack(position);
        snackMachineRepository.save(snackMachine);
        return "You have bought a snack from slot " + position + ".";
    }
}
