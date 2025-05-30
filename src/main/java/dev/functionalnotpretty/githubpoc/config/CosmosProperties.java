package dev.functionalnotpretty.githubpoc.config;

// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "spring.data.cosmos")
public class CosmosProperties {

    private String uri;
    private String key;
    private String secondaryKey;
    private String databaseName;
    private boolean queryMetricsEnabled;

}