package cn.patterncat.jesque.event;

/**
 * Created by patterncat on 2017-11-16.
 */
public enum JobEventType {

    /**
     * The Worker just finished starting up and is about to start running.
     */
    WORKER_START,
    /**
     * The Worker is polling the queue.
     */
    WORKER_POLL,

    /**
     * WHEN CLIENT SUBMIT A JOB
     */
    JOB_SUBMITT,

    /**
     * WHEN CLIENT REMOVE A JOB
     */
    JOB_REMOVE,
    /**
     * The Worker is processing a Job.
     */
    JOB_PROCESS,
    /**
     * The Worker is about to execute a materialized Job.
     */
    JOB_EXECUTE,
    /**
     * The Worker successfully executed a materialized Job.
     */
    JOB_SUCCESS,
    /**
     * The Worker caught an Exception during the execution of a materialized Job.
     */
    JOB_FAILURE,
    /**
     * The Worker caught an Exception during normal operation.
     */
    WORKER_ERROR,
    /**
     * The Worker just finished running and is about to shutdown.
     */
    WORKER_STOP;
}
