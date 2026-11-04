package com.vitalink.platform.shared.infrastructure.configuration;

import org.springframework.http.*;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class PersistenceConflictHandler {
  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  ResponseEntity<ProblemDetail> conflict(ObjectOptimisticLockingFailureException error) {
    return ResponseEntity.status(409)
        .body(
            ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, "Los datos cambiaron. Actualiza antes de continuar."));
  }
}
