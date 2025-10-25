package ef.edu.cibertec.gestion.clientes.api.handler;

import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ef.edu.cibertec.gestion.clientes.api.dto.ErrorResponse;
import ef.edu.cibertec.gestion.clientes.exception.BusinessException;


@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
	    return ResponseEntity
	        .status(HttpStatus.NOT_FOUND) // 404
	        .body(ErrorResponse.of("NOT_FOUND", ex.getMessage()));
	}

	@ExceptionHandler(BusinessException.class)
	ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
	    return ResponseEntity
	        .status(HttpStatus.UNPROCESSABLE_ENTITY) // 422
	        .body(ErrorResponse.of("BUSINESS_RULE", ex.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
	    return ResponseEntity
	        .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
	        .body(ErrorResponse.of("INTERNAL_ERROR", "Ocurrió un error inesperado"));
	}

	  @ExceptionHandler(IllegalArgumentException.class)
	  public ResponseEntity<Map<String,Object>> handleIllegalArg(IllegalArgumentException ex) {
	    return ResponseEntity.status(409).body(Map.of(
	      "code","CONFLICT",
	      "message", ex.getMessage(),
	      "timestamp", java.time.OffsetDateTime.now().toString()
	    ));
	  }

	  @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
	  public ResponseEntity<Map<String,Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
	    return ResponseEntity.status(409).body(Map.of(
	      "code","CONSTRAINT_VIOLATION",
	      "message","Violación de restricción (dni/correo duplicado).",
	      "timestamp", java.time.OffsetDateTime.now().toString()
	    ));
	  }

}

