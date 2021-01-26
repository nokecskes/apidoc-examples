package com.apidoc.demo;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

import javax.xml.bind.ValidationException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;


import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiDocDemoApplication.class)
@AutoConfigureRestDocs()
public class ApiDocumentationJUnit4IntegrationTest {
	
	
	/*
	 *  By default, the JUnitRestDocumentation rule is automatically configured with an output directory based on your project’s build tool
	 *  Otherwise we can configure outputDir:
	 *  @AutoConfigureRestDocs(outputDir = "target/generated-snippets")
	 *  or
	 *  public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");
	 */
	 @Rule
	 public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

	 @Autowired
	 private WebApplicationContext context;

	 private MockMvc mockMvc;
	 
	 /*
	  * We can apply the same default configuration on each test method instead of configuring them individually. E.g.:
	  * 	- which snippets will be generated.
	  * 	- apply preprocessors (e.g. prettyprint() that formats jsons)
	  */
	 @Before
	 public void setup() {
		 this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).apply(documentationConfiguration(this.restDocumentation).snippets().withDefaults(httpRequest(), httpResponse(), responseBody(), requestBody()).and().operationPreprocessors().withRequestDefaults(prettyPrint()).withResponseDefaults(prettyPrint())).build();
	 }

	 
	 /*
	  * If document parameter is different than the method name, duplicated snippets are generated in two directories
	  * 1. named after method name, 2.named after document() parameter
	  * 
	  * We can declare descriptors beforehand, such as FieldDescriptors, to add more information about fields, such as type or description.
	  * 
	  * In addition to the default snippets we can add custom snippets for each request, e.g.: response fields or response headers.
	  */
	 @Test
	 public void getUsers() throws Exception {
		 FieldDescriptor[] user = new FieldDescriptor[] {
				 fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("User id"),
				 fieldWithPath("[].eMail").type(JsonFieldType.STRING).description("User e-mail")
		 };
		 this.mockMvc.perform(get(UserController.API_USERS)).andExpect(status().isOk()).andDo(document("get-users", responseFields(user), responseHeaders(headerWithName("Content-Type").description("The Content-Type of the payload."))));
	 }
	 
	 
	 /*
	  * 
	  * Descriptors can be inserted individually as well.
	  * 
	  */
	 @Test
	 public void getUserById() throws Exception {
		 this.mockMvc.perform(get(UserController.API_USER_BY_ID, 1)).andExpect(status().isOk()).andDo(document("get-user-by-id", responseFields(fieldWithPath("id").type(JsonFieldType.NUMBER).description("User id"),
				 fieldWithPath("eMail").type(JsonFieldType.STRING).description("User e-mail"))));
	 }
	 
	 /*
	  * We can display additional information about fields: type (JsonFieldType), description.
	  * 
	  * We can display constraints as well (e.g. javax.validation.constraints.NotNull).
	  * To display constraints we need to  
	  * 	1. include them as attributes with the field
	  * 	2. and create custom snippet template that displays them in table format
	  * 
	  * To override default templates we should create a new one:
	  * 	- here: src/test/resources/org/springframework/restdocs/templates/asciidoctor/
	  * 	- with the same name but not as .adoc: request-fields.snippet (the original is request-fields.adoc)
	  * 
	  * We can apply additional attributes not only on fields but on the snippet itself e.g. configure a custom title that will be displayed.
	  * 
	  */
	 @Test
	 public void saveUser() throws Exception {
		 User user = new User();
		 user.seteMail("newuser@demo.hu");
		 
		 ConstraintDescriptions userConstraints = new ConstraintDescriptions(User.class);
		 String idConstraints = StringUtils.collectionToDelimitedString(userConstraints.descriptionsForProperty("id"), ". ");
		 String eMailConstraints = StringUtils.collectionToDelimitedString(userConstraints.descriptionsForProperty("eMail"), ". ");
		 this.mockMvc.perform(post(UserController.API_USER).contentType(MediaType.APPLICATION_JSON).content(convertObjectToJsonBytes(user))).andExpect(status().isOk()).andDo(document("save-user", 
                 requestFields(
                         attributes(key("title").value("Save new user fields")),
                         fieldWithPath("id").type(JsonFieldType.NULL).description("User id").attributes(key("constraints").value(idConstraints)),
                         fieldWithPath("eMail").type(JsonFieldType.STRING).description("New password").attributes(key("constraints").value(eMailConstraints)))));
	 }
	 
	 /*
	  * We can add request parameter descriptions.
	  */
	 @Test
	 public void getUserByEmail() throws Exception {
		 this.mockMvc.perform(get(UserController.API_USER).param("eMail", "demo@demo.hu")).andExpect(status().isOk()).andDo(document("get-user-by-email", requestParameters(parameterWithName("eMail").description("User e-mail"))));
	 }
	 
	 /*
	  * We can add path parameter descriptions.
	  * To display pathvariable in documentation we should replace
	  * import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders methods
	  * to
	  * import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders methods
	  */
	 @Test
	 public void deleteUserById() throws Exception {
		 this.mockMvc.perform(delete(UserController.API_USER_BY_ID, 2)).andExpect(status().isOk()).andDo(document("delete-user-by-id", pathParameters(parameterWithName("id").description("User id"))));
	 }
	 
	 private String convertObjectToJsonBytes(Object object) throws IOException {
		 ObjectMapper mapper = new ObjectMapper();
	     return mapper.writeValueAsString(object);
	 }
	 
	 

}
