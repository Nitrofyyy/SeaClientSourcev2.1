// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.FileVisitResult;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.ArrayList;
import java.nio.file.Path;
import java.util.List;

public class AccumulatorPathVisitor extends CountingPathVisitor
{
    private final List<Path> dirList;
    private final List<Path> fileList;
    
    public static AccumulatorPathVisitor withBigIntegerCounters() {
        return new AccumulatorPathVisitor(Counters.bigIntegerPathCounters());
    }
    
    public static AccumulatorPathVisitor withLongCounters() {
        return new AccumulatorPathVisitor(Counters.longPathCounters());
    }
    
    public AccumulatorPathVisitor(final Counters.PathCounters pathCounter) {
        super(pathCounter);
        this.dirList = new ArrayList<Path>();
        this.fileList = new ArrayList<Path>();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof AccumulatorPathVisitor)) {
            return false;
        }
        final AccumulatorPathVisitor other = (AccumulatorPathVisitor)obj;
        return Objects.equals(this.dirList, other.dirList) && Objects.equals(this.fileList, other.fileList);
    }
    
    public List<Path> getDirList() {
        return this.dirList;
    }
    
    public List<Path> getFileList() {
        return this.fileList;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = 31 * result + Objects.hash(this.dirList, this.fileList);
        return result;
    }
    
    public List<Path> relativizeDirectories(final Path parent, final boolean sort, final Comparator<? super Path> comparator) {
        return PathUtils.relativize(this.getDirList(), parent, sort, comparator);
    }
    
    public List<Path> relativizeFiles(final Path parent, final boolean sort, final Comparator<? super Path> comparator) {
        return PathUtils.relativize(this.getFileList(), parent, sort, comparator);
    }
    
    @Override
    public FileVisitResult visitFile(final Path file, final BasicFileAttributes attributes) throws IOException {
        (Files.isDirectory(file, new LinkOption[0]) ? this.dirList : this.fileList).add(file.normalize());
        return super.visitFile(file, attributes);
    }
}
