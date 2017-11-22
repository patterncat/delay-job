package cn.patterncat.job.admin.exception;

import cn.patterncat.rest.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Created by patterncat on 2017-11-18.
 */
@RestControllerAdvice
public class ExceptionAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(JedisConnectionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult jedisConnectionException(JedisConnectionException ex){
        LOGGER.error(ex.getMessage(),ex);
        return ApiResult.fail(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult genericError(final Exception ex) {
        LOGGER.error(ex.getMessage(),ex);
        return ApiResult.fail(ex.getMessage());
    }
}
