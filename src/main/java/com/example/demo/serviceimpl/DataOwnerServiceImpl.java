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
    public DataOwner setUserPoint(long userId) {
        DataOwner user = dataOwnerRepository.findOne(userId);
        user.setPoint(user.getPoint()-1);
        return dataOwnerRepository.save(user);
    }

    @Override
    public DataOwner getUserById(long id) {
        return dataOwnerRepository.findOne(id);
    }
}
