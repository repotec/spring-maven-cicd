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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeServiceUnitTest {

    @InjectMocks
    EmployeeService employeeService;

    @Mock
    EmployeeRepository employeeRepository;

    @Test
    void testCreateNewUser_whenProvideValidEmployee_return2xx() throws Exception {
        //given
        Employee employee = Employee.builder().id(1).firstName("ahmed").lastName("mohammed").build();

        //when
        Mockito.when(employeeRepository.save(Mockito.isA(Employee.class))).thenReturn(employee);
        Employee newEmployee = employeeService.saveEmployee(employee);

        //then
        Mockito.verify(employeeRepository, Mockito.atMostOnce()).save(Mockito.isA(Employee.class));
        Assertions.assertThat(newEmployee).usingComparatorForType((o1, o2)-> 0, Double.class)
                .isEqualToComparingFieldByFieldRecursively(employee);
    }

    @Test
    void testDeleteEmployee_whenProvideEmployeeId_return2xx() throws Exception {
        //given
        Integer givenEmployeeId = 1;

        //when
        Mockito.doNothing().when(employeeRepository).deleteById(Mockito.anyInt());
        employeeService.deleteEmployeeById(givenEmployeeId);

        //then
        Mockito.verify(employeeRepository, Mockito.atMostOnce()).deleteById(Mockito.anyInt());
    }

    @Captor
    ArgumentCaptor<Integer> employeeIdCaptor;

    @Test
    void testDeleteEmployee_whenProvideValidEmployeeId_EmployeeIdShouldPassedToRepo() throws Exception {
        //given
        Integer givenEmployeeId = 1;

        //when
        Mockito.doNothing().when(employeeRepository).deleteById(employeeIdCaptor.capture());
        employeeService.deleteEmployeeById(givenEmployeeId);

        //then
        Mockito.verify(employeeRepository, Mockito.atMostOnce()).deleteById(employeeIdCaptor.capture());
        MatcherAssert.assertThat(employeeIdCaptor.getValue(), CoreMatchers.is(givenEmployeeId));
    }

    @Test
    void testDeleteEmployee_whenProvideValidEmployee_EmployeeIdShouldPassedToRepo() throws Exception {
        //given
        Employee givenEmployee = Employee.builder().id(1).firstName("ahmed").lastName("mohammed").build();

        //when
        ArgumentCaptor<Employee> employeeValueCapture = ArgumentCaptor.forClass(Employee.class);
        Mockito.doAnswer(invocation -> {
            Object employee = invocation.getArgument(0);
            Assertions.assertThat(employee).usingComparatorForType((o1, o2)-> 0, Double.class)
                    .isEqualToComparingFieldByFieldRecursively(givenEmployee);
            return null;
        }).when(employeeRepository).delete(employeeValueCapture.capture());

        employeeService.deleteEmployee(givenEmployee);


        //then
        Mockito.verify(employeeRepository, Mockito.atMostOnce()).delete(employeeValueCapture.capture());
        MatcherAssert.assertThat(employeeValueCapture.getValue(), CoreMatchers.is(givenEmployee));
    }
}