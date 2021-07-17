package github.tornaco.notro.gradle.plugin.core

import java.nio.file.FileVisitResult
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

class ClassFileVisitor extends SimpleFileVisitor<Path> {

    def baseDir

    @Override
    FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String path = file.toString()
        if (path.endsWith('.class')
                && !path.contains(File.separator + 'R$')
                && !path.endsWith(File.separator + 'R.class')) {

            def index = baseDir.length() + 1
            def className = path.substring(index).replace('\\', '.').replace('/', '.').replace('.class', '')

            CommonData.putClassAndPath(className, baseDir)
        }
        return super.visitFile(file, attrs)
    }
}
