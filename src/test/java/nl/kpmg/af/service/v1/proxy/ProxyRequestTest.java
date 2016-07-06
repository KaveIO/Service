package nl.kpmg.af.service.v1.proxy;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by fziliotto on 6-7-16.
 */
public class ProxyRequestTest {

    @Test
    public void recursiveCallTest() throws Exception {
        String target = "http://localhost:9000/v1/proxy/demoPE/predGet/";
        String origin = "http://localhost:9000/v1/proxy/demoPE/predGet/";

        // Same target
        assertTrue(ProxyRequest.isRecursiveCall(target,target));


        //Proxy target has a different host, we should allow the call because it connects to a different data service
        // It may be a problem if the hostname is resolved to the same one that made the request or the data service is distributed on multiple hosts
        target = "http://otherHost:9000/v1/proxy/demoPE/predGet/";
        assertFalse(ProxyRequest.isRecursiveCall(origin,target));

        //Proxy target has /v*/proxy/
        // /v*/proxy/ is a dedicated string in the uri, so it should be not used as the target for a proxy request (this avoids incurring in recursive calls)
        target = "http://localhost:9000/other/v1/proxy/demoPE/predGet/";
        assertTrue(ProxyRequest.isRecursiveCall(origin,target));


        //Proxy target has the same host but a different port, the call is valid (e.g. a second data service is active on the same machine)
        target = "http://localhost:9001/v1/proxy/demoPE/predGet/";
        assertFalse(ProxyRequest.isRecursiveCall(origin,target));


        //Tipical proxy request to a different service in a different host
        target = "http://www.proxied.com/get";
        assertFalse(ProxyRequest.isRecursiveCall(origin,target));


        //Data service call in the same host
        target = "http://localhost:9000/v1/data/demoPE/predictions/";
        assertFalse(ProxyRequest.isRecursiveCall(origin,target));

    }

}