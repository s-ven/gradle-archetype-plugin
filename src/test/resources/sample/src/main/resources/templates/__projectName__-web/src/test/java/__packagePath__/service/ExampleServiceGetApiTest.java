package @packageName@.service;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.Hystrix;
import com.officedepot.servicecore.model.CoreServiceResponse;
import @packageName@.model.ExampleRequest;
import @packageName@.model.ExampleTO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestContext.class)
public class ExampleServiceGetApiTest {

	@Before
    public void setUp() {
    	//If you need to mock the services, make sure to reset before next test
        //Mockito.reset(exampleServiceGetApiMock);
		
    }
	@After
    public void reset() {
		ConfigurationManager.getConfigInstance()
        .setProperty("hystrix.command.default.circuitBreaker.forceOpen",
        false);
    	
		Hystrix.reset();
	}
    
    
    @Test
    public void testServiceGetCall() throws Exception {
    	ExampleServiceGetApi<ExampleRequest, CoreServiceResponse<ExampleRequest, ExampleTO>> exampleServiceGetApi = new ExampleServiceGetApi<>();
    	ExampleRequest er = new ExampleRequest();
		er.setName("test");
		exampleServiceGetApi.setRequest(er);
		exampleServiceGetApi.getProperties().circuitBreakerForceClosed();
		assertTrue(exampleServiceGetApi.execute().getResponseObject().getName().contains("successful"));
	}
    
    @Test
    public void testServiceGetFallbackCall() throws Exception {
    	//force circuit breaker open so fallback is invoked
    	ConfigurationManager.getConfigInstance()
        .setProperty("hystrix.command.default.circuitBreaker.forceOpen",
        true);
    	ExampleServiceGetApi<ExampleRequest, CoreServiceResponse<ExampleRequest, ExampleTO>> exampleServiceGetApi = new ExampleServiceGetApi<>();
    	ExampleRequest er = new ExampleRequest();
    	er.setName("failtest");
    	exampleServiceGetApi.setRequest(er);
    	
    	assertTrue(exampleServiceGetApi.execute().getResponseObject().getName().contains("failed"));
	}
}

