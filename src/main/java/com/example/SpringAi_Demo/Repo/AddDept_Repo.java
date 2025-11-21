package com.example.SpringAi_Demo.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.SpringAi_Demo.Entity.Dept_Entity;

@Repository
public interface AddDept_Repo extends JpaRepository<Dept_Entity, Long>{

	@Query(nativeQuery = true, value = """
			
			select * from role_table order by role_id desc;
			
			""")
	List<Dept_Entity> getListData();
}
