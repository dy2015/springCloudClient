package com.quartz.listener;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.quartz.task.MyScheduler;

@Configuration
public class SchedulerListener implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	public MyScheduler myScheduler;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			myScheduler.scheduleJobs();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean(); 
        return schedulerFactoryBean; 
    }
}