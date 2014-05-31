package com.mydeepsky.android.task;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.mydeepsky.android.task.TaskResult.TaskStatus;

public abstract class Task extends AsyncTask<TaskContext, Integer, TaskResult> {
    public final static String KEY_RESULT = "task_result";
    public final static String KEY_START_TIME = "task_start_time";
    public final static String KEY_END_TIME = "task_end_time";

    private static final int DEFAULT_TIME_OUT = 20 * 1000;

    private boolean mReturn;

    private int mTimeout;

    protected int mDelay;

    private Timer mTimer;

    private Collection<WeakReference<OnTaskListener>> mTaskListeners;

    private Collection<WeakReference<OnTaskFinishListener>> mTaskFinishListeners;

    private TaskContext mReturnContext;

    public Task(int timeout) {
        this.mDelay = 0;
        this.mTimeout = timeout;
        this.mTimer = new Timer();
        this.mTaskListeners = new ArrayList<WeakReference<OnTaskListener>>();
        this.mTaskFinishListeners = new ArrayList<WeakReference<OnTaskFinishListener>>();
    }

    public Task() {
        this(DEFAULT_TIME_OUT);
    }

    public abstract String getID();

    public abstract String getType();

    public abstract void pause();

    public abstract void resume();

    @Override
    protected void onPreExecute() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, mTimeout + mDelay);
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 0:
                onTaskTimeout(Task.this, new TaskTimeoutEvent("Task timeout", mReturnContext));
                break;
            default:
                break;
            }
        }
    };

    @Override
    protected void onPostExecute(TaskResult result) {
        if (mReturn)
            return;
        if (result == null) {
            result = new TaskResult(TaskStatus.Failed, "TaskResult is null");
        }
        if (result.getStatus() == TaskStatus.Finished) {
            onTaskFinished(this, new TaskFinishedEvent(result.getContext()));
        } else if (result.getStatus() == TaskStatus.Cancel) {
            onTaskCancel(this, new TaskCancelEvent(result.getMessage()));
        } else {
            onTaskFailed(this, new TaskFailedEvent(result.getMessage(), result.getContext()));
        }
    }

    public void setTimeout(int timeout) {
        this.mTimeout = timeout;
    }

    public void setReturnContext(TaskContext context) {
        this.mReturnContext = context;
    }

    public void setDelay(int delay) {
        this.mDelay = delay;
    }

    @Override
    protected void onCancelled() {
        if (mReturn)
            return;
        onTaskCancel(this, new TaskCancelEvent("Cancelled"));
    }

    public void addTaskListener(WeakReference<OnTaskListener> listener) {
        if (listener != null) {
            mTaskListeners.add(listener);
        }
    }

    public void addTaskFinishListener(WeakReference<OnTaskFinishListener> listener) {
        if (listener != null) {
            mTaskFinishListeners.add(listener);
        }
    }

    public void removeTaskListener(OnTaskListener listener) {
        mTaskListeners.remove(listener);
    }

    public void removeTaskFinishListener(OnTaskFinishListener listener) {
        mTaskFinishListeners.remove(listener);
    }

    protected void onTaskFinished(Object sender, TaskFinishedEvent event) {
        if (!mReturn) {
            mReturn = true;
            for (WeakReference<OnTaskListener> listener : mTaskListeners) {
                if (listener.get() != null) {
                    listener.get().onTaskFinished(sender, event);
                }
            }
            for (WeakReference<OnTaskFinishListener> listener : mTaskFinishListeners) {
                if (listener.get() != null) {
                    listener.get().onTaskFinished(sender, event);
                }
            }
        }
    }

    protected void onTaskFailed(Object sender, TaskFailedEvent event) {
        if (!mReturn) {
            mReturn = true;
            for (WeakReference<OnTaskListener> listener : mTaskListeners) {
                if (listener.get() != null) {
                    listener.get().onTaskFailed(sender, event);
                }
            }
        }
    }

    protected void onTaskTimeout(Object sender, TaskTimeoutEvent event) {
        if (!mReturn) {
            mReturn = true;
            for (WeakReference<OnTaskListener> listener : mTaskListeners) {
                if (listener.get() != null) {
                    listener.get().onTimeout(sender, event);
                }
            }
        }
    }

    protected void onTaskCancel(Object sender, TaskCancelEvent event) {
        if (!mReturn) {
            mReturn = true;
            for (WeakReference<OnTaskListener> listener : mTaskListeners) {
                if (listener.get() != null) {
                    listener.get().onTaskCancel(sender, event);
                }
            }
        }
    }

    public interface OnTaskListener {
        void onTaskFinished(Object sender, TaskFinishedEvent event);

        void onTaskFailed(Object sender, TaskFailedEvent event);

        void onTimeout(Object sender, TaskTimeoutEvent event);

        void onTaskCancel(Object sender, TaskCancelEvent event);
    }

    public interface OnTaskFinishListener {
        void onTaskFinished(Object sender, TaskFinishedEvent event);
    }
}
