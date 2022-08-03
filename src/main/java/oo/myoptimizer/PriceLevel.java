package oo.myoptimizer;

import oo.HasPriceLevel;
import oo.Side;

import java.math.BigDecimal;

/**
 * Defines a unique price level
 */
public final class PriceLevel implements HasPriceLevel {

    private HasPriceLevel p;
    public PriceLevel(HasPriceLevel priceLevel) {
        this.p = priceLevel;
    }

    /*@Override
    public Side getSide() {
        return p.getSide();
    }
*/
    @Override
    public BigDecimal getPrice() {
        return p.getPrice();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        //result = prime * result + ((getSide() == null) ? 0 : getSide().hashCode());
        result = prime * result + ((getPrice().stripTrailingZeros() == null) ? 0 : getPrice().hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PriceLevel other = (PriceLevel) obj;
        return matches(other);
    }

}
