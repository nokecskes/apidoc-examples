# apidoc-examples

Main branch contains the boilerplate code.

## Try SpringFox with Swagger UI:
	1. Checkout 'springfox_swaggerui' branch
	2. Build: 'mvn clean compile'
	3. Run: 'mvn spring-boot:run'
	4. API documentation is available at:
		- Api docs JSON: /v2/api-docs
		- Api Documentation page (with Swagger UI): /swagger-ui/


## Try Spring RESTDocs:
	1. Checkout 'spring_restdocs' branch
	2. Build: 'mvn clean compile'
	3. Run tests and generate API documentation: 'mvn package'
	4. Generated sources are available at:
		- API documentation: target/generated-docs/index.html
		- Generated snippets: target/generated-snippets/
		

## Try Swagger2Markup:
	1. Checkout 'swagger2markup_demo' branch
	2. Run: 'mvn clean test'
	3. Generated API documentation is at: /target/asciidoc/html/index.html
	
	See configuration source: https://github.com/Swagger2Markup/spring-swagger2markup-demo
		