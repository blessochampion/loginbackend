package com.example.demo;

import com.example.demo.config.ServerConfig;
import com.example.demo.model.*;
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
    private MailSender2 mailSender;
    
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

        /*todo :remove default point*/
        owner.setPoint(10);
        response.setCode(LoginAppResponse.SUCCESS);
        response.setDescription("Success");
        DataOwner user = dataOwnerService.createUser(owner);
        mailSender.sendMail(user);
        response.setData(user);
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

    @PostMapping(value = "/users/recharge")
    @ResponseBody
    public LoginAppResponse<String> recharge(@RequestBody Recharge recharge) {
        LoginAppResponse<String> response = new LoginAppResponse<>();
        response.setCode(LoginAppResponse.FAILURE);
        List<DataOwner> users = dataOwnerService.getUserByEmail(recharge.getEmail());

        if (users.size() == 0) {
            response.setDescription("Email does not exist");
            return response;
        }

        if (users.get(0).getConfirmation_status()==0) {
            response.setDescription("Please confirm your email address. Kindly check your email for the confirmation link");
            return response;
        }

        DataOwner owner = users.get(0);
        int currentPoint  = owner.getPoint() + (int)(recharge.getAmount()/10);
        owner.setPoint(currentPoint);
        dataOwnerService.createUser(owner);
        response.setCode(LoginAppResponse.SUCCESS);
        response.setDescription("Success");
        response.setData("Point added successfully, your new point balance is " + currentPoint);
        return response;
    }



    

    @GetMapping(value = "/users/confirm/{id}")
    @ResponseBody
    private String confirmUser(@PathVariable("id") long id){
        DataOwner owner = dataOwnerService.getUserById(id);
        owner.setConfirmation_status(1);
        dataOwnerService.createUser(owner);
        return  "Your account has been confirmed";
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

    @PostMapping(value = "/users/forgotPassword")
    @ResponseBody
    public LoginAppResponse<String> forgotPassword(@RequestBody PasswordResetRequest resetRequest)
    {
        LoginAppResponse<String> response = new LoginAppResponse<>();
        response.setCode(LoginAppResponse.FAILURE);
       List<DataOwner> user = dataOwnerService.getUserByEmail(resetRequest.getEmail());
       if(user.size() > 0){
           response.setCode(LoginAppResponse.SUCCESS);
           sendPasswordLinkToUserEmail(user.get(0));
           response.setDescription("Success");
           response.setData("Please check your email for the link to reset your password");
       }else {
          response.setDescription("Failure");
          response.setData("User does not exist");
       }

       return response;
    }

    private void sendPasswordLinkToUserEmail(DataOwner owner)
    {


    }

}
