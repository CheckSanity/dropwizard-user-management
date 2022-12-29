package com.usermanagement

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration

class AppConfiguration(
    @JsonProperty("database") val dataSource: DataSourceFactory,
    @JsonProperty("swagger") val swaggerConfig: SwaggerBundleConfiguration
) : Configuration()
