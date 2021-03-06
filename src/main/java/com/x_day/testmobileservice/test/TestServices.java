/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.x_day.testmobileservice.test;

import com.google.gson.Gson;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import ru.ik.model.antif.ms.*;
import ru.ik.model.antif.ms.common.CommonMessage;
import ru.ik.model.antif.ms.fields.Attachment;
import ru.ik.model.antif.ms.fields.ExitpollField;
import ru.ik.model.antif.ms.fields.TotalReportField;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author PARFENOV
 */
public class TestServices {

    /**
     * @param args the command line arguments
     */
    private static final String userpass = "n1901:12345";
    private static final String userpassExit = "ex1901:12345";
    private static final String getData = "http://localhost:8080/mobile/api/mobiledata";
    private static final String observal = "http://localhost:8080/mobile/api/observer";
    private static final String total = "http://localhost:8080/mobile/api/observerTotal";
    private static final String roadMap = "http://localhost:8080/mobile/api/roadMap";
    private static final String exitPoll = "http://localhost:9090/mobile/api/exitpoll";
    private static final String violation = "http://localhost:8080/mobile/api/violation";
    private static final String violationMulti = "http://localhost:8080/mobile/api/violationMultipart";
    private static final String totalMulti = "http://localhost:8080/mobile/api/observerTotalMultipart";

    public static void main(String[] args) throws IOException {
        TestServices testServices = new TestServices();
//        testServices.testGetData();
//        testServices.testObservercontrol();
//        testServices.testMessageTotal();
//        testServices.testeMessageRoadMap();
//        testServices.testMessageExitPoll();
//        testServices.testMessageViolation();
//        testServices.testMessageViolationMulti();
        testServices.testMessageTotalMulti();
    }

    private void testGetData(){
        commontest(getData, null, userpass);
    }
    
    private void testMessageTotalMulti() throws UnsupportedEncodingException, IOException{

        MessageTotalMS messageTotalMS = new MessageTotalMS();
        messageTotalMS.setMessageTime(new Date().getTime()/1000);
        messageTotalMS.setDescription("12");
        messageTotalMS.setSecurityKey(UUID.fromString("00000000-0000-0000-0000-000000000036"));
        messageTotalMS.setCorrection(false);
        messageTotalMS.setElectionDistrictId(UUID.fromString("238c22ac-ac2e-4553-af07-2f977a93276a"));
        List<TotalReportField> lists = new ArrayList<TotalReportField>();
        for (int i = 0; i < 24; i++) {
            lists.add(new TotalReportField("test", String.valueOf(100 + i), Long.valueOf(i)));
        }
        messageTotalMS.setTotalReports(lists);
        
        connectMulti(totalMulti, "J:/test.txt", messageTotalMS, userpass);
    }

    private void testMessageViolationMulti() throws UnsupportedEncodingException, IOException {
        MessageViolationMS messageViolationMS = new MessageViolationMS();
        messageViolationMS.setMessageTime(new Date().getTime()/1000);
        messageViolationMS.setDescription("12");
        messageViolationMS.setSecurityKey(UUID.fromString("00000000-0000-0000-0000-000000000030"));
        messageViolationMS.setMeasures("вот тебе файл multi");
        messageViolationMS.setMobileSupport(true);
        messageViolationMS.setPossibleForgery(true);
        messageViolationMS.setViolationTypeId(UUID.fromString("00000000-0000-0000-0000-000000000036"));
        List<UUID> longs = new ArrayList<UUID>();
        longs.add(UUID.fromString("373c7ef9-eb68-42cb-af0f-299c63f872e7"));
        longs.add(UUID.fromString("a059fce0-a9d6-4f85-91a2-7dcd5f32fe35"));
        messageViolationMS.setViolatorId(longs);

        connectMulti(violationMulti, "J:/test.txt", messageViolationMS, userpass);
    }

    private void testMessageViolation() throws IOException {
        MessageViolationMS messageViolationMS = new MessageViolationMS();
        messageViolationMS.setMessageTime(new Date().getTime()/1000);
        messageViolationMS.setDescription("12");
        messageViolationMS.setSecurityKey(UUID.fromString("00000000-0000-0000-0000-000000000030"));
        messageViolationMS.setMeasures("вот тебе файл с мульти");
        messageViolationMS.setMobileSupport(true);
        messageViolationMS.setPossibleForgery(true);
        messageViolationMS.setViolationTypeId(UUID.fromString("00000000-0000-0000-0000-000000000036"));
        List<UUID> longs = new ArrayList<UUID>();
        longs.add(UUID.fromString("373c7ef9-eb68-42cb-af0f-299c63f872e7"));
        longs.add(UUID.fromString("a059fce0-a9d6-4f85-91a2-7dcd5f32fe35"));
        messageViolationMS.setViolatorId(longs);

        Attachment attachment = fileNameToAttachment("J:/test.txt");
        List<Attachment> attachments = new ArrayList<Attachment>();
        attachments.add(attachment);
        messageViolationMS.setAttachments(attachments);

        commontest(violation, messageViolationMS, userpass);

    }

    private void testMessageExitPoll() {
        MessageExitpollMS exitpollMS = new MessageExitpollMS();
        exitpollMS.setMessageTime(new Date().getTime()/1000);
        exitpollMS.setDescription("12");
        exitpollMS.setSecurityKey(UUID.fromString("00000000-0000-0000-0000-000000000036"));
        exitpollMS.setElectionDistrictId(UUID.fromString("238c22ac-ac2e-4553-af07-2f977a93276a"));
        List<ExitpollField> exitpollFields = new ArrayList<ExitpollField>();
        for (int i = 0; i < 5; i++) {
            exitpollFields.add(new ExitpollField("1000name" + i, "2000" + i, Long.valueOf(i)));
        }
        exitpollMS.setExitpollFields(exitpollFields);

        commontest(exitPoll, exitpollMS, userpassExit);

    }

    private void testeMessageRoadMap() {
        MessageRoadMapMS messageRoadMapMS = new MessageRoadMapMS();
        messageRoadMapMS.setMessageTime(new Date().getTime()/1000);
        messageRoadMapMS.setDescription("12");
        messageRoadMapMS.setSecurityKey(UUID.fromString("00000000-0000-0000-0000-000000000036"));
        messageRoadMapMS.setRoadMapId(UUID.fromString("00000000-0000-0000-0000-000000000099"));
        messageRoadMapMS.setResult(1L);

        commontest(roadMap, messageRoadMapMS, userpass);
    }

    private void testMessageTotal() {
        MessageTotalMS messageTotalMS = new MessageTotalMS();
        messageTotalMS.setMessageTime(new Date().getTime()/1000);
        messageTotalMS.setDescription("12");
        messageTotalMS.setSecurityKey(UUID.fromString("00000000-0000-0000-0000-000000000036"));
        messageTotalMS.setCorrection(false);
        messageTotalMS.setElectionDistrictId(UUID.fromString("238c22ac-ac2e-4553-af07-2f977a93276a"));
        List<TotalReportField> lists = new ArrayList<TotalReportField>();
        for (int i = 0; i < 24; i++) {
            lists.add(new TotalReportField("test", String.valueOf(100 + i), Long.valueOf(i)));
        }
        messageTotalMS.setTotalReports(lists);
        commontest(total, messageTotalMS, userpass);
    }

    private void testObservercontrol() throws IOException {

        MessageObservercontrolMS messageObservercontrolMS = new MessageObservercontrolMS();
        messageObservercontrolMS.setAppearance(858L);
        messageObservercontrolMS.setDescription("12");
        messageObservercontrolMS.setHomeRequest(5L);
        messageObservercontrolMS.setListQuantity(2449L);
        messageObservercontrolMS.setMessageTime(new Date().getTime()/1000);
        messageObservercontrolMS.setOfficialAppearance(1L);
        messageObservercontrolMS.setSecurityKey(UUID.fromString("00000000-0000-0000-0000-000000000036"));

        Attachment attachment = fileNameToAttachment("J:/test.txt");
        List<Attachment> attachments = new ArrayList<Attachment>();
        attachments.add(attachment);
        messageObservercontrolMS.setAttachments(attachments);

        commontest(observal, messageObservercontrolMS, userpass);
    }

    private <T extends CommonMessage> void commontest(String urlStr, T object, String userdata) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            String userpass = userdata;
            String basicAuth = "Basic " + Base64.encode(userpass.getBytes());
            conn.setRequestProperty("Authorization", basicAuth);

            String input;
            if(object!=null) {
                Gson g = new Gson();
                input = g.toJson(object);
//            input=" {\"appearance\":1,\"homeRequest\":2,\"listQuantity\":3,\"officialAppearance\":4,\"description\":\"тест\",\"messageTime\":1374135015,\"securityKey\":60389}";
                System.out.println("Input in Server .... \n");
                System.out.println(input);

                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes(Charset.forName("UTF-8")));
                os.flush();
            }
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * *
     * /////////////////////////////////////////////////// Тест блок для
     * прикрепления файла к сообщению
     * //////////////////////////////////////////////////
     */
    private Attachment fileNameToAttachment(String fileName) throws IOException {
        Attachment attachment = new Attachment();
        File file = new File(fileName);
        attachment.setFileName(file.getName());
        attachment.setFileData(encodeFileToBase64Binary(file));
        return attachment;
    }

    private String encodeFileToBase64Binary(File file)
            throws IOException {

        byte[] bytes = loadFile(file);
        String encoded = Base64.encode(bytes);

        return encoded;
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    /**
     * *
     * ///////////////////////////////////////////////////
     * //////////////////////////////////////////////////
     */
    private <T extends CommonMessage> void connectMulti(String url, String urlFile, T messageMS, String usepass) throws UnsupportedEncodingException, IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userpass));
        HttpPost httppost = new HttpPost(url);

        File file = new File(urlFile);
        FileBody bin = new FileBody(file);
        Gson g = new Gson();

        StringBody comment = new StringBody(g.toJson(messageMS), Charset.forName("UTF-8"));

        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("dto", comment);
        reqEntity.addPart("files", bin);
        reqEntity.addPart("files", bin);
        httppost.setEntity(reqEntity);

        System.out.println("Input in Server .... \n");
        System.out.println(httppost.toString());

        HttpResponse response = httpclient.execute(httppost);

        System.out.println("output server code:" + response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        BufferedReader br = new BufferedReader(new InputStreamReader((is)));
        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }
    }
}
