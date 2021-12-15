package org.acme.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Cacheable;
import javax.persistence.Id;
import javax.persistence.Column;
import java.util.UUID;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;

@Entity
@Table(name = "Finding")
@NamedQuery(name = "Findings.newest", query = "SELECT f FROM Finding f ORDER BY f.postdate DESC", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@NamedQuery(name = "Findings.hottest", query = "SELECT f FROM Finding f WHERE f.postdate > :date ORDER BY f.points DESC", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@Cacheable
public class Finding
{
	@Id
	@Column(length = 16)
	public UUID id;

	@ManyToOne
	@JoinColumn(name = "author", nullable = false)
	public User author;
	
	@Column(nullable = false, length = 50)
	public String title;

	@Column(nullable = false, length = 50)
	public String url;

	@Column(nullable = false, length = 100)
	public String description;

	@Column(nullable = false)
	public int points;
	
	@Column(nullable = false)
	public LocalDateTime postdate;

	public Finding() {}

	public Finding(UUID id, User auth, String title, String url, String desc, int points, LocalDateTime date)
	{
		this.id = id;
		author = auth;
		this.title = title;
		this.url = url;
		description = desc;
		this.points = points;
		postdate = date;
	}
	
	public Finding(UUID id, User auth, String title, String url, String desc)
	{
		this(id, auth, title, url, desc, 0, LocalDateTime.now());
	}
	
	public static Finding validate(User auth, String title, String url, String desc)
	{
		if (auth == null || title == null || title.length() > 50 || url == null || url.length() > 50 || desc == null || desc.length() > 100)
		{
			return null;
		}
		
		return new Finding(UUID.randomUUID(), auth, title, url, desc);
	}
}