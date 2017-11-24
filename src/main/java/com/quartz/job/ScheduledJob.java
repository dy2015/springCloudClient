package com.quartz.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.constant.Constants;
import com.http.NetUtil;

public class ScheduledJob implements Job {
	private SimpleDateFormat dateFormat() {
		return new SimpleDateFormat("HH:mm:ss");
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("AAAA: The time is now " + dateFormat().format(new Date()));
		NetUtil.requestPost(Constants.URL, "");
	}
}
