package com.opensymphony.able.apt;

import static com.sun.mirror.util.DeclarationVisitors.NO_OP;
import static com.sun.mirror.util.DeclarationVisitors.getDeclarationScanner;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableCollection;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/*
 */
public class AbleAnnotationProcessorFactory implements AnnotationProcessorFactory {
    // Process any set of annotations
    private static final Collection<String> supportedAnnotations = unmodifiableCollection(Arrays.asList("javax.persistence.Entity"));

    // No supported options
    private static final Collection<String> supportedOptions = emptySet();

    public Collection<String> supportedAnnotationTypes() {
        return supportedAnnotations;
    }

    public Collection<String> supportedOptions() {
        return supportedOptions;
    }

    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> atds, AnnotationProcessorEnvironment env) {
        return new EntityAnnotationProcessor(env);
    }

    private static class EntityAnnotationProcessor implements AnnotationProcessor {
        private final AnnotationProcessorEnvironment env;

        EntityAnnotationProcessor(AnnotationProcessorEnvironment env) {
            this.env = env;
        }

        public void process() {
            for (TypeDeclaration typeDecl : env.getSpecifiedTypeDeclarations())
                typeDecl.accept(getDeclarationScanner(new VelocityClassVisitor(env), NO_OP));
        }

    }
}
