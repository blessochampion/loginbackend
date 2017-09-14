package com.example.demo.serviceimpl;

import com.example.demo.model.DataOwner;
import com.example.demo.repository.DataOwnerRepository;
import com.example.demo.service.DataOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataOwnerServiceImpl implements DataOwnerService{

    private DataOwnerRepository dataOwnerRepository;

    @Autowired
    public void setDataOwnerRepository(DataOwnerRepository dataOwnerRepository) {
        this.dataOwnerRepository = dataOwnerRepository;
    }

    @Override
    public List<DataOwner> getAllUsers(){
        return dataOwnerRepository.findAll();
    }

    @Override
    public DataOwner createUser(DataOwner user) {
        return  dataOwnerRepository.save(user);
    }

    @Override
    public List<DataOwner> getUserByEmail(String email){
        return dataOwnerRepository.findByEmail(email);
    }

    @Override
    public List<DataOwner> getUserByUsername(String username) {
        return dataOwnerRepository.findByUsername(username);
    }

    @Override
    public DataOwner setUserPoint(long userId, int point) {
        DataOwner user = dataOwnerRepository.findOne(userId);
        user.setPoint(point);
        return dataOwnerRepository.save(user);
    }

    @Override
    public DataOwner getUserById(long id) {
        return dataOwnerRepository.findOne(id);
    }
}
