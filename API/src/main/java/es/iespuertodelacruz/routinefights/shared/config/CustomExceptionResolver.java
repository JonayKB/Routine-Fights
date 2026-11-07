package es.iespuertodelacruz.routinefights.shared.config;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;

/**
 * Turns resolver exceptions into GraphQL errors. Messages from the application's
 * own exceptions and from validation are meant for users and pass through;
 * anything else (driver, mapping, NPE...) is logged and replaced by a generic
 * message so internals never reach the client.
 */
@Component
@Slf4j
public class CustomExceptionResolver extends DataFetcherExceptionResolverAdapter {

    static final String GENERIC_MESSAGE = "Something went wrong, please try again";
    private static final String APP_PACKAGE = "es.iespuertodelacruz.routinefights";

    @Override
    protected GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment env) {
        if (!(ex instanceof RuntimeException)) {
            return null;
        }
        String message;
        ErrorType type = ErrorType.BAD_REQUEST;
        if (ex instanceof AccessDeniedException) {
            message = "Access Denied";
            type = ErrorType.FORBIDDEN;
        } else if (isUserFacing(ex)) {
            message = ex.getMessage();
        } else {
            log.error("Unexpected error in {}", env.getExecutionStepInfo().getPath(), ex);
            message = GENERIC_MESSAGE;
            type = ErrorType.INTERNAL_ERROR;
        }
        return GraphqlErrorBuilder.newError()
                .message(message)
                .errorType(type)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }

    static boolean isUserFacing(Throwable ex) {
        return ex.getMessage() != null
                && (ex.getClass().getName().startsWith(APP_PACKAGE) || ex instanceof IllegalArgumentException);
    }
}
