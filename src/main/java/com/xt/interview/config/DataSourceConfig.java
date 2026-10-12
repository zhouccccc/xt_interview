package com.xt.interview.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 数据源智能配置：
 * 本地起了MySQL就直接连MySQL；如果没起（比如新机器直接拉代码运行），
 * 自动切到本地内置H2(MySQL兼容模式)，数据落盘到target/xt_data，保证任何人拿到代码一键就能跑通。
 */
@Configuration
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/xt_interview}")
    private String mysqlUrl;

    @Value("${spring.datasource.username:root}")
    private String mysqlUsername;

    @Value("${spring.datasource.password:root}")
    private String mysqlPassword;

    @Value("${spring.datasource.driver-class-name:com.mysql.cj.jdbc.Driver}")
    private String mysqlDriver;

    @Bean
    @Primary
    public DataSource dataSource() {
        if (isMySqlAvailable()) {
            // 本地3306端口通了，直接用MySQL
            log.info("【数据源】检测到本地 MySQL 服务(localhost:3306)，正在使用 MySQL 数据源: {}", mysqlUrl);
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(mysqlUrl);
            config.setUsername(mysqlUsername);
            config.setPassword(mysqlPassword);
            config.setDriverClassName(mysqlDriver);
            return new HikariDataSource(config);
        } else {
            // 本地没MySQL，无缝降级到本地持久化引擎，省去面试官装库的步骤
            log.warn("【数据源提示】未检测到本地 3306 端口运行的 MySQL 数据库服务。");
            log.warn("【自动降级】已自动启用内置高兼容数据库引擎 (H2 MySQL兼容模式，文件路径: ./target/xt_data)，确保项目零配置开箱即用！");
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:h2:file:./target/xt_data;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;NON_KEYWORDS=USER");
            config.setDriverClassName("org.h2.Driver");
            config.setUsername("sa");
            config.setPassword("");
            return new HikariDataSource(config);
        }
    }

    // 简单快速探测一下本地3306端口通不通，超时400ms，不卡启动流程
    private boolean isMySqlAvailable() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("127.0.0.1", 3306), 400);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
