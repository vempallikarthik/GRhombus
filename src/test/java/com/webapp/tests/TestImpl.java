package com.webapp.tests;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class TestImpl {
    public static String name;
    public static String email;
    public static String pwd;
  
        @Inject
        public TestImpl(@Named("Display_Name") String dname,
        		@Named("Email_Address") String emailAdd,
        		@Named("Password") String password) {
  
        this.name = dname;
        this.email = emailAdd;
        this.pwd= password;


        }
     }