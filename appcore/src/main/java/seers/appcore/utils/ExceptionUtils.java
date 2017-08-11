package seers.appcore.utils;

public class ExceptionUtils {

	public static void addStackTrace(Exception sourceExc, Exception targetExc) {
		targetExc.setStackTrace(sourceExc.getStackTrace());
	}

	public static RuntimeException getRuntimeException(Exception e) {
		String message = e.getMessage();
		if (message == null) {
			message = e.getClass().getName();
		}
		return getRuntimeException(e, message);
	}

	public static RuntimeException getRuntimeException(Exception e, String message) {
		RuntimeException e2 = new RuntimeException(message);
		ExceptionUtils.addStackTrace(e, e2);
		return e2;
	}

}
