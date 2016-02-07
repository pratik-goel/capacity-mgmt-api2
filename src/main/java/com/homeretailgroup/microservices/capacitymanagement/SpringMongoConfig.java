package com.homeretailgroup.microservices.capacitymanagement;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * 
 * @author pratik.goel
 *
 */

@Configuration
@EnableMongoRepositories
@ComponentScan(basePackageClasses = {Application.class})

public class SpringMongoConfig extends AbstractMongoConfiguration {

    @Override
    public String getDatabaseName(){
        return "capacity-mgmt";
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient("capacity-mgmt-service-mongo");
    }

}
