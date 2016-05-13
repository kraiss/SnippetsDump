package fr.kraiss.scratch.gist;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

/**
 * @author Pierrick Rassat
 * @see https://github.com/kraiss
 */
@Component
@Path("/application")
@Produces(MediaType.TEXT_PLAIN)
public class ApplicationResource {

  // Create the jersey resource with Spring to be sure that the Spring Application Context is correctly initialized
  // Otherwise, Jersey would directly created the resource and we won't be able to check that Spring beans are available

  private @Context ServletContext context;

  private static final String VERSION_SUFFIX = "Implementation-Version: ";
  private String applicationManifest = null;
  private String applicationManifestVersion = null;

  @GET
  @Path("/manifest")
  public Response getManifest() {
    try {
      initManifestResponse();
      return Response.status(Response.Status.OK).entity(applicationManifest).build();
    } catch (Exception e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    }
  }

  @GET
  @Path("/version")
  public Response getVersion() {
    try {
      initManifestResponse();
      return Response.status(Response.Status.OK).entity(applicationManifestVersion).build();
    } catch (Exception e) {
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    }
  }

  public void initManifestResponse() throws Exception {
    if (applicationManifest == null || applicationManifestVersion == null) {
      try (InputStream manifest = context.getResourceAsStream("/META-INF/MANIFEST.MF");
          BufferedReader reader = new BufferedReader(new InputStreamReader(manifest));) {
        StringBuilder sb = new StringBuilder();
        String content;
        while ((content = reader.readLine()) != null) {
          sb.append(content);
          sb.append('\n');
          if (content.startsWith(VERSION_SUFFIX)) {
            applicationManifestVersion = content.substring(VERSION_SUFFIX.length());
          }
        }
        applicationManifest = sb.toString();
      } catch (Exception e) {
        String message = "Could not retrieve application's version.";
        throw new Exception(message, e);
      }
    }
  }
}
