package com.project.iplant.metrics.service;

import org.influxdb.dto.Point;
import org.influxdb.dto.QueryResult;

/**
 * Created by jls on 02/03/17.
 */
public interface InfluxDBService {

    void write(Point point);

    QueryResult query(String queryString);

    boolean isInfluxConnected();

    void setDatabase(String name);
}
