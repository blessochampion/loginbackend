package com.example.demo;

import com.example.demo.model.Content;
import com.example.demo.model.DataOwner;
import com.example.demo.model.LoginAppResponse;
import com.example.demo.service.ContentService;
import com.example.demo.service.DataOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private DataOwnerService dataOwnerService;
    private ContentService contentService;

    @Autowired
    public void setDataOwnerService(DataOwnerService dataOwnerService) {
        this.dataOwnerService = dataOwnerService;
    }

    @Autowired
    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping(value = "/users")
    public List<DataOwner> getAllUsers() {
        return dataOwnerService.getAllUsers();
    }

    @PostMapping(value = "/users/register")
    @ResponseBody
    public LoginAppResponse<DataOwner> register(@RequestBody DataOwner owner) {
        LoginAppResponse<DataOwner> response = new LoginAppResponse<>();
        response.setCode(LoginAppResponse.FAILURE);
        List<DataOwner> users = dataOwnerService.getUserByUsername(owner.getUsername());

        if (users.size() > 0) {
            response.setDescription("Username Exists");
            return response;
        }
        users = dataOwnerService.getUserByEmail(owner.getEmail());

        if (users.size() > 0) {
            response.setDescription("Email Exists");
            return response;
        }

        owner.setPoint(5);
        response.setCode(LoginAppResponse.SUCCESS);
        response.setDescription("Success");
        response.setData(dataOwnerService.createUser(owner));
        return response;

    }

    @PostMapping(value = "/users/authenticate")
    @ResponseBody
    public LoginAppResponse<DataOwner> authenticate(@RequestBody DataOwner owner) {
        LoginAppResponse<DataOwner> response = new LoginAppResponse<>();
        response.setCode(LoginAppResponse.FAILURE);
        List<DataOwner> users = dataOwnerService.getUserByUsername(owner.getUsername());


        if (users.size() == 0 || !users.get(0).getPassword().equals(owner.getPassword())) {
            response.setDescription("Invalid Username/Password");
            return response;
        }

        if (users.get(0).getConfirmation_status()==0) {
            response.setDescription("Please confirm your email address");
            return response;
        }
        response.setCode(LoginAppResponse.SUCCESS);
        response.setDescription("Success");
        response.setData(users.get(0));
        return response;
    }

    @PostMapping(value = "/users/addcontent")
    @ResponseBody
    public LoginAppResponse<DataOwner> addcontent(@RequestBody Content content) {
        LoginAppResponse<DataOwner> response = new LoginAppResponse<>();
        response.setCode(LoginAppResponse.FAILURE);
        DataOwner user = dataOwnerService.getUserById(content.getUserId());
        if (user == null) {
            response.setDescription("Invalid UserId");
            return response;
        }
        user = dataOwnerService.getUserById(content.getUserId());
        user = dataOwnerService.setUserPoint(content.getUserId(), user.getPoint()-1);
        contentService.createContent(content);
        response.setCode(LoginAppResponse.SUCCESS);
        response.setDescription("Success");
        response.setData(user);
        return response;
    }

    @GetMapping(value = "/users/{id}/contents")
    @ResponseBody
    public LoginAppResponse<List<Content>> getAllContentById(@PathVariable("id") Long id) {
        List<Content> contents = contentService.findContentsByUserId(id);
        LoginAppResponse<List<Content>> response = new LoginAppResponse<>();
        response.setCode(LoginAppResponse.SUCCESS);
        response.setData(contents);
         return response;
    }

    @PostMapping(value = "/users/sendmail")
    @ResponseBody
    public String sendMail(@RequestBody DataOwner owner){

        String composedMail = composeEmail(owner);
        ApplicationContext context =
                new ClassPathXmlApplicationContext("Spring-Mail.xml");

        MailSender2 mm = (MailSender2) context.getBean("mailMail");
        mm.sendMail("loginappng@gmail.com",
                owner.getEmail(),
                "Testing123",composedMail);

        return "okay";
    }

    @GetMapping(value = "/users/confirm/{id}")
    private String confirmUser(@PathVariable("is") long id){
        DataOwner owner = dataOwnerService.getUserById(id);
        owner.setConfirmation_status(1);
        dataOwnerService.createUser(owner);
        return  "Your account has been confirmed";
    }

    private String composeEmail(DataOwner owner) {
       return  "<p>Hello"+ owner.getUsername()+ "<br /> " +
                "You recently opened an account with LoginApp, <br /> please click <a href=\"https://loginmobileapp.herokuapp.com/api/users/confirm/"+owner.getId()
               +"\">here</a> to confirm your account. <p>";
    }

    @PostMapping(value = "/users/updatepoint/{id}/{point}")
    @ResponseBody
    public LoginAppResponse<DataOwner> addcontent(@PathVariable("id") long id, @PathVariable("point") int point) {
        LoginAppResponse<DataOwner> response = new LoginAppResponse<>();
        response.setCode(LoginAppResponse.FAILURE);
        DataOwner user = dataOwnerService.getUserById(id);
        if (user == null) {
            response.setDescription("Invalid UserId");
            return response;
        }
        user = dataOwnerService.setUserPoint(id, point);
        response.setCode(LoginAppResponse.SUCCESS);
        response.setDescription("Success");
        response.setData(user);
        return response;
    }

}
