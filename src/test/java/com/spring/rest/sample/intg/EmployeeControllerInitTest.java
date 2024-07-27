package com.spring.rest.sample.intg;

import com.spring.rest.sample.model.Employee;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.assertj.core.api.Assertions;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeControllerInitTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    @Order(1)
    void test_findAllEmployees_returnAllEmployees(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity entityRequest = new HttpEntity<>(headers);

        ResponseEntity<List<Employee>> response = testRestTemplate.exchange("/employees",
                                                                            HttpMethod.GET,
                                                                            entityRequest,
                                                                            new ParameterizedTypeReference<List<Employee>>(){});

        List<Employee> actualEmployees = response.getBody();

        Assertions.assertThat(response.getStatusCode().is2xxSuccessful());
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actualEmployees).isNotEmpty().hasAtLeastOneElementOfType(Employee.class);
    }

    @Test
    @Order(2)
    void test_findById_whenValidEmployeeIdProvided_returnQueriedEmployee(){
        //given
        Employee employee = Employee.builder().id(1).firstName("Ahmed").lastName("Mohammed").build();

        //when
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity entityRequest = new HttpEntity<>(headers);

        Map< String, String > params = new HashMap<>();
        params.put("id", "1");

        ResponseEntity<Employee> response = testRestTemplate.exchange("/employees/{id}",
                                                                      HttpMethod.GET,
                                                                      entityRequest,
                                                                      Employee.class,
                                                                      params);

        Employee actualEmployee = response.getBody();

        //then
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful());
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actualEmployee).usingComparatorForType((o1, o2)-> 0, Double.class)
                .isEqualToComparingFieldByFieldRecursively(employee);
    }

    @Test
    @Order(3)
    void test_createNewEmployee_whenValidEmployeeDetailsProvided_returnNewEmployee(){
        //given
        Employee employee = Employee.builder().id(3).firstName("Ali").lastName("Mustafa").build();

        //when
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Employee> entityRequest = new HttpEntity<>(employee, headers);

        ResponseEntity<Employee> response = testRestTemplate.exchange("/employees",
                                                                        HttpMethod.POST,
                                                                        entityRequest,
                                                                        Employee.class);

        Employee actualNewEmployee = response.getBody();

        //then
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful());
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(actualNewEmployee).usingComparatorForType((o1, o2)-> 0, Double.class)
                .isEqualToComparingFieldByFieldRecursively(employee);
    }
}
