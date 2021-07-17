/*
 * Copyright (C) 2013 Google, Inc.
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package github.tornaco.permission.compiler.common;

import com.google.common.annotations.Beta;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleElementVisitor6;

import static javax.lang.model.element.ElementKind.PACKAGE;

/**
 * Static utility methods pertaining to {@link Element} instances.
 *
 * @author Gregory Kick
 */
@Beta
public final class MoreElements {
    public static PackageElement getPackage(Element element) {
        while (element.getKind() != PACKAGE) {
            element = element.getEnclosingElement();
        }
        return (PackageElement) element;
    }

    private static final ElementVisitor<PackageElement, Void> PACKAGE_ELEMENT_VISITOR =
            new SimpleElementVisitor6<PackageElement, Void>() {
                @Override
                protected PackageElement defaultAction(Element e, Void p) {
                    throw new IllegalArgumentException();
                }

                @Override
                public PackageElement visitPackage(PackageElement e, Void p) {
                    return e;
                }
            };

    public static PackageElement asPackage(Element element) {
        return element.accept(PACKAGE_ELEMENT_VISITOR, null);
    }

    private static final ElementVisitor<TypeElement, Void> TYPE_ELEMENT_VISITOR =
            new SimpleElementVisitor6<TypeElement, Void>() {
                @Override
                protected TypeElement defaultAction(Element e, Void p) {
                    throw new IllegalArgumentException();
                }

                @Override
                public TypeElement visitType(TypeElement e, Void p) {
                    return e;
                }
            };

    public static boolean isType(Element element) {
        return element.getKind().isClass() || element.getKind().isInterface();
    }

    public static TypeElement asType(Element element) {
        return element.accept(TYPE_ELEMENT_VISITOR, null);
    }

    private static final ElementVisitor<VariableElement, Void> VARIABLE_ELEMENT_VISITOR =
            new SimpleElementVisitor6<VariableElement, Void>() {
                @Override
                protected VariableElement defaultAction(Element e, Void p) {
                    throw new IllegalArgumentException();
                }

                @Override
                public VariableElement visitVariable(VariableElement e, Void p) {
                    return e;
                }
            };

    public static VariableElement asVariable(Element element) {
        return element.accept(VARIABLE_ELEMENT_VISITOR, null);
    }

    private static final ElementVisitor<ExecutableElement, Void> EXECUTABLE_ELEMENT_VISITOR =
            new SimpleElementVisitor6<ExecutableElement, Void>() {
                @Override
                protected ExecutableElement defaultAction(Element e, Void p) {
                    throw new IllegalArgumentException();
                }

                @Override
                public ExecutableElement visitExecutable(ExecutableElement e, Void p) {
                    return e;
                }
            };

    public static ExecutableElement asExecutable(Element element) {
        return element.accept(EXECUTABLE_ELEMENT_VISITOR, null);
    }

    public static boolean isAnnotationPresent(Element element,
                                              Class<? extends Annotation> annotationClass) {
        return getAnnotationMirror(element, annotationClass).isPresent();
    }

    public static Optional<AnnotationMirror> getAnnotationMirror(Element element,
                                                                 Class<? extends Annotation> annotationClass) {
        String annotationClassName = annotationClass.getCanonicalName();
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            TypeElement annotationTypeElement = asType(annotationMirror.getAnnotationType().asElement());
            if (annotationTypeElement.getQualifiedName().contentEquals(annotationClassName)) {
                return Optional.of(annotationMirror);
            }
        }
        return Optional.absent();
    }

    public static Predicate<Element> hasModifiers(Modifier... modifiers) {
        return hasModifiers(ImmutableSet.copyOf(modifiers));
    }

    public static Predicate<Element> hasModifiers(final Set<Modifier> modifiers) {
        return new Predicate<Element>() {
            @Override
            public boolean apply(Element input) {
                return input.getModifiers().containsAll(modifiers);
            }
        };
    }

    public static ImmutableSet<ExecutableElement> getLocalAndInheritedMethods(
            TypeElement type, Elements elementUtils) {

        SetMultimap<String, ExecutableElement> methodMap = LinkedHashMultimap.create();
        getLocalAndInheritedMethods(getPackage(type), type, methodMap);
        // Find methods that are overridden. We do this using `Elements.overrides`, which means
        // that it is inherently a quadratic operation, since we have to compare every method against
        // every other method. We reduce the performance impact by (a) grouping methods by name, since
        // a method cannot override another method with a different name, and (b) making sure that
        // methods in ancestor types precede those in descendant types, which means we only have to
        // check a method against the ones that follow it in that order.
        Set<ExecutableElement> overridden = new LinkedHashSet<ExecutableElement>();
        for (String methodName : methodMap.keySet()) {
            List<ExecutableElement> methodList = ImmutableList.copyOf(methodMap.get(methodName));
            for (int i = 0; i < methodList.size(); i++) {
                ExecutableElement methodI = methodList.get(i);
                for (int j = i + 1; j < methodList.size(); j++) {
                    ExecutableElement methodJ = methodList.get(j);
                    if (elementUtils.overrides(methodJ, methodI, type)) {
                        overridden.add(methodI);
                    }
                }
            }
        }
        Set<ExecutableElement> methods = new LinkedHashSet<ExecutableElement>(methodMap.values());
        methods.removeAll(overridden);
        return ImmutableSet.copyOf(methods);
    }

    // Add to `methods` the instance methods from `type` that are visible to code in the
    // package `pkg`. This means all the instance methods from `type` itself and all instance methods
    // it inherits from its ancestors, except private methods and package-private methods in other
    // packages. This method does not take overriding into account, so it will add both an ancestor
    // method and a descendant method that overrides it.
    // `methods` is a multimap from a method name to all of the methods with that name, including
    // methods that override or overload one another. Within those methods, those in ancestor types
    // always precede those in descendant types.
    private static void getLocalAndInheritedMethods(
            PackageElement pkg, TypeElement type, SetMultimap<String, ExecutableElement> methods) {

        for (TypeMirror superInterface : type.getInterfaces()) {
            getLocalAndInheritedMethods(pkg, MoreTypes.asTypeElement(superInterface), methods);
        }
        if (type.getSuperclass().getKind() != TypeKind.NONE) {
            // Visit the superclass after superinterfaces so we will always see the implementation of a
            // method after any interfaces that declared it.
            getLocalAndInheritedMethods(pkg, MoreTypes.asTypeElement(type.getSuperclass()), methods);
        }
        for (ExecutableElement method : ElementFilter.methodsIn(type.getEnclosedElements())) {
            if (!method.getModifiers().contains(Modifier.STATIC)
                    && methodVisibleFromPackage(method, pkg)) {
                methods.put(method.getSimpleName().toString(), method);
            }
        }
    }

    private static boolean methodVisibleFromPackage(ExecutableElement method, PackageElement pkg) {
        // We use Visibility.ofElement rather than .effectiveVisibilityOfElement because it doesn't
        // really matter whether the containing class is visible. If you inherit a public method
        // then you have a public method, regardless of whether you inherit it from a public class.
        Visibility visibility = Visibility.ofElement(method);
        switch (visibility) {
            case PRIVATE:
                return false;
            case DEFAULT:
                return getPackage(method).equals(pkg);
            default:
                return true;
        }
    }

    private MoreElements() {
    }
}