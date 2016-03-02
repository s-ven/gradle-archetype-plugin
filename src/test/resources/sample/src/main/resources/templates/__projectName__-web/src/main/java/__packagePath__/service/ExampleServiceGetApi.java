package @packageName@.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.officedepot.servicecore.exceptions.DataProviderException;
import com.officedepot.servicecore.model.CoreServiceResponse;
import com.officedepot.servicecore.service.AbstractBaseService;

import @packageName@.model.ExampleRequest;
import @packageName@.model.ExampleTO;

/**
 * 
 * It is required to create a new Service Class for each API. APIs can be grouped to use the same threadpool provided by Hystrix. 
 * 
 * To use the same threadpool, make sure the SERVICE_GROUP name is the same.
 * If different threadpool is needed, use a different SERVICE_GROUP name.
 * For cashing use one or the other strategy: @EnableLocalCache enables local cache for successful calls.
 * @EnableLocalCacheForFallBack enabled local cache only for Fallback (unsuccessful) calls. Make sure neither of these 
 * are set for other than GET type of calls
 */
//@EnableLocalCache
//@EnableLocalCacheForFallBack 
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ExampleServiceGetApi<R,T> extends AbstractBaseService<ExampleRequest, ExampleTO> {
	
	private static final Logger logger = LoggerFactory.getLogger(ExampleServiceGetApi.class);
	//TODO: change the SERVICE_NAME to be unique for this API
	private static final String SERVICE_NAME = "ExampleServiceGetApi";
	//TODO: change the SERVICE_GROUP to be unique for this group of API
	private static final String SERVICE_GROUP = "ExampleGroup";
	
	//this is the request data.
	ExampleRequest request;

	//provide a default constructor for controller to init and cleanup
	public ExampleServiceGetApi() {
		this(null);
	}
		
	//provide a constructor with all request data required for call
	public ExampleServiceGetApi(ExampleRequest key) {
		super(SERVICE_GROUP, SERVICE_NAME);
		this.request = key;
	}
	
	@Override
	protected String getServiceName() {
		return SERVICE_NAME;
	}
	
	
	//TODO: implement your logic here. Can create additional classes such as DAO
	@Override
	protected CoreServiceResponse<ExampleRequest, ExampleTO> success() throws DataProviderException{
		logger.debug("success invoked for " + request);
		ExampleTO result = new ExampleTO();
		result.setName("successful for " + request);
		return new CoreServiceResponse<ExampleRequest, ExampleTO>(result);
	}
	
	//TODO: implement your backup logic here. This should avoid remote call.
	protected CoreServiceResponse<ExampleRequest, ExampleTO> fallback(){
		logger.debug("fallback invoked for " + request);
		ExampleTO result = new ExampleTO();
		result.setName("failed for " + request);
		return new CoreServiceResponse<ExampleRequest, ExampleTO>(result);
	}
	
	@Override
	public ExampleRequest getRequest() {
		return request;
	}

	public void setRequest(ExampleRequest key) {
		this.request = key;
	}
	
	/*
	 * ####### CACHE methods ######
	 * TODO: If Local caching is enabled, one of the following two needs to be done:
	 * 1. the class returned by getRequest() method could implement CacheKey interface.<br>
	 * 2. OR this method needs to be implemented correctly.
	 * <br> 
	 * For distributed cache, CacheKey must be implemented
	 */
	/**
	 * Override and return the key value to be used for storing the response in cache.
	 */
	@Override
	protected String getCustomCacheKey() {
		return request.toString();
	}
}