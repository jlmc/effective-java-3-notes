package io.costax.money;

import org.javamoney.moneta.Money;
import org.javamoney.moneta.function.MonetaryOperators;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;
import java.util.List;
import java.util.Locale;
import java.util.Set;

class MoneyManipulationJSR354Test {

    @Test
    void basic() {
        final Set<CurrencyUnit> currencies = Monetary.getCurrencies(new Locale("pt", "PT"));
        final CurrencyUnit currencyUnit = currencies.stream().findFirst().orElseThrow(AssertionError::new);

        System.out.println(currencyUnit);
        System.out.println(currencyUnit.getNumericCode());

        final Money moneyValue = Money.of(123.89, currencyUnit);

        System.out.println(moneyValue);
    }

    @Test
    void yearlyValueCalculation() {
        final CurrencyUnit eur = Monetary.getCurrency("EUR");

        final Money monthly = Money.of(13.14, eur);
        final Money yearlyAmount = monthly.multiply(12L);
        final Money myPart = yearlyAmount.with(MonetaryOperators.percent(25.0D));

        Assertions.assertNotNull(eur);
        Assertions.assertEquals(Money.of(157.68, eur), yearlyAmount);
        Assertions.assertEquals(Money.of(39.42, eur), myPart);
    }

    @Test
    void conventions() {
        final CurrencyUnit euro = Monetary.getCurrency("EUR");
        final Money tenEuro = Money.of(10, euro);

        // get the default ExchangeRateProvider (CompoundRateProvider)
        ExchangeRateProvider exchangeRateProvider = MonetaryConversions.getExchangeRateProvider();

        // get the names of the default provider chain
        // [IDENT, ECB, IMF, ECB-HIST]
        List<String> defaultProviderChain = MonetaryConversions.getDefaultConversionProviderChain();
        // get a specific ExchangeRateProvider (here ECB) [ECB -> European Central Bank]
        ExchangeRateProvider ecbExchangeRateProvider = MonetaryConversions.getExchangeRateProvider("ECB");

        // get the exchange rate from euro to us dollar
        ExchangeRate rate = exchangeRateProvider.getExchangeRate("EUR", "USD");

        CurrencyConversion dollarConversion = MonetaryConversions.getConversion("USD");

        MonetaryAmount inDollar = tenEuro.with(dollarConversion);

        System.out.println(inDollar);
    }
}
