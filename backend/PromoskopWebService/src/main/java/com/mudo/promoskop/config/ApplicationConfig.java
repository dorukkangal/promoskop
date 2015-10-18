package com.mudo.promoskop.config;

import java.beans.PropertyVetoException;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mudo.promoskop.service.BranchService;
import com.mudo.promoskop.service.ProductBranchService;
import com.mudo.promoskop.service.ProductService;
import com.mudo.promoskop.service.impl.BranchServiceImpl;
import com.mudo.promoskop.service.impl.ProductBranchServiceImpl;
import com.mudo.promoskop.service.impl.ProductServiceImpl;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.mudo.promoskop" })
@PropertySource(value = "classpath:/database.properties")
public class ApplicationConfig {

	@Resource
	private Environment environment;

	@Bean
	public JpaTransactionManager transactionManager() throws PropertyVetoException {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws PropertyVetoException {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource());
		entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter());
		entityManagerFactory.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
		entityManagerFactory.setJpaProperties(jpaProperties());
		entityManagerFactory.setPackagesToScan("com.mudo.promoskop");
		entityManagerFactory.setPersistenceProvider(persistenceProvider());
		return entityManagerFactory;
	}

	@Bean
	public ComboPooledDataSource dataSource() throws PropertyVetoException {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(environment.getRequiredProperty("jdbc.driverClass"));
		dataSource.setJdbcUrl(environment.getRequiredProperty("jdbc.url"));
		dataSource.setUser(environment.getRequiredProperty("jdbc.username"));
		dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));

		dataSource.setMinPoolSize(1);
		dataSource.setMaxPoolSize(15);
		dataSource.setAcquireIncrement(1);
		dataSource.setMaxStatements(0);
		dataSource.setMaxIdleTime(0);
		dataSource.setTestConnectionOnCheckout(true);
		dataSource.setPreferredTestQuery("select 1;");
		dataSource.setIdleConnectionTestPeriod(300);

		return dataSource;
	}

	@Bean
	public HibernateJpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setShowSql(true);
		jpaVendorAdapter.setGenerateDdl(false);
		return jpaVendorAdapter;
	}

	@Bean
	public HibernatePersistenceProvider persistenceProvider() {
		HibernatePersistenceProvider persistenceProvider = new HibernatePersistenceProvider();
		return persistenceProvider;
	}

	@Bean
	public MBeanExporter mbeanExporter(EntityManagerFactory entityManagerFactory) throws MalformedObjectNameException {
		MBeanExporter mBeanExporter = new MBeanExporter();
		mBeanExporter.setServer(getServer());

		SessionFactory sf = ((HibernateEntityManagerFactory) entityManagerFactory).getSessionFactory();
		ObjectName on = new ObjectName("Hibernate:application=Statistics");
		if (!mBeanExporter.getServer().isRegistered(on))
			mBeanExporter.registerManagedResource(sf.getStatistics(), on);
		return mBeanExporter;
	}

	private MBeanServer getServer() {
		MBeanServer mbserver = null;
		List<MBeanServer> mbservers = MBeanServerFactory.findMBeanServer(null);
		if (mbservers.size() > 0) {
			mbserver = (MBeanServer) mbservers.get(0);
		}
		if (mbserver == null) {
			mbserver = MBeanServerFactory.createMBeanServer();
		}
		return mbserver;
	}

	@Bean
	public ApplicationBeanAware applicationBeanAware() {
		return new ApplicationBeanAware();
	}

	@Bean
	public ProductService productService() {
		return new ProductServiceImpl();
	}

	@Bean
	public BranchService branchService() {
		return new BranchServiceImpl();
	}

	@Bean
	public ProductBranchService productBranchService() {
		return new ProductBranchServiceImpl();
	}

	private Properties jpaProperties() {
		String[] jpaProperties = new String[] { "hibernate.dialect", "hibernate.hbm2ddl.auto", "hibernate.ejb.naming_strategy", "hibernate.connection.charSet",
				"hibernate.connection.useUnicode", "hibernate.connection.characterEncoding", "hibernate.show_sql", "hibernate.cache.use_second_level_cache",
				"hibernate.cache.use_query_cache", "hibernate.cache.region.factory_class", "hibernate.cache.use_structured_entries", "hibernate.generate_statistics" };

		Properties properties = new Properties();
		for (String property : jpaProperties)
			properties.setProperty(property, environment.getRequiredProperty(property));
		return properties;
	}
}