package com.hotdms.proxy.init;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hotdms.proxy.bean.Task;
import com.hotdms.proxy.bean.TaskLog;
import com.hotdms.proxy.service.TaskService;
import com.hotdms.proxy.web.WebUtils;
 
@Component
@EnableScheduling
public class TaskSchedulingConfigurer implements SchedulingConfigurer, ApplicationContextAware {
	
	private static ApplicationContext taskApplicationContext;
	
	private static Logger logger = LoggerFactory.getLogger(TaskSchedulingConfigurer.class);
	
	private volatile ScheduledTaskRegistrar registrar;  
	
	private final ConcurrentHashMap<Long, ScheduledFuture<?>> scheduledFutures = new ConcurrentHashMap<>();
	 
	@Autowired
	private TaskService taskService;    
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		taskApplicationContext = applicationContext;
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar registrar) {
		registrar.setScheduler(Executors.newScheduledThreadPool(10));
	    this.registrar = registrar;
	}
	
	public void start(List<Task> taskList) {
		for(Task task : taskList) {
			start(task);
		}
	}
	
	public void modify(Task task) {
		 ScheduledFuture<?> scheduledFuture = scheduledFutures.remove(task.getId());
		 scheduledFuture.cancel(true); 
		 start(task);
	}
	
	public void remove(Task task) {
		 ScheduledFuture<?> scheduledFuture = scheduledFutures.remove(task.getId());
		 scheduledFuture.cancel(true);
	}
	
	public void start(Task task) {
		if(Task.STATUS_OK.equals(task.getStatus())) { 
			ScheduledFuture<?> scheduledFuture = registrar.getScheduler().schedule(getRunnable(task), getTrigger(task));
	        scheduledFutures.put(task.getId(),scheduledFuture);
	        logger.info("启动定时任务: " + task.getName() ); 
		}
    }
	
	public void startAll() {
		 start(taskService.findAll());
	}
	
	public void stopAll() {
		Enumeration<Long> enums = scheduledFutures.keys();
		while(enums.hasMoreElements()) {
			 ScheduledFuture<?> scheduledFuture = scheduledFutures.remove(enums.nextElement());
			 scheduledFuture.cancel(true);
		}
	}
	 
	public static Runnable getRunnable(Task task) {
        return new Runnable() {
            @Override
            public void run() { 
            	Date startTime = new Date();
            	JSONObject result = null;
                try {
                    Object obj = taskApplicationContext.getBean(Class.forName(task.getBeanName()));
                    Method method = obj.getClass().getMethod("run", new Class[] {Task.class });
                    result = (JSONObject) method.invoke(obj, task); 
                }  catch (Throwable e) {
                	e.printStackTrace();
                    logger.error(e.getMessage());
                    result = new JSONObject();
                    result.put("status",WebUtils.STATUS_ERROR);
                    result.put("message", e.getMessage());
                }
                if( task.isNeedLog() && result.getBooleanValue("needLog")) {
                	try {
                		taskApplicationContext.getBean(TaskService.class).insert(TaskLog.create(task.getId(), startTime, result));
                	} catch (Throwable e) {
                         logger.error(e.getMessage());
                	}
                }
            }
        };
    }
	
	private static Trigger getTrigger(Task task) {
        return new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) { 
                CronTrigger trigger = new CronTrigger(task.getCronExp());
                Date nextExec = trigger.nextExecutionTime(triggerContext);
                return nextExec;
            }
        };

    }

	

}
