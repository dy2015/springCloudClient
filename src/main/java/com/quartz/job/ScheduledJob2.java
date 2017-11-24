package com.quartz.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ScheduledJob2 implements Job {
	private SimpleDateFormat dateFormat() {
		return new SimpleDateFormat("HH:mm:ss");
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("BBB: The time is now " + dateFormat().format(new Date()));
	}
}
