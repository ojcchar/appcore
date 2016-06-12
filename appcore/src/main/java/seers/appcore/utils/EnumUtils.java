package seers.appcore.utils;

public class EnumUtils {

	public static <T extends Enum<T>> T getEnumParam(String value, Class<T> clEnum) throws Exception {

		T type = null;
		try {
			type = Enum.valueOf(clEnum, value);
		} catch (IllegalArgumentException | NullPointerException e) {
			Exception targetExc = new RuntimeException(
					"Value not valid for enum " + clEnum.getSimpleName() + ": " + value);
			ExceptionUtils.addStackTrace(e, targetExc);
			throw targetExc;
		}

		return type;
	}
}
