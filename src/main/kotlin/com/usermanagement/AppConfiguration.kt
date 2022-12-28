package com.usermanagement

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory

class AppConfiguration(@JsonProperty("database") val dataSource: DataSourceFactory) :
    Configuration()