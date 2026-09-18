package com.guicedee.activitymaster.test;

import org.junit.jupiter.api.Test;

import java.lang.classfile.ClassFile;
import java.lang.classfile.attribute.SignatureAttribute;
import java.lang.reflect.AccessFlag;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicSessionApiTest {
    @Test
    void compiledActivityMasterDeclarationsDoNotExposeStatefulSessions() throws Exception {
        var violations = new ArrayList<String>();
        int inspected = 0;
        // Include feature modules when they have been built in the shared workspace.
        for (String module : List.of("client", "core", "cerial-client", "cerial", "profiles",
                "user-sessions", "geography", "images", "mail")) {
            Path classes = Path.of("..", module, "target", "classes", "com", "guicedee", "activitymaster");
            if (!Files.isDirectory(classes)) continue;
            try (var paths = Files.walk(classes)) {
                for (Path file : paths.filter(p -> p.toString().endsWith(".class")).toList()) {
                    var model = ClassFile.of().parse(file);
                    for (var method : model.methods()) {
                        if (!method.flags().has(AccessFlag.PUBLIC)) continue;
                        inspected++;
                        String signature = method.methodType().stringValue();
                        for (var attribute : method.attributes()) {
                            if (attribute instanceof SignatureAttribute generic) {
                                signature += generic.signature().stringValue();
                            }
                        }
                        if (signature.contains("Lorg/hibernate/reactive/mutiny/Mutiny$Session;")) {
                            violations.add(module + ": " + model.thisClass().asInternalName()
                                    + "#" + method.methodName().stringValue() + signature);
                        }
                    }
                }
            }
        }
        assertTrue(inspected > 0, "No compiled ActivityMaster APIs were inspected");
        assertTrue(violations.isEmpty(), () -> String.join("\n", violations));
    }
}
