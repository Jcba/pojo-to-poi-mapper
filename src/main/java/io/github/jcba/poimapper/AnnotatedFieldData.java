package io.github.jcba.poimapper;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

record AnnotatedFieldData(
    List<Annotation> annotations,
    Object value
){
    <A> Optional<A> findAnnotation(Class<A> type) {
        return annotations.stream()
                .filter(a -> a.annotationType().equals(type))
                .map(type::cast)
                .findFirst();
    }
}