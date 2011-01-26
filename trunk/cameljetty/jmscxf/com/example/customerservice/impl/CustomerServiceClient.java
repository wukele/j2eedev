/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.customerservice.impl;


import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.example.customerservice.Customer;
import com.example.customerservice.CustomerService;

public class CustomerServiceClient {

	public static void main(String args[]) throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				new String[] { "client-applicationContext.xml" });
		CustomerService customerService = (CustomerService) applicationContext
				.getBean("CustomerService");
		
		// First we test for the case where the exception should occur
		List<Customer> customers = null;
		try {
			customers = customerService.getCustomersByName("none");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("This is an expected Exception");
		}
		
		// Now for the positive case were we expect to get back a list of 1 customer
		// If setting higher than 1 you can do a small load test
		for (int c=0;c<1;c++) {
			// Checking for german "Umlaut 锟� here
			customers = customerService.getCustomersByName("M锟絣ler");
			System.out.println("Succesfully called service");
		}
		System.out.println("finished");
		System.exit(0);
	}
}