package org.acme.resource;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import javax.persistence.Query;
import java.util.List;
import org.acme.model.Finding;
import java.util.UUID;
import javax.transaction.Transactional;
import javax.ws.rs.POST;
import org.acme.model.User;
import javax.ws.rs.PUT;
import org.acme.model.Comment;

import static java.time.LocalDateTime.now;

@Path("/findings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FindingResource
{
	@Inject
	EntityManager entityManager;
	
	// Get N newest findings
	@GET
	@Path("/newest/{amount}")
	@RolesAllowed("USER")
	public Response getNewestFindings(@PathParam int amount)
	{
		if (amount < 1)
		{
			return Response.status(404).build();
		}
		
		Query query = entityManager.createNamedQuery("Findings.newest");
		query.setMaxResults(amount);
		List<Finding> results = query.getResultList();
		return results == null ? Response.status(404).build() : Response.ok(results).build();
	}

	// Get 10 hottest findings from 48h
	@GET
	@Path("/hottest")
	@RolesAllowed("USER")
	public Response getHottestFindings()
	{
		Query query = entityManager.createNamedQuery("Findings.hottest");
		query.setParameter("date", now().minusDays(2));
		query.setMaxResults(10);
		List<Finding> results = query.getResultList();
		return results == null ? Response.status(404).build() : Response.ok(results).build();
	}

	// Get specific finding
	@GET
	@Path("/{id}")
	@RolesAllowed("USER")
	public Response getFinding(@PathParam UUID id)
	{
		Finding tmp = entityManager.find(Finding.class, id);
		return tmp == null ? Response.status(404).build() : Response.ok(tmp).build();
	}

	// Add new finding
	@POST
	@Transactional
	@RolesAllowed("USER")
	public Response addFinding(FindingArg finding)
	{
		if (finding == null || finding.author == null)
		{
			return Response.status(400).build();
		}
		
		Finding ent = Finding.validate(entityManager.find(User.class, finding.author), finding.title, finding.url, finding.description);
		
		if (ent == null)
		{
			return Response.status(400).build();
		}
		
		entityManager.persist(ent);
		return Response.ok(ent).status(201).build();
	}

	// Add point
	@PUT
	@Path("/{id}/inc")
	@Transactional
	@RolesAllowed("USER")
	public Response addPoint(@PathParam UUID id)
	{
		Finding ent = entityManager.find(Finding.class, id);
		
		if (ent != null)
		{
			++ent.points;
		}
		
		return ent == null ? Response.status(404).build() : Response.ok(ent).build();
	}

	// Remove point
	@PUT
	@Path("/{id}/dec")
	@Transactional
	@RolesAllowed("USER")
	public Response minusPoint(@PathParam UUID id)
	{
		Finding ent = entityManager.find(Finding.class, id);
		
		if (ent != null)
		{
			--ent.points;
		}
		
		return ent == null ? Response.status(404).build() : Response.ok(ent).build();
	}

	// Post a comment
	@PUT
	@Path("/{id}")
	@Transactional
	@RolesAllowed("USER")
	public Response addComment(@PathParam UUID id, CommentArg com)
	{
		if (com == null || com.author == null)
		{
			return Response.status(400).build();
		}
		
		Comment ent = Comment.validate(entityManager.find(Finding.class, id), entityManager.find(User.class, com.author), com.reply == null ? null : entityManager.find(Comment.class, com.reply), com.content);
		
		if (ent == null)
		{
			return Response.status(400).build();
		}
		
		entityManager.persist(ent);
		return Response.ok(ent).status(201).build();
	}
}

class FindingArg
{	
	public UUID author;
	
	public String title;

	public String url;

	public String description;

	public FindingArg() {}

	public FindingArg(UUID auth, String title, String url, String desc)
	{
		author = auth;
		this.title = title;
		this.url = url;
		description = desc;
	}
}

class CommentArg
{
	public UUID author;

	public UUID reply;

	public String content;

	public CommentArg() {}

	public CommentArg(UUID auth, UUID rep, String content)
	{
		author = auth;
		reply = rep;
		this.content = content;
	}
}