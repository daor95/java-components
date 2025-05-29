/**
 * This class is part of the Programming the Internet of Things project.
 * 
 * It is provided as a simple shell to guide the student and assist with
 * implementation for the Programming the Internet of Things exercises,
 * and designed to be modified by the student as needed.
 */ 

package programmingtheiot.gda.connection;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import programmingtheiot.common.ConfigConst;
import programmingtheiot.common.ConfigUtil;
import programmingtheiot.common.IDataMessageListener;
import programmingtheiot.common.ResourceNameEnum;

import programmingtheiot.data.DataUtil;

/**
 * Shell representation of class for student implementation.
 *
 */
public class CoapClientConnector implements IRequestResponseClient
{
	// static
	
	private static final Logger _Logger =
		Logger.getLogger(CoapClientConnector.class.getName());
	
	// params
	
	
	// constructors
	
	/**
	 * Default.
	 * 
	 * All config data will be loaded from the config file.
	 */
	public CoapClientConnector()
	{
	}
		
	/**
	 * Constructor.
	 * 
	 * @param host
	 * @param isSecure
	 * @param enableConfirmedMsgs
	 */
	public CoapClientConnector(String host, boolean isSecure, boolean enableConfirmedMsgs)
	{
	}
	
	
	// public methods

	/*
	@Override
	public boolean sendDiscoveryRequest(int timeout)
	{
		return false;
	}
	*/


	@Override
	public boolean sendDiscoveryRequest(int timeout)
	{
		try {
			String uri = "coap://localhost:5683/.well-known/core";
			CoapClient client = new CoapClient(uri);
			client.setTimeout(timeout * 1000L); // timeout in ms

			Set<WebLink> resources = client.discover();

			if (resources != null && !resources.isEmpty()) {
				for (WebLink wl : resources) {
					_Logger.info("Discovered resource: " + wl.getURI());
				}
				return true;
			} else {
				_Logger.warning("No resources discovered.");
			}
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "Discovery request failed", e);
		}
		return false;
	}

	@Override
	public boolean sendDeleteRequest(ResourceNameEnum resource, String name, boolean enableCON, int timeout)
	{
		return false;
	}

	/*
	@Override
	public boolean sendGetRequest(ResourceNameEnum resource, String name, boolean enableCON, int timeout)
	{
		return false;
	}
	*/


	@Override
	public boolean sendGetRequest(ResourceNameEnum resource, String name, boolean enableCON, int timeout)
	{
		try {
			String uri = "coap://localhost:5683/" + resource.getResourceName();
			if (name != null && !name.isEmpty()) {
				uri += "?" + name;
			}
			CoapClient client = new CoapClient(uri);
			client.setTimeout(timeout * 1000L); // timeout in ms

			CoapResponse response = client.get();
			if (response != null && response.isSuccess()) {
				_Logger.info("GET response for " + uri + ": " + response.getResponseText());
				return true;
			} else {
				_Logger.warning("GET failed or no response for: " + uri);
			}
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "GET request failed", e);
		}
		return false;
	}

	@Override
	public boolean sendPostRequest(ResourceNameEnum resource, String name, boolean enableCON, String payload, int timeout)
	{
		return false;
	}

	/*
	@Override
	public boolean sendPutRequest(ResourceNameEnum resource, String name, boolean enableCON, String payload, int timeout)
	{
		return false;
	}
	*/

	@Override
	public boolean sendPutRequest(ResourceNameEnum resource, String name, boolean enableCON, String payload, int timeout)
	{
		try {
			String uri = "coap://localhost:5683/" + resource.getResourceName();
			if (name != null && !name.isEmpty()) {
				uri += "?" + name;
			}
			CoapClient client = new CoapClient(uri);
			client.setTimeout(timeout * 1000L); // timeout in ms

			CoapResponse response = client.put(payload, MediaTypeRegistry.APPLICATION_JSON);
			if (response != null && response.isSuccess()) {
				_Logger.info("PUT response for " + uri + ": " + response.getResponseText());
				return true;
			} else {
				_Logger.warning("PUT failed or no response for: " + uri);
			}
		} catch (Exception e) {
			_Logger.log(Level.SEVERE, "PUT request failed", e);
		}
		return false;
	}

	@Override
	public boolean setDataMessageListener(IDataMessageListener listener)
	{
		return false;
	}

	public void clearEndpointPath()
	{
	}
	
	public void setEndpointPath(ResourceNameEnum resource)
	{
	}
	
	@Override
	public boolean startObserver(ResourceNameEnum resource, String name, int ttl)
	{
		return false;
	}

	@Override
	public boolean stopObserver(ResourceNameEnum resourceType, String name, int timeout)
	{
		return false;
	}

	
	// private methods
	
}
