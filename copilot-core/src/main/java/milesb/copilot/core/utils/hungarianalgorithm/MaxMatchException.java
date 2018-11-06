package milesb.copilot.core.utils.hungarianalgorithm;

class MaxMatchException extends RuntimeException{
    MaxMatchException(String msg) {
        super("Failed operation: "+msg);
    }
}
