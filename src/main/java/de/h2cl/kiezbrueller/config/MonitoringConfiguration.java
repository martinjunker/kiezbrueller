package de.h2cl.kiezbrueller.config;

import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;

/**
 * Configuration for Metrics
 * 
 * Created by martin.junker on 05.04.16.
 */
@Configuration
@EnableMetrics
public class MonitoringConfiguration extends MetricsConfigurerAdapter {

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        registerReporter(JmxReporter.forRegistry(metricRegistry).build()).start();
    }
}
