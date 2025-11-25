package com.example.SpringAi_Demo.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SpringAi_Demo.Entity.Login;

@Repository
public interface LoginRepo extends JpaRepository<Login, Integer>{
	Login findByEmail(String email);
}
