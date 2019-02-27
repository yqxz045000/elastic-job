package com.caiyi.sport.step.task;

import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

//stockJob.cron = 0/5 * * * * ?
//stockJob.shardingTotalCount = 4
//stockJob.shardingItemParameters = 0=0,1=1,2=0,3=1

//@ElasticSimpleJob(cron = "0/5 * * * * ?", jobName = "SimpleJobDemo", shardingTotalCount = 4, jobParameter = "测试参数-jobParameter", shardingItemParameters = "0=0,1=1,2=0,3=1")
@Component
public class SimpleJobDemo implements SimpleJob {

	@Override
	public void execute(ShardingContext shardingContext) {
		System.out.println(
				String.format("------Thread ID: %s, 任務總片數: %s, " + "当前分片項: %s.当前參數: %s," + "当前任務名稱: %s.当前任務參數: %s",
						Thread.currentThread().getId(), shardingContext.getShardingTotalCount(),
						shardingContext.getShardingItem(), shardingContext.getShardingParameter(),
						shardingContext.getJobName(), shardingContext.getJobParameter()));
	}
}
