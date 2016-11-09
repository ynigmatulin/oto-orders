/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package order;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private OrderRepository orderRepository;

	@Before
	public void deleteAllBeforeTests() throws Exception {
		orderRepository.deleteAll();
	}

	@Test
	public void shouldReturnRepositoryIndex() throws Exception {

		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(
				jsonPath("$._links.orders").exists());
	}

	@Test
	public void shouldCreateEntity() throws Exception {

		mockMvc.perform(post("/orders").content(
				"{\"productId\": \"1\", \"amount\":\"5\", \"totalSum\":\"12.5\"}")).andExpect(
						status().isCreated()).andExpect(
								header().string("Location", containsString("orders/")));
	}

	@Test
	public void shouldRetrieveEntity() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/orders").content(
				"{\"productId\": \"1\", \"amount\":\"5\",\"totalSum\":\"12.5\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.productId").value("1")).andExpect(
						jsonPath("$.amount").value("5")).andExpect(
								jsonPath("$.totalSum").value("12.5"));
	}

	@Test
	public void shouldQueryEntity() throws Exception {

		mockMvc.perform(post("/orders").content(
				"{\"productId\": \"1\", \"amount\":\"5\",\"totalSum\":\"12.5\"}")).andExpect(
						status().isCreated());

		mockMvc.perform(
				get("/orders/search/findByProductId?productId={productId}", "1")).andExpect(
						status().isOk()).andExpect(
								jsonPath("$._embedded.orders[0].amount").value(
										"5"));
	}

	@Test
	public void shouldUpdateEntity() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/orders").content(
				"{\"productId\": \"1\", \"amount\":\"5\",\"totalSum\":\"12.5\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(put(location).content(
				"{\"productId\": \"1\", \"amount\":\"5\",\"totalSum\":\"11.5\"}")).andExpect(
						status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
		jsonPath("$.productId").value("1")).andExpect(
				jsonPath("$.amount").value("5")).andExpect(
						jsonPath("$.totalSum").value("11.5"));
	}

	@Test
	public void shouldPartiallyUpdateEntity() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/orders").content(
				"{\"productId\": \"4\", \"amount\":\"5\", \"totalSum\":\"12.5\"}")).andDo(print()).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(patch(location).content("{\"totalSum\": \"10\"}")).andDo(print()).andExpect(
						status().isNoContent());

		MvcResult res = mockMvc.perform(get(location)).andExpect(
				status().isOk()).andExpect(
				jsonPath("$.productId").value("4")).andExpect(
					jsonPath("$.amount").value("5")).andExpect(
						jsonPath("$.totalSum").value("10.0")).andReturn();
		System.out.println(res.getResponse().getContentAsString());

	}

	@Test
	public void shouldDeleteEntity() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/orders").content(
				"{\"productId\": \"1\", \"amount\":\"5\", \"totalSum\":\"12.5\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location)).andExpect(status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isNotFound());
	}
}
