package com.example.SpringAi_Demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.SpringAi_Demo.Entity.Dept_Entity;
import com.example.SpringAi_Demo.Service.AddDept_Service;

@Controller
@CrossOrigin(origins = "http://192.168.31.94:3000")
public class AddDept_Controller {

	@Autowired
	private AddDept_Service addDept_Service;
	
	@PostMapping("/add-data")
	public ResponseEntity<String> saveData(@ModelAttribute Dept_Entity dept_Entity){
		try {
			addDept_Service.insertData(dept_Entity);
			return ResponseEntity.ok("Data inserted Successfully!");
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.ok("Data not inserted");
		}
	}
	
	@GetMapping("/get-data")
	@ResponseBody
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<List<Dept_Entity>> getData(@RequestParam(defaultValue = "0", required = false) int pageNo, 
										@RequestParam(defaultValue = "5", required = false) int pageSize, 
										@RequestParam(defaultValue = "asc", required = false) String dir,
										@RequestParam(defaultValue = "roleId", required = false) String columnType){
		List<Dept_Entity> fetchedData = addDept_Service.getData(pageNo, pageSize, dir, columnType);
		return new ResponseEntity<List<Dept_Entity>>(fetchedData, HttpStatus.OK);
	}
}
