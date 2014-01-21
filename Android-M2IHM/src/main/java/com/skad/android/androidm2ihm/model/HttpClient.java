package com.skad.android.androidm2ihm.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by skad on 21/01/14.
 */
public class HttpClient {
    private HttpURLConnection con;
    private String delimiter = "--";
    private String boundary = "SwA" + Long.toString(System.currentTimeMillis()) + "SwA";
    private OutputStream os;

    public HttpClient(String url) throws IOException {
        con = (HttpURLConnection) (new URL(url)).openConnection();
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
    }

    public void connect() throws IOException {
        con.connect();
        os = con.getOutputStream();
    }

    public void setMultipart() throws IOException {
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connect();

    }

    public void addFormPart(String paramName, String value) throws Exception {
        writeParamData(paramName, value);
    }

    private void writeParamData(String paramName, String value) throws Exception {
        os.write((delimiter + boundary + "\r\n").getBytes());
        os.write("Content-Type: text/plain\r\n".getBytes());
        os.write(("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n").getBytes());
        ;
        os.write(("\r\n" + value + "\r\n").getBytes());

    }

    public void addFilePart(String fileName, byte[] data) throws Exception {
        os.write((delimiter + boundary + "\r\n").getBytes());
        os.write(("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"" + fileName + "\"\r\n").getBytes());
        os.write(("Content-Type: application/octet-stream\r\n").getBytes());
        os.write(("Content-Transfer-Encoding: binary\r\n").getBytes());
        os.write("\r\n".getBytes());

        os.write(data);

        os.write("\r\n".getBytes());
    }

    public void disconnect() {
        con.disconnect();
    }

    public void finishMultipart() throws Exception {
        os.write((delimiter + boundary + delimiter + "\r\n").getBytes());
    }

    public void finish() throws IOException {
        os.flush();
    }

    public String getResponse() throws Exception {
        InputStream is = con.getInputStream();
        byte[] b1 = new byte[1024];
        StringBuffer buffer = new StringBuffer();

        while (is.read(b1) != -1)
            buffer.append(new String(b1));

        con.disconnect();

        return buffer.toString();
    }
}
