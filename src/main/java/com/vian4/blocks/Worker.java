package com.voidcitymc.blocks;

public class Worker {
    public static void runAsyncTask(Runnable task) {
        new Thread(task).start();
    }
}
