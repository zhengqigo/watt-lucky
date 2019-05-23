package org.fuelteam.watt.lucky.packet;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import org.fuelteam.watt.lucky.number.BigDecimalUtil;
import org.fuelteam.watt.lucky.number.BigDecimalWrapper;

public class RedPacket {

    private static final double MAGIC = 4 * Math.exp(-0.5) / Math.sqrt(2.0);

    // 正态分布随机数[截断正态分布] mean 平均数, sigma 标准差
    private static double normalVariate(double mean, double sigma) {
        Random random = new Random();
        double z = 0.0;
        while (true) {
            double u1 = random.nextDouble();
            double u2 = 1 - random.nextDouble();
            z = MAGIC * (u1 - 0.5) / u2;
            double zz = z * z / 4.0;
            if (zz <= -Math.log(u2)) break;
        }
        return (mean + z * sigma);
    }

    /**
     * 基于截尾正态分布版红包: 将满足正态分布的数据剔除负值之后剩余的部分限制在一个区间内得到的新的概率分布
     * 1、得到红包平均数和标准差(采用平均值/2 替代真实的样本集的标准差)
     * 2、计算前n-1个红包对应的随机额度
     * a、每个红包分配权重 / 分配的总权重和 * 总发放额度 即为当前发放红包的额度，每个分配红包的权重满足正态分布
     * b、在记录下来已发放额度红包的额度【用于记录最后一个红包的额度】
     * c、循环前面的a和b的操作
     * 3、最后一个红包 = 总发放额度 - 前n-1个红包的总额度
     * 4、返回所有分配的红包 money 派发总额度, n 派发红包个数, minMoney红包最小额度, 现金红包不需要指定, 
     * maxMoney 红包最大额度 防止出现一个人占据整个红包总额
     */
    public static List<BigDecimal> randomSigma(Integer money, int n, double minMoney, double maxMoney) {
        List<BigDecimal> redPackets = Lists.newArrayList();
        BigDecimal mean = BigDecimal.valueOf(money / n); // 平均数
        BigDecimal sigma = BigDecimalUtil.safeDivide(mean, BigDecimal.valueOf(2.0), 2); // 标准差
        List<BigDecimal> divided = Lists.newArrayList(); // 每个红包额度权重
        double sum = 0.0;
        while (divided.size() < n) { // 需要分配额度红包 < 预分配红包的总个数
            double random = normalVariate(mean.doubleValue(), sigma.doubleValue());
            BigDecimalWrapper wrapper = BigDecimalUtil.get(random);
            if (wrapper.gte(sigma) && wrapper.lte(2.0 * sigma.doubleValue())) {
                divided.add(BigDecimal.valueOf(random));
                sum += random;
            }
        }
        double curSum = 0.0;
        for (int i = 0; i < n - 1; i++) {
            double cur = Math.round((divided.get(i).doubleValue() * money / sum));
            if (BigDecimalUtil.get(cur).lt(minMoney)) cur = minMoney;
            redPackets.add(BigDecimal.valueOf(cur));
            curSum += cur;
        }
        BigDecimal remainMoney = BigDecimalUtil.safeSubtract(money, curSum);
        redPackets.add(remainMoney);
        return redPackets;
    }

    /**
     * 随机红包原理: 
     * 第一个红包 额度 = [最小额度,总派发额度/总红包个数 * 2]内任意一个随机数 即为对应红包的额度
     * 第二个红包 额度 = [最小额度,(总派发额度-第一个红包的额度)/(总红包个数-1) * 2]内任意一个随机数 即为对应红包的额度
     * 第三个红包 额度 = [最小额度,(总派发额度-前2个红包的额度)/(总红包个数-2) * 2]内任意一个随机数 即为对应红包的额度
     * 如此循环直至第n-1个红包 额度 = [最小额度,(总派发额度-前n-2个红包的额度)/(总红包个数-(n-2)) * 2]内任意一个随机数 即为对应红包的额度
     * 第n个红包 额度 = 总额度 - 前n-1个红包的派发额度 需要指定当前红包的个数、红包的总额度、红包最小额度
     */
    public static double getRandomMoney(LeftPacket leftPacket, int unit) {
        int leftSize = leftPacket.getLeftSize(); // 剩余红包个数
        double leftMoney = leftPacket.getLeftMoney(); // 剩余的额度
        double minMoney = leftPacket.getMinMoney(); // 最小红包额度

        if (leftSize == 1) {
            leftPacket.setLeftSize(--leftSize);
            return (double) Math.round(leftMoney * unit) / unit;
        }
        Random random = new Random();
        double max = leftMoney / leftSize * 2;
        double money = random.nextDouble() * max;

        money = BigDecimalUtil.get(money).lte(minMoney) ? minMoney : money;
        money = Math.floor(money * unit) / unit;
        leftPacket.setLeftSize(--leftSize);
        leftPacket.setLeftMoney(BigDecimalUtil.safeSubtract(leftMoney, money).doubleValue());

        return money;
    }
}