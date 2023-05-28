package cn.td.aiot;

import cn.td.aiot.common.utils.RSAUtil;
import org.apache.commons.lang3.StringUtils;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class RASTest {
    //获取私钥
//    @Value("${my.rsa.private-key-base64}")
    private String privateKeyStr ="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCp/bU3JvNm4AsQhPkoxCr+vXj6vHiw6+w8iHrYbm3T1psbG3RgNotW+dJfSe9+m0v7qfTWeiY+h+nIBNgPbbdh8uTTA07YXs3VKq5KRfyLC1yBKYvFHznTq6F7kxmhajIqj+F/9JAw+HCE1qeW8kqcLqJ3AxZE1EHN11ZFYT7MHRCnSd2cm0DsfV40knSva7BPksX0NZPvNN3Gk1GJmu5zg3MpgJlGYvIjEpgio6O7v5fvWG6bKhCSZxKB+G/ooqMJ6XjTMs2fARb18bPa0GCAgqGZG7FUFjSokuF1xWAqM9EnHN5N3LjJz3nqMvqxn2IwDDZM5+X6ZYtXn+K39BwPAgMBAAECggEAUKL5A2CGtZYxqXRqfNwRSSW37bjZx5MiTNSw8Vnw3zw9923eloVJM8jt8iWwZe5k7MyCR/tJiQAP9cHWe/sfsmb2BOLc0+ak3SmR48mvMoYBK4P5uVhGphIUV/o/fkJR6b62yWvJunEfbuQkO4ZTviKhDrN4LM5DKNGtKtwmXcvltq96Y1gitZhOL/pPUJVngRuz3C1cdN7SgHfELQ749S2t/TKgMxAU5xRRKdaA4BDJCwb0E4NH14v3uePXHAJEKMWBQuGhW0u8zqgfG+6vg08jF+GKbH2zVN2i++jrPT1T9rqbVCmZ1j18/mbtfwzBGVJ1WqX1Q+gvcZX7A0vdAQKBgQDZQ31CNIPQsWPcmc+LvBhkgB6NswVmCWoZtJxRtb6Dk2GCieqEBZh7E7DPYMqhzuDvMMo1EM1s3FFpQuT12UQxLVLrN1sWSbawn+u8INSNGy3E5cLti+aXGei2HrcITphVes1+/4WQz+f8yY9xIyfhMHwaEcG1fHtV3zjWBZVpVwKBgQDITJFO9ygfFqUA8YSYfX9eEhh6MvLXlGgFS3UM9p5PsyZDAzAgRg8LObstkKPScHq2vq/2IKI+kOX4xzcslt4dLx5xFKNf34aLiX+6S5dVjajefMMSdLfaAv8WLj7m5KC+TsaX+Ib/7IljmdI2Xq/k0Y0XP4kjXetd+Gx6xH7YCQKBgQC2iMozFlXsvyysSZmix8wim7WPzWlqudwnbqOqzqwadXeGKgMbGmxqczeoc6Sq31oB+5bXkJ9Wp5/KdQZSO1DclDzHHAHuLs5LhICU1WxehQgmFfd4hoKcTotCf3m9g/LeVkdSPLHl4ZcBvgJCxouZ2Zdae7KonMYudlWVEqneDwKBgDCSyOZCb7tiTW92InascHfKoZ74vgVjmvo1u1QCdo/TXSMBIemhs8JAJNE+80t0NX4driRc85uCnhJ/lTS9/W1MYPCBVcFJPqKWHKB79l4r9VjHHrNaFL/M3Dlvy5dIuGaOSgDOp0ZnAUZjJiRDQBqYasPJh6NJAmwOc5guEKipAoGBAJowJYPrBszHz0j8xf008FohSk4zdrdupUBWwd24QZcNdf6vLQ8AESujzromZaDUmLJvHhkuJQhzzISganKR0MRLDs24yHlhE1EpVi8pZEvLBviyUXKh4ouc5nnkTU4Jj7jU0RqzVcy1mmzUHLs19xhM3tKPYXRyj1L5wc5juBeG";
    //获取公钥
//    @Value("${my.rsa.public-key-base64}")
    private String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqf21NybzZuALEIT5KMQq/r14+rx4sOvsPIh62G5t09abGxt0YDaLVvnSX0nvfptL+6n01nomPofpyATYD223YfLk0wNO2F7N1SquSkX8iwtcgSmLxR8506uhe5MZoWoyKo/hf/SQMPhwhNanlvJKnC6idwMWRNRBzddWRWE+zB0Qp0ndnJtA7H1eNJJ0r2uwT5LF9DWT7zTdxpNRiZruc4NzKYCZRmLyIxKYIqOju7+X71humyoQkmcSgfhv6KKjCel40zLNnwEW9fGz2tBggIKhmRuxVBY0qJLhdcVgKjPRJxzeTdy4yc956jL6sZ9iMAw2TOfl+mWLV5/it/QcDwIDAQAB";
    /**
     * 生成公钥和密钥
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new RASTest().testNewREA();
        new RASTest().testRSA();

    }
    public void testNewREA() throws Exception {
        //===============生成公钥和私钥，公钥传给客户端，私钥服务端保留==================
        //生成RSA公钥和私钥，并Base64编码
        KeyPair keyPair = RSAUtil.getKeyPair();
//            生成公钥
        String newPublicKeyStr = RSAUtil.getPublicKey(keyPair);
//            生成密钥
        String newPrivateKeyStr = RSAUtil.getPrivateKey(keyPair);
        System.out.println("新的RSA公钥Base64编码:" + newPublicKeyStr);
        System.out.println("新的RSA私钥Base64编码:" + newPrivateKeyStr);
    }

    /**
     * RAS加解密测试
     */
    public void testRSA(){
        try {
            System.out.println("RSA公钥Base64编码:" + publicKeyStr);
            System.out.println("RSA私钥Base64编码:" + privateKeyStr);

            //=================客户端=================
            //hello, i am infi, good night!加密
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入密码：");
            String password = scanner.next();
            System.out.println("请输入验证码：");
            String verifyCode=scanner.next();
            String message = verifyCode + "," + password ;
            //将Base64编码后的公钥转换成PublicKey对象
            PublicKey publicKey = RSAUtil.string2PublicKey(publicKeyStr);
            //用公钥加密
            byte[] publicEncrypt = RSAUtil.publicEncrypt(message.getBytes(), publicKey);
            //加密后的内容Base64编码
            String byte2Base64 = RSAUtil.byte2Base64(publicEncrypt);
//            byte2Base64 = "R2CMwI532b18ZSSshEuq01W0VanK2t7+m1Vy4F37IsrSSgPFXhJ+4OeO/bX9LfS1m1N9rO5bLb/z+WvtjLI/Cceol1a7xsH8ItlIiOMBWd+5pnxuEvYj+MXlabj8R0nniEf+DSMUCFm+rxJv9lOnINa4kMBRxyf3oU94wHBPntKGX4W6RjCRmFBCw5sCtaiz3j+0jdwwsgSwkU5m6bxXgxYTVFOF4ABk1gcEDU7h4h1k84rPXvptT29DtYO//FwaRpqoKuZ/oBSL3+dTnyEqyfwj1RY27/ubWyKz1jft5TgoqZmWz3ZK4MYsW/5w38ZruB2WorJynQQRRS8/cgBQoQ==";
            System.out.println("公钥加密并Base64编码的结果：" + byte2Base64);


            //##############    网络上传输的内容有Base64编码后的公钥 和 Base64编码后的公钥加密的内容     #################



            //===================服务端================
            //将Base64编码后的私钥转换成PrivateKey对象
            PrivateKey privateKey = RSAUtil.string2PrivateKey(privateKeyStr);
            //加密后的内容Base64解码
            byte[] base642Byte = RSAUtil.base642Byte(byte2Base64);
            //用私钥解密
            byte[] privateDecrypt = RSAUtil.privateDecrypt(base642Byte, privateKey);
            //解密后的明文
            System.out.println("解密后的明文: " + new String(privateDecrypt));
//            得到的密码
            System.out.println("得到的密码: " + StringUtils.substringAfter(new String(privateDecrypt),","));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
