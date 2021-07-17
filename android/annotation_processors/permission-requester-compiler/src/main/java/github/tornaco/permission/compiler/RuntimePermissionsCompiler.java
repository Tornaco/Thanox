package github.tornaco.permission.compiler;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.squareup.javapoet.*;
import github.tornaco.permission.compiler.common.Collections;
import github.tornaco.permission.compiler.common.*;
import github.tornaco.permission.requester.RequiresPermission;
import github.tornaco.permission.requester.RuntimePermissions;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static github.tornaco.permission.compiler.SourceFiles.writeSourceFile;
import static javax.lang.model.element.Modifier.*;

/**
 * Created by guohao4 on 2017/9/6.
 * Email: Tornaco@163.com
 */
@SupportedAnnotationTypes("github.tornaco.permission.requester.RuntimePermissions")
public class RuntimePermissionsCompiler extends AbstractProcessor {

    private static final AtomicInteger REQUEST_CODE = new AtomicInteger(0x999);

    private static final boolean DEBUG = true;

    private ErrorReporter mErrorReporter;
    private Types mTypes;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mErrorReporter = new ErrorReporter(processingEnvironment);
        mTypes = processingEnvironment.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Collection<? extends Element> annotatedElements =
                roundEnvironment.getElementsAnnotatedWith(RuntimePermissions.class);

        List<TypeElement> types = new ImmutableList.Builder<TypeElement>()
                .addAll(ElementFilter.typesIn(annotatedElements))
                .build();

        types.forEach(this::processType);

        return true;
    }

    private void processType(TypeElement type) {
        RuntimePermissions annotation = type.getAnnotation(RuntimePermissions.class);
        if (annotation == null) {
            mErrorReporter.abortWithError("@RuntimePermissions annotation is null on Type %s", type);
            return;
        }
        if (type.getKind() != ElementKind.CLASS) {
            mErrorReporter.abortWithError("@RuntimePermissions" + " only applies to class", type);
        }

        NestingKind nestingKind = type.getNestingKind();
        if (nestingKind != NestingKind.TOP_LEVEL) {
            mErrorReporter.abortWithError("@RuntimePermissions" + " only applies to top level class", type);
        }

        checkModifiersIfNested(type);

        // get the fully-qualified class name
        if (annotation.classNameSubFix().length() == 0) {
            mErrorReporter.abortWithError("classNameSubFix should not be empty", type);
        }
        String fqClassName = generatedSubclassName(type, 0, annotation.classNameSubFix());
        // class name
        String className = CompilerUtil.simpleNameOf(fqClassName);
        // Create source.
        String source = generateClass(type, className, type.getSimpleName().toString(), false);

        source = Reformatter.fixup(source);
        writeSourceFile(processingEnv, fqClassName, source, type);
    }

    private String generatedSubclassName(TypeElement type, int depth, String subFix) {
        return generatedClassName(type, null, Strings.repeat("$", depth) + subFix);
    }

    private String generatedClassName(TypeElement type, String prefix, String subFix) {
        String name = type.getSimpleName().toString();
        while (type.getEnclosingElement() instanceof TypeElement) {
            type = (TypeElement) type.getEnclosingElement();
            name = type.getSimpleName() + "_" + name;
        }
        String pkg = CompilerUtil.packageNameOf(type);
        String dot = Strings.isNullOrEmpty(pkg) ? "" : ".";
        String prefixChecked = Strings.isNullOrEmpty(prefix) ? "" : prefix;
        String subFixChecked = Strings.isNullOrEmpty(subFix) ? "" : subFix;
        return pkg + dot + prefixChecked + name + subFixChecked;
    }


    private String generateClass(TypeElement type, String className, String ifaceToImpl, boolean isFinal) {
        if (type == null) {
            mErrorReporter.abortWithError("generateClass was invoked with null type", null);
            return null;
        }
        if (className == null) {
            mErrorReporter.abortWithError("generateClass was invoked with null class name", type);
            return null;
        }
        if (ifaceToImpl == null) {
            mErrorReporter.abortWithError("generateClass was invoked with null iface", type);
            return null;
        }

        String pkg = CompilerUtil.packageNameOf(type);

        ClassName strClz = ClassName.get("java.lang", "Runnable");
        ClassName intClz = ClassName.get("java.lang", "Integer");
        ClassName mapClz = ClassName.get("java.util", "Map");
        TypeName mapOfString = ParameterizedTypeName.get(mapClz, intClz, strClz);
        FieldSpec onGrantMethodsMap = FieldSpec.builder(mapOfString, "ON_GRANT_METHODS_MAP")
                .addModifiers(Modifier.STATIC, Modifier.FINAL, Modifier.PRIVATE).build();

        FieldSpec onDenyMethodsMap = FieldSpec.builder(mapOfString, "ON_DENY_METHODS_MAP")
                .addModifiers(Modifier.STATIC, Modifier.FINAL, Modifier.PRIVATE).build();

        TypeSpec.Builder subClass = TypeSpec.classBuilder(className)
                .addField(boolean.class, "DEBUG", Modifier.STATIC, Modifier.FINAL, Modifier.PRIVATE)
                .addStaticBlock(CodeBlock.of("DEBUG = " + DEBUG + ";\n"))
                .addField(onGrantMethodsMap)
                .addField(onDenyMethodsMap)
                .addStaticBlock(CodeBlock.of("ON_GRANT_METHODS_MAP = new $T<>();\n", HashMap.class))
                .addStaticBlock(CodeBlock.of("ON_DENY_METHODS_MAP = new $T<>();\n", HashMap.class))
                .addMethods(createMethodSpecs(type));

        // Add type params.
        List l = type.getTypeParameters();
        Collections.consumeRemaining(l, o -> {
            TypeParameterElement typeParameterElement = (TypeParameterElement) o;
            subClass.addTypeVariable(TypeVariableName.get(typeParameterElement.toString()));
        });

        if (isFinal) subClass.addModifiers(FINAL);

        JavaFile javaFile = JavaFile.builder(pkg, subClass.build())
                .addFileComment(SettingsProvider.FILE_COMMENT)
                .skipJavaLangImports(true)
                .build();
        return javaFile.toString();
    }

    private Iterable<MethodSpec> createMethodSpecs(TypeElement typeElement) {
        List<MethodSpec> methodSpecs = new ArrayList<>();

        methodSpecs.add(createOnPermissionRequestResultMethod());

        methodSpecs.add(MethodSpec.methodBuilder("checkSelfPermissions")
                .addParameter(ClassName.get("android.content", "Context"), "context")
                .addParameter(String[].class, "perms")
                .addModifiers(Modifier.STATIC)
                .returns(TypeName.BOOLEAN)
                .addCode("for (String p : perms) {\n" +
                        "            if (androidx.core.app.ActivityCompat.checkSelfPermission(context, p)\n" +
                        "                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {\n" +
                        "                return false;\n" +
                        "            }\n" +
                        "        }\n" +
                        "        return true;\n")
                .build());

        List<? extends Element> enclosedElements = typeElement.getEnclosedElements();

        enclosedElements.stream().filter(e -> e.getKind() == ElementKind.METHOD).forEach(e -> {
            RequiresPermission requiresPermission = e.getAnnotation(RequiresPermission.class);
            RequiresPermission.Before before = e.getAnnotation(RequiresPermission.Before.class);
            RequiresPermission.OnDenied onDenied = e.getAnnotation(RequiresPermission.OnDenied.class);
            if (requiresPermission != null) {
                Logger.debug("RequiresPermission found @:" + e);
                try {
                    methodSpecs.add(createMethodForRequiresPermission(typeElement, e,
                            before,
                            onDenied,
                            requiresPermission));
                } catch (Throwable t) {
//                    StringBuilder stacks = new StringBuilder();
//                    for (StackTraceElement se : t.getStackTrace()) {
//                        stacks.append(se.toString()).append("\n");
//                    }
                    mErrorReporter.abortWithError(t.getLocalizedMessage(), e);
                }
            }
        });
        return methodSpecs;
    }

    private MethodSpec createOnPermissionRequestResultMethod() {
        MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("onRequestPermissionsResult")
                .addParameter(TypeName.INT, "requestCode")
                .addParameter(String[].class, "permissions")
                .addParameter(int[].class, "grantResults")
                .addStatement("if (!ON_GRANT_METHODS_MAP.containsKey(requestCode)) return")
                .addStatement("$T<String> permissionsNotGrantList = new $T<String>(permissions.length)",
                        List.class, ArrayList.class)
                .beginControlFlow(" for (int i = 0; i < grantResults.length; i++)")
                .beginControlFlow("if (grantResults[i] != android.content.pm.PackageManager.PERMISSION_GRANTED)")
                .addStatement("permissionsNotGrantList.add(permissions[i])")
                .endControlFlow()
                .endControlFlow()
                .beginControlFlow(" if (permissionsNotGrantList.size() == 0)")
                .addCode("// Now call his method.\n")
                .addStatement("Runnable r = ON_GRANT_METHODS_MAP.remove(requestCode)")
                .addStatement(" if (r != null) r.run()")
                .addStatement("return")
                .endControlFlow()
                .addStatement("Runnable r2 = ON_DENY_METHODS_MAP.remove(requestCode)")
                .addStatement(" if (r2 != null) r2.run()")
                .addModifiers(Modifier.STATIC)
                .addModifiers(Modifier.FINAL);
        return methodSpecBuilder.build();
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    private MethodSpec createMethodForRequiresPermission(TypeElement typeElement, Element e,
                                                         RequiresPermission.Before before,
                                                         RequiresPermission.OnDenied onDenied,
                                                         RequiresPermission requiresPermission)
            throws ClassNotFoundException {

        String methodName = e.getSimpleName().toString();

        // Retrieve all params.
        List<ParameterSpec> parameterSpecs = new ArrayList<>();
        ExecutableElement exe = MoreElements.asExecutable(e);
        List<? extends VariableElement> varList = exe.getParameters();
        for (VariableElement ve : varList) {
            TypeMirror tm = ve.asType();
            TypeName tn = TypeName.get(tm);
            ParameterSpec ps = ParameterSpec.builder(tn, ve.toString(), FINAL).build();
            parameterSpecs.add(ps);
        }

        // Method name.
        methodName += requiresPermission.methodSubFix();

        // Permissions var.
        String[] perms = requiresPermission.value();
        StringBuilder permStatement = new StringBuilder("String permissions[] = new String[]{");
        for (String ignored : perms) {
            permStatement.append("$S").append(", ");
        }
        permStatement.append("}");
        Object[] permArgs = perms;

        int requestCode = REQUEST_CODE.incrementAndGet();

        String methodToCallInRunnable = e.getSimpleName().toString();
        StringBuilder onGrantPassingArgs = new StringBuilder("host." + methodToCallInRunnable + "(");
        for (int i = 0; i < parameterSpecs.size(); i++) {
            ParameterSpec parameterSpec = parameterSpecs.get(i);
            if (i != parameterSpecs.size() - 1)
                onGrantPassingArgs.append(parameterSpec.name).append(", ");
            else onGrantPassingArgs.append(parameterSpec.name);
        }
        onGrantPassingArgs.append(");");

        // OnBefore.
        String onBeforeMethodName = null;
        if (before != null) onBeforeMethodName = before.value();
        String onBeforeCode = before == null ? ""
                : "host." + onBeforeMethodName + "();\n";

        // OnDenied.
        String onDeniedMethodName = null;
        if (onDenied != null) onDeniedMethodName = onDenied.value();
        String onDeniedCode = onDenied == null ? ""
                :
                "Runnable r2 = new Runnable() {\n" +
                        "            @Override\n" +
                        "            public void run() {\n" +
                        "                host." + onDeniedMethodName + "();\n" +
                        "            }\n" +
                        "        };\n" +
                        "ON_DENY_METHODS_MAP.put(code, r2);\n";

        // Check host is Activity or Fragment.
        boolean isActivity;
        boolean isFragment;

        isActivity = MoreTypes.isTargetType(typeElement, "android.app.Activity");
        isFragment = MoreTypes.isTargetType(typeElement, "android.app.Fragment")
                || MoreTypes.isTargetType(typeElement, "androidx.fragment.app.Fragment");

        if (!isActivity && !isFragment) {
            mErrorReporter.abortWithError("Only Activity or Fragment is accepted", typeElement);
        }

        MethodSpec.Builder methodSpecBuilder =
                MethodSpec.methodBuilder(methodName)
                        .addParameters(parameterSpecs)
                        .addParameter(ClassName.bestGuess(typeElement.getQualifiedName().toString()), "host", FINAL)
                        .addStatement(permStatement.toString(), permArgs)
                        .addCode(String.format("if (checkSelfPermissions(%s, permissions))" +
                                        "{" +
                                        "" + onGrantPassingArgs.toString() + "\n" +
                                        " return;}\n",
                                isActivity ? "host" : "host.getActivity()"))
                        .addCode(onBeforeCode)
                        .addStatement("int code = $L", requestCode)

                        .addCode("Runnable r = new Runnable() {\n" +
                                "            @Override\n" +
                                "            public void run() {\n" +
                                "                " +
                                onGrantPassingArgs.toString() + "\n" +
                                "            }\n" +
                                "        };\n")
                        .addStatement("ON_GRANT_METHODS_MAP.put(code, r)")
                        .addCode(onDeniedCode)
                        .addStatement(
                                isActivity ?
                                        "androidx.core.app.ActivityCompat.requestPermissions(host, permissions, code)"
                                        : "host.requestPermissions(permissions, code)")
                        .addModifiers(Modifier.STATIC)
                        .addModifiers(Modifier.FINAL);
        return methodSpecBuilder.build();
    }

    private void checkModifiersIfNested(TypeElement type) {
        ElementKind enclosingKind = type.getEnclosingElement().getKind();
        if (enclosingKind.isClass() || enclosingKind.isInterface()) {
            if (type.getModifiers().contains(PRIVATE)) {
                mErrorReporter.abortWithError("@RuntimePermissions class must not be private", type);
            }
            if (!type.getModifiers().contains(STATIC)) {
                mErrorReporter.abortWithError("Nested @RuntimePermissions class must be static", type);
            }
        }
        // In principle type.getEnclosingElement() could be an ExecutableElement (for a class
        // declared inside a method), but since RoundEnvironment.getElementsAnnotatedWith doesn't
        // return such classes we won't see them here.
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}

