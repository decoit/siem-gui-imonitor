/* 
 * Copyright (C) 2015 DECOIT GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.decoit.siemgui.ext.icinga.dbconnector;

import java.util.Properties;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;


/**
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Configuration
@ComponentScan(basePackages = { "de.decoit.siemgui.ext.icinga.dbconnector.services" })
@EnableJpaRepositories(basePackages = { "de.decoit.siemgui.ext.icinga.dbconnector.repositories" },
					   entityManagerFactoryRef = "icingaEntityManagerFactory",
					   transactionManagerRef = "icingaTransactionManager")
@PropertySource({ "classpath:icinga-db-connector.properties" })
public class IcingaConnectorConfig {
	private final Logger LOG = LoggerFactory.getLogger(IcingaConnectorConfig.class.getName());

	@Resource
	private Environment env;


	@Bean
	@Qualifier("icingaDataSource")
	public DataSource icingaDataSource() {
		BasicDataSource dataSource = new BasicDataSource();

		dataSource.setDriverClassName(env.getProperty("icinga.jdbc.driver"));
		dataSource.setUrl(env.getProperty("icinga.jdbc.url"));

		if(env.getProperty("icinga.jdbc.user") != null && env.getProperty("icinga.jdbc.user").length() > 0) {
			dataSource.setUsername(env.getProperty("icinga.jdbc.user"));
		}
		if(env.getProperty("icinga.jdbc.pass") != null && env.getProperty("icinga.jdbc.pass").length() > 0) {
			dataSource.setPassword(env.getProperty("icinga.jdbc.pass"));
		}

		return dataSource;
	}


	@Bean
	@Qualifier("icingaEntityManagerFactory")
	public EntityManagerFactory icingaEntityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

		vendorAdapter.setDatabase(determineDatabase());
		vendorAdapter.setGenerateDdl(Boolean.parseBoolean(env.getProperty("icinga.vendoradapter.generateddl")));
		vendorAdapter.setShowSql(Boolean.parseBoolean(env.getProperty("icinga.vendoradapter.showsql")));

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setPersistenceUnitName("icingaPU");
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("de.decoit.siemgui.ext.icinga.dbconnector.entities");
		factory.setDataSource(icingaDataSource());
		factory.setJpaProperties(hibernateProperties());
		factory.afterPropertiesSet();

		return factory.getObject();
	}


	@Bean
	@Qualifier("icingaTransactionManager")
	public PlatformTransactionManager icingaTransactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(icingaEntityManagerFactory());

		return txManager;
	}


	private Database determineDatabase() {
		switch(env.getProperty("icinga.vendoradapter.database")) {
			case "mysql":
				LOG.debug("Using Database.MYSQL");
				return Database.MYSQL;
			case "postgresql":
				LOG.debug("Using Database.POSTGRESQL");
				return Database.POSTGRESQL;
			default:
				LOG.warn("Using Database.DEFAULT, please set 'icinga.vendoradapter.database' property according to your DBMS. Allowed values: 'mysql', 'postgresql'");
				return Database.DEFAULT;
		}
	}


	private Properties hibernateProperties() {
		Properties prop = new Properties();

		prop.setProperty("hibernate.globally_quoted_identifiers", "false");

		return prop;
	}
}
