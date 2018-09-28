package com.campsite.demo;

import com.dao.BookingDAO;
import com.daoimpl.BookingDAOImpl;
import com.domain.Booking;
import com.service.BookingService;
import com.serviceimpl.BookingServiceImpl;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.controller")

@SpringBootApplication
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
		dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/sys?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=PST");
		dataSource.setUsername("root");
		dataSource.setPassword("12345678");
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
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        return hibernateProperties;
    }



	public static void main(String[] args) {
		SpringApplication.run(BookingApplication.class, args);
	}
}

