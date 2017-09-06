
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

public class CronTriggerClass {
	public static void main( String[] args ) throws Exception
    {

    	JobDetail job = new JobDetail();
    	job.setName("JobWeather");
    	job.setJobClass(CurrentDataExtraction.class);

    	CronTrigger trigger = new CronTrigger();
    	trigger.setName("TriggerWeather");
    	trigger.setCronExpression("0 0/59 * * * ?");

    	//schedule it
    	Scheduler scheduler = new StdSchedulerFactory().getScheduler();
    	scheduler.start();
    	scheduler.scheduleJob(job, trigger);

    }
}
