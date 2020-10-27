package xyz.phanta.ae2fc.coremod;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

public class CraftingCpuTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] code) {
        if (transformedName.equals("appeng.me.cluster.implementations.CraftingCPUCluster")) {
            System.out.println("[ae2fc] Transforming CraftingCPUCluster...");
            ClassReader reader = new ClassReader(code);
            ClassWriter writer = new ClassWriter(reader, 0);
            reader.accept(new TransformCraftingCPUCluster(Opcodes.ASM5, writer), 0);
            return writer.toByteArray();
        }
        return code;
    }

    private static class TransformCraftingCPUCluster extends ClassVisitor {

        TransformCraftingCPUCluster(int api, ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            if (name.equals("executeCrafting")) {
                return new TransformExecuteCrafting(api, super.visitMethod(access, name, desc, signature, exceptions));
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }

    }

    private static class TransformExecuteCrafting extends MethodVisitor {

        private boolean gotInventory = false;

        TransformExecuteCrafting(int api, MethodVisitor mv) {
            super(api, mv);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            if (opcode == Opcodes.GETFIELD
                    && owner.equals("appeng/me/cluster/implementations/CraftingCPUCluster") && name.equals("inventory")) {
                gotInventory = true;
            }
            super.visitFieldInsn(opcode, owner, name, desc);
        }

        @Override
        public void visitLineNumber(int line, Label start) {
            gotInventory = false;
            super.visitLineNumber(line, start);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
            if (gotInventory) {
                if (opcode == Opcodes.INVOKESTATIC
                        && owner.equals("appeng/util/item/AEItemStack") && name.equals("fromItemStack")) {
                    gotInventory = false;
                    super.visitMethodInsn(Opcodes.INVOKESTATIC,
                            "xyz/phanta/ae2fc/handler/CoreModHooks",
                            "wrapFluidPacketStack",
                            "(Lappeng/api/storage/data/IAEItemStack;)Lappeng/api/storage/data/IAEItemStack;",
                            false);
                }
            } else if (opcode == Opcodes.INVOKESPECIAL
                    && owner.equals("net/minecraft/inventory/InventoryCrafting") && name.equals("<init>")) {
                super.visitMethodInsn(Opcodes.INVOKESTATIC,
                        "xyz/phanta/ae2fc/handler/CoreModHooks",
                        "wrapCraftingBuffer",
                        "(Lnet/minecraft/inventory/InventoryCrafting;)Lnet/minecraft/inventory/InventoryCrafting;",
                        false);
            }
        }

    }

}
