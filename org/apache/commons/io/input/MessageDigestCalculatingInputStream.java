// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.io.InputStream;
import java.security.MessageDigest;

public class MessageDigestCalculatingInputStream extends ObservableInputStream
{
    private final MessageDigest messageDigest;
    
    public MessageDigestCalculatingInputStream(final InputStream inputStream, final MessageDigest MessageDigest) {
        super(inputStream);
        this.messageDigest = MessageDigest;
        this.add(new MessageDigestMaintainingObserver(MessageDigest));
    }
    
    public MessageDigestCalculatingInputStream(final InputStream inputStream, final String algorithm) throws NoSuchAlgorithmException {
        this(inputStream, MessageDigest.getInstance(algorithm));
    }
    
    public MessageDigestCalculatingInputStream(final InputStream inputStream) throws NoSuchAlgorithmException {
        this(inputStream, MessageDigest.getInstance("MD5"));
    }
    
    public MessageDigest getMessageDigest() {
        return this.messageDigest;
    }
    
    public static class MessageDigestMaintainingObserver extends Observer
    {
        private final MessageDigest messageDigest;
        
        public MessageDigestMaintainingObserver(final MessageDigest messageDigest) {
            this.messageDigest = messageDigest;
        }
        
        @Override
        public void data(final int input) throws IOException {
            this.messageDigest.update((byte)input);
        }
        
        @Override
        public void data(final byte[] input, final int offset, final int length) throws IOException {
            this.messageDigest.update(input, offset, length);
        }
    }
}
