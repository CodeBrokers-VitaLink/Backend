package com.vitalink.platform.care.interfaces.rest.resources;

public record MeasurementRangeResource(
    String id,
    String unit,
    Double normalMin,
    Double normalMax,
    Double dangerBelow,
    Double dangerFrom,
    Double dangerAtOrBelow,
    NumericRangeResource systolic,
    NumericRangeResource diastolic) {}
