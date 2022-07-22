package mackatozis.cassandra.demo.config;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.shaded.guava.common.collect.Lists;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Value("${cassandra.contact-points:}")
    private String cassandraContactPoints;

    @Value("${cassandra.port:}")
    private int cassandraPort;

    @Value("${cassandra.replication.factor:}")
    private int replicationFactor;

    @Value("${cassandra.local-datacenter}")
    private String localDatacenter;

    @Value("${cassandra.migration.executionProfileName:}")
    private String migrationProfile;

    @Value("${application.cassandra.migration.request-timeout-seconds:10}")
    private int migrationRequestTimeout;

    @Value("${application.cassandra.migration.connection-init-query-timeout-seconds:}")
    private int migrationConnectionInitQueryTimeout;

    @Value("${application.cassandra.migration.control-connection-timeout-seconds:}")
    private int migrationControlConnectionTimeout;

    @Value("${cassandra.migration.keyspace-name:}")
    private String keyspaceName;

    @Override
    public int getPort() {
        return cassandraPort;
    }

    @Override
    public String getContactPoints() {
        return cassandraContactPoints;
    }

    @Override
    public String getKeyspaceName() {
        return keyspaceName;
    }

    @Override
    public List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(getKeyspaceName());
        specification.ifNotExists();
        specification.withSimpleReplication(replicationFactor);

        return Lists.newArrayList(specification);
    }

    @Override
    public String getLocalDataCenter() {
        // https://docs.datastax.com/en/developer/java-driver/4.4/manual/core/load_balancing/
        return localDatacenter;
    }

    private DriverConfigLoader getDriverConfigLoader() {
        return DriverConfigLoader.programmaticBuilder()
                // create a profile that will be used for the migrations
                .startProfile(migrationProfile)
                // basic.request.timeout
                .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(migrationRequestTimeout))
                // advanced.connection.init-query-timeout
                .withDuration(DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT,
                        Duration.ofSeconds(migrationConnectionInitQueryTimeout))
                // advanced.control-connection.timeout
                .withDuration(DefaultDriverOption.CONTROL_CONNECTION_TIMEOUT, Duration.ofSeconds(migrationControlConnectionTimeout))
                .endProfile()
                .build();
    }

    // Spring Beans

    // Cassandra Migration plugin expects a session bean with a particular name:
    // see CassandraMigrationAutoConfiguration::CQL_SESSION_BEAN_NAME
    @Bean(name = "cassandraMigrationCqlSession")
    public CqlSessionFactoryBean cassandraMigrationCqlSession() {
        CqlSessionFactoryBean bean = super.cassandraSession();
        // the Spring Boot way of configuring the Cassandra Driver is with a DriverConfigLoaderBuilderCustomizer bean
        // @Bean
        // public DriverConfigLoaderBuilderCustomizer driverConfigLoaderBuilderCustomizer() {}
        //
        // however it is not picked up by the migration session, thus we need to replace it with a new one
        DriverConfigLoader loader = getDriverConfigLoader();
        bean.setSessionBuilderConfigurer(builder -> builder.withConfigLoader(loader));
        return bean;
    }

    @Bean
    @Primary
    public CqlSessionFactoryBean cassandraSession() {
        //This is the Primary cassandra session bean used by Spring
        return super.cassandraSession();
    }

}
