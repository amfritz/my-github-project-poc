package dev.functionalnotpretty.githubpoc.config;
// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.DirectConnectionConfig;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.core.ResponseDiagnostics;
import com.azure.spring.data.cosmos.core.ResponseDiagnosticsProcessor;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

@Configuration
@EnableConfigurationProperties(CosmosProperties.class)
@EnableCosmosRepositories
public class CosmosDbConfiguration extends AbstractCosmosConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(CosmosDbConfiguration.class);

    private final CosmosProperties properties;

    public CosmosDbConfiguration(CosmosProperties properties) {
        this.properties = properties;
    }

    @Bean
    public CosmosClientBuilder cosmosClientBuilder() {
        DirectConnectionConfig directConnectionConfig = DirectConnectionConfig.getDefaultConfig();
        return new CosmosClientBuilder()
                .endpoint(properties.getUri())
                .key(properties.getKey())
                .directMode(directConnectionConfig);
    }

    @Bean
    public CosmosConfig cosmosConfig() {
        return CosmosConfig.builder()
                .responseDiagnosticsProcessor(new ResponseDiagnosticsProcessorImplementation())
                .enableQueryMetrics(properties.isQueryMetricsEnabled())
                .build();
    }

    @Override
    protected String getDatabaseName() {
        return properties.getDatabaseName();
    }

    private static class ResponseDiagnosticsProcessorImplementation implements ResponseDiagnosticsProcessor {

        @Override
        public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {
            if (responseDiagnostics != null) {
                var diag = responseDiagnostics.getCosmosResponseStatistics();
                if (diag != null) {
                    logger.info("Response diagnostics: {}", diag);
                }
            }
//            logger.info("Response Diagnostics {}", .getRequestCharge());
        }
    }
}