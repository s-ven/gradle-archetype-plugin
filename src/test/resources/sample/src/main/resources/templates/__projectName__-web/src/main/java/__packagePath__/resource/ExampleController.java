package@packageName @.resource;

  import javax.annotation.PostConstruct;
  import javax.annotation.PreDestroy;

  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Controller;
  import org.springframework.web.bind.annotation.PathVariable;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RequestMethod;
  import org.springframework.web.bind.annotation.ResponseBody;

  import com.officedepot.servicecore.model.CoreServiceResponse;
  import com.officedepot.servicecore.resource.AbstractBaseController;

  import@packageName @.model.ExampleTO;
  import@packageName @.model.ExampleRequest;
  import@packageName @.service.ExampleServiceGetApi;

  import io.swagger.annotations.Api;
  import io.swagger.annotations.ApiOperation;
  import io.swagger.annotations.ApiResponse;
  import io.swagger.annotations.ApiResponses;

/**
 * This is the entry point to the Rest Service. You could add multiple API methods in this same controller or create additional Controller classes,
 * former is preferred. Create a separate Service class for each API.
 */
@Controller
@Api(value = "/", description = "Example operations")
public class ExampleController extends AbstractBaseController {

  private final static Logger LOGGER = LoggerFactory.getLogger(ExampleController.class);

  @Autowired
  public ExampleServiceGetApi<ExampleRequest, ExampleTO> exampleServiceGetApi;

  @ApiOperation(
    value = "Find api1 by key", notes = "Returns a string when a key is passed to simulate API success",
    response = CoreServiceResponse.class
  )
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Invalid input")}
  )
  @RequestMapping(value = "/api1/{key}", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json; charset=utf-8")
  @ResponseBody
  public CoreServiceResponse<ExampleRequest, ExampleTO> getDataFromService(@PathVariable("key") String key) {
    ExampleRequest request = new ExampleRequest();
    request.setName(key);

    //TODO: you should return whole Response so client can look for errors
    return getExampleServiceGetApi(request).execute();
  }

  //create service instance with request data
  protected ExampleServiceGetApi<ExampleRequest, ExampleTO> getExampleServiceGetApi(ExampleRequest request) {
    exampleServiceGetApi.setRequest(request);
    return exampleServiceGetApi;
  }


  /**
   * This method initializes controller and services
   */
  @PostConstruct
  protected void init() {
    LOGGER.info("ExampleController init method called");
    try {
      //this initiates Local cache if enabled, etc
      new ExampleServiceGetApi<ExampleRequest, ExampleTO>().init();
    } catch (Exception e) {
      LOGGER.error("service init failed: " + e.getMessage(), e);
    }
    //TODO: add init for other services as needed
  }

  /**
   * This method cleans up controller and services
   */
  @PreDestroy
  protected void destroy() {
    LOGGER.info("ExampleController destroy method called");
    try {
      //this shuts down Local cache, etc
      new ExampleServiceGetApi<ExampleRequest, ExampleTO>().cleanup();
    } catch (Exception e) {
      LOGGER.error("service cleanup failed: " + e.getMessage(), e);
    }
    //TODO: add cleanup for other services added
  }
}