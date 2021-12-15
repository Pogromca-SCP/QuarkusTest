package org.acme.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Cacheable;
import javax.persistence.Id;
import javax.persistence.Column;
import java.util.UUID;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
@Table(name = "Comment")
@Cacheable
public class Comment
{
	@Id
	@Column(length = 16)
	public UUID id;

	@ManyToOne
	@JoinColumn(name = "finding", nullable = false)
	public Finding finding;

	@ManyToOne
	@JoinColumn(name = "author", nullable = false)
	public User author;

	@ManyToOne
	@JoinColumn(name = "reply")
	public Comment reply;

	@Column(nullable = false, length = 100)
	public String content;

	public Comment() {}

	public Comment(UUID id, Finding find, User auth, Comment rep, String content)
	{
		this.id = id;
		finding = find;
		author = auth;
		reply = rep;
		this.content = content;
	}
	
	public static Comment validate(Finding find, User auth, Comment rep, String content)
	{
		if (find == null || auth == null || content == null || content.length() > 100)
		{
			return null;
		}
		
		return new Comment(UUID.randomUUID(), find, auth, rep, content);
	}
}