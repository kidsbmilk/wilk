package com.zz4955.wilk;

import com.jcabi.ssh.Shell;
import com.jcabi.ssh.Ssh;
import com.jcabi.ssh.SshByPassword;

import java.io.IOException;
import java.net.UnknownHostException;

public class jcabi_ex2 {

    public static void main(String[] args) throws IOException {
        Shell shell = new SshByPassword("192.168.1.89", 22, "soybeanmilk", "");
        String stdout = new Shell.Plain(shell).exec("ls");
        System.out.println(stdout);
    }
}
