package sample.microprofile.book.client.header;

import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

@ConstrainedTo(RuntimeType.CLIENT)
public class PropagateJwtHeadersFactory implements ClientHeadersFactory {

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders,
            MultivaluedMap<String, String> outgoingHeaders) {

        String rawToken = "...."; // 保管していたものをどこかから取得する

        var newHeadersMap = new MultivaluedHashMap<String, String>(outgoingHeaders);
        newHeadersMap.add(HttpHeaders.AUTHORIZATION, "Bearer " + rawToken);

        return newHeadersMap;
    }
}
