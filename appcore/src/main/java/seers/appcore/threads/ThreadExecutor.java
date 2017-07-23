package seers.appcore.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;

public class ThreadExecutor {

	public static final String ELEMENT_PARAM = "element";
	public static final String ELEMENTS_PARAM = "elements";

	@SuppressWarnings("rawtypes")
	public static List<ThreadProcessor> executeOneByOne(final List objects, Class<? extends ThreadProcessor> class1,
			final ThreadParameters params, int poolSize) throws Exception {

		ThreadCommandExecutor executor = new ThreadCommandExecutor();
		executor.setCorePoolSize(poolSize);
		try {

			// create the threads
			List<ThreadProcessor> procs = new ArrayList<>();

			for (Object element : objects) {

				ThreadParameters newParams = new ThreadParameters(params);
				newParams.addParam(ELEMENT_PARAM, element);

				ThreadProcessor newInstance = class1.getConstructor(ThreadParameters.class).newInstance(newParams);
				procs.add(newInstance);
			}

			// run the threads
			CountDownLatch cntDwnLatch = new CountDownLatch(procs.size());
			for (ThreadProcessor proc : procs) {
				executor.executeCommRunnable(new CommandLatchRunnable(proc, cntDwnLatch, (long) procs.size()));
			}
			cntDwnLatch.await();

			return procs;
		} finally {
			executor.shutdown();
		}
	}

	@SuppressWarnings("rawtypes")
	public static List<ThreadProcessor> executePaginated(final List objects, Class<? extends ThreadProcessor> class1,
			final ThreadParameters params, int poolSize, int pageSize) throws Exception {
		ThreadCommandExecutor executor = new ThreadCommandExecutor();
		executor.setCorePoolSize(poolSize);
		try {

			// create the threads
			List<ThreadProcessor> procs = new ArrayList<>();
			int num = objects.size();
			for (int offset = 0; offset < num; offset += pageSize) {

				int fromIndex = offset;
				int toIndex = offset + pageSize;
				if (toIndex >= num) {
					toIndex = num;
				}
				List sublist = objects.subList(fromIndex, toIndex);

				ThreadParameters newParams = new ThreadParameters(params);
				newParams.addParam(ELEMENTS_PARAM, sublist);
				ThreadProcessor newInstance = class1.getConstructor(ThreadParameters.class).newInstance(newParams);
				procs.add(newInstance);
			}

			// run the threads
			CountDownLatch cntDwnLatch = new CountDownLatch(procs.size());
			for (ThreadProcessor proc : procs) {
				executor.executeCommRunnable(new CommandLatchRunnable(proc, cntDwnLatch));
			}
			cntDwnLatch.await();

			return procs;

		} finally {
			executor.shutdown();
		}
	}

	@SuppressWarnings("rawtypes")
	public static List<ThreadProcessor> executePaginated(final List objects, Class<? extends ThreadProcessor> class1,
			final ThreadParameters params, int poolSize) throws Exception {
		int pageSize = getPageSize(objects, poolSize);
		return executePaginated(objects, class1, params, poolSize, pageSize);
	}

	@SuppressWarnings("rawtypes")
	public static List<ThreadProcessor> executePaginated(final List objects, Class<? extends ThreadProcessor> class1,
			final ThreadParameters params) throws Exception {

		// threads
		int poolSize = 15;
		int pageSize = getPageSize(objects, poolSize);

		return executePaginated(objects, class1, params, poolSize, pageSize);
	}

	@SuppressWarnings("rawtypes")
	public static int getPageSize(final List objects, int poolSize) {
		int pageSize = (int) Math.ceil((double) objects.size() / poolSize);
		return pageSize;
	}

}
