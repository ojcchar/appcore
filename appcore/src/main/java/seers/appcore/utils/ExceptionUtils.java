package seers.appcore.utils;

public class ExceptionUtils {

	public static void addStackTrace(Exception sourceExc, Exception targetExc) {
		targetExc.setStackTrace(sourceExc.getStackTrace());
	}

	public static RuntimeException getRuntimeException(Exception e) {
		RuntimeException e2 = new RuntimeException(e.getMessage());
		ExceptionUtils.addStackTrace(e, e2);
		return e2;
	}

}
