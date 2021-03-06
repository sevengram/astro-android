package com.mydeepsky.android.task;

import com.mydeepsky.android.util.StringUtil;

public class TaskTimeoutEvent {
    private String message;

    private TaskContext context;

    public TaskTimeoutEvent(String message) {
        this(message, null);
    }

    public TaskTimeoutEvent(String message, TaskContext context) {
        this.message = message;
        if (context != null) {
            this.context = context;
        } else {
            this.context = new TaskContext();
        }
    }

    public String getMessage() {
        return StringUtil.safeString(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TaskContext getContext() {
        return context;
    }

    public void setContext(TaskContext context) {
        this.context = context;
    }
}
