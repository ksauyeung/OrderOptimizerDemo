package oo.myoptimizer;

public class InvalidOrderInstructionException extends RuntimeException {
    public InvalidOrderInstructionException(String reason) {
        super(reason);
    }

}
