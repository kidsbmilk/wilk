package com.zz4955.wilk;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.jcraft.jsch.*;

import java.io.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ssh_login {

    public static void main(String[] args) {
        try {
            JSch jSch = new JSch();
            final Session session = jSch.getSession("zhangzhen5", "relay.xiaomi.com"); // 这个username不能乱填，要不然生成的二维码扫后没效果。
            session.setUserInfo(new MyUserInfo());
            session.connect();

            final ChannelShell channel = (ChannelShell) session.openChannel("shell");
            final InputStream inputStream = channel.getInputStream();
            final OutputStream outputStream = channel.getOutputStream();
            final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));


            ExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
            ListenableFuture<Boolean> stringListenableFuture = (ListenableFuture<Boolean>) executorService.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    channel.connect();

                    outputStream.write("\n".getBytes()); // 这个应该输入\n\r才合适。
                    outputStream.flush();

                    String msg = null;
                    boolean b = true;
                    while((msg = in.readLine())!=null){ // 这个是读取一行，服务器那边显示：Your [EMAIL] password: ，这里是提示要输入用户密码，因为没有输入回车换行，所以这个版本是无法显示“Your [EMAIL] password:”的。
                    // 使用InputStream的available可以显示“Your [EMAIL] password:”。从这个也可以知道如何交互式的输入密码与动态验证码：当服务器那边显示：Your [EMAIL] password: 时，就用OutputStream输入密码与动态验证码就可以了。 TODO.
                        if(msg.length() == 0) {
                            continue;
                        }
                        if(b) {
                            System.out.println(msg.substring(3)); // 这个为什么要去除几前3个字符？我猜与上面的写入"\n"的回显有关。
                            // 这个是观察出来的，具体原因还有待研究。
                            b = false;
                            continue;
                        }
                        System.out.println(msg);
                        if(msg.startsWith("Command")) {
                            break;
                        }
                    }
                    return true;
                }
            });
            if(stringListenableFuture.get()) {

                outputStream.write("help\n\r".getBytes());
                outputStream.flush();
                        String buf = null;
                        while ((buf = in.readLine()) != null) {
                            System.out.println(buf);
                        }
            }
        } catch (JSchException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
