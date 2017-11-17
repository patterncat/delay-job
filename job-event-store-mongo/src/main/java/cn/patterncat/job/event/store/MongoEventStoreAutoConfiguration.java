package cn.patterncat.job.event.store;

import cn.patterncat.job.event.store.component.MongoStoreEventListener;
import cn.patterncat.job.event.store.dao.JobLogDao;
import com.mongodb.MongoClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by patterncat on 2017-11-17.
 */
@Configuration
@ConditionalOnProperty(
        value = "jesque.store.mongo.enabled",
        havingValue = "true",
        matchIfMissing = false
)
@ComponentScan("cn.patterncat.job.event.store")
@ConditionalOnClass(MongoClient.class)
@AutoConfigureAfter(MongoAutoConfiguration.class)
public class MongoEventStoreAutoConfiguration {

    @Bean
    public MongoStoreEventListener mongoStoreEventListener(JobLogDao jobLogDao){
        return new MongoStoreEventListener(jobLogDao);
    }
}
