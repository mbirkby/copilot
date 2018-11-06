package milesb.copilot.core.domain;

public class InvalidAreaOfInterestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	InvalidAreaOfInterestException(String msg) {
    	 super("Invalid AreaOfInterest: "+msg);
     }
}
