package org.fuelteam.watt.lucky.utils;

import java.io.StringReader;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.methods.HttpPost;
import org.fuelteam.watt.httpclient.RequestExecutor;

public class WebServiceUtil {

    private final static String xmlVersion = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    private final static String soap12Envelope = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">%s</soap:Envelope>";
    private final static String soapBody = "<soap:Body>%s</soap:Body>";
    private final static String soapHeader = "<%s %s>%s</%s>";
    
    public final static String DEFAULT_NAMESPACE = "xmlns=\"http://tempuri.org/\"";
    
    
    public static String prepare(String method, String namespace, String params) {
        String header = String.format(soapHeader, method, namespace, params, method);
        String body = String.format(soapBody, header);
        return xmlVersion + String.format(soap12Envelope, body);
    }

    public static String prepare(String method, String params) {
        String header = String.format(soapHeader, method, DEFAULT_NAMESPACE, params, method);
        String body = String.format(soapBody, header);
        return xmlVersion + String.format(soap12Envelope, body);
    }

    public static Object xmlToObject(Class<?> clazz, String xmlStr) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        // 进行将Xml转成对象的核心接口
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader sr = new StringReader(xmlStr);
        Object xmlObject = unmarshaller.unmarshal(sr);
        return xmlObject;
    }

    public static Pair<Integer, String> post(String asmxUrl, Map<String, String> headers, String body, int connectionTimeout,
            int soTimeout) throws Exception {
        Pair<Integer, String> result = new RequestExecutor<HttpPost>().build(HttpPost.class).on(asmxUrl, null, headers, body)
                .timeout(connectionTimeout, soTimeout).string();
        return result;
    }
}
