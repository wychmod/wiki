package com.wychmod.wiki.job;

import com.wychmod.wiki.service.EbookSnapshotService;
import com.wychmod.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class EbookSnapshotJob {

    private static final Logger LOG = LoggerFactory.getLogger(EbookSnapshotJob.class);
    @Resource
    EbookSnapshotService ebookSnapshotService;

    @Resource
    SnowFlake snowFlake;

    /**
     每 30 秒更新电子书信息
     */
    @Scheduled(cron = "0/50 * * * * ?")
    public void doSnapshot() {
        // 增加日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("生成今日电子书快照开始");
        long start = System.currentTimeMillis();
        ebookSnapshotService.genSnapshot();
        LOG.info("生成今日电子书快照结束，耗时：{}毫秒",System.currentTimeMillis()-start);
    }
}