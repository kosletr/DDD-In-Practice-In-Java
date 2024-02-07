package com.example.demo.api.snack_machines;

import java.math.BigDecimal;

public record SnackPileDTO (String snack, int quantity, BigDecimal price) {
}
