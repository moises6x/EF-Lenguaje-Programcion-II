package ef.edu.cibertec.gestion.clientes.api.handler;

import ef.edu.cibertec.gestion.clientes.api.dto.ErrorResponse;
import ef.edu.cibertec.gestion.clientes.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // --- 404 ---
  // Si usas ChangeSetPersister.NotFoundException, añade ese import y este handler.
  // Alternativa: usa ResponseStatusException(HttpStatus.NOT_FOUND, "msg")
  @ExceptionHandler(org.springframework.data.crossstore.ChangeSetPersister.NotFoundException.class)
  ResponseEntity<ErrorResponse> handleNotFound(org.springframework.data.crossstore.ChangeSetPersister.NotFoundException ex) {
    log.warn("404 NotFound: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ErrorResponse.of("NOT_FOUND", ex.getMessage()));
  }

  // También cubre cualquier ResponseStatusException que lances
  @ExceptionHandler(ResponseStatusException.class)
  ResponseEntity<ErrorResponse> handleRse(ResponseStatusException ex) {
    log.warn("RSE {}: {}", ex.getStatusCode(), ex.getReason());
    return ResponseEntity.status(ex.getStatusCode())
        .body(ErrorResponse.of(ex.getStatusCode().toString(), ex.getReason()));
  }

  // --- 422 Regla de negocio ---
  @ExceptionHandler(BusinessException.class)
  ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
    log.warn("Regla de negocio: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(ErrorResponse.of("BUSINESS_RULE", ex.getMessage()));
  }

  // --- 400 Validación @Valid en @RequestBody ---
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
    String detalle = ex.getBindingResult().getFieldErrors().stream()
        .map(this::formatFieldError)
        .collect(Collectors.joining(" | "));
    log.warn("400 BadRequest (validation): {}", detalle);
    return ResponseEntity.badRequest().body(Map.of(
        "code", "VALIDATION_ERROR",
        "message", "Datos inválidos",
        "detail", detalle,
        "timestamp", OffsetDateTime.now().toString()
    ));
  }

  private String formatFieldError(FieldError fe) {
    return fe.getField() + ": " + (fe.getDefaultMessage() == null ? "inválido" : fe.getDefaultMessage());
  }

  // --- 400 JSON mal formado / tipo incorrecto ---
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String,Object>> handleNotReadable(HttpMessageNotReadableException ex) {
    log.warn("400 BadRequest (not readable): {}", ex.getMostSpecificCause().getMessage());
    return ResponseEntity.badRequest().body(Map.of(
        "code","MESSAGE_NOT_READABLE",
        "message","Cuerpo de la petición inválido o mal formado.",
        "timestamp", OffsetDateTime.now().toString()
    ));
  }

  // --- 409 Integridad de datos (únicos, FK, etc.) ---
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String,Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
    log.warn("409 DataIntegrity: {}", ex.getMostSpecificCause().getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
        "code","CONSTRAINT_VIOLATION",
        "message","Violación de restricción (por ejemplo, dni/correo duplicado).",
        "timestamp", OffsetDateTime.now().toString()
    ));
  }

  // --- 409 argumentos inválidos a nivel de servicio ---
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String,Object>> handleIllegalArg(IllegalArgumentException ex) {
    log.warn("409 IllegalArgument: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
        "code","CONFLICT",
        "message", ex.getMessage(),
        "timestamp", OffsetDateTime.now().toString()
    ));
  }

  // --- 403 seguridad (opcional si usas Spring Security) ---
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
    log.warn("403 Forbidden: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(ErrorResponse.of("FORBIDDEN", "No tienes permisos para esta operación."));
  }


  // --- 500 fallback ---
  @ExceptionHandler(Exception.class)
  ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
    log.error("500 Unexpected error", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorResponse.of("INTERNAL_ERROR", "Ocurrió un error inesperado"));
  }
  
  @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
  public ResponseEntity<Map<String,Object>> handleNoResourceFound(NoResourceFoundException ex) {
      Map<String,Object> body = new HashMap<>();
      body.put("status", 404);
      body.put("error", "Not Found");
      body.put("message", ex.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

}

