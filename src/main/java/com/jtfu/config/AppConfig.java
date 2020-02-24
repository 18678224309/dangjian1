package com.jtfu.config;

import org.springframework.context.annotation.Import;

@Import({MybatisConfig.class, FreeMakerConfig.class})
public class AppConfig {
}
