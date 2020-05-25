package com.project.iplant.metrics.service;

import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Service;

@Service
public class InfluxDBServiceImpl implements InfluxDBService, InitializingBean {

    @Autowired
    InfluxDBTemplate<Point> influxDBTemplate;


    public void write(Point point) {
        influxDBTemplate.write(point);
    }

    public QueryResult query(String queryString) {
        Query query = new Query(queryString, influxDBTemplate.getDatabase());
        return influxDBTemplate.query(query);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        influxDBTemplate.createDatabase();
    }

    @Override
    public boolean isInfluxConnected() {
        Pong pong = influxDBTemplate.ping();
        return true;
    }

    @Override
    public void setDatabase(String name) {
        influxDBTemplate.getConnectionFactory().getProperties().setDatabase(name);
    }
}