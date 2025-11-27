package com.example.SpringAi_Demo.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SpringAi_Demo.Entity.RefreshToken_Entity;

public interface RefreshToken_Repo extends JpaRepository<RefreshToken_Entity, Integer>{
 
	Optional<RefreshToken_Entity> findByJti(String jti);
}
