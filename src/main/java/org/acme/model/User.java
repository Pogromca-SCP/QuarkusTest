package org.acme.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Cacheable;
import javax.persistence.Id;
import javax.persistence.Column;
import java.util.UUID;

@Entity
@Table(name = "User")
@NamedQuery(name = "Users.get", query = "SELECT u FROM User u WHERE u.username = :user AND u.password = :pass", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@Cacheable
public class User
{
	@Id
	@Column(length = 16)
	public UUID id;
	
	@Column(nullable = false, length = 40)
	public String username;

	@Column(nullable = false, length = 40)
	public String password;

	@Column(length = 100)
	public String description;

	public User() {}

	public User(UUID id, String name, String pass, String desc)
	{
		this.id = id;
		username = name;
		password = pass;
		description = desc;
	}
	
	public static User validate(String name, String pass, String desc)
	{
		if (name == null || name.length() > 40 || pass == null || pass.length() > 40)
		{
			return null;
		}
		
		if (desc != null && desc.length() > 100)
		{
			return null;
		}
		
		return new User(UUID.randomUUID(), name, pass, desc);
	}
}