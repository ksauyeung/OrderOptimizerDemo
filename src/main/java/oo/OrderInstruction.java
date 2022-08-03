package oo;

public interface OrderInstruction extends HasPriceLevel {

    Side getSide();
    long getVolume();

}
