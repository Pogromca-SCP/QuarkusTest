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
import java.util.UUID;
import org.acme.model.User;
import javax.transaction.Transactional;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource
{
	@Inject
	EntityManager entityManager;
	
	// Get specific user
	@GET
	@Path("/{id}")
	@RolesAllowed("USER")
	public Response getUser(@PathParam UUID id)
	{
		User tmp = entityManager.find(User.class, id);
		return tmp == null ? Response.status(404).build() : Response.ok(tmp).build();
	}

	// Add new user
	@POST
	@Transactional
	@RolesAllowed("USER")
	public Response addUser(UserArg user)
	{
		if (user == null)
		{
			return Response.status(400).build();
		}
		
		User ent = User.validate(user.username, user.password, user.description);
		
		if (ent == null)
		{
			return Response.status(400).build();
		}
		
		entityManager.persist(ent);
		return Response.ok(ent).status(201).build();
	}

	// Update user
	@PUT
	@Path("/{id}")
	@Transactional
	@RolesAllowed("USER")
	public Response updateUser(@PathParam UUID id, UserArg user)
	{
		if (user == null)
		{
			return Response.status(400).build();
		}
		
		User ent = entityManager.find(User.class, id);
		User tmp = User.validate(user.username, user.password, user.description);
		
		if (ent != null && tmp != null)
		{
			ent.username = tmp.username;
			ent.password = tmp.password;
			ent.description = tmp.description;
		}
		
		return ent == null ? Response.status(404).build() : Response.ok(ent).build();
	}
}

class UserArg
{
	public String username;
	
	public String password;
	
	public String description;
	
	public UserArg() {}
	
	public UserArg(String name, String pass, String desc)
	{
		username = name;
		password = pass;
		description = desc;
	}
}