package seers.appcore.threads.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ThreadProcessor {

	protected ThreadParameters params;
	private String threadName;
	protected Logger LOGGER;

	public ThreadProcessor(ThreadParameters params) {
		this.params = params;
		LOGGER = LoggerFactory.getLogger(this.getClass());
	}

	abstract public void executeJob() throws Exception;

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
		LOGGER = LoggerFactory.getLogger(threadName);
	}
}
