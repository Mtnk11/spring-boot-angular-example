package kz.madina.сustomer;

import kz.madina.exception.ResourceNotFound;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{
    private static List<Customer> customers;

    static {
        customers = new ArrayList<>();

        Customer madina = new Customer(
                1,
                "Madina",
                "madina@mail.ru",
                24
        );
        Customer dina = new Customer(
                5,
                "Dina",
                "dina@mail.ru",
                25
        );Customer adina = new Customer(
                9,
                "Adina",
                "adina@mail.ru",
                22
        );
        customers.add(madina);
        customers.add(dina);
        customers.add(adina);
    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        return  customers.stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst();
    }

    @Override
    public void save(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(a->a.getEmail().equals(email));
    }
    @Override
    public boolean existPersonWithId(Integer id) {
        return customers.stream()
                .anyMatch(a->a.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customers.stream()
                .filter(c->c.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);
    }

    @Override
    public void update(Customer customerToUpdate) {
        customers.add(customerToUpdate);
    }


}