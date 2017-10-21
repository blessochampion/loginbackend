package com.example.demo.repository;

import com.example.demo.model.DataOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface DataOwnerRepository extends JpaRepository<DataOwner, Long> {

    List<DataOwner> findByEmail(String email);
    
    List<DataOwner> findByUsername(String username);

}
