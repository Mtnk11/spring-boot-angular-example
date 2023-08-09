package kz.madina.сustomer;

import kz.madina.exception.DuplicateResourceException;
import kz.madina.exception.RequestValidationException;
import kz.madina.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(Integer id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(
                        () -> new ResourceNotFound("customer with id [%s] not found".formatted(id)
                        ));
    }

    public void create(CustomerRegistrationRequest customer) {
        String email = customer.email();
        if (customerDao.existPersonWithEmail(email)) {
            throw new DuplicateResourceException("Email [%s] is already taken".formatted(email));
        }
        customerDao.save(
                new Customer(
                        customer.name(),
                        customer.email(),
                        customer.age()
                )
        );
    }

    public void deleteById(Integer customerId) {
       if (!customerDao.existPersonWithId(customerId)){
           throw new ResourceNotFound("customer with id [%s] not found".formatted(customerId));
       }
        customerDao.deleteCustomerById(customerId);
    }

    public void update(Integer customerId, CustomerRegistrationRequest updateCustomer) {
        Customer customerToUpdate = getCustomerById(customerId);

        boolean change = false;
        if (updateCustomer.name()!=null &&
                !updateCustomer.name().equals(customerToUpdate.getName())) {
            customerToUpdate.setName(updateCustomer.name());
            change = true;
        }
        if (updateCustomer.age()!=null &&
                !updateCustomer.age().equals(customerToUpdate.getAge())) {
            customerToUpdate.setAge(updateCustomer.age());
            change = true;
        }
        if (updateCustomer.email()!=null &&
                !updateCustomer.email().equals(customerToUpdate.getEmail())) {
            customerToUpdate.setEmail(updateCustomer.email());
            change = true;
        }

        if (!change) {
            throw new RequestValidationException( "no data changes found");
        }
        customerDao.update(customerToUpdate);
    }
}
