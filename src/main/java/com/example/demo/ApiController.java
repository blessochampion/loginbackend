package com.example.demo;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.example.demo.model.Content;
import com.example.demo.model.DataOwner;
import com.example.demo.model.LoginAppResponse;
import com.example.demo.service.ContentService;
import com.example.demo.service.DataOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

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
        List<DataOwner> users = dataOwnerService.getUserByEmail(owner.getEmail());

        if (users.size() > 0) {
            response.setDescription("User already Exists");
            return response;
        }

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
        response.setCode(LoginAppResponse.SUCCESS);
        response.setDescription("Success");
        response.setData(users.get(0));
        return response;
    }

    @PostMapping(value = "/users/addcontent")
    @ResponseBody
    public LoginAppResponse<DataOwner> authenticate(@RequestBody Content content) {
        LoginAppResponse<DataOwner> response = new LoginAppResponse<>();
        response.setCode(LoginAppResponse.FAILURE);
        DataOwner user = dataOwnerService.getUserById(content.getUserId());
        if (user == null) {
            response.setDescription("Invalid UserId");
            return response;
        }
        user = dataOwnerService.setUserPoint(content.getUserId());
        contentService.createContent(content);
        response.setCode(LoginAppResponse.SUCCESS);
        response.setDescription("Success");
        response.setData(user);
        return response;
    }

    @GetMapping(value = "users/{id}/contents")
    @ResponseBody
    public LoginAppResponse<List<Content>> getAllContentById(@PathVariable("id") Long id) {
        List<Content> contents = contentService.findContentsByUserId(id);
        LoginAppResponse<List<Content>> response = new LoginAppResponse<>();
        response.setCode(LoginAppResponse.SUCCESS);
        response.setData(contents);
         return response;
    }


}
