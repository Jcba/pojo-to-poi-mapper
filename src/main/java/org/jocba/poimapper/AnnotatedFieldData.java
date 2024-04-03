package org.jocba.poimapper;

import java.lang.annotation.Annotation;
import java.util.List;

record AnnotatedFieldData(
    List<Annotation> annotations,
    Object value,
    String fieldName
){}