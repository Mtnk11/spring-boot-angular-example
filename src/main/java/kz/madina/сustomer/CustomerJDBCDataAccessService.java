package kz.madina.сustomer;

import kz.madina.exception.ResourceNotFound;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper rowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id, name,email,age
                 FROM customer
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        var sql = """
                SELECT id, name, email, age 
                FROM customer
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, rowMapper, customerId)
                .stream()
                .findFirst();
    }

    @Override
    public void save(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, age)
                VALUES(?,?,?)
                """;
        int update = jdbcTemplate.update(sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge());
        System.out.println("jdbcTemplate.update = " + update);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        var sql = """
                SELECT count(id) 
                FROM customer
                WHERE email = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existPersonWithId(Integer id) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        var sql = """
                DELETE 
                FROM customer
                WHERE id = ?
                """;
        jdbcTemplate.update(sql,customerId);

    }

    @Override
    public void update(Customer customerToUpdate) {
        if (customerToUpdate.getName() != null) {
            var sql = """
                UPDATE customer
                SET name = ?
                where id = ?
                """;
            jdbcTemplate.update(sql,customerToUpdate.getName(),customerToUpdate.getId());
        }
        if (customerToUpdate.getEmail() != null) {
            var sql = """
                UPDATE customer
                SET age = ?
                where id = ?
                """;
            jdbcTemplate.update(sql,customerToUpdate.getAge(),customerToUpdate.getId());
        }
        if (customerToUpdate.getEmail() != null) {
            var sql = """
                UPDATE customer
                SET email = ?
                where id = ?
                """;
            jdbcTemplate.update(sql,customerToUpdate.getEmail(),customerToUpdate.getId());
        }

    }
}
