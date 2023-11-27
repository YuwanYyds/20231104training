package com.example.demo.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Inquiry;
@Primary
@Repository
public class InquiryDaoImpl implements InquiryDao {
	
	
	@Autowired
	 private JdbcTemplate jdbcTemplate;
	
	@Override
	public void insertInquiry(Inquiry inquiry) {
		jdbcTemplate.update("INSERT INTO inquiry(id, name, email,password,contents,created) VALUES(?,?,?,?,?,?)",
				inquiry.getId(),inquiry.getName(), inquiry.getEmail(),inquiry.getPassword(),inquiry.getContents(), inquiry.getCreated());

	}

	@Override
	public List<Inquiry> getAll() {
		String sql = "SELECT id, name, email,password,contents,created FROM inquiry";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
		List<Inquiry> list = new ArrayList<Inquiry>();
		for(Map<String, Object> result : resultList) {
			Inquiry inquiry = new Inquiry();
			inquiry.setId((int)result.get("id"));
			inquiry.setName((String)result.get("name"));
			inquiry.setEmail((String)result.get("email"));
			inquiry.setPassword((String)result.get("password"));
			inquiry.setContents((String)result.get("contents"));
			inquiry.setCreated((LocalDateTime)result.get("created"));
			list.add(inquiry);
		}
		return list;
	}

	@Override
	public int updateInquiry(Inquiry inquiry) {
		return jdbcTemplate.update("UPDATE inquiry SET name= ?, email= ?, password= ?, contents= ?, WHERE id= ?",
				inquiry.getName(), inquiry.getEmail(), inquiry.getPassword(), inquiry.getContents(), inquiry.getId());
	}

}
