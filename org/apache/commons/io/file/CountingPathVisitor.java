// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.file;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.FileVisitResult;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class CountingPathVisitor extends SimplePathVisitor
{
    static final String[] EMPTY_STRING_ARRAY;
    private final Counters.PathCounters pathCounters;
    
    public static CountingPathVisitor withBigIntegerCounters() {
        return new CountingPathVisitor(Counters.bigIntegerPathCounters());
    }
    
    public static CountingPathVisitor withLongCounters() {
        return new CountingPathVisitor(Counters.longPathCounters());
    }
    
    public CountingPathVisitor(final Counters.PathCounters pathCounter) {
        this.pathCounters = Objects.requireNonNull(pathCounter, "pathCounter");
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CountingPathVisitor)) {
            return false;
        }
        final CountingPathVisitor other = (CountingPathVisitor)obj;
        return Objects.equals(this.pathCounters, other.pathCounters);
    }
    
    public Counters.PathCounters getPathCounters() {
        return this.pathCounters;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.pathCounters);
    }
    
    @Override
    public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
        this.pathCounters.getDirectoryCounter().increment();
        return FileVisitResult.CONTINUE;
    }
    
    @Override
    public String toString() {
        return this.pathCounters.toString();
    }
    
    protected void updateFileCounters(final Path file, final BasicFileAttributes attributes) {
        this.pathCounters.getFileCounter().increment();
        this.pathCounters.getByteCounter().add(attributes.size());
    }
    
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attributes) throws IOException {
        if (Files.exists(file, new LinkOption[0])) {
            this.updateFileCounters(file, attributes);
        }
        return FileVisitResult.CONTINUE;
    }
    
    static {
        EMPTY_STRING_ARRAY = new String[0];
    }
}
