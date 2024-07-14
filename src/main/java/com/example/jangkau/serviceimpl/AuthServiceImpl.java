package com.example.jangkau.serviceimpl;

import com.example.jangkau.config.Config;
import com.example.jangkau.config.EmailSender;
import com.example.jangkau.config.EmailTemplate;
import com.example.jangkau.config.SimpleStringUtils;
import com.example.jangkau.dto.auth.*;
import com.example.jangkau.mapper.AuthMapper;
import com.example.jangkau.models.User;
import com.example.jangkau.models.oauth2.Role;
import com.example.jangkau.repositories.UserRepository;
import com.example.jangkau.repositories.oauth2.RoleRepository;
import com.example.jangkau.services.AuthService;
import com.example.jangkau.services.ValidationService;
import com.example.jangkau.services.oauth.Oauth2UserDetailService;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ValidationService validationService;

    @Value("${BASEURL}")
    private String baseUrl;

    @Value("${AUTHURL}")
    private String authUrl;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private EmailTemplate emailTemplate;

    @Autowired
    private EmailSender emailSender;

    @Value("${expired.token.password.minute:}")
    private int expiredToken;

    @Autowired
    private Config config;

    @Autowired
    private Oauth2UserDetailService userDetailsService;

    public User register(RegisterRequest request) {
        validationService.validate(request);
        String[] roleNames = {"ROLE_USER", "ROLE_READ", "ROLE_WRITE"}; // admin

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exist");
        }

        if (userRepository.existsByEmailAddress(request.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exist");
        }
        User user = new User();
        user.setUsername(request.getUsername().toLowerCase());
        user.setEmailAddress(request.getEmailAddress());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        String password = encoder.encode(request.getPassword().replaceAll("\\s+", ""));
        List<Role> r = roleRepository.findByNameIn(roleNames);

        user.setRoles(r);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        validationService.validate(request);
        User checkUser = userRepository.findByUsername(request.getUsername());

        if ((checkUser != null) && (encoder.matches(request.getPassword(), checkUser.getPassword()))) {
            if (!checkUser.isEnabled()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not enabled");
            }
        }
        if (checkUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (!(encoder.matches(request.getPassword(), checkUser.getPassword()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password");
        }
        String url = baseUrl + "/oauth/token?username=" + checkUser.getUsername() +
                "&password=" + request.getPassword() +
                "&grant_type=password" +
                "&client_id=my-client-web" +
                "&client_secret=password";
        ResponseEntity<Map> response = restTemplateBuilder.build().exchange(url, HttpMethod.POST, null, new
                ParameterizedTypeReference<Map>() {
                }
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            User user = userRepository.findByUsername(request.getUsername());
            List<String> roles = new ArrayList<>();

            for (Role role : user.getRoles()) {
                roles.add(role.getName());
            }

            return authMapper.toLoginResponse(response);
        } else {
            throw new ResponseStatusException(response.getStatusCode(), "User not found");
        }
    }

    @Override
    public Object sendEmailOtp(EmailRequest request, String subject) {
        validationService.validate(request);
        String message = "Thanks, please check your email for activation.";

        User found = userRepository.findByEmailAddress(request.getEmailAddress());
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Email Not Found");
        }
        String template = subject.equalsIgnoreCase("Register") ? emailTemplate.getRegisterTemplate() : emailTemplate.getResetPassword();
        if (StringUtils.isEmpty(found.getOtp())) {
            User search;
            String otp;
            do {
                otp = SimpleStringUtils.randomString(6, true);
                search = userRepository.findByOtp(otp);
            } while (search != null);
            Date dateNow = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateNow);
            calendar.add(Calendar.MINUTE, expiredToken);
            Date expirationDate = calendar.getTime();
            found.setOtp(otp);
            found.setOtpExpiredDate(expirationDate);
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername()));
            template = template.replaceAll("\\{\\{TOKEN}}", otp);
            userRepository.save(found);
        } else {
            template = template.replaceAll("\\{\\{USERNAME}}", (found.getUsername()));
            template = template.replaceAll("\\{\\{TOKEN}}", found.getOtp());
        }
        emailSender.sendAsync(found.getEmailAddress(), subject, template);
        return message;
    }

    @Override
    public Object confirmOtp(String otp) {
        User user = userRepository.findByOtp(otp);
        if (null == user) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "OTP Not Found");
        }
        if (user.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.OK, "Account Already Active, Please login!");
        }
        String today = config.convertDateToString(new Date());

        String dateToken = config.convertDateToString(user.getOtpExpiredDate());
        if (Long.parseLong(today) > Long.parseLong(dateToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your token is expired. Please Get token again.");
        }
        user.setEnabled(true);
        userRepository.save(user);

        return "Success, Please login!";
    }

    @Override
    public Object checkOtpValid(OtpRequest otp) {
        validationService.validate(otp);
        if (otp.getOtp() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otp is required");

        User user = userRepository.findByOtp(otp.getOtp());
        if (user == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otp Not Valid");

        return "Success, Please Change New Password!";
    }

    @Override
    public Object resetPassword(ResetPasswordRequest request) {
        validationService.validate(request);
        User user = userRepository.findByOtp(request.getOtp());
        if (user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Otp Not Valid");

        user.setPassword(encoder.encode(request.getNewPassword().replaceAll("\\s+", "")));
        user.setOtpExpiredDate(null);
        user.setOtp(null);

        userRepository.save(user);

        return "Success Reset Password, Please login with your new password!";
    }

    @Override
    public User getCurrentUser(Principal principal) {
        try {
            User user = userRepository.findByUsername(principal.getName());
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
            }
            return userRepository.save(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        }
    }

    @Override
    public Object signWithGoogle(MultiValueMap<String, String> parameters) throws IOException {
        Map<String, String> map = parameters.toSingleValueMap();
        String accessToken = map.get("accessToken");

        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        Oauth2 oauth2 = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential).setApplicationName("Oauth2").build();
        Userinfoplus profile;
        try {
            profile = oauth2.userinfo().get().execute();
        } catch (GoogleJsonResponseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, e.getDetails().getMessage());
        }
        profile.toPrettyString();
        User user = userRepository.findByEmailAddress(profile.getEmail());
        if (null != user) {
            if (!user.isEnabled()) {
                sendEmailOtp(new EmailRequest(user.getEmailAddress()), "Activate Account");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your Account is disable. Please chek your email for activation.");
            }

            String oldPassword = user.getPassword();
            if (!encoder.matches(profile.getId(), oldPassword)) {
                user.setPassword(encoder.encode(profile.getId()));
                userRepository.save(user);
            }

            String url = authUrl +
                    "?username=" + user.getUsername() +
                    "&password=" + profile.getId() +
                    "&grant_type=password" +
                    "&client_id=my-client-web" +
                    "&client_secret=password";
            System.out.println(url);
            ResponseEntity<Map> response = restTemplateBuilder.build().exchange(url, HttpMethod.POST, null, new ParameterizedTypeReference<Map>() {
            });

            if (response.getStatusCode() == HttpStatus.OK) {
                user.setPassword(oldPassword);
                userRepository.save(user);
                return authMapper.toLoginResponse(response);
            }
        } else {
//            register
            return register(new RegisterRequest(
                    profile.getEmail(),
                    profile.getEmail(),
                    profile.getEmail(),
                    profile.getEmail(),
                    profile.getId()
            ));
        }
        return user;
    }
}
