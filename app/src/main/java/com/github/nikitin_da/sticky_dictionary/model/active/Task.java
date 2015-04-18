package com.github.nikitin_da.sticky_dictionary.model.active;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.github.nikitin_da.sticky_dictionary.util.Loggi;

import java.util.concurrent.Executor;

/**
 * Alternative variant of AsyncTask.
 *
 * @param <Result> type of the processing result
 *
 * @author Artem Zinnatullin [artem.zinnatullin@gmail.com]
 */
public abstract class Task<Result> {

    public static final int STATE_NOT_LAUNCHED = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_FINISHED = 2;
    public static final int STATE_CANCELLED = 3;

    private static final Handler UI_THREAD_HANDLER = new Handler(Looper.getMainLooper());

    private volatile int mState = STATE_NOT_LAUNCHED;
    private final TaskListener<Result> mListener;

    /**
     * Creates new task
     *
     * @param listener for callbacks, can be null, if you want to just run some code
     */
    public Task(@Nullable TaskListener<Result> listener) {
        mListener = listener;
    }

    private String getTag() {
        return ((Object) this).getClass().getName();
    }

    /**
     * Gets current state of the task
     *
     * @return one of #STATE_NOT_LAUNCHED..#STATE_FINISHED
     */
    public int getState() {
        return mState;
    }

    /**
     * Runs this task with notifying listener if required
     *
     * @param executor to run the task on it
     */
    public final void run(final Executor executor) {
        if (mState == STATE_CANCELLED) {
            return;
        }

        if (mState != STATE_NOT_LAUNCHED) {
            throw new IllegalStateException("Task was already launched!");
        }

        if (executor == null) {
            throw new IllegalArgumentException("Executor can not be null");
        }

        if (isCancelled()) {
            return;
        }

        mState = STATE_RUNNING;

        UI_THREAD_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                if (isCancelled()) {
                    return;
                }

                notifyListenerAboutOnPreExecute();

                if (isCancelled()) {
                    return;
                }

                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (isCancelled()) {
                            return;
                        }

                        final Result result;

                        try {
                            result = doWork();
                        } catch (Throwable e) {
                            if (isCancelled()) {
                                return;
                            }

                            notifyListenerAboutOnExceptionOccurred(e);
                            return;
                        }

                        mState = STATE_FINISHED;

                        if (isCancelled()) {
                            return;
                        }


                        if (!notifyListenerAboutDataProcessedBackground(result)) {
                            return;
                        }

                        UI_THREAD_HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                if (isCancelled()) {
                                    return;
                                }

                                notifyListenerAboutDataProcessed(result);
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * Cancels execution of the task
     */
    public final void cancel() {
        mState = STATE_CANCELLED;
    }

    /**
     * Your implementation can check result of this method in a loop for example in the doWork() and stop execution if result if true
     *
     * @return true if task was cancelled, false otherwise
     */
    protected final boolean isCancelled() {
        return mState == STATE_CANCELLED;
    }

    /**
     * Override this method and do some work, bitch! (Breaking Bad)
     *
     * @return typed result of the processing, can be null if task's listener is ready for this
     * @throws Throwable that can occur in execution process
     */
    protected abstract Result doWork() throws Throwable;

    private void notifyListenerAboutOnPreExecute() {
        if (mListener != null) {
            UI_THREAD_HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mListener.onPreExecute();
                    } catch (Exception e) {
                        Loggi.e(getTag(), "notifyListenerAboutOnPreExecute", e);
                    }
                }
            });
        }
    }

    private void notifyListenerAboutOnExceptionOccurred(final Throwable e) {
        if (mListener != null) {
            UI_THREAD_HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mListener.onProblemOccurred(e);
                        notifyListenerAboutAfterAllCallbacks();
                    } catch (Exception e) {
                        Loggi.e(getTag(), "notifyListenerAboutOnExceptionOccurred", e);
                    }
                }
            });
        }
    }

    private boolean notifyListenerAboutDataProcessedBackground(Result result) {
        if (mListener != null) {
            try {
                mListener.onDataProcessedBackground(result);
                return true;
            } catch (Exception e) {
                Loggi.e(getTag(), "notifyListenerAboutDataProcessedBackground", e);
                return false;
            }
        }

        return false;
    }

    private void notifyListenerAboutDataProcessed(final Result result) {
        if (mListener != null) {
            UI_THREAD_HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mListener.onDataProcessed(result);
                        notifyListenerAboutAfterAllCallbacks();
                    } catch (Exception e) {
                        Loggi.e(getTag(), "notifyListenerAboutDataProcessed", e);
                    }
                }
            });
        }
    }

    private void notifyListenerAboutAfterAllCallbacks() {
        if (mListener != null) {
            UI_THREAD_HANDLER.post(new Runnable() {
                @Override public void run() {
                    try {
                        mListener.afterAllCallbacks();
                    } catch (Exception e) {
                        Loggi.e(getTag(), "notifyListenerAboutAfterAllCallbacks", e);
                    }
                }
            });
        }
    }
}

