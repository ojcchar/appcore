package seers.appcore.threads.processor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ThreadParameters {

	private Map<String, Object> params;

	public ThreadParameters(ThreadParameters params2) {
		params = new LinkedHashMap<>(params2.params);
	}

	public ThreadParameters() {
		params = new LinkedHashMap<>();
	}

	public void addParam(String param, Object value) {
		params.put(param, value);
	}

	public void removeParam(Object param) {
		params.remove(param);
	}

	public String getStringParam(String param) {
		return getParam(String.class, param);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getListParam(Class<T> cl, String param) {
		return getParam(List.class, param);
	}

	@SuppressWarnings("unchecked")
	public <T> T getParam(Class<T> cl, String param) {
		return (T) params.get(param);
	}

	public void changeParamName(String param1, String param2) {
		Object val1 = params.get(param1);
		removeParam(param1);
		addParam(param2, val1);
	}
}
