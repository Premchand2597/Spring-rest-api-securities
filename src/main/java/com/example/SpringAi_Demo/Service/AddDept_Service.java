package com.example.SpringAi_Demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.SpringAi_Demo.Entity.Dept_Entity;
import com.example.SpringAi_Demo.Repo.AddDept_Repo;

@Service
public class AddDept_Service {

	@Autowired
	private AddDept_Repo addDept_Repo;
	
	public void insertData(Dept_Entity dept_Entity) {
		addDept_Repo.save(dept_Entity);
	}
	
	public List<Dept_Entity> getData(int pageNo, int pageSize, String dir, String columnType) {
		Sort sort = ("asc").equalsIgnoreCase(dir) ? Sort.by(columnType).ascending() : Sort.by(columnType).descending();
		Pageable p = PageRequest.of(pageNo, pageSize, sort);
		Page<Dept_Entity> pageList = addDept_Repo.findAll(p);
		List<Dept_Entity> listData = pageList.getContent();
		return listData;
	}
}
