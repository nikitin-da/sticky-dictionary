package com.example.dmitry.handheld_dictionary.model.active;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import com.example.dmitry.handheld_dictionary.App;
import com.example.dmitry.handheld_dictionary.util.Loggi;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author Artem Zinnatullin [artem.zinnatullin@gmail.com]
 */
public abstract class BaseActiveModel {

    private static final Executor EXECUTOR_SERVICE = Executors.newCachedThreadPool(new ThreadFactory() {
        private final AtomicInteger mThreadsCounter = new AtomicInteger(0);
        private final int mThreadPriority = 3;

        @Override
        public Thread newThread(@NonNull Runnable r) {
            int threadId = mThreadsCounter.getAndIncrement();
            Thread thread = new Thread(r, "ActiveModelThread#" + threadId);
            thread.setPriority(mThreadPriority);
            return thread;
        }
    });

    private final Context mContext;

    private Queue<Task> mActiveModelTasks = new ConcurrentLinkedQueue<Task>();

    public BaseActiveModel(@NonNull Context context) {
        mContext = context.getApplicationContext();
        mQueueDispatcherThread.start();

        if (shouldInject()) {
            App.get(context).inject(this);
        }
    }

    protected boolean shouldInject() {
        return false;
    }

    protected String getLogTag() {
        return ((Object) this).getClass().getSimpleName();
    }

    private Thread mQueueDispatcherThread = new Thread(getClass().getName() + "#queue dispatcher") {
        @Override
        public void run() {
            while (!isInterrupted()) {
                int removedTasksCount = 0;

                for (Task task : mActiveModelTasks) {
                    if (isInterrupted()) {
                        Loggi.w(getClass().getName(), "stopping handling async tasks");
                        return;
                    }

                    final int taskState = task.getState();

                    if (taskState == Task.STATE_FINISHED || taskState == Task.STATE_CANCELLED) {
                        removedTasksCount++;
                        mActiveModelTasks.remove(task);
                    }
                }

                if (removedTasksCount != 0) {
                    Loggi.d(getClass().getName(), "removed finished tasks count:" + removedTasksCount);
                }

                SystemClock.sleep(1000);
            }

            Loggi.w(getClass().getName(), "stopping handling async tasks");
        }
    };

    /**
     * Gets context for this active model
     *
     * @return application context instance
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * Gets default executor for background tasks
     *
     * @return executor for background tasks
     */
    @SuppressWarnings("UnusedDeclaration") protected static Executor getExecutor() {
        return EXECUTOR_SERVICE;
    }

    protected void executeTask(Task task) {
        mActiveModelTasks.add(task);
        task.run(EXECUTOR_SERVICE);
    }

    /**
     * Cancels all running background tasks in this active model
     */
    public void cancelAllRunningTasks() {
        int count = 0;

        for (Task task : mActiveModelTasks) {
            try {
                task.cancel();
                count++;
            } catch (Exception e) {
                Loggi.w(getClass().getName(), "cancelAllRunningTasks", e);
            }
        }

        Loggi.i(getClass().getName(), "cancelAllRunningTasks, cancelled " + count + " tasks");
    }

    /**
     * Releases resources of this Active Model, cancels running tasks
     */
    public void release() {
        cancelAllRunningTasks();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        Thread thread = mQueueDispatcherThread;

        if (thread != null) {
            thread.interrupt();
        }
    }
}