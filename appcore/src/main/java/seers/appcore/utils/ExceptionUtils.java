package seers.appcore.utils;

public class ExceptionUtils {

	public static void addStackTrace(Exception sourceExc, Exception targetExc) {
		targetExc.setStackTrace(sourceExc.getStackTrace());
	}

}
