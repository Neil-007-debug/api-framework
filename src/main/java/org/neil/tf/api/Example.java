package org.neil.tf.api;

import com.mashape.unirest.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
public class Example {


    @Async
    public Future<String> test1(Long time, int a) throws InterruptedException {
        Thread.sleep(time);
        System.out.println(Thread.currentThread().getName());
        test();
        return new AsyncResult<String>("fff");
    }

    public void test() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
    }
}
