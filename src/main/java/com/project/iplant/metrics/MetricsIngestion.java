package com.project.iplant.metrics;

import com.project.iplant.metrics.service.InfluxDBService;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;

public class MetricsIngestion {

    @Autowired
    InfluxDBService influxDBService;

    public void ingest(Point point) {
        influxDBService.write(point);
    }
}
