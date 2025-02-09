package com.boa.tcautomation.util;

import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;

@Component
public class SshUtil {

    @Value("${ssh.port:22}")
    private int port;

    @Value("${ssh.user:root}")
    private String user;

    @Value("${ssh.password:root}")
    private String password;

    private Session createSession(String host) throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setPassword(password);

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        return session;
    }

    public String executeCommand(String host, String command) throws Exception {
        Session session = createSession(host);

        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(command);
        channel.setErrStream(System.err);

        InputStream in = channel.getInputStream();
        channel.connect();

        StringBuilder outputBuffer = new StringBuilder();
        int readByte = in.read();
        while (readByte != 0xffffffff) {
            outputBuffer.append((char) readByte);
            readByte = in.read();
        }

        channel.disconnect();
        session.disconnect();

        return outputBuffer.toString();
    }

    public void uploadFile(String host, String localFile, String remoteFile) throws Exception {
        Session session = createSession(host);

        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
        channel.put(localFile, remoteFile);

        channel.disconnect();
        session.disconnect();
    }

    public void downloadFile(String host, String remoteFile, String localFile) throws Exception {
        Session session = createSession(host);

        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
        channel.get(remoteFile, localFile);

        channel.disconnect();
        session.disconnect();
    }

    public boolean fileExists(String host, String remoteFile) throws Exception {
        Session session = createSession(host);

        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();

        boolean exists;
        try {
            channel.lstat(remoteFile);
            exists = true;
        } catch (SftpException e) {
            exists = false;
        }

        channel.disconnect();
        session.disconnect();

        return exists;
    }

    public void deleteFile(String host, String remoteFile) throws Exception {
        Session session = createSession(host);

        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
        channel.rm(remoteFile);

        channel.disconnect();
        session.disconnect();
    }
}
