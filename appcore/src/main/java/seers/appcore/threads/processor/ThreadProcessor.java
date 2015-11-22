package seers.appcore.threads.processor;

public interface ThreadProcessor {

	void processJob() throws ThreadException;

	String getName();
}
