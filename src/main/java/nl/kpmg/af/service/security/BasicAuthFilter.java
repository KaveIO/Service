package nl.kpmg.af.service.security;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import nl.kpmg.af.service.data.MongoDBUtil;
import org.apache.catalina.core.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import nl.kpmg.af.service.data.security.User;
import nl.kpmg.af.service.data.security.repository.UserRepository;
import org.springframework.stereotype.Component;


/**
 * Created by fziliotto on 24-6-16.
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
@Component
public class BasicAuthFilter implements ContainerRequestFilter {
  @Autowired
  UserRepository userRepository;

  @Autowired
  MongoDBUtil mongoDBUtil;

    /**
   * The name of the http request header containing the authentication user.
   */
  public static final String BASIC_AUTHENTICATION_HEADER = "Authorization";
  /**
   * Class logger
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(BasicAuthFilter.class);
  private String username = "";
  private String password = "";
  private String realm = "DataService";



  private ServiceRequest createServiceRequest(ContainerRequestContext servletRequest) {
    ServiceRequest request = new V0ServiceRequest(servletRequest);
    if (!request.isValid()) {
      request = new V1ServiceRequest(servletRequest);
    }
    return request;
  }

  /**
   * Apply the filter : check input request, validate or not with user auth
   *
   * @param containerRequest The request from Tomcat server
   */
  @Override
  public void filter(ContainerRequestContext containerRequest) throws WebApplicationException {

    // GET, POST, PUT, DELETE, ...
    String method = containerRequest.getMethod();

    if (method.equals("OPTIONS")) {
      containerRequest.abortWith(Response.status(Response.Status.OK).build());
    }

    String auth = containerRequest.getHeaderString(BASIC_AUTHENTICATION_HEADER);

    // lap : loginAndPassword
    String[] credentials = BasicAuth.decode(auth);

    // Get the authentification passed in HTTP headers parameters

    // If the user does not have the right (does not provide any HTTP Basic Auth)
    if (auth == null) {
      throw new WebApplicationException(challengeResponse( "", "").build());
    }

    // If login or password fail
    if (credentials == null || credentials.length != 2) {
      throw new WebApplicationException(challengeResponse("","").build());
    }
    LOGGER.debug("Filtering request with basic authentication. Input username:password = {}:{}",
            credentials[0], credentials[1]);

    ServiceRequest serviceRequest = createServiceRequest(containerRequest);

    User user = userRepository.findOneByUsername(credentials[0]);
    if(user==null){
      throw new WebApplicationException(challengeResponse( "User does not exist", "").build());
    }else
      if(user.getPassword().equals(credentials[1])) {
      // We configure your Security Context here
      String scheme = containerRequest.getUriInfo().getRequestUri().getScheme();
      containerRequest.setSecurityContext(new UserSecurityContext(user,scheme));
      return;
    }else{
      throw new WebApplicationException(challengeResponse( "Wrong Password", "").build());
    }

    // DO YOUR DATABASE CHECK HERE (replace that line behind)...
    // User authentificationResult = AuthentificationThirdParty.authentification(lap[0], lap[1]);
    // User authentificationResult;

    // Our system refuse login and password
    /*
     * if(authentificationResult == null){ throw new
     * WebApplicationException(Response.Status.UNAUTHORIZED); }
     */

    // TODO : HERE YOU SHOULD ADD PARAMETER TO REQUEST, TO REMEMBER USER ON YOUR REST SERVICE...

  }

  /**
   * Method to for modifying the response object with an access denied message.
   *
   * @param error
   * @param description
   * @return always false allows for sweet one-liner on a challenge
   */
  protected Response.ResponseBuilder challengeResponse(String error,
                                                       String description) {
    Response.ResponseBuilder builder = null;
    builder = Response.status(Response.Status.UNAUTHORIZED).entity(error);

    StringBuilder header = new StringBuilder();

    header.append("Basic realm=\"");
    header.append("DataService\"");


    if (error != null) {
      header.append(", error=\"").append(error).append("\"");
    }
    if (description != null) {
      header.append(", error_description=\"").append(description).append("\"");
    }
    builder.header("WWW-Authenticate", header.toString());

    return builder;
  }


}


