package com.caiyi.sport.step.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.caiyi.sport.step.listener.MyElasticJobListener;
import com.caiyi.sport.step.task.SimpleJobDemo;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

@Configuration
public class ElasticJobConfig {

	@Autowired
	private ZookeeperRegistryCenter regCenter;

	/**
	 * 配置任务监听器
	 * 
	 * @return
	 */
	@Bean
	public ElasticJobListener elasticJobListener() {
		return new MyElasticJobListener();
	}

	/**
	 * 配置任务详细信息
	 * 
	 * @param jobClass
	 * @param cron
	 * @param shardingTotalCount
	 * @param shardingItemParameters
	 * @return
	 */
	private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass, final String cron,
			final int shardingTotalCount, final String shardingItemParameters) {
		return LiteJobConfiguration
				.newBuilder(
						new SimpleJobConfiguration(
								JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount)
										.shardingItemParameters(shardingItemParameters).build(),
								jobClass.getCanonicalName()))
				.overwrite(true).build();
	}

	/**
	 * 自定义注解，然后将注解的类全部走一遍注册bean。
	 * 
	 * @param simpleJob
	 * @param cron
	 * @param shardingTotalCount
	 * @param shardingItemParameters
	 * @return
	 */
	@Bean(initMethod = "init")
	public JobScheduler simpleJobScheduler(final SimpleJobDemo simpleJob, @Value("${stockJob.cron}") final String cron,
			@Value("${stockJob.shardingTotalCount}") final int shardingTotalCount,
			@Value("${stockJob.shardingItemParameters}") final String shardingItemParameters) {
		MyElasticJobListener elasticJobListener = new MyElasticJobListener();
		return new SpringJobScheduler(simpleJob, regCenter,
				getLiteJobConfiguration(simpleJob.getClass(), "0/5 * * * * ?", 4, "0=0,1=1,2=0,3=1"),
				elasticJobListener);
	}
}
