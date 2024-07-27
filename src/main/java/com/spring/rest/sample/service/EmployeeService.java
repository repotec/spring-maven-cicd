package com.spring.rest.sample.service;

import com.spring.rest.sample.exception.RecordNotFoundException;
import com.spring.rest.sample.model.Employee;
import com.spring.rest.sample.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> findAllEmployees(){
        return (List)employeeRepository.findAll();
    }

    public Employee findByEmployeeId(Integer id){
        return employeeRepository.findById(id).orElseThrow(RecordNotFoundException::new);
    }

    public Employee saveEmployee(Employee employee){
        System.out.println(employee.getId() + "|" + employee.getFirstName());
        return employeeRepository.save(employee);
    }

    public void deleteEmployeeById(Integer id){
        employeeRepository.deleteById(id);
    }

    public void deleteEmployee(Employee employee){
        employeeRepository.delete(employee);
    }
}
