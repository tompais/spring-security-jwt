package com.example.springsecurityjwt.error.handler

import com.example.springsecurityjwt.utils.camelToSnake
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [IllegalArgumentException::class, IllegalStateException::class])
    protected fun handleConflict(ex: RuntimeException) {
        throw ResponseStatusException(BAD_REQUEST, ex.message, ex)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> = ex.bindingResult.allErrors.associate { error ->
        (error as FieldError).field.camelToSnake() to error.getDefaultMessage()
    }.let { errors ->
        throw ResponseStatusException(BAD_REQUEST, errors.toString(), ex)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        throw ResponseStatusException(BAD_REQUEST, ex.message, ex)
    }

    private fun getExceptionParameter(e: JsonMappingException): String =
        e.path.joinToString(".") {
            if (it.index >= 0)
                "[${it.index}]"
            else it.fieldName
        }

    @ExceptionHandler(MissingKotlinParameterException::class)
    fun handleMissingKotlinParameter(missingKotlinParameterException: MissingKotlinParameterException) {
        throw ResponseStatusException(
            BAD_REQUEST,
            "key [${getExceptionParameter(missingKotlinParameterException)}] not found",
            missingKotlinParameterException
        )
    }

    @ExceptionHandler(MismatchedInputException::class)
    fun handleMismatchedInput(mismatchedInputException: MismatchedInputException) {
        throw ResponseStatusException(
            BAD_REQUEST,
            "type mismatch for key [${getExceptionParameter(mismatchedInputException)}]",
            mismatchedInputException
        )
    }

    @ExceptionHandler(JsonProcessingException::class)
    fun handleJsonProcessing(jsonProcessingException: JsonProcessingException) {
        throw ResponseStatusException(BAD_REQUEST, "error in parsing body", jsonProcessingException)
    }
}
