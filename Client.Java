package cas;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.HttpClient;

public final class Client {
 private static final Logger LOG = Logger.getLogger(Client.class.getName());
 

 public static String getTicket(final String server, final String domain, final String username,
   final String password, final String service) {
	 
  LOG.info("Within getTicket()");
  notNull(server, "server must not be null");
  notNull(domain, "domain must not be null");
  notNull(username, "username must not be null");
  notNull(password, "password must not be null");
  notNull(service, "service must not be null");

  return getServiceTicket(server, getTicketGrantingTicket(server, domain, username, password), service); 
 }

 private static String getServiceTicket(final String server, final String ticketGrantingTicket, final String service) {

  if (ticketGrantingTicket == null){ 
	  return null;  
  }
  
  final HttpClient client = new HttpClient();
  final PostMethod post = new PostMethod(server + "/" + ticketGrantingTicket);
  post.setRequestBody(new NameValuePair[] { new NameValuePair("service", service) });
  String response = null;
  try {
   client.executeMethod(post);
   //final String response = post.getResponseBodyAsString();
   response = post.getResponseBodyAsString();
   switch (post.getStatusCode()) {
   case 200:
    return response;
   default:
    //LOG.warning("Invalid response code (" + post.getStatusCode() + ") from CAS server!");
    //LOG.info("Response (1ka): " + response.substring(0, Math.min(1024, response.length())));
    break;
   }
  }
  catch (final IOException e) {
   LOG.warning(e.getMessage());
  }
  finally {
   post.releaseConnection();
  }
  //System.out.println("-->"+response);
  return response;
 }

 private static String getTicketGrantingTicket(final String server, final String domain, final String username,
   final String password) {
  final HttpClient client = new HttpClient();
  final PostMethod post = new PostMethod(server);
  post.setRequestBody(new NameValuePair[] { new NameValuePair("domain", domain),
    new NameValuePair("username", username), new NameValuePair("password", password) });
  try {
   client.executeMethod(post);
   final String response = post.getResponseBodyAsString();
   switch (post.getStatusCode()) {
   case 201: {
    final Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*").matcher(response);

    if (matcher.matches())
     return matcher.group(1);

    LOG.warning("Successful ticket granting request, but no ticket found!");
    LOG.info("Response (1kb): " + response.substring(0, Math.min(1024, response.length())));
    break;
   }
   default:
    LOG.warning("Invalid response code (" + post.getStatusCode() + ") from CAS server!");
    LOG.info("Response (1kc): " + response.substring(0, Math.min(1024, response.length())));
    break;
   }
  }
  catch (final IOException e) {
   LOG.warning(e.getMessage());
  }
  finally {
   post.releaseConnection();
  }
  return null;
 }

 private static void notNull(final Object object, final String message) {
  if (object == null)
   throw new IllegalArgumentException(message);
 }

 public static void main(final String[] args) {
	  
  final String server = "https://CASSERVER/sso/v1/tickets";
  final String domain = "www.CASSERVERDOMAIN.COM";
  final String username = "danieljredfern";
  final String password = "Ilovekittens";
  final String service = "https://APPLICATIONSERVER/WHATEVER/WHATEVER";
  
  String casTicket = getTicket(server, domain, username, password, service);
  LOG.info(getTicket(server, domain, username, password, service));
  
  //The java class will print out an ST- ticket. You use that within the next line below
  System.out.println(casTicket);
  
  // Once you have the ticket, then run the URL with the ticket passed
  //https://CASSERVER/sso/serviceValidate?service=https://APPLICATIONSERVER/WHATEVER/WHATEVER&ticket=ST-271-K3v21d6pvqTmp1tCR3gi-sso
 }
 
}
