package com.github.nikitin_da.sticky_dictionary.util;

import java.util.Random;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public final class RandomUtil {

    private RandomUtil() {
    }

    public static long nextPositiveLong() {
        Random random = new Random();
        return (long) (random.nextDouble() * Long.MAX_VALUE);
    }
}
