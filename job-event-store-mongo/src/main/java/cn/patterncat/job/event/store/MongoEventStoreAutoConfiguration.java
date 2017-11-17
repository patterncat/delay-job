package cn.patterncat.job.event.store;

import com.mongodb.MongoClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by patterncat on 2017-11-17.
 */
@Configuration
@ConditionalOnProperty(
        value = "jesque.mongo.enabled",
        havingValue = "true",
        matchIfMissing = false
)
@ComponentScan("cn.patterncat.job.event.store")
@ConditionalOnClass(MongoClient.class)
@AutoConfigureAfter(MongoAutoConfiguration.class)
public class MongoEventStoreAutoConfiguration {
}
