# Spring Boot 3 AOT support for Spring GraphQL 

This project demonstrates getting Spring or GraphQL working with Spring Boot 3 and Spring Framework 6 AOT


Make sure you have the latest 22.3 build of GraalVM. I'm using the Liberica NIK version as installed from `sdkman`. Run the following commands to install and make default this version: `sdk install java 22.3.r17.ea-nik && sdk default java 22.3.r17.ea-nik`

Run `build.sh` to produce and run a native binary in the `target` directory. Then visit [`localhost:8080/graphiql`](http://localhost:8080/graphiql) to see the schema in action. Issue the following query:

```
query {
 customers { 
  id, name 
 }
}
```