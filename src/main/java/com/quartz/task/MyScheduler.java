package com.quartz.task;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.quartz.job.ScheduledJob;
import com.quartz.job.ScheduledJob2;

@Component
public class MyScheduler {
	@Autowired
    SchedulerFactoryBean schedulerFactoryBean;
    public void scheduleJobs() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        startJob(scheduler,ScheduledJob.class,"job1", "group1","trigger1","0/3 * * * * ?"); 
        startJob(scheduler,ScheduledJob2.class,"job2", "group2","trigger2","0/3 * * * * ?"); 
        startJob(scheduler,ScheduledJob2.class,"job3", "group3","trigger3","0/5 * * * * ?"); 
    }

	private void startJob(Scheduler scheduler,Class<? extends Job> clazz,String jobName,String jobGroup,String triggerName,String cronExpression) throws SchedulerException{
        JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobName, jobGroup).build(); 
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression); 
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerName, jobGroup) .withSchedule(scheduleBuilder).build(); 
        scheduler.scheduleJob(jobDetail,cronTrigger); 
    } 
}
