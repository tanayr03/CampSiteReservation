package com.campsite.demo;

import com.dao.BookingDAO;
import com.daoimpl.BookingDAOImpl;
import com.service.BookingService;
import com.serviceimpl.BookingServiceImpl;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.controller")
@SpringBootApplication
@EnableSwagger2
public class BookingApplication {

    @Autowired
    private Environment env;

    @Bean
    public BookingService bookingService() {
        return new BookingServiceImpl();
    }

    @Bean
    public BookingDAO bookingDAO() {
        return new BookingDAOImpl();
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

    @Bean
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://bookingdbinstance.cxvawqwnhbnx.us-west-1.rds.amazonaws.com:3306/bookingdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=PST");
        dataSource.setUsername("dbuser");
        dataSource.setPassword("root1234");
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(getDataSource());
        sessionFactory.setPackagesToScan( "com" );
        sessionFactory.setAnnotatedClasses();
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        return hibernateProperties;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookingApplication.class, args);
    }
}

