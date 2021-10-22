// 
// Decompiled by Procyon v0.5.36
// 

package io.prplz.memoryfix;

import java.util.Iterator;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.commons.RemappingClassAdapter;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ClassNode;
import java.util.function.BiConsumer;
import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer
{
    public byte[] transform(final String name, final String transformedName, final byte[] bytes) {
        if (name.equals("CapeUtils")) {
            return this.transformCapeUtils(bytes);
        }
        if (name.equals("io.prplz.memoryfix.CapeImageBuffer")) {
            return this.transformMethods(bytes, this::transformCapeImageBuffer);
        }
        if (transformedName.equals("net.minecraft.client.resources.AbstractResourcePack")) {
            return this.transformMethods(bytes, this::transformAbstractResourcePack);
        }
        if (transformedName.equals("net.minecraft.client.Minecraft")) {
            return this.transformMethods(bytes, this::transformMinecraft);
        }
        return bytes;
    }
    
    private byte[] transformMethods(final byte[] bytes, final BiConsumer<ClassNode, MethodNode> transformer) {
        final ClassReader classReader = new ClassReader(bytes);
        final ClassNode classNode = new ClassNode();
        classReader.accept((ClassVisitor)classNode, 0);
        classNode.methods.forEach(m -> transformer.accept(classNode, m));
        final ClassWriter classWriter = new ClassWriter(0);
        classNode.accept((ClassVisitor)classWriter);
        return classWriter.toByteArray();
    }
    
    private byte[] transformCapeUtils(final byte[] bytes) {
        final ClassWriter classWriter = new ClassWriter(2);
        final RemappingClassAdapter adapter = new RemappingClassAdapter((ClassVisitor)classWriter, (Remapper)new Remapper() {
            public String map(final String typeName) {
                if (typeName.equals("CapeUtils$1")) {
                    return "io.prplz.memoryfix.CapeImageBuffer".replace('.', '/');
                }
                return typeName;
            }
        });
        final ClassReader classReader = new ClassReader(bytes);
        classReader.accept((ClassVisitor)adapter, 8);
        return classWriter.toByteArray();
    }
    
    private void transformCapeImageBuffer(final ClassNode clazz, final MethodNode method) {
        for (final AbstractInsnNode insn : method.instructions) {
            if (insn instanceof MethodInsnNode) {
                final MethodInsnNode methodInsn = (MethodInsnNode)insn;
                if (methodInsn.name.equals("parseCape")) {
                    methodInsn.owner = "CapeUtils";
                }
                else {
                    if (!methodInsn.name.equals("setLocationOfCape")) {
                        continue;
                    }
                    methodInsn.setOpcode(182);
                    methodInsn.owner = "net/minecraft/client/entity/AbstractClientPlayer";
                    methodInsn.desc = "(Lnet/minecraft/util/ResourceLocation;)V";
                }
            }
        }
    }
    
    private void transformAbstractResourcePack(final ClassNode clazz, final MethodNode method) {
        if ((method.name.equals("getPackImage") || method.name.equals("func_110586_a")) && method.desc.equals("()Ljava/awt/image/BufferedImage;")) {
            for (final AbstractInsnNode insn : method.instructions) {
                if (insn.getOpcode() == 176) {
                    method.instructions.insertBefore(insn, (AbstractInsnNode)new MethodInsnNode(184, "io.prplz.memoryfix.ResourcePackImageScaler".replace('.', '/'), "scalePackImage", "(Ljava/awt/image/BufferedImage;)Ljava/awt/image/BufferedImage;", false));
                }
            }
        }
    }
    
    private void transformMinecraft(final ClassNode clazz, final MethodNode method) {
        final Iterator<AbstractInsnNode> iter = (Iterator<AbstractInsnNode>)method.instructions.iterator();
        while (iter.hasNext()) {
            final AbstractInsnNode insn = iter.next();
            if (insn.getOpcode() == 184) {
                final MethodInsnNode methodInsn = (MethodInsnNode)insn;
                if (!methodInsn.owner.equals("java/lang/System") || !methodInsn.name.equals("gc")) {
                    continue;
                }
                iter.remove();
            }
        }
    }
}
