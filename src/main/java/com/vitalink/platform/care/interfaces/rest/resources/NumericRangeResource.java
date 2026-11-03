package com.vitalink.platform.care.interfaces.rest.resources;

public record NumericRangeResource(
    Double normalMin,
    Double normalMax,
    Double dangerBelow,
    Double dangerFrom,
    Double dangerAtOrBelow) {}
