package com.armineasy.activitymaster.activity;

import com.armineasy.activitymaster.activitymaster.ActivityMasterService;
import com.armineasy.activitymaster.activitymaster.DefaultEnterprise;
import com.armineasy.activitymaster.activitymaster.services.IActivityMasterProgressMonitor;
import com.armineasy.activitymaster.activity.configs.DefaultTestConfig;
import com.jwebmp.guicedinjection.GuiceContext;
import com.jwebmp.logger.LogFactory;
import com.jwebmp.logger.logging.LogColourFormatter;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@ExtendWith(DefaultTestConfig.class)
@Log
public class TestEnterpriseCreation
{
	@Test
	public void testEnterpriseCreation()
	{
		IActivityMasterProgressMonitor soutMonitor = new IActivityMasterProgressMonitor()
		{
			int totalTasks = 0;
			int currentTasks = 0;

			@Override
			public void progressUpdate(String source, String message)
			{
				log.info(source + " - " + message + " || " + currentTasks + "/" + totalTasks);
			}

			@Override
			public Integer getCurrentTask()
			{
				return currentTasks;
			}

			@Override
			public void setCurrentTask(Integer i)
			{
				currentTasks = i;
			}

			@Override
			public Integer getTotalTasks()
			{
				return totalTasks;
			}

			@Override
			public void setTotalTasks(Integer i)
			{
				totalTasks = i;
			}
		};
		LogColourFormatter.setRenderBlack(false);

		FileHandler fh;

		try {
			// This block configure the logger with handler and formatter
			fh = new FileHandler("C:/temp/EnterpriseCreateLogFile.log",false);
			fh.setLevel(Level.FINE);
			Logger.getLogger("").addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

			// the following statement is used to log any messages
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		GuiceContext.get(ActivityMasterService.class)
		            .startNewEnterprise(DefaultEnterprise.TestEnterprise, "admin", "admin", soutMonitor);
	}
}
