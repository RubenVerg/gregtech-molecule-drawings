package com.rubenverg.moldraw;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Map;
import java.util.Optional;

/**
 * 自定义的 Material 查找器，直接使用 ChemicalHelper 的公共方法。
 */
public final class CustomMaterialLookup {

    private CustomMaterialLookup() {
        // 工具类，禁止实例化
    }

    public static Optional<MaterialStack> getMaterialEntry(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return Optional.empty();
        }

        // 方法1: 直接使用 ChemicalHelper.getMaterial(ItemStack)
        try {
            MaterialStack materialStack = ChemicalHelper.getMaterial(stack);
            if (materialStack != null && materialStack.material() != null) {
                return Optional.of(materialStack);
            }
        } catch (Throwable t) {
            MolDraw.LOGGER.debug("CustomMaterialLookup: ChemicalHelper.getMaterial(ItemStack) failed", t);
        }

        // 方法2: 使用 ChemicalHelper.getMaterial(ItemLike)
        try {
            MaterialStack materialStack = ChemicalHelper.getMaterial(stack.getItem());
            if (materialStack != null && materialStack.material() != null) {
                return Optional.of(materialStack);
            }
        } catch (Throwable t) {
            MolDraw.LOGGER.debug("CustomMaterialLookup: ChemicalHelper.getMaterial(ItemLike) failed", t);
        }

        // 方法3: 使用 UnificationEntry 查找
        try {
            UnificationEntry unificationEntry = ChemicalHelper.getUnificationEntry(stack.getItem());
            if (unificationEntry != null && unificationEntry.material != null) {
                TagPrefix prefix = unificationEntry.tagPrefix;
                Material material = unificationEntry.material;
                long amount = prefix != null ? prefix.getMaterialAmount(material) : 1;
                return Optional.of(new MaterialStack(material, amount));
            }
        } catch (Throwable t) {
            MolDraw.LOGGER.debug("CustomMaterialLookup: UnificationEntry lookup failed", t);
        }

        // 方法4: 检查 ITEM_MATERIAL_INFO 映射
        try {
            // 通过反射访问 ChemicalHelper.ITEM_MATERIAL_INFO
            var field = ChemicalHelper.class.getDeclaredField("ITEM_MATERIAL_INFO");
            field.setAccessible(true);
            var itemMaterialInfoMap = field.get(null);

            if (itemMaterialInfoMap instanceof Map) {
                @SuppressWarnings("unchecked")
                var info = ((Map<ItemLike, Object>) itemMaterialInfoMap).get(stack.getItem());
                if (info != null) {
                    // 尝试获取 MaterialStack
                    var method = info.getClass().getMethod("getMaterial");
                    var result = method.invoke(info);
                    if (result instanceof MaterialStack) {
                        return Optional.of((MaterialStack) result);
                    }
                }
            }
        } catch (Throwable t) {
            MolDraw.LOGGER.debug("CustomMaterialLookup: ITEM_MATERIAL_INFO lookup failed", t);
        }

        return Optional.empty();
    }

    /**
     * 直接获取 Material 对象（不包装 MaterialStack）
     */
    public static Optional<Material> getMaterial(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return Optional.empty();
        }

        // 尝试直接获取 Material
        try {
            // 使用 ChemicalHelper.getUnificationEntry 获取 Material
            UnificationEntry entry = ChemicalHelper.getUnificationEntry(stack.getItem());
            if (entry != null && entry.material != null) {
                return Optional.of(entry.material);
            }
        } catch (Throwable t) {
            MolDraw.LOGGER.debug("CustomMaterialLookup: getMaterial via UnificationEntry failed", t);
        }

        // 通过 MaterialStack 获取
        Optional<MaterialStack> materialStack = getMaterialEntry(stack);
        return materialStack.map(MaterialStack::material);
    }

    /**
     * 获取 ItemStack 的 TagPrefix
     */
    public static Optional<TagPrefix> getTagPrefix(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return Optional.empty();
        }

        try {
            TagPrefix prefix = ChemicalHelper.getPrefix(stack.getItem());
            if (prefix != null) {
                return Optional.of(prefix);
            }
        } catch (Throwable t) {
            MolDraw.LOGGER.debug("CustomMaterialLookup: getTagPrefix failed", t);
        }

        return Optional.empty();
    }
}
