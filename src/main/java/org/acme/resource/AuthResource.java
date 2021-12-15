package org.acme.resource;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.POST;
import javax.annotation.security.PermitAll;
import javax.ws.rs.core.Response;
import javax.persistence.Query;
import java.util.List;
import org.acme.model.User;

import static org.acme.util.TokenUtils.generateToken;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource
{
	@Inject
	EntityManager entityManager;
	
	// Open session
	@POST
	@PermitAll
	public Response login(AuthRequest auth)
	{
		if (auth == null)
		{
			return Response.status(404).build();
		}
		
		Query query = entityManager.createNamedQuery("Users.get");
		query.setParameter("user", auth.username);
		query.setParameter("pass", auth.password);
		query.setMaxResults(1);
		List<User> results = query.getResultList();
		User ent = results != null && results.size() > 0 ? results.get(0) : null;
		
		try
		{
			return ent == null ? Response.status(404).build() : Response.ok(new AuthResponse(generateToken(ent.username, 3600L, "https://acme.org"))).build();
		}
		catch (Exception e)
		{
			return Response.status(404).build();
		}
	}
}

class AuthRequest
{
	public String username;
	
	public String password;
	
	public AuthRequest() {}
	
	public AuthRequest(String user, String pass)
	{
		username = user;
		password = pass;
	}
}

class AuthResponse
{
	public String token;
	
	public AuthResponse() {}
	
	public AuthResponse(String tok)
	{
		token = tok;
	}
}