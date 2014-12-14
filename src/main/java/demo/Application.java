package demo;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application implements InitializingBean {

    private static final MetricRegistry metrics = new MetricRegistry();

    //    @Resource(name = "redisTemplate")
//    private ListOperations<String, String> listOps;
    @Autowired
//    @Resource(name = "redisTemplate")
    private RedisTemplate template;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(SECONDS)
                .convertDurationsTo(MILLISECONDS)
                .build();
        reporter.start(1, SECONDS);
        while (true) {
            metrics
                    .meter("main")
                    .mark();
            template
                    .boundSetOps("main")
                    .add(randomAlphanumeric(10));
        }
    }
}
