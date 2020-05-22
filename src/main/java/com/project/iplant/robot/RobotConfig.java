package com.project.iplant.robot;

import java.io.*;
import java.util.Properties;

public class RobotConfig {
    Properties prop = new Properties();
    final InputStream inputPropFile;
    final OutputStream outputPropFile;

    public RobotConfig(String propertiesName) throws IOException, FileNotFoundException {
        inputPropFile = new FileInputStream("src/main/resources/" + propertiesName);
        outputPropFile = new FileOutputStream("src/main/resources/" + propertiesName);
    }

    public RobotConfig(File file) throws IOException {
        inputPropFile = new FileInputStream(file);
        outputPropFile = new FileOutputStream(file);
        prop.load(inputPropFile);
    }

    public void setRobotProperties(Robot robot) throws IOException {
        if(robot.getAddress() != null)
            prop.setProperty("address", robot.getAddress());
        prop.setProperty("mode",robot.getMode());
        prop.setProperty("lastChecked", robot.getLastChecked());
        prop.store(outputPropFile, null);
    }

    public Robot getRobotProperties() throws IOException {
        prop.load(inputPropFile);
        Robot robot = new Robot();
        robot.setAddress(prop.getProperty("address"));
        robot.setLastChecked(prop.getProperty("lastChecked"));
        robot.setMode(prop.getProperty("mode"));
        return robot;
    }
}
