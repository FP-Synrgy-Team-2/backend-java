package com.example.jangkau.resources;

import com.example.jangkau.models.Account;
import com.example.jangkau.models.Transactions;
import com.example.jangkau.models.User;
import com.example.jangkau.models.oauth2.Client;
import com.example.jangkau.models.oauth2.Role;
import com.example.jangkau.models.oauth2.RolePath;
import com.example.jangkau.repositories.AccountRepository;
import com.example.jangkau.repositories.TransactionRepository;
import com.example.jangkau.repositories.UserRepository;
import com.example.jangkau.repositories.oauth2.ClientRepository;
import com.example.jangkau.repositories.oauth2.RolePathRepository;
import com.example.jangkau.repositories.oauth2.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Service
public class DatabaseSeeder implements ApplicationRunner {

//    private Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolePathRepository rolePathRepository;

    @Autowired
    private AccountRepository accountRepository;

    private String[] users = new String[]{
            "admin@mail.com:Admin1:Admin123!:Admin:08123456789:ROLE_SUPERUSER ROLE_USER ROLE_ADMIN",
            "johndoe@mail.com:Johndoe123:Johndoe123!:John Doe:08987654321:ROLE_USER",
            "fulan@mail.com:Fulan123:Fulan123!:Fulan:08123456789:ROLE_USER"
    };

    private String[] clients = new String[]{
            "my-client-apps:ROLE_READ ROLE_WRITE",
            "my-client-web:ROLE_READ ROLE_WRITE",
            "my-client-wb:ROLE_READ ROLE_WRITE",
    };

    private String[] roles = new String[]{
            "ROLE_SUPERUSER:user_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_ADMIN:user_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_USER:user_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_READ:oauth_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS",
            "ROLE_WRITE:oauth_role:^/.*:GET|PUT|POST|PATCH|DELETE|OPTIONS"
    };
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments applicationArguments) {
        this.insertRoles();
        this.insertClients();
        this.insertUsers();
    }

    @Transactional
    public void insertRoles() {
        for (String role : roles) {
            String[] str = role.split(":");
            String name = str[0];
            String type = str[1];
            String pattern = str[2];
            String[] methods = str[3].split("\\|");
            Role oldRole = roleRepository.findOneByName(name);
            if (null == oldRole) {
                oldRole = new Role();
                oldRole.setName(name);
                oldRole.setType(type);
                oldRole.setRolePaths(new ArrayList<>());
                for (String m : methods) {
                    String rolePathName = name.toLowerCase() + "_" + m.toLowerCase();
                    RolePath rolePath = rolePathRepository.findOneByName(rolePathName);
                    if (null == rolePath) {
                        rolePath = new RolePath();
                        rolePath.setName(rolePathName);
                        rolePath.setMethod(m.toUpperCase());
                        rolePath.setPattern(pattern);
                        rolePath.setRole(oldRole);
                        rolePathRepository.save(rolePath);
                        oldRole.getRolePaths().add(rolePath);
                    }
                }
            }
            roleRepository.save(oldRole);
        }
    }

    @Transactional
    public void insertClients() {
        for (String c : clients) {
            String[] s = c.split(":");
            String clientName = s[0];
            String[] clientRoles = s[1].split("\\s");
            Client oldClient = clientRepository.findOneByClientId(clientName);
            if (null == oldClient) {
                oldClient = new Client();
                oldClient.setClientId(clientName);
                oldClient.setAccessTokenValiditySeconds(3600);
                oldClient.setRefreshTokenValiditySeconds(7257600);
                oldClient.setGrantTypes("password refresh_token authorization_code");
                oldClient.setClientSecret(encoder.encode("password"));
                oldClient.setApproved(true);
                oldClient.setRedirectUris("");
                oldClient.setScopes("read write");
                List<Role> rls = roleRepository.findByNameIn(clientRoles);

                if (!rls.isEmpty()) {
                    oldClient.getAuthorities().addAll(rls);
                }
            }
            clientRepository.save(oldClient);
        }
    }

    @Transactional
    public void insertUsers() {
        List<User> userList = new ArrayList<>();
        int i = 0;
        for (String userData : users) {
            String[] str = userData.split(":");
            String emailAddress = str[0];
            String username = str[1];
            String password = str[2];
            String fullName = str[3];
            String phoneNumber = str[4];
            String[] roleNames = str[5].split("\\s");

            User oldUser = userRepository.findByUsername(username);
            if (null == oldUser) {
                oldUser = new User();
                oldUser.setUsername(username);
                oldUser.setEmailAddress(emailAddress);
                oldUser.setPassword(encoder.encode(password));
                oldUser.setFullName(fullName);
                oldUser.setPhoneNumber(phoneNumber);
                List<Role> r = roleRepository.findByNameIn(roleNames);
                oldUser.setRoles(r);
            }

            userList.add(oldUser);
            userRepository.save(oldUser);
            insertAccounts(oldUser, oldUser.getFullName(), i);
            i++;
        }
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(2024, Calendar.JULY, 1);
//        Date date0 = calendar.getTime();
//        calendar.set(2024, Calendar.JULY, 26);
//        Date date1 = calendar.getTime();
//        calendar.set(2024, Calendar.JULY, 27);
//        Date date2 = calendar.getTime();
//        insertTransactions(userList.get(0), userList.get(1), 200000.0, date0);
//        insertTransactions(userList.get(2), userList.get(1), 700000.0, date1);
//        insertTransactions(userList.get(1), userList.get(0), 500000.0, date2);
    }

    @Transactional
    public void insertAccounts(User user, String ownerName, int i) {
        User oldUser = userRepository.findByUsername(user.getUsername());
        Account oldAccount = accountRepository.findByUser(oldUser).orElse(null);
        if (null == oldAccount) {
            oldAccount = Account.builder()
                    .user(user)
                    .ownerName(ownerName)
                    .balance(DummyResource.ACCOUNTS_BALANCE[i])
                    .build();
            oldAccount.setPin(DummyResource.ACCOUNTS_PIN[i], encoder);
            accountRepository.save(oldAccount);
        }
    }

//    @Transactional
//    public void insertTransactions(User sender, User recipient, Double amount, Date transactionDate) {
//        Account sourceAccount = accountRepository.findByUser(sender).orElse(null);
//        Account recipientAccount = accountRepository.findByUser(recipient).orElse(null);
//        if ((null == sourceAccount) || (null == recipientAccount)) throw new RuntimeException("Account not found");
//        else {
//            Transactions transactions = Transactions.builder()
//                    .accountId(sourceAccount)
//                    .beneficiaryAccount(recipientAccount)
//                    .amount(amount)
//                    .transactionDate(transactionDate)
//                    .note("db seeder test")
//                    .build();
//            transactionRepository.save(transactions);
//            sourceAccount.setBalance(sourceAccount.getBalance() - (transactions.getAmount() + transactions.getAdminFee()));
//            recipientAccount.setBalance(recipientAccount.getBalance() + transactions.getAmount());
//            accountRepository.save(sourceAccount);
//            accountRepository.save(recipientAccount);
//        }
//    }
}
