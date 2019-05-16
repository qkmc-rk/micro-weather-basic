package xyz.ruankun.microweatherbasic.config;
//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                           O\  =  /O
//                        ____/`---'\____
//                      .'  \\|     |//  `.
//                     /  \\|||  :  |||//  \
//                    /  _||||| -:- |||||-  \
//                    |   | \\\  -  /// |   |
//                    | \_|  ''\---/''  |   |
//                    \  .-\__  `-`  ___/-. /
//                  ___`. .'  /--.--\  `. . __
//               ."" '<  `.___\_<|>_/___.'  >'"".
//              | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//              \  \ `-.   \_ __\ /__ _/   .-` /  /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//                      Buddha Bless, No Bug !

import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.ruankun.microweatherbasic.job.WeatherDataSyncJob;

@Configuration
public class QuartzConfiguration {

    /**
     * at this configuration,we need explain:
     * 1.jobDetail,we use JobBuilder API to construct a jobDetail bean.
     * we explain what event will be executed by this job
     * 2.Trigger,when the event will be executed
     */

    @Value("${micro-weather-basic.quartz.JOB_SYNC_TIME}")
    private long JOB_SYNC_TIME;

    @Bean
    public JobDetail weatherDataSyncJob_JobDetail(){
        //JobBuilder
        JobBuilder jobBuilder = JobBuilder.newJob(WeatherDataSyncJob.class);
        jobBuilder.withIdentity("weatherDataSyncJob");
        //keep the job exists in the quartz
        jobBuilder.storeDurably();
        return jobBuilder.build();
    }

    @Bean
    public Trigger simpleJobTrigger(){
        //1.simpleScheduleBuilder
        //2.trigger builder
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        simpleScheduleBuilder.withIntervalInSeconds((int)JOB_SYNC_TIME);
        simpleScheduleBuilder.repeatForever();

        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger();
        triggerBuilder.forJob(weatherDataSyncJob_JobDetail());
        triggerBuilder.withIdentity("weatherDataSyncTrigger");
        triggerBuilder.withSchedule(simpleScheduleBuilder);
        return triggerBuilder.build();
    }

}
