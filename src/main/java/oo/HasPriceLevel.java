package oo;

import java.math.BigDecimal;

public interface HasPriceLevel {

    BigDecimal getPrice();

    default boolean matches(HasPriceLevel another) {

        if(another == null || this.getPrice() == null || another.getPrice() == null) {
            return false;
        }

        return this.getPrice().compareTo(another.getPrice()) == 0;
    }
/*
    default boolean matches(HasPriceLevel another) {

        if(another == null || this.getSide() == null || another.getSide() == null || this.getPrice() == null || another.getPrice() == null) {
            return false;
        }

        return this.getSide() == another.getSide() && this.getPrice().compareTo(another.getPrice()) == 0;
    }
 */
}
