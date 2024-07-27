package com.spring.rest.sample.unit;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.rest.sample.model.Employee;
import com.spring.rest.sample.repository.EmployeeRepository;
import com.spring.rest.sample.service.EmployeeService;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    @MockBean
    EmployeeRepository employeeRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testFindAllEmployees_when_shouldReturn2xx() throws Exception {
        //given
        List<Employee> givenEmployeeList = new ArrayList<>() {{
            add(Employee.builder().id(1).firstName("ahmed").lastName("mohammed").build());
        }};

        //when
        Mockito.when(employeeService.findAllEmployees()).thenReturn(givenEmployeeList);

        RequestBuilder request = MockMvcRequestBuilders.get("/employees")
                                                        .accept(MediaType.APPLICATION_JSON_VALUE)
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE);

        //then
        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<Employee> actualEmployeeList = new ObjectMapper().readValue(content, new TypeReference<List<Employee>>() {});

        //then
        Mockito.verify(employeeService, Mockito.atMostOnce()).findAllEmployees();
        Assertions.assertThat(actualEmployeeList.get(0).getId()).isEqualTo(givenEmployeeList.get(0).getId());
    }

    @Test
    void testPostUser_whenProvideValidEmployee_shouldPostedAndReturn201() throws Exception {
        //given
        Employee givenEmployee = Employee.builder().id(1).firstName("ahmed").lastName("mohammed").build();

        //when
        ArgumentCaptor<Employee> employeeValueCapture = ArgumentCaptor.forClass(Employee.class);
        Mockito.when(employeeService.saveEmployee(employeeValueCapture.capture())).thenReturn(givenEmployee);

        RequestBuilder request = MockMvcRequestBuilders.post("/employees")
                                                       .accept(MediaType.APPLICATION_JSON_VALUE)
                                                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                       .content(objectMapper.writeValueAsString(givenEmployee));

        MvcResult result = mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
        String content = result.getResponse().getContentAsString();
        Employee response = new ObjectMapper().readValue(content, new TypeReference<Employee>() {});

        //then
        Mockito.verify(employeeRepository, Mockito.atMostOnce()).save(employeeValueCapture.capture());
        MatcherAssert.assertThat(employeeValueCapture.getValue(), CoreMatchers.is(givenEmployee));
        MatcherAssert.assertThat(response, CoreMatchers.is(givenEmployee));
    }

    @Test
    void testDeleteUser_whenProvideValidEmployeeId_shouldDeleteAndReturn200() throws Exception {
        //given
        Integer givenEmployeeId = 1;

        //when
        ArgumentCaptor<Integer> employeeIdValueCapture = ArgumentCaptor.forClass(Integer.class);;
        Mockito.doNothing().when(employeeService).deleteEmployeeById(employeeIdValueCapture.capture());

        RequestBuilder request = MockMvcRequestBuilders.delete("/employees/{id}", givenEmployeeId)
                                                        .accept(MediaType.APPLICATION_JSON_VALUE)
                                                        .contentType(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        //then
        Mockito.verify(employeeRepository, Mockito.atMostOnce()).deleteById(employeeIdValueCapture.capture());
        MatcherAssert.assertThat(employeeIdValueCapture.getValue(), CoreMatchers.is(givenEmployeeId));
    }
}