package com.qualisystems.pythonDriverPlugin;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.DataOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.InetAddress;
import java.nio.file.Files;
import java.util.Base64;

public class ResourceManagementService {

    private static final String TargetServerURLFormat = "http://%s:%s/%s";

    private static final String LoginEndpointPath = "ResourceManagerApiService/Logon";
    private static final String LogonRequestFormat =
        "<Logon>\n" +
        "<username>%s</username>\n" +
        "<password>%s</password>\n" +
        "<domainName>%s</domainName>\n" +
        "</Logon>";

    private static final String UpdateDriverEndpointPath = "ResourceManagerApiService/UpdateDriver";
    private static final String UpdateDriverRequestFormat =
        "<UpdateDriver>\n" +
        "<driverName>%s</driverName>\n" +
        "<driverFile>%s</driverFile>\n" +
        "<driverFileName>%s</driverFileName>\n" +
        "</UpdateDriver>";

    private static final String UpdateScriptEndpointPath = "ResourceManagerApiService/UpdateScript";
    private static final String UpdateScriptRequestFormat =
            "<UpdateScript>\n" +
            "<scriptName>%s</scriptName>\n" +
            "<scriptFile>%s</scriptFile>\n" +
            "<scriptFileName>%s</scriptFileName>\n" +
            "</UpdateScript>";


    private final String _serverAddress;
    private final String _hostname;
    private final int _port;
    private String _authToken;

    public static ResourceManagementService OpenConnection(String serverAddress, int port, String username, String password, String domain) throws Exception {

        ResourceManagementService resourceManagementService = new ResourceManagementService(serverAddress, port);

        resourceManagementService.login(username, password, domain);

        return resourceManagementService;
    }

    public void updateDriver(String driverName, File newDriverFile) throws Exception {

        Update(driverName, newDriverFile,UpdateDriverEndpointPath,UpdateDriverRequestFormat);
    }

    public void updateScript(String scriptName, File newScriptFile)throws Exception {

        Update(scriptName, newScriptFile,UpdateScriptEndpointPath,UpdateScriptRequestFormat);
    }

    private void Update(String authoringItemName, File newFile, String endpointPath, String updateRequestFormat) throws Exception {
        String base64DriverFile = Base64.getEncoder().encodeToString(Files.readAllBytes(newFile.toPath()));

        String serverURL = String.format(TargetServerURLFormat, _serverAddress, _port, endpointPath);

        sendMessage(new URL(serverURL), String.format(updateRequestFormat, authoringItemName, base64DriverFile, newFile.getName()));
    }



    private void login(String username, String password, String domain) throws Exception {

        String serverURL = String.format(TargetServerURLFormat, _serverAddress, _port, LoginEndpointPath);

        Document doc = sendMessage(new URL(serverURL), String.format(LogonRequestFormat, username, password, domain));

        NodeList tokenElement = doc.getElementsByTagName("Token");

        if (tokenElement.getLength() != 1)
            throw new Exception("No token element in logon response");

        _authToken = tokenElement.item(0).getAttributes().getNamedItem("Token").getTextContent();
    }

    private Document sendMessage(URL requestURL, String message) throws Exception {

        HttpURLConnection con = (HttpURLConnection) requestURL.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("DateTimeFormat", "MM/dd/yyyy HH:mm");
        con.setRequestProperty("ClientTimeZoneId", "UTC");
        con.setRequestProperty("Content-Type", "text/xml");
        con.setRequestProperty("Accept", "*/*");
        con.setRequestProperty("Authorization", String.format("MachineName=%s;Token=%s", _hostname, _authToken));
        con.setRequestProperty("Host", _serverAddress + ":" + Integer.toString(_port));

        con.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());

        wr.writeBytes(message);

        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document responseXml = builder.parse(con.getInputStream());

        NodeList elements = responseXml.getElementsByTagName("Error");

        if (elements.getLength() > 0)
            throw new Exception(String.format("API Message: %s", elements.item(0).getTextContent()));

        if (!isSuccessResponseCode(responseCode))
            throw new Exception("Error making request, Response code: " + responseCode);

        return responseXml;
    }

    private boolean isSuccessResponseCode(int responseCode) {
        return responseCode >= 200 && responseCode < 300;
    }

    private ResourceManagementService(String serverAddress, int port) {

        _serverAddress = serverAddress;
        _port = port;

        String hostname;

        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (Exception ex) {
            hostname = "localhost";
        }

        _hostname = hostname;
    }


}
