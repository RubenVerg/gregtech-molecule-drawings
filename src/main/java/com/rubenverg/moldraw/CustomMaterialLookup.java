package com.rubenverg.moldraw;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;

import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * 自定义的 Material 查找器（避免在编译时直接调用 ChemicalHelper.getMaterialEntry(ItemLike)）。
 *
 * 策略（按优先级）：
 * 1) 尝试通过 GTCEuAPI.materialManager 上可能存在的直接方法（通过反射查找名含 "getMaterial"、接受 ItemStack/Item 的方法）；
 * 2) 尝试获取 materialManager 的所有 Material（通过反射），并比较每个 Material 的代表物品（代表 ItemStack）与目标 ItemStack；
 * 3) 若找到相同 Material，则用 new MaterialStack(material, 1) 返回（若构造器或签名不同会捕获异常并继续）。
 *
 * 注意：本实现是兼容层，尽量稳健地处理反射失败并返回 Optional.empty()。
 */
public final class CustomMaterialLookup {

    private static final Object MATERIAL_MANAGER = GTCEuAPI.materialManager;

    private CustomMaterialLookup() {
        // 工具类，禁止实例化
    }

    public static Optional<MaterialStack> getMaterialEntry(ItemStack stack) {
        if (stack == null || stack.isEmpty() || MATERIAL_MANAGER == null) {
            return Optional.empty();
        }

        // 1) 尝试在 materialManager 上查找接受 ItemStack/Item 的 getMaterial* 方法（反射）
        try {
            Method[] methods = MATERIAL_MANAGER.getClass().getMethods();
            for (Method method : methods) {
                String name = method.getName().toLowerCase();
                Class<?>[] params = method.getParameterTypes();
                if (name.contains("getmaterial") && params.length == 1) {
                    // 支持接收 ItemStack 的方法（params[0] 是 ItemStack 的超类型）
                    if (params[0].isAssignableFrom(ItemStack.class)) {
                        Object res = method.invoke(MATERIAL_MANAGER, stack);
                        Optional<MaterialStack> converted = convertToMaterialStack(res);
                        if (converted.isPresent()) {
                            return converted;
                        }
                    }
                }
            }
        } catch (Throwable t) {
            // 反射可能失败，记录一次性调试日志以便排查（避免重复刷屏）
            MolDraw.LOGGER.debug("CustomMaterialLookup: direct manager method lookup failed", t);
        }

        // 2) 通过 materialManager 获取所有材料并逐个匹配代表物品（反射）
        try {
            Method[] managerMethods = MATERIAL_MANAGER.getClass().getMethods();
            Method getAllMaterialsMethod = null;
            for (Method method : managerMethods) {
                String name = method.getName().toLowerCase();
                Class<?> returnType = method.getReturnType();
                if ((name.contains("getmaterials") || name.contains("getall") || name.contains("materiallist")) &&
                        (Collection.class.isAssignableFrom(returnType) || Map.class.isAssignableFrom(returnType))) {
                    getAllMaterialsMethod = method;
                    break;
                }
            }

            if (getAllMaterialsMethod != null) {
                Object all = getAllMaterialsMethod.invoke(MATERIAL_MANAGER);
                Iterable<?> iterable = null;
                if (all instanceof Map) {
                    iterable = ((Map<?, ?>) all).values();
                } else if (all instanceof Iterable) {
                    iterable = (Iterable<?>) all;
                }

                if (iterable != null) {
                    String[] candidateNames = {
                            "getrepresentativestack",
                            "getrepresentativestackornull",
                            "getrepresentative",
                            "getrepresentativeitem"
                    };

                    for (Object matObj : iterable) {
                        if (!(matObj instanceof Material)) {
                            continue;
                        }

                        Material mat = (Material) matObj;
                        ItemStack rep = null;

                        // 尝试通过常见方法名获取代表 ItemStack（反射）
                        for (String candidate : candidateNames) {
                            try {
                                Method repMethod = mat.getClass().getMethod(candidate);
                                Object repObj = repMethod.invoke(mat);
                                if (repObj instanceof ItemStack) {
                                    rep = (ItemStack) repObj;
                                    break;
                                }
                            } catch (NoSuchMethodException ignored) {
                                // 继续尝试其他方法名
                            } catch (Throwable ignored) {
                                // 忽略单个材料上的反射异常，继续匹配下一材料
                            }
                        }

                        if (rep != null) {
                            // 尽量宽松地比较：相同 item 或完全相同的 item + tag
                            try {
                                if (rep.getItem() == stack.getItem() || ItemStack.isSameItemSameTags(rep, stack)) {
                                    try {
                                        return Optional.of(new MaterialStack(mat, 1));
                                    } catch (Throwable t) {
                                        MolDraw.LOGGER.debug(
                                                "CustomMaterialLookup: failed to construct MaterialStack for matched material",
                                                t);
                                    }
                                }
                            } catch (Throwable ignored) {
                                // 防御性编程：如果比较方法抛异常，继续尝试下一个材料
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            MolDraw.LOGGER.debug("CustomMaterialLookup: material iteration lookup failed", t);
        }

        // 3) 无法找到，返回空
        return Optional.empty();
    }

    private static Optional<MaterialStack> convertToMaterialStack(Object o) {
        if (o == null) {
            return Optional.empty();
        }
        try {
            if (o instanceof MaterialStack) {
                return Optional.of((MaterialStack) o);
            }
            if (o instanceof Optional) {
                Optional<?> oo = (Optional<?>) o;
                if (oo.isPresent() && oo.get() instanceof MaterialStack) {
                    return Optional.of((MaterialStack) oo.get());
                }
            }
        } catch (Throwable ignored) {
            // 防御性：任何意外都视为未能转换
        }
        return Optional.empty();
    }
}
