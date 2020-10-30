package th.ac.kmitl.atm.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import th.ac.kmitl.atm.model.Customer;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    private List<Customer> customerList;

    @PostConstruct
    public void postConstruct() {
        this.customerList = new ArrayList<>();
    }

    public void createCustomer(Customer customer) {
        // ....hash pin....
        String hashPin = hash(customer.getPin());
        customer.setPin(hashPin);
        customerList.add(customer);
    }

    public List<Customer> getCustomers() {
        return new ArrayList<>(this.customerList);
    }

    public Customer findCustomer(int id) {
        for (Customer customer : customerList) {
            if (customer.getId() == id)
                return customer;
        }
        return null;
    }

    public Customer checkPin(Customer inputCustomer) {
        Customer storesCustomer = findCustomer(inputCustomer.getId());

        if(storesCustomer != null) {
            String hashPin = storesCustomer.getPin();

            if(BCrypt.checkpw(inputCustomer.getPin(), hashPin))
                return storesCustomer;
        }
        return null;
    }

    private String hash(String pin) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(pin, salt);
    }

}
