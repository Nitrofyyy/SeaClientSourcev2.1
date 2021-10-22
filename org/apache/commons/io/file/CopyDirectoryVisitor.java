// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.file;

import java.nio.file.attribute.FileAttribute;
import java.nio.file.LinkOption;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.Arrays;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.CopyOption;

public class CopyDirectoryVisitor extends CountingPathVisitor
{
    private static final CopyOption[] EMPTY_COPY_OPTIONS;
    private final CopyOption[] copyOptions;
    private final Path sourceDirectory;
    private final Path targetDirectory;
    
    public CopyDirectoryVisitor(final Counters.PathCounters pathCounter, final Path sourceDirectory, final Path targetDirectory, final CopyOption... copyOptions) {
        super(pathCounter);
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
        this.copyOptions = ((copyOptions == null) ? CopyDirectoryVisitor.EMPTY_COPY_OPTIONS : copyOptions.clone());
    }
    
    protected void copy(final Path sourceFile, final Path targetFile) throws IOException {
        Files.copy(sourceFile, targetFile, this.copyOptions);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final CopyDirectoryVisitor other = (CopyDirectoryVisitor)obj;
        return Arrays.equals(this.copyOptions, other.copyOptions) && Objects.equals(this.sourceDirectory, other.sourceDirectory) && Objects.equals(this.targetDirectory, other.targetDirectory);
    }
    
    public CopyOption[] getCopyOptions() {
        return this.copyOptions.clone();
    }
    
    public Path getSourceDirectory() {
        return this.sourceDirectory;
    }
    
    public Path getTargetDirectory() {
        return this.targetDirectory;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(this.copyOptions);
        result = 31 * result + Objects.hash(this.sourceDirectory, this.targetDirectory);
        return result;
    }
    
    @Override
    public FileVisitResult preVisitDirectory(final Path directory, final BasicFileAttributes attributes) throws IOException {
        final Path newTargetDir = this.targetDirectory.resolve(this.sourceDirectory.relativize(directory));
        if (Files.notExists(newTargetDir, new LinkOption[0])) {
            Files.createDirectory(newTargetDir, (FileAttribute<?>[])new FileAttribute[0]);
        }
        return super.preVisitDirectory(directory, attributes);
    }
    
    @Override
    public FileVisitResult visitFile(final Path sourceFile, final BasicFileAttributes attributes) throws IOException {
        final Path targetFile = this.targetDirectory.resolve(this.sourceDirectory.relativize(sourceFile));
        this.copy(sourceFile, targetFile);
        return super.visitFile(targetFile, attributes);
    }
    
    static {
        EMPTY_COPY_OPTIONS = new CopyOption[0];
    }
}
