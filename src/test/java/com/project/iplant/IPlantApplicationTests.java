package com.project.iplant;

import com.project.iplant.robot.Robot;
import com.project.iplant.robot.RobotConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
class IPlantApplicationTests {

	@Test
	void testWriteAndRead() throws IOException {
			File testFile = new File("src/main/resources/robot2.properties");
			testFile.createNewFile();
			RobotConfig robotConfig = new RobotConfig(testFile);
			Robot robot = new Robot("tcp://1.1.1.1", "Auto", "1970-01-01");
			robotConfig.setRobotProperties(robot);
			Robot robot2 = robotConfig.getRobotProperties();
			assertEquals("tcp://1.1.1.1", robot2.getAddress());
			assertEquals("Auto", robot2.getMode());
			assertEquals("1970-01-01", robot2.getLastChecked());
			testFile.delete();
	}

}
