package com.github.jcba.poimapper;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

record AnnotatedFieldData(
    List<Annotation> annotations,
    Object value,
    String fieldName
){
    <A> Optional<A> findAnnotation(Class<A> type) {
        return annotations.stream()
                .filter(a -> a.annotationType().equals(type))
                .map(type::cast)
                .findFirst();
    }
}