package xyz.mocoder.mite.math;

import java.util.Random;

import static java.lang.Math.*;

//该类用于雷雨天雷电生成位置的计算,使用Box-Muller算法
public class FunctionalRandom {
    //Box-Muller算法,玩家周围一定距离不生成雷电
    public static double generateRandom(Random random) {
        double u=random.nextDouble();
        double v=random.nextDouble();
        double res=sqrt(-2*log(u))*cos(2*PI*v);
        if(res>=0) {
            res+=0.4D;
        } else {
            res-=0.4D;
        }
        return res;
    }
}
