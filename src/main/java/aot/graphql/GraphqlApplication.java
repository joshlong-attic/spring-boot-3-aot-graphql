package aot.graphql;


import graphql.GraphQL;
import graphql.analysis.QueryVisitorFieldArgumentEnvironment;
import graphql.analysis.QueryVisitorFieldArgumentInputValue;
import graphql.execution.Execution;
import graphql.execution.nextgen.result.RootExecutionResultNode;
import graphql.language.*;
import graphql.parser.ParserOptions;
import graphql.schema.*;
import graphql.schema.validation.SchemaValidationErrorCollector;
import graphql.util.NodeAdapter;
import graphql.util.NodeZipper;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.ClassPathResource;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static aot.graphql.GraphqlApplication.SCHEMA_RESOURCE;

@ImportRuntimeHints(GraphqlRuntimeHintsRegistrar.class)
@SpringBootApplication
public class GraphqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlApplication.class, args);
    }

    final static ClassPathResource SCHEMA_RESOURCE = new ClassPathResource("/graphql/schema.graphqls");

    @Bean
    GraphQlSourceBuilderCustomizer customizer() {
        return builder -> builder.schemaResources(SCHEMA_RESOURCE);
    }
}

@Controller
class CustomerGraphqlController {

    @QueryMapping
    Collection<Customer> customers() {
        return List.of(new Customer(1, "Andreas"), new Customer(2, "Rossen"));
    }
}

record Customer(Integer id, String name) {
}

class GraphqlRuntimeHintsRegistrar implements RuntimeHintsRegistrar {

    private final MemberCategory[] values = MemberCategory.values();

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        List.of(Customer.class, CustomerGraphqlController.class).forEach(c -> hints.reflection().registerType(c, this.values));
        Set.of(new ClassPathResource("graphiql/index.html"), SCHEMA_RESOURCE)
                .forEach(s -> hints.resources().registerResource(s));
        Set.of("i18n/Validation.properties", "i18n/Validation", "i18n/Execution.properties", "i18n/General.properties")
                .forEach(r -> hints.resources().registerResourceBundle(r));
        Set.of("graphql.analysis.QueryTraversalContext", "graphql.schema.idl.SchemaParseOrder")
                .forEach(typeName -> hints.reflection().registerType(TypeReference.of(typeName), this.values));
        Set.of(
                        Argument.class, ArrayValue.class, Boolean.class, BooleanValue.class, DataFetchingEnvironment.class,
                        Directive.class, DirectiveDefinition.class, DirectiveLocation.class, Document.class,
                        EnumTypeDefinition.class, EnumTypeExtensionDefinition.class, EnumValue.class, EnumValueDefinition.class,
                        Execution.class, Field.class, FieldDefinition.class, FloatValue.class, FragmentDefinition.class,
                        FragmentSpread.class, GraphQL.class, GraphQLArgument.class, GraphQLCodeRegistry.Builder.class,
                        GraphQLDirective.class, GraphQLEnumType.class, GraphQLEnumValueDefinition.class,
                        GraphQLFieldDefinition.class, GraphQLInputObjectField.class, GraphQLInputObjectType.class,
                        GraphQLInterfaceType.class, GraphQLList.class, GraphQLNamedType.class, GraphQLNonNull.class,
                        GraphQLObjectType.class, GraphQLOutputType.class, GraphQLScalarType.class, GraphQLSchema.class,
                        GraphQLSchemaElement.class, GraphQLUnionType.class, ImplementingTypeDefinition.class,
                        InlineFragment.class, InputObjectTypeDefinition.class, InputObjectTypeExtensionDefinition.class,
                        InputValueDefinition.class, IntValue.class, InterfaceTypeDefinition.class,
                        InterfaceTypeExtensionDefinition.class, List.class, ListType.class, NodeAdapter.class, NodeZipper.class,
                        NonNullType.class, NullValue.class, ObjectField.class, ObjectTypeDefinition.class,
                        ObjectTypeExtensionDefinition.class, ObjectValue.class, OperationDefinition.class,
                        OperationTypeDefinition.class, ParserOptions.class, QueryVisitorFieldArgumentEnvironment.class,
                        QueryVisitorFieldArgumentInputValue.class, RootExecutionResultNode.class, ScalarTypeDefinition.class,
                        ScalarTypeExtensionDefinition.class, SchemaDefinition.class, SchemaExtensionDefinition.class,
                        SchemaValidationErrorCollector.class, SelectionSet.class, StringValue.class, TypeDefinition.class,
                        TypeName.class, UnionTypeDefinition.class, UnionTypeExtensionDefinition.class, VariableDefinition.class,
                        VariableReference.class
                ) //
                .forEach(aClass -> hints.reflection().registerType(aClass, this.values));
    }
}
