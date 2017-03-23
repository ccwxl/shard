package org.gpstable.mapper;

import org.gpstable.domain.User;

import java.util.List;

public interface UserMapper {
	
	void insert(User user);

	User query(Long id);

	void delete(Long id);

	void update(Long id);

	List<String> deviceSelect();
}
