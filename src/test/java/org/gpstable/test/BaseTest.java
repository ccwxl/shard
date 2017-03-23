package org.gpstable.test;

import org.gpstable.config.AppConfig;
import org.gpstable.config.DataBaseConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DataBaseConfig.class, AppConfig.class})
public class BaseTest {
	
}
