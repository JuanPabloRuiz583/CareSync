package graphql_api.config;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import io.grpc.StatusRuntimeException;

@Component
public class GraphQlExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        if (ex instanceof IllegalArgumentException) {
            return GraphqlErrorBuilder.newError(env)
                    .message(ex.getMessage())
                    .errorType(ErrorType.BAD_REQUEST)
                    .build();
        }
        if (ex instanceof AccessDeniedException) {
            return GraphqlErrorBuilder.newError(env)
                    .message(ex.getMessage())
                    .errorType(ErrorType.FORBIDDEN)
                    .build();
        }
        if (ex instanceof StatusRuntimeException grpcException) {
            ErrorType type = switch (grpcException.getStatus().getCode()) {
                case NOT_FOUND -> ErrorType.NOT_FOUND;
                case INVALID_ARGUMENT -> ErrorType.BAD_REQUEST;
                default -> ErrorType.INTERNAL_ERROR;
            };
            return GraphqlErrorBuilder.newError(env).message(grpcException.getStatus().getDescription()).errorType(type).build();
        }
        return null;
    }
}
