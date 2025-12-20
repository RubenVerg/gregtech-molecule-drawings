package com.adsioho.gtm.compat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 兼容性工具：在不调用第三方 mod 的 Material.isNull() 的情况下，判断一个 Material（或类似对象）是否应被视为“空”。
 *
 * 实现策略：
 * - null -> true
 * - 通过反射寻找常见的数值字段/方法（getAmount, getStackSize, amount, qty 等），若存在且值 <= 0 -> true
 * - 通过反射寻找常见的名称方法（getName, getUnlocalizedName, getDisplayName 等），若返回 null/空串/"null"/"air"/"unknown" -> true
 *
 * 设计原则：尽量保守，不抛出异常，若无法判定则返回 false（即认为非空）。
 *
 * 请在替换仓库中所有 Material.isNull(...) 的调用为 MaterialHelper.isNull(...)（可使用提供的替换脚本）。
 */
public final class MaterialHelper {

    private MaterialHelper() { /* no instantiation */ }

    /**
     * 判断 material 是否可视为“空”。
     * 不会调用任何外部 mod 定义的 isNull()。
     *
     * @param material 任意对象（通常为 Material 或类似类型）
     * @return true 如果判定为空；false 否则
     */
    public static boolean isNull(Object material) {
        if (material == null) return true;

        try {
            Class<?> cls = material.getClass();

            // 1) 数量/大小类检查（若存在且 <= 0 则视为空）
            String[] numericMethods = {
                    "getAmount", "getStackSize", "getSize", "getSizeInUnits", "getQuantity", "getQty", "getCount"
            };
            for (String methodName : numericMethods) {
                try {
                    Method m = cls.getMethod(methodName);
                    Object val = m.invoke(material);
                    if (val instanceof Number) {
                        if (((Number) val).longValue() <= 0L) return true;
                    }
                } catch (NoSuchMethodException ignored) {} catch (Throwable ignored) {}
            }

            // 1b) 也尝试查找常见字段（如 amount, stackSize, qty）
            String[] numericFields = { "amount", "stackSize", "size", "quantity", "qty", "count" };
            for (String fieldName : numericFields) {
                try {
                    Field f = cls.getDeclaredField(fieldName);
                    f.setAccessible(true);
                    Object val = f.get(material);
                    if (val instanceof Number) {
                        if (((Number) val).longValue() <= 0L) return true;
                    }
                } catch (NoSuchFieldException ignored) {} catch (Throwable ignored) {}
            }

            // 2) 名称类检查（若名称为 null / 空 / "null" / "air" / "unknown" 则视为空）
            String[] nameMethods = {
                    "getName", "getUnlocalizedName", "getLocalizedName", "getDisplayName", "name", "getId"
            };
            for (String methodName : nameMethods) {
                try {
                    Method m = cls.getMethod(methodName);
                    Object val = m.invoke(material);
                    if (val == null) return true;
                    String s = val.toString().trim();
                    if (s.isEmpty()) return true;
                    String lower = s.toLowerCase();
                    if (lower.equals("null") || lower.equals("air") || lower.equals("unknown") ||
                            lower.equals("empty")) {
                        return true;
                    }
                } catch (NoSuchMethodException ignored) {} catch (Throwable ignored) {}
            }

            // 2b) 字段名称检查
            String[] nameFields = { "name", "id", "unlocalizedName", "displayName", "localizedName" };
            for (String fieldName : nameFields) {
                try {
                    Field f = cls.getDeclaredField(fieldName);
                    f.setAccessible(true);
                    Object val = f.get(material);
                    if (val == null) return true;
                    String s = val.toString().trim();
                    if (s.isEmpty()) return true;
                    String lower = s.toLowerCase();
                    if (lower.equals("null") || lower.equals("air") || lower.equals("unknown") ||
                            lower.equals("empty")) {
                        return true;
                    }
                } catch (NoSuchFieldException ignored) {} catch (Throwable ignored) {}
            }

        } catch (Throwable t) {
            // 任何反射异常都不应抛出，保持兼容：无法判定 -> 返回 false（认为非空）
            // 可将日志记录到 debug 位置（如果项目有日志系统，可在此处调用）。
        }

        return false;
    }
}
