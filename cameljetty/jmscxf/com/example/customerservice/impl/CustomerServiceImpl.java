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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.example.customerservice.Customer;
import com.example.customerservice.CustomerService;


public class CustomerServiceImpl implements CustomerService {

	public List<Customer> getCustomersByName(String name) {
		Customer cust = new Customer();
		cust.setName(name);
		cust.getAddress().add("");
		cust.setNumOrders(1);
		cust.setRevenue(10000);
		cust.setTest(new BigDecimal(1.5));
		return Arrays.asList(new Customer[] {cust});
	}
	
}
