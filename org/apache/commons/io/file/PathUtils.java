// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.file;

import java.net.URI;
import java.nio.file.Paths;
import java.nio.file.FileVisitor;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.DosFileAttributeView;
import java.util.stream.Stream;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import java.util.Collection;
import java.nio.file.DirectoryStream;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.AclEntry;
import org.apache.commons.io.IOUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.nio.file.NoSuchFileException;
import java.io.InputStream;
import java.nio.file.Files;
import java.net.URL;
import java.nio.file.CopyOption;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.OpenOption;
import java.nio.file.LinkOption;
import java.nio.file.FileVisitOption;

public final class PathUtils
{
    public static final DeleteOption[] EMPTY_DELETE_OPTION_ARRAY;
    public static final FileVisitOption[] EMPTY_FILE_VISIT_OPTION_ARRAY;
    public static final LinkOption[] EMPTY_LINK_OPTION_ARRAY;
    public static final OpenOption[] EMPTY_OPEN_OPTION_ARRAY;
    
    private static AccumulatorPathVisitor accumulate(final Path directory, final int maxDepth, final FileVisitOption[] fileVisitOptions) throws IOException {
        return visitFileTree(AccumulatorPathVisitor.withLongCounters(), directory, toFileVisitOptionSet(fileVisitOptions), maxDepth);
    }
    
    public static Counters.PathCounters cleanDirectory(final Path directory) throws IOException {
        return cleanDirectory(directory, PathUtils.EMPTY_DELETE_OPTION_ARRAY);
    }
    
    public static Counters.PathCounters cleanDirectory(final Path directory, final DeleteOption... options) throws IOException {
        return visitFileTree(new CleaningPathVisitor(Counters.longPathCounters(), options, new String[0]), directory).getPathCounters();
    }
    
    public static Counters.PathCounters copyDirectory(final Path sourceDirectory, final Path targetDirectory, final CopyOption... copyOptions) throws IOException {
        return visitFileTree(new CopyDirectoryVisitor(Counters.longPathCounters(), sourceDirectory, targetDirectory, copyOptions), sourceDirectory).getPathCounters();
    }
    
    public static Path copyFile(final URL sourceFile, final Path targetFile, final CopyOption... copyOptions) throws IOException {
        try (final InputStream inputStream = sourceFile.openStream()) {
            Files.copy(inputStream, targetFile, copyOptions);
            return targetFile;
        }
    }
    
    public static Path copyFileToDirectory(final Path sourceFile, final Path targetDirectory, final CopyOption... copyOptions) throws IOException {
        return Files.copy(sourceFile, targetDirectory.resolve(sourceFile.getFileName()), copyOptions);
    }
    
    public static Path copyFileToDirectory(final URL sourceFile, final Path targetDirectory, final CopyOption... copyOptions) throws IOException {
        try (final InputStream inputStream = sourceFile.openStream()) {
            Files.copy(inputStream, targetDirectory.resolve(sourceFile.getFile()), copyOptions);
            return targetDirectory;
        }
    }
    
    public static Counters.PathCounters countDirectory(final Path directory) throws IOException {
        return visitFileTree(new CountingPathVisitor(Counters.longPathCounters()), directory).getPathCounters();
    }
    
    public static Counters.PathCounters delete(final Path path) throws IOException {
        return delete(path, PathUtils.EMPTY_DELETE_OPTION_ARRAY);
    }
    
    public static Counters.PathCounters delete(final Path path, final DeleteOption... options) throws IOException {
        return Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS) ? deleteDirectory(path, options) : deleteFile(path, options);
    }
    
    public static Counters.PathCounters deleteDirectory(final Path directory) throws IOException {
        return deleteDirectory(directory, PathUtils.EMPTY_DELETE_OPTION_ARRAY);
    }
    
    public static Counters.PathCounters deleteDirectory(final Path directory, final DeleteOption... options) throws IOException {
        return visitFileTree(new DeletingPathVisitor(Counters.longPathCounters(), options, new String[0]), directory).getPathCounters();
    }
    
    public static Counters.PathCounters deleteFile(final Path file) throws IOException {
        return deleteFile(file, PathUtils.EMPTY_DELETE_OPTION_ARRAY);
    }
    
    public static Counters.PathCounters deleteFile(final Path file, final DeleteOption... options) throws IOException {
        if (Files.isDirectory(file, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchFileException(file.toString());
        }
        final Counters.PathCounters pathCounts = Counters.longPathCounters();
        final boolean exists = Files.exists(file, LinkOption.NOFOLLOW_LINKS);
        final long size = exists ? Files.size(file) : 0L;
        if (overrideReadOnly(options) && exists) {
            setReadOnly(file, false, LinkOption.NOFOLLOW_LINKS);
        }
        if (Files.deleteIfExists(file)) {
            pathCounts.getFileCounter().increment();
            pathCounts.getByteCounter().add(size);
        }
        return pathCounts;
    }
    
    private static boolean overrideReadOnly(final DeleteOption[] options) {
        if (options == null) {
            return false;
        }
        for (final DeleteOption deleteOption : options) {
            if (deleteOption == StandardDeleteOption.OVERRIDE_READ_ONLY) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean directoryAndFileContentEquals(final Path path1, final Path path2) throws IOException {
        return directoryAndFileContentEquals(path1, path2, PathUtils.EMPTY_LINK_OPTION_ARRAY, PathUtils.EMPTY_OPEN_OPTION_ARRAY, PathUtils.EMPTY_FILE_VISIT_OPTION_ARRAY);
    }
    
    public static boolean directoryAndFileContentEquals(final Path path1, final Path path2, final LinkOption[] linkOptions, final OpenOption[] openOptions, final FileVisitOption[] fileVisitOption) throws IOException {
        if (path1 == null && path2 == null) {
            return true;
        }
        if (path1 == null ^ path2 == null) {
            return false;
        }
        if (!Files.exists(path1, new LinkOption[0]) && !Files.exists(path2, new LinkOption[0])) {
            return true;
        }
        final RelativeSortedPaths relativeSortedPaths = new RelativeSortedPaths(path1, path2, Integer.MAX_VALUE, linkOptions, fileVisitOption);
        if (!relativeSortedPaths.equals) {
            return false;
        }
        final List<Path> fileList1 = relativeSortedPaths.relativeFileList1;
        final List<Path> fileList2 = relativeSortedPaths.relativeFileList2;
        for (final Path path3 : fileList1) {
            final int binarySearch = Collections.binarySearch(fileList2, path3);
            if (binarySearch <= -1) {
                throw new IllegalStateException("Unexpected mismatch.");
            }
            if (!fileContentEquals(path1.resolve(path3), path2.resolve(path3), linkOptions, openOptions)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean directoryContentEquals(final Path path1, final Path path2) throws IOException {
        return directoryContentEquals(path1, path2, Integer.MAX_VALUE, PathUtils.EMPTY_LINK_OPTION_ARRAY, PathUtils.EMPTY_FILE_VISIT_OPTION_ARRAY);
    }
    
    public static boolean directoryContentEquals(final Path path1, final Path path2, final int maxDepth, final LinkOption[] linkOptions, final FileVisitOption[] fileVisitOptions) throws IOException {
        return new RelativeSortedPaths(path1, path2, maxDepth, linkOptions, fileVisitOptions).equals;
    }
    
    public static boolean fileContentEquals(final Path path1, final Path path2) throws IOException {
        return fileContentEquals(path1, path2, PathUtils.EMPTY_LINK_OPTION_ARRAY, PathUtils.EMPTY_OPEN_OPTION_ARRAY);
    }
    
    public static boolean fileContentEquals(final Path path1, final Path path2, final LinkOption[] linkOptions, final OpenOption[] openOptions) throws IOException {
        if (path1 == null && path2 == null) {
            return true;
        }
        if (path1 == null ^ path2 == null) {
            return false;
        }
        final Path nPath1 = path1.normalize();
        final Path nPath2 = path2.normalize();
        final boolean path1Exists = Files.exists(nPath1, linkOptions);
        if (path1Exists != Files.exists(nPath2, linkOptions)) {
            return false;
        }
        if (!path1Exists) {
            return true;
        }
        if (Files.isDirectory(nPath1, linkOptions)) {
            throw new IOException("Can't compare directories, only files: " + nPath1);
        }
        if (Files.isDirectory(nPath2, linkOptions)) {
            throw new IOException("Can't compare directories, only files: " + nPath2);
        }
        if (Files.size(nPath1) != Files.size(nPath2)) {
            return false;
        }
        if (path1.equals(path2)) {
            return true;
        }
        try (final InputStream inputStream1 = Files.newInputStream(nPath1, openOptions);
             final InputStream inputStream2 = Files.newInputStream(nPath2, openOptions)) {
            return IOUtils.contentEquals(inputStream1, inputStream2);
        }
    }
    
    public static List<AclEntry> getAclEntryList(final Path sourcePath) throws IOException {
        final AclFileAttributeView fileAttributeView = Files.getFileAttributeView(sourcePath, AclFileAttributeView.class, new LinkOption[0]);
        return (fileAttributeView == null) ? null : fileAttributeView.getAcl();
    }
    
    public static boolean isEmpty(final Path path) throws IOException {
        return Files.isDirectory(path, new LinkOption[0]) ? isEmptyDirectory(path) : isEmptyFile(path);
    }
    
    public static boolean isEmptyDirectory(final Path directory) throws IOException {
        try (final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)) {
            if (directoryStream.iterator().hasNext()) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isEmptyFile(final Path file) throws IOException {
        return Files.size(file) <= 0L;
    }
    
    static List<Path> relativize(final Collection<Path> collection, final Path parent, final boolean sort, final Comparator<? super Path> comparator) {
        Stream<Path> stream = collection.stream().map((Function<? super Path, ? extends Path>)parent::relativize);
        if (sort) {
            stream = ((comparator == null) ? stream.sorted() : stream.sorted(comparator));
        }
        return stream.collect((Collector<? super Path, ?, List<Path>>)Collectors.toList());
    }
    
    public static Path setReadOnly(final Path path, final boolean readOnly, final LinkOption... options) throws IOException {
        final DosFileAttributeView fileAttributeView = Files.getFileAttributeView(path, DosFileAttributeView.class, options);
        if (fileAttributeView != null) {
            fileAttributeView.setReadOnly(readOnly);
            return path;
        }
        final PosixFileAttributeView posixFileAttributeView = Files.getFileAttributeView(path, PosixFileAttributeView.class, options);
        if (posixFileAttributeView != null) {
            final PosixFileAttributes readAttributes = posixFileAttributeView.readAttributes();
            final Set<PosixFilePermission> permissions = readAttributes.permissions();
            permissions.remove(PosixFilePermission.OWNER_WRITE);
            permissions.remove(PosixFilePermission.GROUP_WRITE);
            permissions.remove(PosixFilePermission.OTHERS_WRITE);
            return Files.setPosixFilePermissions(path, permissions);
        }
        throw new IOException("No DosFileAttributeView or PosixFileAttributeView for " + path);
    }
    
    static Set<FileVisitOption> toFileVisitOptionSet(final FileVisitOption... fileVisitOptions) {
        return (Set<FileVisitOption>)((fileVisitOptions == null) ? EnumSet.noneOf(FileVisitOption.class) : Arrays.stream(fileVisitOptions).collect((Collector<? super FileVisitOption, ?, Set<? super FileVisitOption>>)Collectors.toSet()));
    }
    
    public static <T extends FileVisitor<? super Path>> T visitFileTree(final T visitor, final Path directory) throws IOException {
        Files.walkFileTree(directory, visitor);
        return visitor;
    }
    
    public static <T extends FileVisitor<? super Path>> T visitFileTree(final T visitor, final Path start, final Set<FileVisitOption> options, final int maxDepth) throws IOException {
        Files.walkFileTree(start, options, maxDepth, visitor);
        return visitor;
    }
    
    public static <T extends FileVisitor<? super Path>> T visitFileTree(final T visitor, final String first, final String... more) throws IOException {
        return visitFileTree(visitor, Paths.get(first, more));
    }
    
    public static <T extends FileVisitor<? super Path>> T visitFileTree(final T visitor, final URI uri) throws IOException {
        return visitFileTree(visitor, Paths.get(uri));
    }
    
    private PathUtils() {
    }
    
    static {
        EMPTY_DELETE_OPTION_ARRAY = new DeleteOption[0];
        EMPTY_FILE_VISIT_OPTION_ARRAY = new FileVisitOption[0];
        EMPTY_LINK_OPTION_ARRAY = new LinkOption[0];
        EMPTY_OPEN_OPTION_ARRAY = new OpenOption[0];
    }
    
    private static class RelativeSortedPaths
    {
        final boolean equals;
        final List<Path> relativeFileList1;
        final List<Path> relativeFileList2;
        
        private RelativeSortedPaths(final Path dir1, final Path dir2, final int maxDepth, final LinkOption[] linkOptions, final FileVisitOption[] fileVisitOptions) throws IOException {
            List<Path> tmpRelativeDirList1 = null;
            List<Path> tmpRelativeDirList2 = null;
            List<Path> tmpRelativeFileList1 = null;
            List<Path> tmpRelativeFileList2 = null;
            if (dir1 == null && dir2 == null) {
                this.equals = true;
            }
            else if (dir1 == null ^ dir2 == null) {
                this.equals = false;
            }
            else {
                final boolean parentDirExists1 = Files.exists(dir1, linkOptions);
                final boolean parentDirExists2 = Files.exists(dir2, linkOptions);
                if (!parentDirExists1 || !parentDirExists2) {
                    this.equals = (!parentDirExists1 && !parentDirExists2);
                }
                else {
                    final AccumulatorPathVisitor visitor1 = accumulate(dir1, maxDepth, fileVisitOptions);
                    final AccumulatorPathVisitor visitor2 = accumulate(dir2, maxDepth, fileVisitOptions);
                    if (visitor1.getDirList().size() != visitor2.getDirList().size() || visitor1.getFileList().size() != visitor2.getFileList().size()) {
                        this.equals = false;
                    }
                    else {
                        tmpRelativeDirList1 = visitor1.relativizeDirectories(dir1, true, null);
                        tmpRelativeDirList2 = visitor2.relativizeDirectories(dir2, true, null);
                        if (!tmpRelativeDirList1.equals(tmpRelativeDirList2)) {
                            this.equals = false;
                        }
                        else {
                            tmpRelativeFileList1 = visitor1.relativizeFiles(dir1, true, null);
                            tmpRelativeFileList2 = visitor2.relativizeFiles(dir2, true, null);
                            this.equals = tmpRelativeFileList1.equals(tmpRelativeFileList2);
                        }
                    }
                }
            }
            this.relativeFileList1 = tmpRelativeFileList1;
            this.relativeFileList2 = tmpRelativeFileList2;
        }
    }
}
