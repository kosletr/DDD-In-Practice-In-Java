package com.example.demo.tests;

import com.example.demo.logic.shared_kernel.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class MoneyTests {
    @Test
    void sumOfTwoMoneysProducesCorrectResult () {
            Money money1 = Money.of(1,2,3,4,5,6);
            Money money2 = Money.of(1,2,3,4,5,6);

            Money sum = money1.add(money2);

            assertThat(sum.getOneCentCount()).isEqualTo(2);
            assertThat(sum.getTenCentCount()).isEqualTo(4);
            assertThat(sum.getQuarterCount()).isEqualTo(6);
            assertThat(sum.getOneDollarCount()).isEqualTo(8);
            assertThat(sum.getFiveDollarCount()).isEqualTo(10);
            assertThat(sum.getTwentyDollarCount()).isEqualTo(12);
    }

    @Test
     void twoMoneysEqualIfContainSameMoney() {
        Money money1 = Money.of(1,2,3,4,5,6);
        Money money2 = Money.of(1,2,3,4,5,6);

       assertThat(money1)
               .isEqualTo(money2)
               .hasSameHashCodeAs(money2);
    }

    @Test
    void twoMoneysNotEqualIfContainSameAmountButDifferentMoney() {
        Money hundredCents = Money.of(100,0,0,0,0,0);

        assertThat(hundredCents)
                .isNotEqualTo(Money.Dollar)
                .doesNotHaveSameHashCodeAs(Money.Dollar);
    }

    @ParameterizedTest
    @CsvSource({
            "-1,  0,  0,  0,  0,  0",
            " 0, -2,  0,  0,  0,  0",
            " 0,  0, -3,  0,  0,  0",
            " 0,  0,  0, -4,  0,  0",
            " 0,  0,  0,  0, -5,  0",
            " 0,  0,  0,  0,  0, -6"
    })
    void cannotCreateMoneyWithNegativeValue(int oneCentCount, int tenCentCount, int quarterCount, int oneDollarCount, int fiveDollarCount, int twentyDollarCount) {
        Throwable throwable = catchThrowable(() -> Money.of(oneCentCount, tenCentCount, quarterCount, oneDollarCount, fiveDollarCount, twentyDollarCount));

        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 0, 0, 0, 0, 0, 0.01",
            "1, 2, 0, 0, 0, 0, 0.21",
            "1, 2, 3, 0, 0, 0, 0.96",
            "1, 2, 3, 4, 0, 0, 4.96",
            "1, 2, 3, 4, 5, 0, 29.96",
            "1, 2, 3, 4, 5, 6, 149.96",
            "11, 0, 0, 0, 0, 0, 0.11",
            "110, 0, 0, 0, 100, 0, 501.1"
    })
    void calculateCorrectMoneyAmount(int oneCentCount, int tenCentCount, int quarterCount, int oneDollarCount, int fiveDollarCount, int twentyDollarCount, BigDecimal expectedAmount) {
        Money money = Money.of(oneCentCount,tenCentCount,quarterCount,oneDollarCount,fiveDollarCount,twentyDollarCount);

        assertThat(money.amount()).isEqualByComparingTo(expectedAmount);
    }

    @Test
    void subtractionOfTwoMoneysProducesCorrectResult() {
        Money money1 = Money.of(1,2,3,4,5,6);
        Money money2 = Money.of(1,1,1,1,1,1);

        Money sum = money1.subtract(money2);

        assertThat(sum.getOneCentCount()).isEqualTo(0);
        assertThat(sum.getTenCentCount()).isEqualTo(1);
        assertThat(sum.getQuarterCount()).isEqualTo(2);
        assertThat(sum.getOneDollarCount()).isEqualTo(3);
        assertThat(sum.getFiveDollarCount()).isEqualTo(4);
        assertThat(sum.getTwentyDollarCount()).isEqualTo(5);
    }


    @Test
    void cannotSubtractIfCoinsAreNotEnough () {
        Money money1 = Money.of(0,1,0,0,0,0);
        Money money2 = Money.of(1,0,0,0,0,0);

        Throwable throwable = catchThrowable(() -> money1.subtract(money2));

        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }


    @ParameterizedTest
    @CsvSource({
            "1, 0, 0, 0, 0, 0, ¢1",
            "0, 0, 0, 1, 0, 0, $1.00",
            "1, 0, 0, 1, 0, 0, $1.01",
            "0, 0, 2, 1, 0, 0, $1.50"
    })
    void toStringShouldReturnMoneyAmount(int oneCentCount, int tenCentCount, int quarterCount, int oneDollarCount, int fiveDollarCount, int twentyDollarCount, String expectedAmount) {
        Money money = Money.of(oneCentCount,tenCentCount,quarterCount,oneDollarCount,fiveDollarCount,twentyDollarCount);

        assertThat(money).hasToString(expectedAmount);
    }

    @Test
    void productOfMoneyWithMultiplierProducesCorrectResult() {
        Money dollar = Money.Dollar;
        int multiplier = 2;

        Money product = dollar.multiply(multiplier);

        assertThat(product.getOneCentCount()).isZero();
        assertThat(product.getTenCentCount()).isZero();
        assertThat(product.getQuarterCount()).isZero();
        assertThat(product.getOneDollarCount()).isEqualTo(2);
        assertThat(product.getFiveDollarCount()).isZero();
        assertThat(product.getTwentyDollarCount()).isZero();
    }

    // TODO
    //    @ParameterizedTest
    //    @CsvSource({
    //
    //    })
    //    void allocateShouldReturnMoneyWithHighestDenomination(int oneCentCount, int tenCentCount, int quarterCount, int oneDollarCount, int fiveDollarCount, int twentyDollarCount, String expectedAmount) {
    //
    //    }
}
