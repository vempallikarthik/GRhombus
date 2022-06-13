package com.webapp.tests;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class TestConfiguration extends AbstractModule {

    @Override
    protected void configure() {
    Properties properties = new Properties();
    try {
        properties.load(new FileReader("Test.properties"));
        Names.bindProperties(binder(), properties);
    } catch (FileNotFoundException e) {
        System.out.println("The configuration file Test.properties can not be found");
    } catch (IOException e) {
        System.out.println("I/O Exception during loading configuration");
    }

    }
}
