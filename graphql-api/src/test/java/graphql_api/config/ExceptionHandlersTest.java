package graphql_api.config;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import io.grpc.Status;
import org.junit.jupiter.api.Test;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.AccessDeniedException;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExceptionHandlersTest {
    private final GraphQlExceptionHandler handler=new GraphQlExceptionHandler();
    private GraphQLError resolve(Throwable error) { return handler.resolveToSingleError(error,mock(DataFetchingEnvironment.class,RETURNS_DEEP_STUBS)); }
    @Test void mapsApplicationAndSecurityErrors() { assertThat(resolve(new IllegalArgumentException("bad")).getErrorType()).isEqualTo(ErrorType.BAD_REQUEST); assertThat(resolve(new AccessDeniedException("no")).getErrorType()).isEqualTo(ErrorType.FORBIDDEN); }
    @Test void mapsGrpcErrorsAndIgnoresUnknown() { assertThat(resolve(Status.NOT_FOUND.withDescription("missing").asRuntimeException()).getErrorType()).isEqualTo(ErrorType.NOT_FOUND); assertThat(resolve(Status.INVALID_ARGUMENT.withDescription("bad").asRuntimeException()).getErrorType()).isEqualTo(ErrorType.BAD_REQUEST); assertThat(resolve(Status.UNAVAILABLE.withDescription("down").asRuntimeException()).getErrorType()).isEqualTo(ErrorType.INTERNAL_ERROR); assertThat(resolve(new RuntimeException("x"))).isNull(); }
    @Test void mapsRestErrors() { var response=new RestExceptionHandler().badRequest(new IllegalArgumentException("bad")); assertThat(response.getStatusCode().value()).isEqualTo(400); assertThat(response.getBody()).containsEntry("error","bad"); }
}
