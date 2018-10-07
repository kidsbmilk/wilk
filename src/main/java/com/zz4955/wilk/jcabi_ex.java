package com.zz4955.wilk;

import com.jcabi.ssh.Shell;
import com.jcabi.ssh.Ssh;

import java.io.IOException;
import java.net.UnknownHostException;

public class jcabi_ex {

    public static void main(String[] args) throws IOException {
        Shell shell = new Ssh("192.168.1.89", 22, "soybeanmilk", "-----BEGIN RSA PRIVATE KEY-----\n" +
                "-----END RSA PRIVATE KEY-----\n");
        String stdout = new Shell.Plain(shell).exec("ls");
        System.out.println(stdout);
    }
}
