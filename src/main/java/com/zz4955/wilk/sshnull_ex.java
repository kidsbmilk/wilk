package com.zz4955.wilk;

import com.jcraft.jsch.*;

import java.io.*;

public class sshnull_ex {

    public static void main(String[] args) {
        try {
            JSch jSch = new JSch();
            Session session = jSch.getSession("", "relay.xiaomi.com");
//            session.setPassword("");
            session.setUserInfo(new MyUserInfo());
//            session.setConfig("StrictHostKeyChecking", "no");
//            session.setConfig("PreferredAuthentications","keyboard-interactive");
            session.connect();
//            ChannelExec ec = (ChannelExec) session.openChannel("exec");
//            ec.setCommand("ls");
//            ec.setInputStream(null);
//            ec.setErrStream(System.out);
//            ec.setOutputStream(System.out);
//            ec.connect();
//            while(!ec.isClosed()) {
//                Thread.sleep(500);
//            }
//            ec.disconnect();
//            session.disconnect();
            ChannelShell channel = (ChannelShell) session.openChannel("shell");
            channel.connect();
            InputStream inputStream = channel.getInputStream();
            OutputStream outputStream = channel.getOutputStream();
//            String cmd = "ls \n\r";
//            outputStream.write(cmd.getBytes());
//            String cmd2 = "cd /home/jenkins/workspace/ggservice \n\r";
//            outputStream.write(cmd2.getBytes());
//            String cmd3 = "pwd \n\r";
//            outputStream.write(cmd3.getBytes());

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
//            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            outputStream.write("\n".getBytes());
            outputStream.flush();

            char[] ctp = new char[100];
            String msg = null;

//            while(in.read(ctp) != 0) {
//                System.out.println(ctp);
//            }

            boolean b = true;
            while((msg = in.readLine())!=null){
                if(msg.length() == 0) {
                    continue;
                }
//                System.out.println(msg.length());
                if(b) {
                    System.out.println(msg.substring(3)); // 这个为什么要去除几前3个字符？我猜与上面的写入"\n"的回显有关。
                    // 这个是观察出来的，具体原因还有待研究。
                    b = false;
                    continue;
                }
                System.out.println(msg);
//                if(msg.startsWith("Your [EMAIL] password")) {
//                    outputStream.write(br.read());
//                    outputStream.flush();
//                }
//                if(msg.startsWith("Your [EMAIL] password")) {
//                    outputStream.write(br.read());
//                    outputStream.flush();
//                }
            }
            in.close();

        } catch (JSchException e) {
            System.out.println(e.getMessage());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
