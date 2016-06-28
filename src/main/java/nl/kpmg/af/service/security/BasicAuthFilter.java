package nl.kpmg.af.service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;



/**
 * Jersey HTTP Basic Auth filter
 * 
 * @author Deisss (LGPLv3)
 */
public class BasicAuthFilter implements ContainerRequestFilter {
  @Context
  HttpServletRequest request;
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
    // myresource/get/56bCA for example
    String path = containerRequest.getUriInfo().getPath(true);

    Response response;

    if (method.equals("OPTIONS")) {
      containerRequest.abortWith(Response.status(Response.Status.OK).build());
    }

    String auth = containerRequest.getHeaderString(BASIC_AUTHENTICATION_HEADER);

    // lap : loginAndPassword
    String[] lap = BasicAuth.decode(auth);

    // Get the authentification passed in HTTP headers parameters

    // If the user does not have the right (does not provide any HTTP Basic Auth)
    if (auth == null) {
      throw new WebApplicationException(challengeResponse( "", "").build());
    }

    // If login or password fail
    if (lap == null || lap.length != 2) {
      throw new WebApplicationException(challengeResponse("","").build());
    }
    LOGGER.debug("Filtering request with basic authentication. Input username:password = {}:{}",
            lap[0], lap[1]);

    ServiceRequest serviceRequest = createServiceRequest(containerRequest);


    /*
    // Valves aren't executed in the actual application scope so Spring is not able to inject our
    // ApplicationContextFetch so we try to fetch this from our servlet context.
    WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(
            request.getContext().getServletContext());
    UserRepository userRepository = webApplicationContext.getBean(UserRepository.class);

    User user = userRepository.findOneByUsername(username);
    if (user.getPassword().equals(password)) {
      GenericPrincipal principal = new GenericPrincipal(
              request.getContext().getRealm(),
              username,
              password,
              user.getRoles());

      request.setUserPrincipal(principal);
      request.setAuthType("BASIC");
      return true;
    }
    if (authenticated == false) {
      containerRequest.abortWith(Response.status(Response.Status.OK).build());

    }*/
    // DO YOUR DATABASE CHECK HERE (replace that line behind)...
    // User authentificationResult = AuthentificationThirdParty.authentification(lap[0], lap[1]);
    // User authentificationResult;

    // Our system refuse login and password
    /*
     * if(authentificationResult == null){ throw new
     * WebApplicationException(Response.Status.UNAUTHORIZED); }
     */

    // TODO : HERE YOU SHOULD ADD PARAMETER TO REQUEST, TO REMEMBER USER ON YOUR REST SERVICE...

    return;
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


