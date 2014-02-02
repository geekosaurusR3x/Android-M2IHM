package com.skad.android.androidm2ihm.model;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class used for faciliting the connection with the server
 * Created by skad on 21/01/14.
 */
public class HttpClient {
    /**
     * Connection with the server
     */
    private HttpURLConnection con;
    /**
     * delimiter of the http protocol
     */
    private String delimiter = "--";
    /**
     * time of the connection
     */
    private String boundary = "SwA" + Long.toString(System.currentTimeMillis()) + "SwA";
    /**
     * OutputStream for reading the response of the server
     */
    private OutputStream os;

    /**
     * Constructor
     * Initialise the connection to the given url
     * Set the RequestMethode to POST
     *
     * @param url
     * @throws IOException
     */
    public HttpClient(String url) throws IOException {
        con = (HttpURLConnection) (new URL(url)).openConnection();
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
    }

    /**
     * Connect to to the url and wait for the response
     *
     * @throws IOException
     */
    public void connect() throws IOException {
        con.connect();
        os = con.getOutputStream();
    }

    /**
     * Activate Multipart Mode for the connection.
     * Need for uploading file
     *
     * @throws IOException
     */
    public void setMultipart() throws IOException {
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connect();

    }

    /**
     * Adding part of a form to the connection like : Login : value
     *
     * @param paramName
     * @param value
     * @throws Exception
     */
    public void addFormPart(String paramName, String value) throws Exception {
        os.write((delimiter + boundary + "\r\n").getBytes());
        os.write("Content-Type: text/plain\r\n".getBytes());
        os.write(("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n").getBytes());
        ;
        os.write(("\r\n" + value + "\r\n").getBytes());
    }

    /**
     * Adding part for uploading file
     *
     * @param fileName
     * @param data
     * @throws Exception
     */
    public void addFilePart(String fileName, byte[] data) throws Exception {
        os.write((delimiter + boundary + "\r\n").getBytes());
        os.write(("Content-Type: application/octet-stream\r\n").getBytes());
        os.write(("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"" + fileName + "\"\r\n").getBytes());
        os.write(("Content-Transfer-Encoding: binary\r\n").getBytes());
        os.write("\r\n".getBytes());

        os.write(data);

        os.write("\r\n".getBytes());
    }

    /**
     * for closing the connection
     */
    public void disconnect() {
        con.disconnect();
    }

    /**
     * Adding the end of the form
     *
     * @throws Exception
     */
    public void finishMultipart() throws Exception {
        os.write((delimiter + boundary + delimiter + "\r\n").getBytes());
    }

    /**
     * flushing the last bytes into the connection
     *
     * @throws IOException
     */
    public void finish() throws IOException {
        os.flush();
    }

    /**
     * Read the response of the server
     *
     * @return a ArrayByte
     * @throws Exception
     */
    public byte[] getResponse() throws Exception {
        InputStream is = con.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);

        ByteArrayBuffer baf = new ByteArrayBuffer(5000);
        int current = 0;
        while ((current = bis.read()) != -1) {
            baf.append((byte) current);
        }
        return baf.toByteArray();
    }

    /**
     * Request a file from the server
     *
     * @param file
     * @throws IOException
     */
    public void requestFile(String file) throws IOException {
        con.getOutputStream().write(("name=" + file).getBytes());
    }
}
