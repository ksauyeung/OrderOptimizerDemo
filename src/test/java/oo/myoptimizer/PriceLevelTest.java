package oo.myoptimizer;

import oo.HasPriceLevel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class PriceLevelTest {

    @Test
    void MatchesShouldReturnFalseIfAnotherIsNull() {

        HasPriceLevel priceLevel = new HasPriceLevel() {
            @Override
            public BigDecimal getPrice() {
                return null;
            }
        };

        HasPriceLevel another = null;

        Assertions.assertFalse(priceLevel.matches(another));
    }

    @Test
    void MatchesShouldReturnFalseIfPricesAreNull() {
        HasPriceLevel priceLevel = new HasPriceLevel() {
            @Override
            public BigDecimal getPrice() {
                return null;
            }
        };

        HasPriceLevel another = new HasPriceLevel() {
            @Override
            public BigDecimal getPrice() {
                return null;
            }
        };

        Assertions.assertFalse(priceLevel.matches(another));
    }

    @Test
    void MatchesShouldReturnFalseIfPricesAreDifferent() {
        HasPriceLevel priceLevel = new HasPriceLevel() {
            @Override
            public BigDecimal getPrice() {
                return new BigDecimal(1.23);
            }
        };

        HasPriceLevel another = new HasPriceLevel() {
            @Override
            public BigDecimal getPrice() {
                return new BigDecimal(1.299999999999999);
            }
        };
        Assertions.assertFalse(priceLevel.matches(another));
    }

    @Test
    void MatchesShouldReturnTrueIfSamePrice() {

        HasPriceLevel priceLevel = new HasPriceLevel() {
            @Override
            public BigDecimal getPrice() {
                return new BigDecimal(1.23);
            }
        };

        HasPriceLevel another = new HasPriceLevel() {
            @Override
            public BigDecimal getPrice() {
                return new BigDecimal(1.23);
            }
        };
        Assertions.assertTrue(priceLevel.matches(another));
    }

}
