package cn.patterncat.jesque.component;

import net.greghaines.jesque.utils.ConcurrentHashSet;
import net.greghaines.jesque.utils.ConcurrentSet;
import net.greghaines.jesque.worker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * https://github.com/moznion/jesque-robust-worker-pool/blob/master/src/main/java/net/moznion/jesque/worker/RobustWorkerPool.java
 * Creates a fixed number of identical Workers, each on a separate {@code Thread}.
 * <p>
 * RobustWorkerPool monitors status of pooling workers and if it detects died worker then reincarnate a new worker.
 */
public class RobustWorkerPool implements Worker {

    private static final Logger LOGGER = LoggerFactory.getLogger(RobustWorkerPool.class);

    private static final long NO_DELAY = 0;

    private final ConcurrentSet<Worker> workerSet;
    private final ConcurrentMap<Worker, Thread> workerThreadMap;

    private final WorkerPoolEventEmitter workerPoolEventEmitter;
    private final Callable<? extends Worker> workerFactory;
    private final ThreadFactory threadFactory;
    private final long delayToStartPollingMillis;

    private volatile int numWorkers;

    private volatile boolean isStarted;
    private volatile boolean isEnded;
    private volatile boolean isCalledJoin;
    private volatile long joinMillis;

    /**
     * Create a RobustWorkerPool with the given number of Workers and the default
     * {@code ThreadFactory} and the default delay to start polling (default: no delay).
     *
     * @param workerFactory a Callable that returns an implementation of Worker
     * @param numWorkers    the number of Workers to create
     */
    public RobustWorkerPool(final Callable<? extends Worker> workerFactory, final int numWorkers) {
        this(workerFactory, numWorkers, Executors.defaultThreadFactory(), NO_DELAY);
    }

    /**
     * Create a RobustWorkerPool with the given number of Workers and the given
     * {@code ThreadFactory} and the default delay to start polling (default: no delay).
     *
     * @param workerFactory a Callable that returns an implementation of Worker
     * @param numWorkers    the number of Workers to create
     * @param threadFactory the factory to create pre-configured Threads
     */
    public RobustWorkerPool(final Callable<? extends Worker> workerFactory, final int numWorkers,
                            final ThreadFactory threadFactory) {
        this(workerFactory, numWorkers, threadFactory, NO_DELAY);
    }

    /**
     * Create a RobustWorkerPool with the given number of Workers and the given
     * {@code ThreadFactory} and the given delay to start polling.
     *
     * @param workerFactory             a Callable that returns an implementation of Worker
     * @param numWorkers                the number of Workers to create
     * @param threadFactory             the factory to create pre-configured Threads
     * @param delayToStartPollingMillis the milliseconds that represents delay to start polling when a new worker is spawned
     */
    public RobustWorkerPool(final Callable<? extends Worker> workerFactory, final int numWorkers,
                            final ThreadFactory threadFactory, final long delayToStartPollingMillis) {
        this.numWorkers = numWorkers;
        this.workerFactory = workerFactory;
        this.threadFactory = threadFactory;

        isStarted = false;
        isEnded = false;
        isCalledJoin = false;
        joinMillis = -1;

        workerSet = new ConcurrentHashSet<>(numWorkers);
        workerThreadMap = new ConcurrentHashMap<>(numWorkers);

        for (int i = 0; i < numWorkers; i++) {
            try {
                final Worker worker = workerFactory.call();
                workerSet.add(worker);
                workerThreadMap.put(worker, threadFactory.newThread(worker));
            } catch (RuntimeException re) {
                throw re;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        workerPoolEventEmitter = new WorkerPoolEventEmitter(this);

        this.delayToStartPollingMillis = delayToStartPollingMillis;
    }

    /**
     * Shutdown this pool and wait millis time per thread or until all threads are finished if millis is 0.
     *
     * @param now    if true, an effort will be made to stop any jobs in progress
     * @param millis the time to wait in milliseconds for the threads to join; a timeout of 0 means to wait forever.
     * @throws InterruptedException if any thread has interrupted the current thread.
     *                              The interrupted status of the current thread is cleared when this exception is thrown.
     */
    public void endAndJoin(final boolean now, final long millis) throws InterruptedException {
        end(now);
        join(millis);
    }

    /**
     * Join to internal threads and wait millis time per thread or until all
     * threads are finished if millis is 0.
     *
     * @param millis the time to wait in milliseconds for the threads to join; a
     *               timeout of 0 means to wait forever.
     * @throws InterruptedException if any thread has interrupted the current thread. The
     *                              interrupted status of the current thread is cleared when this
     *                              exception is thrown.
     */
    @Override
    public void join(final long millis) throws InterruptedException {
        isCalledJoin = true;
        joinMillis = millis;
        for (final Thread thread : workerThreadMap.values()) {
            thread.join(millis);
        }
    }

    @Override
    public String getName() {
        final StringBuilder sb = new StringBuilder(128 * workerThreadMap.size());
        String prefix = "";
        for (final Worker worker : workerSet) {
            sb.append(prefix).append(worker.getName());
            prefix = " | ";
        }
        return sb.toString();
    }

    @Override
    public WorkerEventEmitter getWorkerEventEmitter() {
        return this.workerPoolEventEmitter;
    }

    @Override
    public void run() {
        isStarted = true;
        workerThreadMap.values().forEach(Thread::start);
        Thread.yield();
    }

    @Override
    public void end(final boolean now) {
        isEnded = true;
        for (final Worker worker : workerSet) {
            worker.end(now);
        }
    }

    @Override
    public boolean isShutdown() {
        final Iterator<Worker> iter = workerSet.iterator();
        return !iter.hasNext() || iter.next().isShutdown();
    }

    @Override
    public boolean isPaused() {
        final Iterator<Worker> iter = workerSet.iterator();
        return !iter.hasNext() || iter.next().isPaused();
    }

    @Override
    public void togglePause(final boolean paused) {
        for (final Worker worker : workerSet) {
            worker.togglePause(paused);
        }
    }

    @Override
    public boolean isProcessingJob() {
        boolean processingJob = false;
        for (final Worker worker : workerSet) {
            processingJob = worker.isProcessingJob();
            if (processingJob) {
                break;
            }
        }
        return processingJob;
    }

    @Override
    public JobFactory getJobFactory() {
        final Iterator<Worker> iter = workerSet.iterator();
        if (!iter.hasNext()) {
            return null;
        }
        return iter.next().getJobFactory();
    }

    @Override
    public Collection<String> getQueues() {
        final Iterator<Worker> iter = workerSet.iterator();
        if (!iter.hasNext()) {
            return null;
        }
        return iter.next().getQueues();
    }

    @Override
    public void addQueue(final String queueName) {
        for (final Worker worker : workerSet) {
            worker.addQueue(queueName);
        }
    }

    @Override
    public void removeQueue(final String queueName, final boolean all) {
        for (final Worker worker : workerSet) {
            worker.removeQueue(queueName, all);
        }
    }

    @Override
    public void removeAllQueues() {
        workerSet.forEach(Worker::removeAllQueues);
    }

    @Override
    public void setQueues(final Collection<String> queues) {
        for (final Worker worker : workerSet) {
            worker.setQueues(queues);
        }
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        final Iterator<Worker> iter = workerSet.iterator();
        if (!iter.hasNext()) {
            return null;
        }
        return iter.next().getExceptionHandler();
    }

    @Override
    public void setExceptionHandler(final ExceptionHandler exceptionHandler) {
        for (final Worker worker : workerSet) {
            worker.setExceptionHandler(exceptionHandler);
        }
    }

    /**
     * Adjust number of active workers.
     *
     * @param diedWorker a worker which fires WORKER_STOP event
     */
    private synchronized void adjustWorkers(Worker diedWorker) {
        // Remove died worker from pooling information.
        workerSet.remove(diedWorker);
        workerThreadMap.remove(diedWorker);

        // If already `end()` has called, do not reincarnate (make) or terminate (kill) a worker.
        if (!isEnded) {
            {
                // Adjust number of workers when missing workers existed.
                // Spawn new workers up to the upper limit of workers.
                final int numCurrentWorkers = workerSet.size();
                LOGGER.debug("Number of current workers: {}", numCurrentWorkers);
                if (numCurrentWorkers < numWorkers) {
                    final int missingWorkersNum = numWorkers - numCurrentWorkers;
                    LOGGER.debug("Missing workers: {}", missingWorkersNum);
                    IntStream.rangeClosed(1, missingWorkersNum)
                            .forEach(i -> spawnMissingWorker(diedWorker));
                }
            }
            {
                // Adjust number of workers when excess workers existed.
                // Terminate excess workers.
                // NOTE: FOR JUST IN CASE
                final int numCurrentWorkers = workerSet.size();
                LOGGER.debug("Number of current workers: {}", numCurrentWorkers);
                if (numCurrentWorkers > numWorkers) {
                    final int excessWorkersNum = numCurrentWorkers - numWorkers;
                    LOGGER.debug("Excess workers: {}", excessWorkersNum);
                    IntStream.rangeClosed(1, excessWorkersNum)
                            .forEach(i -> terminateExcessWorker());
                }
            }
        }
    }

    /**
     * Spawn a new worker. Spawned worker's information is based on died worker.
     *
     * @param diedWorker base worker
     */
    private void spawnMissingWorker(Worker diedWorker) {
        final Collection<String> queues = diedWorker.getQueues();
        final ExceptionHandler exceptionHandler = diedWorker.getExceptionHandler();
        final Map<WorkerEvent, Set<WorkerListener>> eventToListenersMap =
                workerPoolEventEmitter.getEventToListenersMapContainer().getEventToListenersMap();

        try {
            final Worker worker = workerFactory.call();
            worker.setQueues(queues); // restore queues.
            worker.setExceptionHandler(exceptionHandler); // restore exception handler.

            // restore event listeners.
            for (Map.Entry<WorkerEvent, Set<WorkerListener>> entry : eventToListenersMap.entrySet()) {
                WorkerEvent event = entry.getKey();
                Set<WorkerListener> listeners = entry.getValue();
                for (WorkerListener listener : listeners) {
                    worker.getWorkerEventEmitter().addListener(listener, event);
                }
            }

            // Add a new worker into pooling information.
            Thread newThread = threadFactory.newThread(worker);
            workerSet.add(worker);
            workerThreadMap.put(worker, newThread);

            if (isStarted) {
                newThread.start();
            }

            if (isCalledJoin) {
                newThread.join(joinMillis);
            }

            LOGGER.debug("Spawned a new worker (Worker Name: {})", worker.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void terminateExcessWorker() {
        List<Worker> workers = workerSet.stream().collect(Collectors.toList());
        if (workers.size() <= 0) {
            // DO NOTHING. There is no any worker
            return;
        }

        // Pick up a worker. That will be terminated.
        Worker victim = workers.get(0);

        // Shutdown worker gracefully. It fires "WORKER_STOP" but it may not be effective.
        victim.end(false);

        // Remove a terminated worker from information of worker pooling.
        workerSet.remove(victim);
        workerThreadMap.remove(victim);
    }

    private class WorkerPoolEventEmitter implements WorkerEventEmitter {
        private final RobustWorkerPool pool;

        private final EventToListenerMapContainer eventToListenersMapContainer;

        public WorkerPoolEventEmitter(RobustWorkerPool pool) {
            this.pool = pool;
            if (this.pool.workerSet == null) {
                throw new RuntimeException("Workers must not be null");
            }

            eventToListenersMapContainer = new EventToListenerMapContainer();

            // IF WORKER_START event is received, show message
            addListener((event, worker, queue, job, runner, result, t) -> {
                LOGGER.debug("Worker is started (Worker Name: {})", worker.getName());
                final long delay = this.pool.delayToStartPollingMillis;
                if (delay > 0) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        LOGGER.warn(e.toString());
                    }
                }
            }, WorkerEvent.WORKER_START);

            // If WORKER_STOP event is received, adjust active workers (reincarnate as a new worker or terminate excesses)
            addListener((event, worker, queue, job, runner, result, t) -> {
                LOGGER.debug("Worker is stopped (Worker Name: {})", worker.getName());
                adjustWorkers(worker);
            }, WorkerEvent.WORKER_STOP);
        }

        @Override
        public void addListener(final WorkerListener listener) {
            for (final Worker worker : pool.workerSet) {
                worker.getWorkerEventEmitter().addListener(listener);
            }

            eventToListenersMapContainer.addListener(Arrays.asList(WorkerEvent.values()), listener);
        }

        @Override
        public void addListener(final WorkerListener listener, final WorkerEvent... events) {
            for (final Worker worker : pool.workerSet) {
                worker.getWorkerEventEmitter().addListener(listener, events);
            }

            eventToListenersMapContainer.addListener(Arrays.asList(events), listener);
        }

        @Override
        public void removeListener(final WorkerListener listener) {
            for (final Worker worker : pool.workerSet) {
                worker.getWorkerEventEmitter().removeListener(listener);
            }

            eventToListenersMapContainer.removeListener(Arrays.asList(WorkerEvent.values()), listener);
        }

        @Override
        public void removeListener(final WorkerListener listener, final WorkerEvent... events) {
            for (final Worker worker : pool.workerSet) {
                worker.getWorkerEventEmitter().removeListener(listener, events);
            }

            eventToListenersMapContainer.removeListener(Arrays.asList(events), listener);
        }

        @Override
        public void removeAllListeners() {
            for (final Worker worker : pool.workerSet) {
                worker.getWorkerEventEmitter().removeAllListeners();
            }

            eventToListenersMapContainer.removeAllListeners(Arrays.asList(WorkerEvent.values()));
        }

        @Override
        public void removeAllListeners(final WorkerEvent... events) {
            for (final Worker worker : pool.workerSet) {
                worker.getWorkerEventEmitter().removeAllListeners(events);
            }

            eventToListenersMapContainer.removeAllListeners(Arrays.asList(events));
        }

        public EventToListenerMapContainer getEventToListenersMapContainer() {
            return eventToListenersMapContainer;
        }
    }

    /**
     * Container of Map that has relationship between WorkerEvent and set of WorkerListeners.
     * <p>
     * This class used to restore listeners when reincarnating a new worker.
     */
    private static class EventToListenerMapContainer {
        private final Map<WorkerEvent, Set<WorkerListener>> eventToListenersMap;

        public EventToListenerMapContainer() {
            eventToListenersMap = new HashMap<>();

            // initialize for each events.
            for (WorkerEvent event : WorkerEvent.values()) {
                eventToListenersMap.put(event, new HashSet<>());
            }
        }

        public void addListener(List<WorkerEvent> events, WorkerListener listener) {
            for (WorkerEvent event : events) {
                eventToListenersMap.get(event).add(listener);
            }
        }

        public void removeListener(List<WorkerEvent> events, WorkerListener listener) {
            for (WorkerEvent event : events) {
                eventToListenersMap.get(event).remove(listener);
            }
        }

        public void removeAllListeners(List<WorkerEvent> events) {
            for (WorkerEvent event : events) {
                eventToListenersMap.get(event).clear();
            }
        }

        public Map<WorkerEvent, Set<WorkerListener>> getEventToListenersMap() {
            return eventToListenersMap;
        }
    }

    public ConcurrentSet<Worker> getWorkerSet() {
        return workerSet;
    }

    public ConcurrentMap<Worker, Thread> getWorkerThreadMap() {
        return workerThreadMap;
    }

    public int getNumWorkers() {
        return numWorkers;
    }

    public void setNumWorkers(int numWorkers) {
        this.numWorkers = numWorkers;
    }
}
