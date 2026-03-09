package com.sharko.yura.taskflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class TaskflowApplication {

    private static final Logger log = LoggerFactory.getLogger(TaskflowApplication.class);

	public static void main(String[] args) {

        SpringApplication.run(TaskflowApplication.class, args);

	}

    @EventListener(ApplicationReadyEvent.class)
    public void onStart(){

        log.info("TaskFlow application started");

    }

}
