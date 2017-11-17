package cn.patterncat.job.event.store;

import org.elasticsearch.client.Client;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.NodeClientFactoryBean;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;

/**
 * Created by patterncat on 2017-11-17.
 */
@Configuration
@ConditionalOnProperty(
        value = "jesque.store.elasticsearch.enabled",
        havingValue = "true",
        matchIfMissing = false
)
@ComponentScan("cn.patterncat.job.event.store")
@ConditionalOnClass({ Client.class, TransportClientFactoryBean.class,
        NodeClientFactoryBean.class })
@AutoConfigureAfter(ElasticsearchAutoConfiguration.class)
public class EsEventStoreAutoConfiguration {
}
